<template>
  <div class="movie-list-bg">
    <div class="movie-list-container glass-card">
      <div class="header-bar">
        <h1 class="page-title">电影列表</h1>
        <router-link to="/userhome" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>
      <ul class="movie-list">
        <li v-for="movie in movieList" :key="movie.id" class="movie-item glass-card">
          <div class="movie-poster" v-if="movie.posterUrl" :style="{ backgroundImage: 'url(' + movie.posterUrl + ')' }"></div>
          <div class="movie-info">
            <router-link :to="'/movie/' + movie.id" class="movie-title" :title="movie.title">{{ movie.title }}</router-link>
            <div class="movie-meta">
              <span>🎬 {{ movie.director || '未知' }}</span>
              <span>🏷️ {{ movie.genre || '未知' }}</span>
              <span>⭐ {{ movie.rating || '-' }}/10</span>
            </div>
          </div>
        </li>
      </ul>
      <p v-if="movieList.length === 0" class="no-movies">没有电影数据</p>
    </div>
  </div>
</template>

<script>
import { getMovies } from '@/services/MovieService';

export default {
  data() {
    return {
      movieList: []
    };
  },
  async created() {
    await this.loadMovies();
  },
  methods: {
    async loadMovies() {
      try {
        const response = await getMovies();
        if (response && response.data && Array.isArray(response.data)) {
          this.movieList = response.data;
        } else {
          console.error('电影数据格式不正确');
        }
      } catch (error) {
        console.error('加载电影列表失败', error);
      }
    }
  }
};
</script>

<style scoped>
/* 背景渐变和玻璃卡片 */
.movie-list-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #161c2a 0%, #243b55 100%);
  padding: 40px 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}
.glass-card {
  background: rgba(36, 56, 95, 0.87);
  box-shadow: 0 8px 32px #0f7bff44, 0 1.5px 12px #1b426688;
  border-radius: 22px;
  border: 1.5px solid rgba(112,170,255,0.09);
  backdrop-filter: blur(7px);
  transition: box-shadow 0.25s;
}
.glass-card:hover {
  box-shadow: 0 12px 40px #70aaff77, 0 2px 18px #4062bb44;
}

/* header bar 样式 */
.header-bar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;
}
.page-title {
  font-size: 2.3em;
  font-weight: 900;
  text-align: center;
  margin-bottom: 0;
  text-shadow: 0 2px 22px #12c2e9b0, 1px 1px 8px #0008;
  letter-spacing: 2px;
  background: linear-gradient(90deg,#70f2ff,#fff176 60%,#ff4b2b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
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

/* 主体容器 */
.movie-list-container {
  max-width: 1150px;
  width: 97vw;
  margin: 0 auto;
  padding: 34px 36px 34px 36px;
  color: #e2f1ff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  user-select: none;
  position: relative;
  overflow: visible;
}

/* 列表区 */
.movie-list {
  display: flex;
  flex-wrap: wrap;
  gap: 28px 26px;
  padding: 8px 0 0 0;
  list-style: none;
  margin: 0;
  justify-content: flex-start;
}

.movie-item {
  flex: 0 0 315px;
  display: flex;
  flex-direction: column;
  min-height: 420px;
  margin-bottom: 10px;
  position: relative;
  transition: transform 0.23s;
  overflow: hidden;
  box-sizing: border-box;
}
.movie-item:hover {
  transform: translateY(-10px) scale(1.025);
  z-index: 2;
  box-shadow: 0 10px 40px #70aaff88, 0 2px 12px #4062bb44;
}

.movie-poster {
  height: 340px;
  background-size: cover;
  background-position: center center;
  border-radius: 18px 18px 0 0;
  border-bottom: 3px solid #ff4b2b;
  transition: box-shadow 0.24s;
  box-shadow: 0 6px 24px #ff416c33;
}
.movie-info {
  padding: 18px 22px 8px 22px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  color: #fff;
  box-sizing: border-box;
}
.movie-title {
  font-weight: 900;
  font-size: 1.37em;
  margin-bottom: 8px;
  color: #fff176;
  text-decoration: none;
  text-shadow: 1px 1px 7px #ff4b2baa,0 2px 10px #0008;
  user-select: text;
  transition: color 0.21s;
  word-break: break-all;
}
.movie-title:hover {
  color: #70f2ff;
  text-shadow: 1px 1px 10px #70f2ff, 0 0 12px #fff;
}
.movie-meta {
  font-size: 1em;
  display: flex;
  gap: 16px;
  color: #eaf6ff;
  font-weight: 600;
  margin-bottom: 7px;
  user-select: text;
  flex-wrap: wrap;
}

/* 无电影提示 */
.no-movies {
  text-align: center;
  font-size: 1.3em;
  margin-top: 50px;
  color: #b9e9f7;
  letter-spacing: 1.2px;
  background: rgba(112,242,255,0.06);
  border-radius: 12px;
  padding: 28px 0;
}

/* 响应式适配 */
@media (max-width: 800px) {
  .movie-list-container {
    padding: 16px 3vw;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
  .movie-list {
    gap: 14px 0;
    justify-content: center;
  }
  .movie-item {
    flex: 0 0 96vw;
    min-width: 96vw;
    max-width: 99vw;
  }
  .movie-poster {
    height: 220px;
  }
}
@media (max-width: 480px) {
  .movie-list-container {
    padding: 7px 1vw;
    border-radius: 10px;
  }
  .movie-title {
    font-size: 1.07em;
  }
  .movie-info {
    padding: 10px 7px 6px 7px;
  }
  .movie-item {
    padding: 0;
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