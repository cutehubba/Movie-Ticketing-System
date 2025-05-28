<template>
  <div class="profile-container">
    <div class="particles-container">
      <div class="particle" v-for="i in 30" :key="i" :style="particleStyle(i)"></div>
    </div>

    <div class="profile-card">
      <h2 class="neon-text">👤 个人信息</h2>
      
      <div v-if="user" class="user-info">
        <div class="info-item">
          <span class="info-label">用户ID:</span>
          <span class="info-value">{{ user.id }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">用户名:</span>
          <span class="info-value">{{ user.username }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">昵称:</span>
          <span class="info-value">{{ user.nickname || '未设置' }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">邮箱:</span>
          <span class="info-value">{{ user.email }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">角色:</span>
          <span class="info-value role-badge" :class="user.role">
            {{ user.role === 'admin' ? '管理员' : '普通用户' }}
          </span>
        </div>
      </div>
      
      <button @click="goBack" class="btn-neon">返回主页</button>
    </div>
  </div>
</template>

<script>
import { getUserById } from '@/services/userService'

export default {
  name: 'ProfileView',
  data() {
    return {
      user: null
    }
  },
  async created() {
    const userId = this.$route.params.id
    if (userId) {
      try {
        const response = await getUserById(userId)
        if (response.code === 200) {
          this.user = response.data
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
      }
    }
  },
  methods: {
    goBack() {
      if (!this.user) {
        // fallback
        this.$router.push('/userhome')
        return
      }
      if (this.user.role === 'admin') {
        this.$router.push('/home')
      } else {
        this.$router.push('/userhome')
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
}
</script>

<style scoped>
.profile-container {
  font-family: 'Montserrat', sans-serif;
  min-height: 100vh;
  color: #fff;
  background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
  padding: 40px 20px;
  position: relative;
  overflow-x: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1;
}

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

.profile-card {
  position: relative;
  z-index: 2;
  background: rgba(20, 20, 30, 0.85);
  padding: 40px 30px;
  border-radius: 24px;
  width: 500px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(12px);
  text-align: center;
  border: 1px solid rgba(100, 100, 150, 0.3);
}

.neon-text {
  font-family: 'Orbitron', sans-serif;
  font-size: 2.5rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  animation: neon 1.5s ease-in-out infinite alternate;
  text-shadow:
    0 0 5px #00f3ff,
    0 0 10px #00f3ff,
    0 0 20px #ff00de,
    0 0 30px #ff00de;
  margin-bottom: 30px;
}

.user-info {
  text-align: left;
  margin-bottom: 40px;
}

.info-item {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid rgba(100, 100, 150, 0.3);
}

.info-label {
  display: inline-block;
  width: 80px;
  color: #00f3ff;
  font-weight: 600;
}

.info-value {
  color: #fff;
}

.role-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
}

.role-badge.user {
  background-color: rgba(76, 175, 80, 0.2);
  color: #4caf50;
  border: 1px solid #4caf50;
}

.role-badge.admin {
  background-color: rgba(244, 67, 54, 0.2);
  color: #f44336;
  border: 1px solid #f44336;
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
  margin-top: 20px;
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
</style>