<template>
  <div class="glass-bg">
    <div class="glass-card main-card">
      <h2 class="title">影厅管理</h2>

      <!-- 添加影厅 -->
      <div class="form-section">
        <input v-model="newHall.name" placeholder="影厅名称" class="input" />
        <input v-model.number="newHall.seatCount" placeholder="座位数" type="number" class="input" />
        <textarea v-model="newHall.description" placeholder="影厅描述" class="textarea"></textarea>
        <button @click="addHall" class="glow-button">
          <span>➕ 添加影厅</span>
        </button>
      </div>

      <!-- 编辑影厅 -->
      <transition name="fade">
        <div v-if="editingHall" class="form-section edit-section">
          <h3>编辑影厅</h3>
          <input v-model="editingHall.name" placeholder="影厅名称" class="input" />
          <input v-model.number="editingHall.seatCount" placeholder="座位数" type="number" class="input" />
          <textarea v-model="editingHall.description" placeholder="影厅描述" class="textarea"></textarea>
          <div class="edit-btn-row">
            <button @click="updateHallData" class="glow-button">💾 保存修改</button>
            <button @click="cancelEdit" class="cancel-button">取消</button>
          </div>
        </div>
      </transition>

      <!-- 影厅列表 -->
      <div v-if="halls.length > 0" class="hall-list">
        <transition-group name="fade" tag="div">
          <div v-for="hall in halls" :key="hall.id" class="hall-item glass-card">
            <div class="hall-header">
              <h3>{{ hall.name }}</h3>
              <span class="seat-badge">🪑 {{ hall.seatCount }}</span>
            </div>
            <p class="hall-desc">{{ hall.description }}</p>
            <div class="action-buttons">
              <button @click="editHall(hall)" class="glow-button">✏️ 编辑</button>
              <button @click="deleteHallById(hall.id)" class="cancel-button">🗑 删除</button>
            </div>
          </div>
        </transition-group>
      </div>

      <div v-else class="no-data">
        <svg width="42" height="42" fill="none" viewBox="0 0 24 24">
          <circle cx="12" cy="12" r="10" fill="#00f7ff22"/>
          <path d="M8 12h8M8 16h5M9 8h6" stroke="#00f7ff" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <div>该影院暂无影厅</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getHallsByCinemaId, createHall, updateHall, deleteHall } from '@/services/hall';

const route = useRoute();
const cinemaId = route.params.id;

const halls = ref([]);
const newHall = ref({
  name: '',
  seatCount: 0,
  description: '',
  cinemaId: parseInt(cinemaId)
});

const editingHall = ref(null);

const loadHalls = async () => {
  halls.value = await getHallsByCinemaId(cinemaId);
};

const addHall = async () => {
  if (!newHall.value.name.trim() || newHall.value.seatCount <= 0) {
    alert('请输入有效的影厅名称和座位数');
    return;
  }
  try {
    await createHall(newHall.value);
    await loadHalls();
    newHall.value = { name: '', seatCount: 0, description: '', cinemaId: parseInt(cinemaId) };
  } catch (error) {
    console.error('添加影厅失败:', error);
  }
};
const editHall = (hall) => {
  editingHall.value = { ...hall };
};

const updateHallData = async () => {
  if (!editingHall.value.name.trim() || editingHall.value.seatCount <= 0) {
    alert('请输入有效的影厅名称和座位数');
    return;
  }
  await updateHall(editingHall.value.id, editingHall.value);
  editingHall.value = null;
  await loadHalls();
};

const deleteHallById = async (id) => {
  if (!confirm('确认删除该影厅？')) return;
  await deleteHall(id);
  halls.value = halls.value.filter(hall => hall.id !== id);
};

const cancelEdit = () => {
  editingHall.value = null;
};

onMounted(loadHalls);
</script>

<style scoped>
/* 背景和主卡片 */
.glass-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #0a2a43 0%, #15253c 60%, #1b4266 100%);
  display: flex;
  align-items: flex-start;
  justify-content: center;
  padding: 40px 0;
  position: relative;
  width: 100vw;
  box-sizing: border-box;
  overflow-x: hidden;
}
.glass-bg::before {
  content: "";
  position: absolute;
  left: 10vw;
  top: 10vh;
  width: 240px;
  height: 240px;
  background: radial-gradient(circle at 50% 50%, #00f7ff55 0%, transparent 80%);
  filter: blur(60px);
  z-index: 0;
  pointer-events: none;
}

.main-card {
  max-width: 520px;
  width: 100%;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  padding: 36px 30px 30px 30px;
  border-radius: 24px;
  box-shadow: 0 10px 32px #00f7ff33, 0 1.5px 12px #1b426688;
  background: rgba(25,29,40,0.82);
  animation: cardFadeIn 0.7s;
  box-sizing: border-box;
  overflow: visible;
}

@keyframes cardFadeIn {
  from { opacity: 0; transform: translateY(40px) scale(0.96);}
  to { opacity: 1; transform: none; }
}

.title {
  font-size: 2rem;
  margin-bottom: 24px;
  color: #00f7ff;
  text-shadow: 0 0 10px #00f7ff44;
  letter-spacing: 2px;
  text-align: center;
}

/* 表单区 */
.form-section {
  background: rgba(255,255,255,0.06);
  border-radius: 14px;
  padding: 18px 14px 12px 14px;
  margin-bottom: 22px;
  box-shadow: 0 2px 12px #00f7ff22 inset;
  box-sizing: border-box;
  overflow: visible;
}

.input,
.textarea {
  display: block;
  margin-bottom: 12px;
  width: 100%;
  padding: 12px 14px;
  background: linear-gradient(90deg,#1b2845 0%, #4062bb 100%);
  border: 1.5px solid #00f7ffcc;
  border-radius: 10px;
  color: #fff;
  font-size: 1rem;
  transition: box-shadow 0.2s, border-color 0.2s, background 0.2s;
  box-shadow: 0 1.5px 6px #00f7ff33;
  outline: none;
  box-sizing: border-box;
  word-break: break-all;
  overflow-x: auto;
}

.input:focus, .textarea:focus {
  border-color: #fff;
  background: linear-gradient(90deg, #1b2845 0%, #00f7ff 80%);
  box-shadow: 0 0 12px #00f7ff99;
  color: #fff;
}

.textarea {
  min-height: 60px;
  resize: vertical;
}

/* 按钮 */
.glow-button {
  background: linear-gradient(90deg,#00f7ff 0%,#70e6ff 100%);
  color: #14213d;
  border: none;
  padding: 10px 20px;
  border-radius: 16px;
  box-shadow: 0 2px 16px #00f7ffbb, 0 1px 8px #4062bb44;
  cursor: pointer;
  font-size: 1.08rem;
  font-weight: 600;
  margin-right: 8px;
  margin-bottom: 5px;
  transition: background 0.21s, transform 0.14s;
}
.glow-button:hover {
  background: linear-gradient(90deg,#00d6e6 0%,#70e6ff 100%);
  transform: translateY(-1px) scale(1.04);
}

.cancel-button {
  background: transparent;
  color: #ff6b6b;
  border: 1.5px solid #ff6b6b;
  padding: 10px 20px;
  border-radius: 16px;
  cursor: pointer;
  font-size: 1.05rem;
  font-weight: 600;
  margin-bottom: 5px;
  transition: background 0.15s, border 0.15s, color 0.15s;
}
.cancel-button:hover {
  background: #ff6b6b22;
  color: #fff;
  border-color: #fff;
}

.edit-section {
  margin-top: 10px;
  box-shadow: 0 2px 16px #00f7ff22 inset;
}

.edit-btn-row {
  display: flex;
  gap: 12px;
  margin-top: 8px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.hall-list {
  margin-top: 22px;
  width: 100%;
  box-sizing: border-box;
  overflow: visible;
}
.hall-item {
  margin-top: 18px;
  padding: 18px 18px 10px 18px;
  border-radius: 18px;
  background: rgba(18,23,38,0.83);
  box-shadow: 0 4px 24px #00f7ff22, 0 1.5px 10px #4062bb22;
  position: relative;
  animation: fadeInItem 0.5s;
  box-sizing: border-box;
  overflow-wrap: break-word;
  word-break: break-all;
  overflow-x: auto;
}
@keyframes fadeInItem {
  from { opacity: 0; transform: translateY(24px);}
  to { opacity: 1; transform: none; }
}
.hall-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
  flex-wrap: wrap;
}
.seat-badge {
  background: linear-gradient(90deg,#00f7ff 0%,#70e6ff 100%);
  color: #14213d;
  font-weight: bold;
  border-radius: 10px;
  padding: 2px 14px;
  font-size: 1.05rem;
  box-shadow: 0 2px 8px #00f7ff33;
  margin-left: 12px;
  white-space: nowrap;
}
.hall-desc {
  color: #b9e9f7;
  font-size: 1rem;
  margin: 6px 0 2px 0;
  word-break: break-all;
  white-space: pre-wrap;
}

.action-buttons {
  margin-top: 10px;
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

/* 空数据提示 */
.no-data {
  text-align: center;
  color: #00f7ffbb;
  margin-top: 40px;
  font-size: 1.15rem;
  opacity: 0.84;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 动画 */
.fade-enter-active, .fade-leave-active,
.fade-move {
  transition: all 0.38s cubic-bezier(.55,0,.1,1);
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
  transform: translateY(18px) scale(0.98);
}

/* 响应式防止溢出，优化小屏体验 */
@media (max-width: 800px) {
  .main-card {
    max-width: 99vw;
    padding: 16px 2vw 18px 2vw;
  }
  .form-section, .hall-item {
    padding: 10px 5px 8px 5px;
  }
  .input, .textarea {
    font-size: 0.97rem;
    padding: 8px 6px;
  }
}
@media (max-width: 480px) {
  .main-card {
    max-width: 100vw;
    padding: 7px 1vw 8px 1vw;
    border-radius: 10px;
  }
  .hall-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 2px;
  }
}
</style>