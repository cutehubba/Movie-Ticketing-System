<template>
  <div class="home-container">
    <div class="particles-container">
      <div class="particle" v-for="i in 30" :key="i" :style="particleStyle(i)"></div>
    </div>

    <!-- 页面头部 -->
    <header class="home-header">
      <!-- 用户昵称显示 -->
      <div class="user-nickname">
        👋 欢迎，{{ nickname }}
      </div>

      <!-- 退出登录按钮 -->
      <button class="logout-btn" @click="logout">退出登录</button>

      <h1 class="neon-text">🎥 欢迎来到电影网站</h1>
      <p class="subtitle">浏览最新的电影与排期，尽情享受观影体验。</p>
      <div class="btn-group">
        <button @click="goToMovieList" class="btn-neon">🎬 电影管理</button>
        <button @click="goToCinemaList" class="btn-neon">🍿 电影院管理</button>
        <button @click="goToOrderList" class="btn-neon">🛒 订单管理</button>
        <button @click="goToScheduleList" class="btn-neon">📅 排期管理</button>
        <button @click="goToUserList" class="btn-neon">👥 用户列表</button>
        <button @click="viewProfile" class="btn-neon">👤 我的信息</button>
      </div>
    </header>

    <!-- 电影展示区域 -->
    <section class="movie-list">
      <h2 class="section-title">🔥 热门电影推荐</h2>
      <div class="card-wrapper">
        <div class="movie-card" v-for="movie in movies" :key="movie.id">
          <img :src="movie.poster" alt="电影海报" />
          <h3>{{ movie.title }}</h3>
          <p>{{ movie.description }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: "HomeView",
  data() {
    return {
      movies: [
        {
          id: 1,
          title: "奇幻之旅",
          description: "一场梦幻般的冒险之旅即将开启。",
          poster: "/images/1.jpg"
        },
        {
          id: 2,
          title: "爱情故事",
          description: "一段令人动容的爱情故事。",
          poster: "/images/2.jpg"
        },
        {
          id: 3,
          title: "科幻之门",
          description: "通往未来的科技之门已打开。",
          poster: "/images/3.jpg"
        }
      ],
      nickname: ''
    };
  },
  mounted() {
    const storedName = localStorage.getItem('nickname');
    if (storedName) {
      this.nickname = storedName;
    } else {
      this.nickname = '游客';
    }
  },
  methods: {
    logout() {
      localStorage.clear();
      this.$router.push('/');
    },
    goToMovieList() {
      this.$router.push('/movies');
    },
    goToCinemaList() {
      this.$router.push('/cinemas');
    },
    goToOrderList() {
      this.$router.push('/orders');
    },
    goToScheduleList() {
      this.$router.push('/schedules');
    },
    goToUserList() {
      this.$router.push('/users');
    },
    viewProfile() {
      const userId = localStorage.getItem('userId');
      if (userId) {
        this.$router.push(`/profile/${userId}`);
      } else {
        alert('请先登录');
        this.$router.push('/login');
      }
    },
    particleStyle(i) {
      const size = Math.random() * 6 + 3 + 'px';
      const top = Math.random() * 100 + '%';
      const left = Math.random() * 100 + '%';
      const delay = Math.random() * 20 + 's';
      const duration = 15 + Math.random() * 10 + 's';
      return {
        width: size,
        height: size,
        top,
        left,
        animationDelay: delay,
        animationDuration: duration,
      };
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&family=Montserrat:wght@300;400;600&display=swap');

.home-container {
  font-family: 'Montserrat', sans-serif;
  min-height: 100vh;
  color: #fff;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  padding: 40px 20px;
  position: relative;
  overflow-x: hidden;
  text-align: center;
  z-index: 1;
}

/* 用户昵称显示 */
.user-nickname {
  position: absolute;
  top: 20px;
  right: 30px;
  font-size: 1.1rem;
  font-weight: 600;
  color: #00f3ff;
  background: rgba(255, 255, 255, 0.05);
  padding: 8px 16px;
  border-radius: 20px;
  backdrop-filter: blur(4px);
  box-shadow: 0 0 10px rgba(0, 243, 255, 0.3);
  z-index: 10;
}

/* 新增退出登录按钮样式 */
.logout-btn {
  position: absolute;
  top: 60px; /* 比昵称低一点 */
  right: 30px;
  padding: 6px 14px;
  background-color: #ff4d4d;
  border: none;
  border-radius: 16px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 0 8px #ff4d4dcc;
  transition: background-color 0.3s ease;
  z-index: 10;
}

.logout-btn:hover {
  background-color: #cc0000;
}

/* 粒子背景 */
.particles-container {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  z-index: 0;
  overflow: hidden;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation-name: float;
  animation-timing-function: linear;
  animation-iteration-count: infinite;
  opacity: 0.8;
}

@keyframes float {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 1;
  }
  100% {
    transform: translateY(-1000px) rotate(720deg);
    opacity: 0;
  }
}

/* 头部 */
.home-header {
  position: relative;
  z-index: 2;
  margin-bottom: 60px;
}

.neon-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 3rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  animation: neon 1.5s ease-in-out infinite alternate;
  text-shadow:
    0 0 5px #00f3ff,
    0 0 10px #00f3ff,
    0 0 20px #ff00de,
    0 0 30px #ff00de;
}

@keyframes neon {
  from {
    text-shadow:
      0 0 5px #00f3ff,
      0 0 10px #00f3ff,
      0 0 20px #ff00de,
      0 0 30px #ff00de;
  }
  to {
    text-shadow:
      0 0 10px #00f3ff,
      0 0 20px #ff00de,
      0 0 30px #ff00de,
      0 0 40px #00f3ff;
  }
}

.subtitle {
  font-size: 1.3rem;
  margin-top: 12px;
  color: rgba(255,255,255,0.7);
}

/* 按钮组 */
.btn-group {
  margin-top: 30px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 18px;
}

.btn-neon {
  position: relative;
  padding: 14px 32px;
  font-family: 'Orbitron', sans-serif;
  font-size: 1.1rem;
  color: #00f3ff;
  background: transparent;
  border: 2px solid #00f3ff;
  border-radius: 25px;
  cursor: pointer;
  overflow: hidden;
  box-shadow: 0 0 12px #00f3ffcc;
  transition: all 0.3s ease;
  z-index: 1;
  opacity: 1;
}

.btn-neon:hover {
  color: #000;
  background: #00f3ff;
  box-shadow: 0 0 20px #00f3ffee;
}

.btn-neon::before {
  content: '';
  position: absolute;
  top: -60px;
  left: 0;
  width: 100%;
  height: 200px;
  background: #00f3ff;
  transition: 0.5s;
  z-index: -1;
  border-radius: 50%;
}

.btn-neon:hover::before {
  top: -120px;
}

/* 电影列表区域 */
.movie-list {
  position: relative;
  z-index: 2;
  margin-top: 50px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 40px;
  background: linear-gradient(90deg, #00f3ff, #ff00de);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.card-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 28px;
}

.movie-card {
  background: rgba(255,255,255,0.1);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  width: 220px;
  padding: 20px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.25);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.movie-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 30px rgba(0, 243, 255, 0.7);
}

.movie-card img {
  width: 100%;
  height: 310px;
  object-fit: cover;
  border-radius: 16px;
  margin-bottom: 15px;
}

.movie-card h3 {
  font-size: 1.3rem;
  margin-bottom: 8px;
  color: #00f3ff;
}

.movie-card p {
  font-size: 1rem;
  color: rgba(255,255,255,0.7);
  line-height: 1.3;
}
</style>
