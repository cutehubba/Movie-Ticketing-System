<script>
export default {
  name: 'App',
  methods: {
    goToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<template>
  <div id="app">
    <router-view v-if="$route.path !== '/'"></router-view>
    <div v-else class="home-container">
      <div class="particles-container">
        <div class="particle" v-for="i in 30" :key="i"></div>
      </div>
      <div class="header">
        <h1 class="neon-text">电影票务管理系统</h1>
        <button @click="goToLogin" class="btn-login">
          <span>进入系统</span>
          <div class="liquid"></div>
        </button>
      </div>
      <div class="content">
        <h2 class="content-title">光影世界，尽在掌握</h2>
        <p class="content-subtitle">体验专业级的电影票务管理解决方案</p>
        <div class="features">
          <div class="feature-card">
            <div class="feature-icon">🎬</div>
            <h3>电影管理</h3>
            <p>全面掌控影院排片</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💰</div>
            <h3>票务销售</h3>
            <p>实时监控票房数据</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>数据分析</h3>
            <p>深度洞察观众喜好</p>
          </div>
        </div>
      </div>
      <div class="footer">
        <p>© 2025 电影票务管理系统 | 打造极致观影体验</p>
      </div>
    </div>
  </div>
</template>

<!-- 注意此处不要scoped -->
<style>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&family=Montserrat:wght@300;400;600&display=swap');

/* 全局消除滚动条和边框 */
html, body, #app {
  width: 100%;
  min-width: 100%;
  max-width: 100%;
  height: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  border: none !important;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  font-family: 'Montserrat', sans-serif;
  color: #fff;
  box-sizing: border-box;
  /* 禁止横向溢出 */
  overflow-x: hidden;
}

body::-webkit-scrollbar,
html::-webkit-scrollbar {
  width: 0 !important;
  height: 0 !important;
  background: transparent !important;
  display: none;
}

/* Firefox */
body, html {
  scrollbar-width: none;
}

#app {
  box-sizing: border-box;
  min-height: 100vh;
}

.home-container {
  position: relative;
  width: 100%;
  min-width: 100%;
  max-width: 100%;
  padding: 40px 0;
  margin: 0;
  z-index: 1;
  box-sizing: border-box;
  border: none;
}

.particles-container {
  position: fixed;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;
  border: none;
}

.particle {
  position: absolute;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  animation: float 15s infinite linear;
  border: none;
}

/* 你可以继续补充粒子的定位样式 */

@keyframes float {
  0% { transform: translateY(0) rotate(0deg); opacity: 1; }
  100% { transform: translateY(-1000px) rotate(720deg); opacity: 0; }
}

.btn-login {
  position: relative;
  padding: 15px 40px;
  background: transparent;
  border: 2px solid #00f3ff;
  color: #fff;
  font-size: 1.2rem;
  font-family: 'Orbitron', sans-serif;
  letter-spacing: 2px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
  margin-top: 30px;
  z-index: 1;
  box-shadow: 0 0 10px rgba(0, 243, 255, 0.5);
}

.btn-login:hover {
  color: #000;
  box-shadow: 0 0 20px rgba(0, 243, 255, 0.8);
}

.btn-login .liquid {
  position: absolute;
  top: -60px;
  left: 0;
  width: 100%;
  height: 200px;
  background: #00f3ff;
  transition: 0.5s;
  z-index: -1;
}

.btn-login:hover .liquid {
  top: -120px;
}

.btn-login .liquid::before,
.btn-login .liquid::after {
  content: '';
  position: absolute;
  width: 200%;
  height: 200%;
  top: 0;
  left: 50%;
  transform: translate(-50%, -75%);
}

.btn-login .liquid::before {
  border-radius: 45%;
  background: rgba(20, 20, 20, 1);
  animation: animate 5s linear infinite;
}

.btn-login .liquid::after {
  border-radius: 40%;
  background: rgba(20, 20, 20, 0.5);
  animation: animate 10s linear infinite;
}

@keyframes animate {
  0%   { transform: translate(-50%, -75%) rotate(0deg);}
  100% { transform: translate(-50%, -75%) rotate(360deg);}
}

.content {
  margin: 60px 0;
}

.content-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 600;
  background: linear-gradient(90deg, #00f3ff, #ff00de);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.content-subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 50px;
}

.features {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 30px;
  margin-top: 50px;
  width: 100vw;
  box-sizing: border-box;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  padding: 30px;
  width: 280px;
  transition: transform 0.3s, box-shadow 0.3s;
  border: 1px solid rgba(255, 255, 255, 0.2);
  margin-bottom: 20px;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
  background: rgba(255, 255, 255, 0.15);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
  color: #00f3ff;
}

.feature-card p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
}

.footer {
  margin-top: 80px;
  padding: 20px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9rem;
  width: 100vw;
}

@media (max-width: 900px) {
  .features {
    flex-direction: column;
    align-items: center;
  }
  .feature-card {
    width: 90vw;
    max-width: 340px;
  }
  .content {
    margin: 30px 0;
  }
  .content-title {
    font-size: 2rem;
  }
}

@media (max-width: 600px) {
  .header h1 {
    font-size: 2rem;
  }
  .content-title {
    font-size: 1.3rem;
  }
  .feature-card {
    padding: 18px;
    width: 96vw;
    max-width: 96vw;
  }
  .home-container {
    padding: 20px 0;
  }
}

/* ...后续样式不变... */
</style>