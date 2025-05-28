<template>
  <div class="cinema-container">
    <div class="cinema-card">
      <div class="header-bar">
        <h1>🎬 影院列表</h1>
        <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>

      <form @submit.prevent="addCinemaHandler" class="cinema-form">
        <input v-model="newCinema.name" placeholder="影院名称" required />
        <input v-model="newCinema.location" placeholder="影院位置" required />
        <input v-model="newCinema.contact_number" placeholder="联系电话" />
        <input v-model.number="newCinema.capacity" type="number" min="1" placeholder="容纳人数" />
        <input v-model="newCinema.description" placeholder="简介" />
        <button type="submit" :disabled="isSubmitting">
          <span v-if="isSubmitting">⏳</span>
          <span v-else>➕ 添加影院</span>
        </button>
      </form>

      <p v-if="cinemas.length === 0" class="empty-text">没有影院数据</p>

      <ul class="cinema-list">
        <li v-for="cinema in cinemas" :key="cinema.id" class="cinema-item">
          <router-link v-if="cinema.id" :to="'/cinema/' + cinema.id" class="cinema-link">
            {{ cinema.name }} - {{ cinema.location }}
          </router-link>
          <button @click="goToHallList(cinema.id)" v-if="cinema.id" class="btn hall-btn">影厅</button>
          <button @click="editCinema(cinema)" v-if="cinema.id" class="btn edit-btn">编辑</button>
          <button @click="deleteCinemaHandler(cinema.id)" v-if="cinema.id" class="btn delete-btn">删除</button>
        </li>
      </ul>

      <div v-if="editingCinema" class="edit-section">
        <h3>✏️ 编辑影院</h3>
        <input v-model="editingCinema.name" placeholder="影院名称" />
        <input v-model="editingCinema.location" placeholder="影院位置" />
        <input v-model="editingCinema.contact_number" placeholder="联系电话" />
        <input v-model.number="editingCinema.capacity" type="number" min="1" placeholder="容纳人数" />
        <input v-model="editingCinema.description" placeholder="简介" />
        <button @click="updateCinemaHandler" :disabled="isSubmitting" class="btn save-btn">
          {{ isSubmitting ? "保存中..." : "保存修改" }}
        </button>
        <button @click="cancelEdit" class="btn cancel-btn">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getCinemas,
  addCinema,
  updateCinema,
  deleteCinema
} from "@/services/CinemaService";

export default {
  name: "CinemaList",
  data() {
    return {
      cinemas: [],
      newCinema: {
        name: "",
        location: "",
        contact_number: "",
        capacity: null,
        description: ""
      },
      editingCinema: null,
      isSubmitting: false
    };
  },
  async created() {
    await this.loadCinemas();
  },
  methods: {
    goToHallList(cinemaId) {
      this.$router.push({ name: 'HallList', params: { id: cinemaId } });
    },
    async loadCinemas() {
      try {
        this.cinemas = await getCinemas();
      } catch (error) {
        console.error("影院列表加载失败：", error);
        alert("加载影院列表失败，请稍后再试！");
      }
    },
    async addCinemaHandler() {
      if (!this.newCinema.name || !this.newCinema.location) {
        alert("影院名称和位置不能为空！");
        return;
      }
      try {
        this.isSubmitting = true;
        await addCinema(this.newCinema);
        this.newCinema = {
          name: "",
          location: "",
          contact_number: "",
          capacity: null,
          description: ""
        };
        await this.loadCinemas();
      } catch (error) {
        console.error("添加影院失败：", error);
        alert("添加影院失败，请稍后再试！");
      } finally {
        this.isSubmitting = false;
      }
    },
    editCinema(cinema) {
      this.editingCinema = { ...cinema };
    },
    async updateCinemaHandler() {
      try {
        this.isSubmitting = true;
        await updateCinema(this.editingCinema.id, this.editingCinema);
        this.editingCinema = null;
        await this.loadCinemas();
      } catch (error) {
        console.error("更新影院失败：", error);
        alert("更新影院失败，请稍后再试！");
      } finally {
        this.isSubmitting = false;
      }
    },
    async deleteCinemaHandler(id) {
      try {
        if (confirm("确定要删除这个影院吗？")) {
          await deleteCinema(id);
          await this.loadCinemas();
        }
      } catch (error) {
        console.error("删除影院失败：", error);
        alert("删除影院失败，请稍后再试！");
      }
    },
    cancelEdit() {
      if (this.editingCinema && (this.editingCinema.name || this.editingCinema.location)) {
        if (confirm("您有未保存的更改，确定要取消吗？")) {
          this.editingCinema = null;
        }
      } else {
        this.editingCinema = null;
      }
    }
  }
};
</script>

<style scoped>
.cinema-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #192447 0%, #14213d 60%, #0f2027 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  color: #eee;
  padding: 20px;
  position: relative;
}
.cinema-container::before {
  content: "";
  position: absolute;
  left: 20%; top: 10%;
  width: 200px; height: 200px;
  background: radial-gradient(circle at 50% 50%, #70aaff99 0%, transparent 80%);
  filter: blur(60px);
  z-index: 0;
}
.cinema-card {
  background: rgba(20, 20, 30, 0.85);
  padding: 40px 30px;
  border-radius: 24px;
  width: 420px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(12px);
  text-align: center;
  border: 1px solid rgba(100, 100, 150, 0.3);
  position: relative;
  z-index: 1;
  transition: box-shadow 0.3s, transform 0.3s;
  animation: cardFadeIn 0.9s ease;
}
.cinema-card:hover {
  box-shadow: 0 16px 48px rgba(0, 0, 0, 0.85);
  transform: translateY(-4px) scale(1.03);
}
@keyframes cardFadeIn {
  from { opacity: 0; transform: translateY(64px) scale(0.96);}
  to { opacity: 1; transform: none; }
}
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
.cinema-card h1 {
  font-size: 2rem;
  margin-bottom: 30px;
  text-shadow: 0 0 8px #0f7bffaa;
  color: #70aaff;
}
.cinema-form input,
.edit-section input {
  width: 48%;
  padding: 14px 16px;
  margin: 0 1% 20px 1%;
  border-radius: 24px;
  border: 1.5px solid #304c89;
  outline: none;
  font-size: 1rem;
  background: linear-gradient(90deg, #182848 0%, #4062bb 100%);
  color: #fff;
  box-shadow: 0 2px 8px #0f7bff33;
  transition: background 0.3s, box-shadow 0.3s, color 0.3s;
}
.cinema-form input::placeholder,
.edit-section input::placeholder {
  color: #aaa;
}
.cinema-form input:focus,
.edit-section input:focus {
  border-color: #70aaff;
  background: linear-gradient(90deg, #1b2845 0%, #70aaff 100%);
  color: #fff;
  box-shadow: 0 0 8px #0f7bffbb;
}
.cinema-form button, .edit-section .save-btn, .cancel-btn {
  background: linear-gradient(90deg, #0f7bff 0%, #70aaff 100%);
  border-radius: 24px;
  box-shadow: 0 8px 24px #0f7bff44;
  font-size: 1.15rem;
  width: 100%;
  padding: 14px 0;
  border: none;
  color: white;
  font-weight: 700;
  cursor: pointer;
  margin-bottom: 10px;
  transition: background-color 0.3s, box-shadow 0.3s, transform 0.15s;
}
.cinema-form button:hover:not(:disabled), .edit-section .save-btn:hover:not(:disabled) {
  background: linear-gradient(90deg, #4286f4 0%, #70aaff 100%);
}
.cinema-form button:disabled, .edit-section .save-btn:disabled {
  background: #444 !important;
  box-shadow: none !important;
  cursor: not-allowed;
}
.empty-text {
  color: #888;
  font-style: italic;
  margin-bottom: 20px;
}
.cinema-list {
  list-style-type: none;
  padding: 0;
  margin-bottom: 30px;
  max-height: 300px;
  overflow-y: auto;
  scrollbar-width: thin;
  scrollbar-color: #70aaff #23233b;
}
.cinema-list::-webkit-scrollbar {
  width: 6px;
  background: #23233b;
}
.cinema-list::-webkit-scrollbar-thumb {
  background: #70aaff;
  border-radius: 8px;
}
.cinema-item {
  margin: 12px 0;
  font-size: 1.2rem;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  background: rgba(30,50,80,0.10);
  border-radius: 16px;
  padding: 8px 16px;
  transition: background 0.3s, box-shadow 0.3s;
}
.cinema-item:hover {
  background: rgba(50,120,255,0.08);
  box-shadow: 0 2px 16px #0f7bff22;
}
.cinema-link {
  text-decoration: none;
  color: #70aaff;
  text-shadow: 0 0 5px #70aaffaa;
  flex-grow: 1;
  text-align: left;
}
.cinema-link:hover {
  color: #4286f4;
  text-shadow: 0 0 8px #4286f4cc;
}
.btn {
  padding: 6px 14px;
  border-radius: 20px;
  border: none;
  font-weight: 600;
  cursor: pointer;
  color: white;
  box-shadow: 0 4px 12px rgba(15, 123, 255, 0.8);
  transition: background-color 0.3s, transform 0.15s;
}
.btn:active {
  transform: scale(0.95);
}
.edit-btn {
  background-color: #0f7bff;
}
.edit-btn:hover {
  background-color: #0a5ec7;
}
.delete-btn {
  background-color: #ff5555;
  box-shadow: 0 4px 12px rgba(255, 85, 85, 0.8);
}
.delete-btn:hover {
  background-color: #c74444;
}
.hall-btn {
  background: linear-gradient(90deg, #14a098 0%, #70e6ff 100%);
  color: #fff;
  box-shadow: 0 4px 12px #14a09855;
}
.hall-btn:hover {
  background: linear-gradient(90deg, #11998e 0%, #38ef7d 100%);
}
.edit-section {
  background: rgba(20, 20, 30, 0.75);
  padding: 20px;
  border-radius: 20px;
  box-shadow: inset 0 0 12px rgba(15, 123, 255, 0.5);
  margin-top: 20px;
}
.edit-section h3 {
  margin-bottom: 15px;
  color: #70aaff;
  text-shadow: 0 0 6px #70aaffcc;
}
.save-btn {
  background-color: #0f7bff;
  width: 48%;
}
.save-btn:hover:not(:disabled) {
  background-color: #0a5ec7;
}
.cancel-btn {
  background-color: #666;
  width: 48%;
  margin-left: 4%;
}
.cancel-btn:hover {
  background-color: #444;
}
button:disabled {
  background-color: #444 !important;
  cursor: not-allowed;
  box-shadow: none !important;
}
@media (max-width: 600px) {
  .cinema-card {
    width: 98vw;
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
  .cinema-form input, .edit-section input {
    font-size: 0.95rem;
    padding: 10px 8px;
    width: 98%;
    margin: 0 1% 12px 1%;
  }
}
</style>