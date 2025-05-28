<template>
  <div class="cinema-bg">
    <div class="cinema-card glass-card">
      <div class="header-bar">
        <h1>🎬 影院列表</h1>
        <router-link to="/userhome" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>

      <p v-if="cinemas.length === 0" class="empty-text">
        <svg width="36" height="36" viewBox="0 0 24 24" style="vertical-align:middle;" fill="none">
          <circle cx="12" cy="12" r="10" fill="#70aaff22"/>
          <path d="M8 12h8M8 16h5M9 8h6" stroke="#70aaff" stroke-width="2" stroke-linecap="round"/>
        </svg>
        没有影院数据
      </p>

      <ul class="cinema-list">
        <li v-for="cinema in cinemas" :key="cinema.id" class="cinema-item glass-card cinema-item-ani">
          <router-link :to="'/cinema/' + cinema.id" class="cinema-link">
            <span class="cinema-name">{{ cinema.name }}</span>
            <span class="cinema-location">📍{{ cinema.location }}</span>
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { getCinemas } from "@/services/CinemaService";

export default {
  name: "CinemaList",
  data() {
    return {
      cinemas: [],
    };
  },
  async created() {
    await this.loadCinemas();
  },
  methods: {
    async loadCinemas() {
      try {
        this.cinemas = await getCinemas();
      } catch (error) {
        console.error("影院列表加载失败：", error);
        alert("加载影院列表失败，请稍后再试！");
      }
    },
  },
};
</script>

<style scoped>
.cinema-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #161c2a 0%, #243b55 100%);
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 40px 0;
}
.glass-card {
  background: rgba(36, 56, 95, 0.89);
  box-shadow: 0 8px 32px #0f7bff44, 0 1.5px 12px #1b426688;
  border-radius: 22px;
  border: 1.5px solid rgba(112,170,255,0.09);
  backdrop-filter: blur(7px);
  transition: box-shadow 0.25s;
}
.glass-card:hover {
  box-shadow: 0 12px 40px #70aaff77, 0 2px 18px #4062bb44;
}

.cinema-card {
  padding: 40px 30px 30px 30px;
  min-width: 340px;
  max-width: 480px;
  width: 100%;
  margin: 0 auto;
  text-align: center;
  position: relative;
  animation: fadeInCard 0.7s;
}
@keyframes fadeInCard {
  from { opacity: 0; transform: translateY(40px);}
  to   { opacity: 1; transform: none;}
}

/* 顶部 header bar */
.header-bar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 32px;
  gap: 16px;
}
.cinema-card h1 {
  font-size: 2.1rem;
  margin-bottom: 0;
  text-shadow: 0 0 14px #0f7bffaa, 1px 1px 10px #0008;
  color: #70aaff;
  letter-spacing: 2px;
}
.back-home-btn {
  display: inline-block;
  margin-left: 20px;
  padding: 9px 20px;
  background: linear-gradient(90deg, #70f2ff 0%, #ff4b2b 100%);
  color: #222;
  font-size: 1em;
  font-weight: 700;
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 10px #70f2ff33;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.17s, color 0.15s, box-shadow 0.21s;
  user-select: none;
  white-space: nowrap;
}
.back-home-btn:hover {
  background: linear-gradient(90deg, #fff176 0%, #70f2ff 100%);
  color: #ff4b2b;
  box-shadow: 0 4px 18px #fff17644;
}

.empty-text {
  color: #70aaffcc;
  font-style: italic;
  margin-bottom: 22px;
  font-size: 1.17rem;
  letter-spacing: 1.1px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  background: rgba(112, 242, 255, 0.07);
  border-radius: 10px;
  padding: 15px 0 15px 0;
}

.cinema-list {
  list-style-type: none;
  padding: 0;
  margin: 0 0 16px 0;
}

.cinema-item {
  margin: 16px 0;
  font-size: 1.2rem;
  display: flex;
  align-items: stretch;
  gap: 0;
  border-radius: 14px;
  background: rgba(112,170,255,0.09);
  box-shadow: 0 2px 14px #70aaff22;
  transition: background 0.2s, box-shadow 0.2s, transform 0.18s;
  overflow: hidden;
  animation: fadeInItem 0.7s;
}
@keyframes fadeInItem {
  from { opacity: 0; transform: translateY(18px);}
  to   { opacity: 1; transform: none;}
}
.cinema-item-ani:hover, .cinema-item:hover {
  background: rgba(112,170,255,0.18);
  box-shadow: 0 4px 24px #70aaff44, 0 2px 12px #4062bb33;
  transform: scale(1.03);
}

.cinema-link {
  display: flex;
  gap: 18px;
  align-items: center;
  text-decoration: none;
  color: #70aaff;
  text-shadow: 0 0 5px #70aaffaa;
  flex-grow: 1;
  text-align: left;
  padding: 14px 18px;
  border-radius: 14px;
  font-weight: 600;
  font-size: 1.12rem;
  transition: color 0.23s, background 0.23s;
  word-break: break-all;
}
.cinema-link:hover {
  color: #fff176;
  background: linear-gradient(90deg, #70aaff22 0%, #fff17622 100%);
  text-shadow: 0 0 12px #fff176bb, 0 0 3px #fff;
}

.cinema-name {
  font-size: 1.15em;
  font-weight: 700;
  color: #fff;
  text-shadow: 0 2px 10px #70aaff44;
  margin-right: 6px;
  letter-spacing: 1.2px;
}

.cinema-location {
  font-size: 1.05em;
  color: #b9e9f7;
  opacity: 0.95;
  text-shadow: 0 0 4px #70aaff44;
  margin-left: 10px;
  letter-spacing: 0.5px;
}

@media (max-width: 600px) {
  .cinema-card {
    min-width: 0;
    max-width: 99vw;
    padding: 18px 4vw 14px 4vw;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
  .cinema-item {
    font-size: 1.03rem;
    margin: 9px 0;
  }
  .cinema-link {
    padding: 9px 6px;
    font-size: 1rem;
    flex-direction: column;
    align-items: flex-start;
    gap: 3px;
  }
  .cinema-name {
    font-size: 1em;
  }
  .cinema-location {
    font-size: 0.97em;
    margin-left: 0;
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