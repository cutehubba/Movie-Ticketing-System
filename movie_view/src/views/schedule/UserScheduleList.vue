<template>
  <div class="schedule-management">
    <div class="card">
      <div class="header-bar">
        <h1>🎬 电影排期管理</h1>
        <router-link to="/userhome" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>

      <button @click="loadSchedules" :disabled="isLoading" class="glass-btn">
        {{ isLoading ? '加载中...' : '刷新排期' }}
      </button>

      <p v-if="schedules.length === 0 && !isLoading">暂无排期数据</p>

      <!-- 排期列表 -->
      <ul v-if="schedules.length > 0" class="schedule-list">
        <li v-for="s in schedules" :key="s.id">
          <div>
            <span class="title">
              电影: {{ s.movieName || ('电影' + s.movieId) }} | 
              影院: {{ s.cinemaName || ('影院' + s.cinemaId) }} | 
              影厅: {{ s.hallName || ('影厅' + s.hallId) }}
            </span><br />
            <span class="time">时间: {{ formatTime(s.startTime) }} - {{ formatTime(s.endTime) }}</span><br />
            <span class="info">票价: ¥{{ s.price.toFixed(2) }} | 状态: {{ s.status }}</span>
          </div>
          <div class="actions">
            <button
              @click="goToSeatList(s.hallId, s.id)"
              class="glass-btn"
              :disabled="isLoading"
            >选座购票</button>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import {
  getSchedules
} from "@/services/ScheduleService";

import { getCinemaDetails } from "@/services/CinemaService";
import { getMovieDetails } from "@/services/MovieService";
import { getHallDetails } from "@/services/hall";

export default {
  name: "ScheduleManagement",
  data() {
    return {
      schedules: [],
      isLoading: false,
      cinemaCache: {}, // 缓存影院详情
      movieCache: {},  // 缓存电影详情
      hallCache: {},   // 缓存影厅详情
    };
  },
  methods: {
    formatTime(str) {
      const date = new Date(str);
      if (isNaN(date)) return str;
      const y = date.getFullYear();
      const m = String(date.getMonth() + 1).padStart(2, "0");
      const d = String(date.getDate()).padStart(2, "0");
      const hh = String(date.getHours()).padStart(2, "0");
      const mm = String(date.getMinutes()).padStart(2, "0");
      return `${y}-${m}-${d} ${hh}:${mm}`;
    },

    goToSeatList(hallId, id) {
      this.$router.push({ 
        name: 'UserSeatList', 
        params: { hallId, id }
      });
    },

    async loadSchedules() {
      this.isLoading = true;
      try {
        const schedulesData = await getSchedules();

        // 收集所有唯一影院ID、电影ID、影厅ID
        const uniqueCinemaIds = [...new Set(schedulesData.map(s => s.cinemaId))];
        const uniqueMovieIds = [...new Set(schedulesData.map(s => s.movieId))];
        const uniqueHallIds = [...new Set(schedulesData.map(s => s.hallId))];

        // 请求未缓存的影院详情
        const cinemaPromises = uniqueCinemaIds.map(async (cid) => {
          if (!this.cinemaCache[cid]) {
            try {
              const res = await getCinemaDetails(cid);
              this.cinemaCache[cid] = res.data.data || res.data || {};
            } catch (e) {
              console.warn(`获取影院${cid}详情失败`, e);
              this.cinemaCache[cid] = { name: `影院${cid}` };
            }
          }
        });

        // 请求未缓存的电影详情
        const moviePromises = uniqueMovieIds.map(async (mid) => {
          if (!this.movieCache[mid]) {
            try {
              const res = await getMovieDetails(mid);
              this.movieCache[mid] = res.data.data || res.data || {};
            } catch (e) {
              console.warn(`获取电影${mid}详情失败`, e);
              this.movieCache[mid] = { title: `电影${mid}` };
            }
          }
        });

        // 请求未缓存的影厅详情
        const hallPromises = uniqueHallIds.map(async (hid) => {
          if (!this.hallCache[hid]) {
            try {
              const res = await getHallDetails(hid);
              this.hallCache[hid] = res.data.data || res.data || {};
            } catch (e) {
              console.warn(`获取影厅${hid}详情失败`, e);
              this.hallCache[hid] = { name: `影厅${hid}` };
            }
          }
        });

        await Promise.all([...cinemaPromises, ...moviePromises, ...hallPromises]);

        // 赋予每条排期对应的影院名、电影名和影厅名
        this.schedules = schedulesData.map(s => ({
          ...s,
          cinemaName: this.cinemaCache[s.cinemaId]?.name || `影院${s.cinemaId}`,
          movieName: this.movieCache[s.movieId]?.title || `电影${s.movieId}`,
          hallName: this.hallCache[s.hallId]?.name || `影厅${s.hallId}`,
        }));

      } catch (e) {
        alert("加载排期失败！");
        console.error(e);
      } finally {
        this.isLoading = false;
      }
    }
  },
  mounted() {
    this.loadSchedules();
  },
};
</script>

<style scoped>
.schedule-management {
  max-width: 900px;
  margin: 1rem auto;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: white;
  background: linear-gradient(to bottom right, #1f1c2c, #928dab);
  min-height: 100vh;
  padding: 60px 20px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  user-select: none;
}

.card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  backdrop-filter: blur(12px);
  padding: 40px 30px;
  width: 100%;
  max-width: 700px;
  box-shadow: 
    0 0 30px rgba(0, 255, 247, 0.3),
    inset 0 0 15px #00fff7aa;
}

/* 新增 header bar */
.header-bar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 20px;
  gap: 16px;
}

.back-home-btn {
  display: inline-block;
  margin-left: 20px;
  padding: 9px 20px;
  background: linear-gradient(90deg, #00fff7 0%, #928dab 100%);
  color: #222;
  font-size: 1em;
  font-weight: 700;
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 10px #00fff733;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.17s, color 0.15s, box-shadow 0.21s;
  user-select: none;
  white-space: nowrap;
}
.back-home-btn:hover {
  background: linear-gradient(90deg, #fff176 0%, #00fff7 100%);
  color: #928dab;
  box-shadow: 0 4px 18px #fff17644;
}

h1, h3 {
  text-align: center;
  margin-bottom: 0;
  color: #00fff7;
  text-shadow: 0 0 10px #00fff7;
}

.glass-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 2px solid #00fff7;
  color: #00fff7;
  padding: 10px 18px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s ease;
  margin-right: 10px;
  user-select: none;
}

.glass-btn:hover:not(:disabled) {
  background: #00fff7;
  color: #000;
}

.glass-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.schedule-list {
  list-style: none;
  padding: 0;
  margin-top: 15px;
}

.schedule-list li {
  background: rgba(255, 255, 255, 0.06);
  padding: 12px 18px;
  border-radius: 10px;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #e0f7f7;
}

.title {
  font-weight: 600;
  font-size: 1.05rem;
  color: #00fff7;
}

.time, .info {
  font-size: 0.9rem;
  color: #a0e7f7;
}

.actions button {
  margin-left: 10px;
}

@media (max-width: 700px) {
  .card {
    padding: 15px 3vw;
    max-width: 99vw;
    min-width: 0;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-bottom: 15px;
  }
  .back-home-btn {
    padding: 6px 12px;
    font-size: 0.93em;
    border-radius: 9px;
    margin-left: 0;
    margin-top: 4px;
  }
}
</style>