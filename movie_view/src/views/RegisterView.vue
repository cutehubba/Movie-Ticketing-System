<template>
  <div class="register-bg">
    <div class="particle-bg">
      <div class="particle" v-for="i in 14" :key="i"></div>
    </div>
    <div class="register-card">
      <h2>📝 用户注册</h2>
      <input v-model="username" placeholder="请输入用户名" />
      <input v-model="nickname" placeholder="请输入昵称" />
      <input v-model="password" type="password" placeholder="请输入密码" />
      <input v-model="email" type="email" placeholder="请输入邮箱" />
      <select v-model="role" class="role-select">
        <option value="user">普通用户</option>
        <option value="admin">管理员</option>
      </select>
      <button @click="register" :disabled="loading" class="glow-btn">
        <span v-if="loading">⏳ 正在注册...</span>
        <span v-else>注册</span>
      </button>
      <button @click="goToLogin" class="back-btn">已有账号？返回登录</button>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerUser } from '@/services/userService'

const username = ref('')
const nickname = ref('')
const password = ref('')
const email = ref('')
const role = ref('user')
const errorMessage = ref('')
const loading = ref(false)
const router = useRouter()

const register = async () => {
  if (!username.value || !nickname.value || !password.value || !email.value) {
    errorMessage.value = '请填写完整的注册信息'
    return
  }
  loading.value = true
  errorMessage.value = ''
  try {
    const res = await registerUser({
      username: username.value,
      nickname: nickname.value,
      password: password.value,
      email: email.value,
      role: role.value
    })
    if (res.code === 200 || res.success) {
      alert('注册成功！请登录～')
      router.push('/login')
    } else {
      errorMessage.value = res.message || '注册失败'
    }
  } catch (err) {
    if (err.response && err.response.data) {
      const data = err.response.data
      const firstKey = Object.keys(data)[0]
      errorMessage.value = data[firstKey] || '注册失败'
    } else {
      errorMessage.value = '注册请求失败，请检查网络或服务器'
    }
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-bg {
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
  background: rgba(112, 170, 255, 0.15);
  border-radius: 50%;
  opacity: 0.55;
  animation: floatUp 13s infinite linear;
}
.particle:nth-child(1)  { width: 42px; height: 42px; top: 14%; left: 10%; animation-delay: 0s; }
.particle:nth-child(2)  { width: 18px; height: 18px; top: 54%; left: 5%; animation-delay: 2s; }
.particle:nth-child(3)  { width: 22px; height: 22px; top: 35%; left: 85%; animation-delay: 3.2s;}
.particle:nth-child(4)  { width: 37px; height: 37px; top: 80%; left: 50%; animation-delay: 1.1s;}
.particle:nth-child(5)  { width: 29px; height: 29px; top: 70%; left: 80%; animation-delay: 0.7s;}
.particle:nth-child(6)  { width: 20px; height: 20px; top: 60%; left: 32%; animation-delay: 1.7s;}
.particle:nth-child(7)  { width: 32px; height: 32px; top: 20%; left: 67%; animation-delay: 2.8s;}
.particle:nth-child(8)  { width: 15px; height: 15px; top: 85%; left: 18%; animation-delay: 1.3s;}
.particle:nth-child(9)  { width: 37px; height: 37px; top: 30%; left: 73%; animation-delay: 2.5s;}
.particle:nth-child(10) { width: 28px; height: 28px; top: 78%; left: 41%; animation-delay: 0.9s;}
.particle:nth-child(11) { width: 13px; height: 13px; top: 67%; left: 59%; animation-delay: 1.6s;}
.particle:nth-child(12) { width: 19px; height: 19px; top: 53%; left: 90%; animation-delay: 2.2s;}
.particle:nth-child(13) { width: 25px; height: 25px; top: 17%; left: 31%; animation-delay: 2.7s;}
.particle:nth-child(14) { width: 21px; height: 21px; top: 38%; left: 23%; animation-delay: 1.1s;}
@keyframes floatUp {
  0%   { transform: translateY(0) scale(1);}
  100% { transform: translateY(-120px) scale(1.12);}
}

.register-card {
  background: rgba(20, 20, 30, 0.92);
  padding: 44px 34px 36px 34px;
  border-radius: 26px;
  width: 370px;
  max-width: 95vw;
  box-sizing: border-box;
  box-shadow: 0 10px 36px #0f7bff33, 0 1.5px 12px #1b426688;
  backdrop-filter: blur(14px);
  text-align: center;
  border: 1.5px solid rgba(100, 180, 255, 0.15);
  z-index: 2;
  animation: fadeInPop 0.8s;
}

@keyframes fadeInPop {
  from { opacity: 0; transform: scale(0.92) translateY(40px);}
  to   { opacity: 1; transform: none;}
}

.register-card h2 {
  font-size: 2rem;
  margin-bottom: 30px;
  text-shadow: 0 0 8px #0f7bff88;
  color: #70aaff;
  letter-spacing: 2px;
}

.register-card input,
.register-card select {
  width: 100%;
  box-sizing: border-box;
  padding: 15px 18px;
  margin-bottom: 20px;
  border-radius: 14px;
  border: none;
  outline: none;
  font-size: 1.08rem;
  background: linear-gradient(90deg, rgba(255,255,255,0.09) 0%, rgba(112,170,255,0.08) 100%);
  color: #eee;
  box-shadow: 0 1.5px 8px #0f7bff22 inset;
  transition: background 0.3s, box-shadow 0.2s, color 0.2s;
}

.register-card input::placeholder {
  color: #aaa;
}

.register-card input:focus,
.register-card select:focus {
  background: rgba(255, 255, 255, 0.18);
  box-shadow: 0 0 8px #0f7bffbb;
  color: #d0e7ff;
}

.register-card select {
  color: #70aaff;
  font-weight: 600;
  background: linear-gradient(90deg,rgba(112,170,255,0.12) 0%,rgba(255,255,255,0.14) 100%);
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
  .register-card {
    width: 98vw;
    max-width: 98vw;
    padding: 24px 2vw 18px 2vw;
  }
}
</style>