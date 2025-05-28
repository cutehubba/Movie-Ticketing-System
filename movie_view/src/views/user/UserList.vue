<template>
  <div class="user-container">
    <div class="user-card">
      <div class="header-bar">
        <h1>👥 用户列表</h1>
        <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>
      <p v-if="users.length === 0" class="empty-text">没有用户数据</p>

      <table class="user-table">
        <thead>
          <tr>
            <th>用户名</th>
            <th>昵称</th>
            <th>邮箱</th>
            <th>角色</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.nickname || '（无）' }}</td>
            <td>{{ user.email }}</td>
            <td>
              <span :class="['role-badge', user.role]">
                {{ user.role === 'admin' ? '管理员' : '用户' }}
              </span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { getAllUsers } from "@/services/UserService";

export default {
  name: "UserList",
  data() {
    return {
      users: []
    };
  },
  async created() {
    await this.loadUsers();
  },
  methods: {
    async loadUsers() {
      try {
        const res = await getAllUsers();
        this.users = res.data;
      } catch (error) {
        console.error("用户列表加载失败：", error);
        alert("加载用户列表失败，请稍后再试！");
      }
    }
  }
};
</script>

<style scoped>
.user-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1f1f2e, #121217);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #eee;
  padding: 20px;
}

.user-card {
  background: rgba(20, 20, 30, 0.85);
  padding: 40px 30px;
  border-radius: 24px;
  width: 500px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(12px);
  text-align: center;
  border: 1px solid rgba(100, 100, 150, 0.3);
  position: relative;
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

.user-card h1 {
  font-size: 2rem;
  margin-bottom: 0;
  text-shadow: 0 0 8px #0f7bffaa;
  color: #70aaff;
}

.user-table {
  width: 100%;
  margin-top: 20px;
  border-collapse: collapse;
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.4);
}

.user-table th,
.user-table td {
  padding: 14px 20px;
  text-align: left;
  color: #eee;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.user-table th {
  background: rgba(255, 255, 255, 0.08);
  font-weight: bold;
  font-size: 1rem;
  color: #70aaff;
  text-shadow: 0 0 4px #0f7bffaa;
}

.user-table tbody tr:hover {
  background: rgba(15, 123, 255, 0.1);
}

.role-badge {
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: bold;
}

.role-badge.user {
  background-color: #4caf50;
}

.role-badge.admin {
  background-color: #f44336;
}

.empty-text {
  color: #888;
  font-style: italic;
  margin-bottom: 20px;
}

@media (max-width: 600px) {
  .user-card {
    width: 99vw;
    padding: 20px 5px;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
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