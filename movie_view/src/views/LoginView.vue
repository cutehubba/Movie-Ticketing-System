<template>
  <div class="login-bg">
    <div class="particle-bg">
      <div class="particle" v-for="i in 16" :key="i"></div>
    </div>
    <div class="login-card">
      <h2>🔐 用户登录</h2>
      <input v-model="username" placeholder="请输入用户名" />
      <input v-model="password" type="password" placeholder="请输入密码" />

      <!-- 角色选择 -->
      <select v-model="role" class="role-select">
        <option value="user">普通用户</option>
        <option value="admin">管理员</option>
      </select>

      <button @click="login" :disabled="loading" class="glow-btn">
        <span v-if="loading">⏳ 正在登录...</span>
        <span v-else>登录</span>
      </button>

      <!-- 注册按钮 -->
      <button @click="goToRegister" class="back-btn">还没有账号？点我注册</button>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginUser } from '@/services/userService'

const username = ref('')
const password = ref('')
const role = ref('user')
const errorMessage = ref('')
const loading = ref(false)
const router = useRouter()

const login = async () => {
  if (!username.value || !password.value) {
    errorMessage.value = "用户名和密码不能为空"
    return
  }
  loading.value = true
  errorMessage.value = ''
  try {
    const res = await loginUser({
      username: username.value,
      password: password.value,
      role: role.value
    })
    if (res.code === 200) {
      const token = res.data.token
      const userId = res.data.user.id
      const nickname = res.data.user.nickname

      localStorage.setItem('token', token)
      localStorage.setItem('userId', userId)
      localStorage.setItem('nickname', nickname)

      if (role.value === 'admin') {
        router.push('/home')
      } else {
        router.push('/userhome')
      }
    } else {
      errorMessage.value = res.data.message || '登录失败'
    }
  } catch (err) {
    errorMessage.value = '登录请求失败，请检查服务器'
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-bg {
  min-height: 100vh;
  width: 100vw;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: linear-gradient(135deg, #192447 0%, #14213d 100%);
}

.particle-bg {
  position: absolute;
  top: 0; left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 0;
  overflow: hidden;
  pointer-events: none;
}

.particle {
  position: absolute;
  background: rgba(112, 170, 255, 0.18);
  border-radius: 50%;
  opacity: 0.66;
  animation: floatUp 11s infinite linear;
}
.particle:nth-child(1)  { width: 38px; height: 38px; top: 18%; left: 12%; animation-delay: 0s; }
.particle:nth-child(2)  { width: 18px; height: 18px; top: 55%; left: 7%; animation-delay: 2s; }
.particle:nth-child(3)  { width: 24px; height: 24px; top: 40%; left: 80%; animation-delay: 3.2s;}
.particle:nth-child(4)  { width: 38px; height: 38px; top: 80%; left: 53%; animation-delay: 1.1s;}
.particle:nth-child(5)  { width: 40px; height: 40px; top: 70%; left: 85%; animation-delay: 0.7s;}
.particle:nth-child(6)  { width: 22px; height: 22px; top: 60%; left: 35%; animation-delay: 1.7s;}
.particle:nth-child(7)  { width: 30px; height: 30px; top: 22%; left: 67%; animation-delay: 2.8s;}
.particle:nth-child(8)  { width: 15px; height: 15px; top: 85%; left: 18%; animation-delay: 1.3s;}
.particle:nth-child(9)  { width: 38px; height: 38px; top: 30%; left: 73%; animation-delay: 2.5s;}
.particle:nth-child(10) { width: 26px; height: 26px; top: 77%; left: 41%; animation-delay: 0.9s;}
.particle:nth-child(11) { width: 12px; height: 12px; top: 67%; left: 59%; animation-delay: 1.6s;}
.particle:nth-child(12) { width: 21px; height: 21px; top: 53%; left: 90%; animation-delay: 2.2s;}
.particle:nth-child(13) { width: 26px; height: 26px; top: 13%; left: 31%; animation-delay: 2.7s;}
.particle:nth-child(14) { width: 34px; height: 34px; top: 38%; left: 23%; animation-delay: 1.1s;}
.particle:nth-child(15) { width: 12px; height: 12px; top: 92%; left: 72%; animation-delay: 3.7s;}
.particle:nth-child(16) { width: 18px; height: 18px; top: 80%; left: 93%; animation-delay: 1.8s;}

@keyframes floatUp {
  0%   { transform: translateY(0) scale(1);}
  100% { transform: translateY(-120px) scale(1.12);}
}

.login-card {
  background: rgba(20, 20, 30, 0.90);
  padding: 44px 34px 36px 34px;
  border-radius: 26px;
  width: 370px;
  box-shadow: 0 10px 36px #0f7bff44, 0 1.5px 12px #1b426688;
  backdrop-filter: blur(14px);
  text-align: center;
  border: 1.5px solid rgba(100, 180, 255, 0.18);
  z-index: 2;
  animation: fadeInPop 0.7s;
}

@keyframes fadeInPop {
  from { opacity: 0; transform: scale(0.92) translateY(40px);}
  to   { opacity: 1; transform: none;}
}

.login-card {
  background: rgba(20, 20, 30, 0.90);
  padding: 44px 34px 36px 34px;
  border-radius: 26px;
  width: 370px;
  max-width: 95vw;
  box-sizing: border-box; /* 关键 */
  box-shadow: 0 10px 36px #0f7bff44, 0 1.5px 12px #1b426688;
  backdrop-filter: blur(14px);
  text-align: center;
  border: 1.5px solid rgba(100, 180, 255, 0.18);
  z-index: 2;
  animation: fadeInPop 0.7s;
}

.login-card input,
.login-card select {
  width: 100%;
  box-sizing: border-box; /* 关键 */
  padding: 15px 18px;
  margin-bottom: 20px;
  border-radius: 14px;
  border: none;
  outline: none;
  font-size: 1.08rem;
  background: linear-gradient(90deg, rgba(255,255,255,0.08) 0%, rgba(112,170,255,0.06) 100%);
  color: #eee;
  box-shadow: 0 1.5px 8px #0f7bff33 inset;
  transition: background 0.3s, box-shadow 0.2s, color 0.2s;
}

@media (max-width: 500px) {
  .login-card {
    width: 98vw;
    max-width: 98vw;
    padding: 24px 2vw 18px 2vw;
  }
}

.login-card input::placeholder {
  color: #aaa;
}

.login-card input:focus,
.login-card select:focus {
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 0 8px #0f7bffbb;
  color: #d0e7ff;
}

.login-card select {
  color: #70aaff;
  font-weight: 600;
  background: linear-gradient(90deg,rgba(112,170,255,0.07) 0%,rgba(255,255,255,0.10) 100%);
}

.glow-btn {
  width: 100%;
  padding: 15px 0;
  background: linear-gradient(90deg,#0f7bff 0%,#70aaff 100%);
  border: none;
  border-radius: 32px;
  color: white;
  font-size: 1.15rem;
  font-weight: 700;
  cursor: pointer;
  box-shadow: 0 7px 22px #0f7bff99;
  transition: background 0.3s, box-shadow 0.2s;
  margin-bottom: 10px;
}

.glow-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #4286f4 0%, #70aaff 100%);
  box-shadow: 0 8px 26px #70aaff66;
}

.glow-btn:disabled {
  background: #444;
  cursor: not-allowed;
  box-shadow: none;
}

.back-btn {
  margin-top: 16px;
  background: transparent;
  border: none;
  color: #70aaff;
  font-weight: 600;
  cursor: pointer;
  text-decoration: underline;
  font-size: 0.97rem;
  transition: color 0.3s;
}

.back-btn:hover {
  color: #4286f4;
}

.error {
  margin-top: 18px;
  color: #ff6b6b;
  font-weight: 600;
  text-shadow: 0 0 5px #ff6b6baa;
  font-size: 1.05rem;
  letter-spacing: 1.2px;
}

@media (max-width: 500px) {
  .login-card {
    width: 96vw;
    padding: 30px 6vw 18px 6vw;
  }
  .login-bg {
    padding: 0;
  }
}
</style>