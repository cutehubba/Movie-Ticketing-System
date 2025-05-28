<template>
  <div class="schedule-management">
    <div class="card">
      <div class="header-bar">
        <h1>🎬 电影排期管理</h1>
        <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>
      <!-- 添加排期 -->
      <!-- 新增排期表单 -->
      <form @submit.prevent="addScheduleHandler" class="form">
        <select v-model.number="newSchedule.movieId" required>
          <option value="" disabled>请选择电影</option>
          <option v-for="movie in movieList" :key="movie.id" :value="movie.id">
            {{ movie.title || `电影${movie.id}` }}
          </option>
        </select>

        <select v-model.number="newSchedule.cinemaId" required>
          <option value="" disabled>请选择影院</option>
          <option v-for="cinema in cinemaList" :key="cinema.id" :value="cinema.id">
            {{ cinema.name || `影院${cinema.id}` }}
          </option>
        </select>

        <select v-model.number="newSchedule.hallId" required>
          <option value="" disabled>请选择影厅</option>
          <option v-for="hall in hallList" :key="hall.id" :value="hall.id">
            {{ hall.name || `影厅${hall.id}` }}
          </option>
        </select>

        <!-- 其它字段保持不变 -->
        <input v-model="newSchedule.startTime" type="datetime-local" required />
        <input v-model="newSchedule.endTime" type="datetime-local" required />
        <input v-model.number="newSchedule.price" type="number" placeholder="票价" required step="0.01" />
        <select v-model="newSchedule.status" required>
          <option value="available">可用</option>
          <option value="cancelled">已取消</option>
          <option value="completed">已完成</option>
        </select>
        <button type="submit" class="glow-btn" :disabled="isSubmitting">
          {{ isSubmitting ? '添加中...' : '添加排期' }}
        </button>
      </form>

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
              @click="goToSeatList(s.hallId)"
              class="glass-btn"
              :disabled="isSubmitting"
            >座位管理</button>
            <button @click="startEdit(s)" class="glass-btn" :disabled="isSubmitting">编辑</button>
            <button @click="deleteScheduleHandler(s.id)" class="danger-btn" :disabled="isSubmitting">删除</button>
          </div>
        </li>
      </ul>

      <!-- 编辑排期 -->
      <!-- 编辑排期表单 -->
      <div v-if="editingSchedule" class="edit-area glass-card">
        <h3>✏️ 编辑排期</h3>
        <select v-model.number="editingSchedule.movieId" required>
          <option value="" disabled>请选择电影</option>
          <option v-for="movie in movieList" :key="movie.id" :value="movie.id">
            {{ movie.title || `电影${movie.id}` }}
          </option>
        </select>

        <select v-model.number="editingSchedule.cinemaId" required>
          <option value="" disabled>请选择影院</option>
          <option v-for="cinema in cinemaList" :key="cinema.id" :value="cinema.id">
            {{ cinema.name || `影院${cinema.id}` }}
          </option>
        </select>

        <select v-model.number="editingSchedule.hallId" required>
          <option value="" disabled>请选择影厅</option>
          <option v-for="hall in hallList" :key="hall.id" :value="hall.id">
            {{ hall.name || `影厅${hall.id}` }}
          </option>
        </select>
        <input v-model="editingSchedule.startTime" type="datetime-local" required />
        <input v-model="editingSchedule.endTime" type="datetime-local" required />
        <input v-model.number="editingSchedule.price" type="number" placeholder="票价" required step="0.01" />
        <select v-model="editingSchedule.status" required>
          <option value="available">可用</option>
          <option value="cancelled">已取消</option>
          <option value="completed">已完成</option>
        </select>
        <button @click="updateScheduleHandler" class="glow-btn" :disabled="isSubmitting">
          {{ isSubmitting ? '保存中...' : '保存修改' }}
        </button>
        <button @click="cancelEdit" class="cancel-btn" :disabled="isSubmitting">取消编辑</button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getSchedules,
  createSchedule,
  updateSchedule,
  deleteSchedule,
} from "@/services/ScheduleService";

import { getCinemaDetails, getCinemas } from "@/services/CinemaService";
import { getMovieDetails, getMovies } from "@/services/MovieService";
import { getHallDetails, getHalls } from "@/services/hall";

// 时间格式转换函数
function toApiTime(datetimeLocalStr) {
  // "2025-05-27T08:00" -> "2025-05-27T08:00:00+08:00"
  return datetimeLocalStr ? `${datetimeLocalStr}:00+08:00` : '';
}

export default {
  name: "ScheduleManagement",
  data() {
    return {
      schedules: [],
      newSchedule: {
        movieId: null,
        cinemaId: null,
        hallId: null,
        startTime: "",
        endTime: "",
        price: 0.0,
        status: "available",
      },
      editingSchedule: null,
      isLoading: false,
      isSubmitting: false,
      cinemaCache: {},
      movieCache: {},
      hallCache: {},
      cinemaList: [],
      movieList: [],
      hallList: [],
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

    goToSeatList(hallId) {
      this.$router.push({ name: 'SeatList', params: { hallId } });
    },

    async loadBaseData() {
      try {
        const [cinemas, movies, halls] = await Promise.all([
          getCinemas(),
          getMovies(),
          getHalls(),
        ]);
        this.cinemaList = cinemas || [];
        this.movieList = movies.data?.data || movies.data || [];
        this.hallList = halls || [];
        this.cinemaList.forEach(c => this.cinemaCache[c.id] = c);
        this.movieList.forEach(m => this.movieCache[m.id] = m);
        this.hallList.forEach(h => this.hallCache[h.id] = h);
      } catch (e) {
        alert("基础数据加载失败！");
        console.error(e);
      }
    },

    async loadSchedules() {
      this.isLoading = true;
      try {
        const schedulesData = await getSchedules();

        // 依然补充缓存（如果后端数据不全，防止详情缺失）
        const uniqueCinemaIds = [...new Set(schedulesData.map(s => s.cinemaId))];
        const uniqueMovieIds  = [...new Set(schedulesData.map(s => s.movieId))];
        const uniqueHallIds   = [...new Set(schedulesData.map(s => s.hallId))];

        const cinemaPromises = uniqueCinemaIds.map(async (cid) => {
          if (!this.cinemaCache[cid]) {
            try {
              const res = await getCinemaDetails(cid);
              this.cinemaCache[cid] = res.data.data || res.data || {};
            } catch (e) {
              this.cinemaCache[cid] = { name: `影院${cid}` };
            }
          }
        });
        const moviePromises = uniqueMovieIds.map(async (mid) => {
          if (!this.movieCache[mid]) {
            try {
              const res = await getMovieDetails(mid);
              this.movieCache[mid] = res.data.data || res.data || {};
            } catch (e) {
              this.movieCache[mid] = { title: `电影${mid}` };
            }
          }
        });
        const hallPromises = uniqueHallIds.map(async (hid) => {
          if (!this.hallCache[hid]) {
            try {
              const res = await getHallDetails(hid);
              this.hallCache[hid] = res.data.data || res.data || {};
            } catch (e) {
              this.hallCache[hid] = { name: `影厅${hid}` };
            }
          }
        });

        await Promise.all([...cinemaPromises, ...moviePromises, ...hallPromises]);

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
    },

    async addScheduleHandler() {
      this.isSubmitting = true;
      try {
        // 格式化时间字段
        const payload = {
          ...this.newSchedule,
          startTime: toApiTime(this.newSchedule.startTime),
          endTime: toApiTime(this.newSchedule.endTime),
        };
        console.log("创建排片提交数据", payload); // 打印提交内容
        await createSchedule(payload);
        alert("添加成功！");
        this.resetNew();
        await this.loadSchedules();
      } catch (e) {
        alert("添加失败！");
        console.error(e);
      } finally {
        this.isSubmitting = false;
      }
    },

    startEdit(s) {
      this.editingSchedule = { ...s };
    },

    async updateScheduleHandler() {
      this.isSubmitting = true;
      try {
        // 编辑时也需要格式化时间
        const payload = {
          ...this.editingSchedule,
          startTime: toApiTime(this.editingSchedule.startTime),
          endTime: toApiTime(this.editingSchedule.endTime),
        };
        await updateSchedule(this.editingSchedule.id, payload);
        alert("更新成功！");
        this.editingSchedule = null;
        await this.loadSchedules();
      } catch (e) {
        alert("更新失败！");
        console.error(e);
      } finally {
        this.isSubmitting = false;
      }
    },

    cancelEdit() {
      this.editingSchedule = null;
    },

    async deleteScheduleHandler(id) {
      if (!confirm("确定删除该排期吗？")) return;
      this.isSubmitting = true;
      try {
        await deleteSchedule(id);
        alert("删除成功！");
        await this.loadSchedules();
      } catch (e) {
        alert("删除失败！");
        console.error(e);
      } finally {
        this.isSubmitting = false;
      }
    },

    resetNew() {
      this.newSchedule = {
        movieId: null,
        cinemaId: null,
        hallId: null,
        startTime: "",
        endTime: "",
        price: 0.0,
        status: "available",
      };
    },
  },
  async mounted() {
    await this.loadBaseData();
    await this.loadSchedules();
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
  margin-bottom: 20px;
  color: #00fff7;
  text-shadow: 0 0 10px #00fff7;
}

.form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 15px;
}

.form input,
.form select {
  flex: 1 1 120px;
  padding: 12px 10px;
  border-radius: 10px;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  font-size: 1rem;
  outline: none;
  transition: background 0.3s ease;
}

.form input:focus,
.form select:focus {
  background: rgba(0, 255, 247, 0.3);
  box-shadow: 0 0 8px #00fff7aa;
}

.glow-btn {
  background: #00fff7;
  border: none;
  color: #000;
  padding: 14px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  width: 100%;
  margin-top: 15px;
  transition: box-shadow 0.3s ease;
  box-shadow: 0 0 15px #00fff7;
}

.glow-btn:hover:not(:disabled) {
  box-shadow: 0 0 40px #00fff7;
}

.glow-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  box-shadow: none;
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

.danger-btn {
  background: #ff007c;
  border: none;
  color: #fff;
  padding: 10px 18px;
  border-radius: 12px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s ease;
  user-select: none;
}

.danger-btn:hover:not(:disabled) {
  background: #ff0059;
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

.edit-area {
  margin-top: 20px;
  padding: 15px;
  background: rgba(30, 40, 50, 0.85);
  border-radius: 12px;
  box-shadow: 0 0 15px #008080aa inset;
  color: #b0e0e6;
}

.cancel-btn {
  background: #666;
  color: #ddd;
  border: none;
  padding: 8px 14px;
  margin-left: 10px;
  border-radius: 8px;
  cursor: pointer;
  user-select: none;
  transition: background 0.3s ease;
}

.cancel-btn:hover:not(:disabled) {
  background: #555;
}
</style>