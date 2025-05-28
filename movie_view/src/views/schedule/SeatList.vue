<template>
  <div class="glass-card">
    <div class="header-bar">
      <h2 class="title">座位选择</h2>
      <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
    </div>
    <div class="mode-buttons">
      <button
        :class="{ active: mode === 'occupy' }"
        @click="setMode('occupy')"
      >占用模式</button>
      <button
        :class="{ active: mode === 'release' }"
        @click="setMode('release')"
      >释放模式</button>
    </div>

    <div v-if="seats.length > 0" class="seat-grid">
      <div
        v-for="seat in seats"
        :key="seat.id"
        class="seat-wrapper"
      >
        <div
          class="seat"
          :class="{
            occupied: seat.occupied,
            selected: selectedSeatIds.includes(seat.id)
          }"
          @click="handleSeatClick(seat)"
        ></div>
        <div class="seat-label">{{ seat.seatNumber }}</div>
      </div>
    </div>
    <div v-else class="no-data">暂无座位信息</div>

    <div class="action-buttons" v-if="selectedSeatIds.length > 0">
      <button class="glow-button" @click="confirmSelection">
        {{ mode === 'occupy' ? '确认占用' : '确认释放' }}
      </button>
      <button class="cancel-button" @click="clearSelection">清除选择</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getSeatsByHallId, occupySeats, releaseSeats } from '@/services/seat';

const route = useRoute();
const hallId = parseInt(route.params.hallId);

const seats = ref([]);
const selectedSeatIds = ref([]);
const mode = ref('occupy'); // 'occupy' or 'release'
const loading = ref(false);

const setMode = (m) => {
  if (mode.value !== m) {
    mode.value = m;
    selectedSeatIds.value = [];
  }
};

const loadSeats = async () => {
  try {
    loading.value = true;
    const res = await getSeatsByHallId(hallId);
    seats.value = res || [];
  } catch (err) {
    console.error('获取座位失败:', err);
  } finally {
    loading.value = false;
  }
};

// 获取已选中的座位编号
const getSelectedSeatNumbers = () => {
  return seats.value
    .filter(seat => selectedSeatIds.value.includes(seat.id))
    .map(seat => seat.seatNumber);
};

const handleSeatClick = (seat) => {
  if (mode.value === 'occupy') {
    if (seat.occupied) return; // 不能选已占用
  } else if (mode.value === 'release') {
    if (!seat.occupied) return; // 不能选未占用
  }
  const index = selectedSeatIds.value.indexOf(seat.id);
  if (index >= 0) {
    selectedSeatIds.value.splice(index, 1);
  } else {
    selectedSeatIds.value.push(seat.id);
  }
};

const confirmSelection = async () => {
  const seatNumbers = getSelectedSeatNumbers();
  if (seatNumbers.length === 0) return;
  try {
    loading.value = true;
    if (mode.value === 'occupy') {
      await occupySeats(hallId, seatNumbers);
      alert(`已成功占用座位：${seatNumbers.join(', ')}`);
    } else {
      await releaseSeats(hallId, seatNumbers);
      alert(`已成功释放座位：${seatNumbers.join(', ')}`);
    }
    selectedSeatIds.value = [];
    await loadSeats();
  } catch (err) {
    alert('操作失败，请重试');
  } finally {
    loading.value = false;
  }
};

const clearSelection = () => {
  selectedSeatIds.value = [];
};

onMounted(loadSeats);
</script>

<style scoped>
.glass-card {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(12px);
  border-radius: 16px;
  padding: 20px;
  margin: 20px;
  color: #e0e0e0;
  box-shadow: 0 0 12px #00f7ff88;
}

/* 新增 header bar */
.header-bar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 10px;
  gap: 16px;
}
.back-home-btn {
  display: inline-block;
  margin-left: 20px;
  padding: 9px 20px;
  background: linear-gradient(90deg, #00f7ff 0%, #928dab 100%);
  color: #222;
  font-size: 1em;
  font-weight: 700;
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 10px #00f7ff33;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.17s, color 0.15s, box-shadow 0.21s;
  user-select: none;
  white-space: nowrap;
}
.back-home-btn:hover {
  background: linear-gradient(90deg, #fff176 0%, #00f7ff 100%);
  color: #928dab;
  box-shadow: 0 4px 18px #fff17644;
}

.title {
  font-size: 24px;
  margin-bottom: 0;
  color: #00f7ff;
  text-align: center;
}

.mode-buttons {
  text-align: center;
  margin-bottom: 10px;
}
.mode-buttons button {
  background: transparent;
  border: 1px solid #00f7ff;
  color: #00f7ff;
  margin: 0 5px;
  padding: 6px 18px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}
.mode-buttons button.active {
  background: #00f7ff;
  color: #111;
}

.seat-grid {
  display: grid;
  grid-template-columns: repeat(10, 1fr);
  gap: 10px;
  margin-top: 20px;
}

.seat-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.seat {
  width: 40px;
  height: 40px;
  background: #111;
  border: 1px solid #00f7ff;
  border-radius: 8px;
  cursor: pointer;
  transition: transform 0.2s, background 0.2s;
}

.seat:hover {
  transform: scale(1.1);
}

.seat.occupied {
  background: #ff4c4c;  /* 红色 */
  border-color: #ff0000;
  cursor: not-allowed;
}

.seat.selected {
  background: #00f7ff;
  font-weight: bold;
}

.seat-label {
  margin-top: 4px;
  color: #00f7ff;
  font-size: 12px;
  user-select: none;
  pointer-events: none;
}

.glow-button {
  background: #00f7ff;
  color: #000;
  border: none;
  padding: 8px 14px;
  border-radius: 8px;
  box-shadow: 0 0 10px #00f7ff;
  cursor: pointer;
  margin-right: 8px;
}

.cancel-button {
  background: transparent;
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  padding: 8px 14px;
  border-radius: 8px;
  cursor: pointer;
}

.action-buttons {
  margin-top: 20px;
  text-align: center;
}

.no-data {
  text-align: center;
  color: #999;
  margin-top: 30px;
}

@media (max-width: 600px) {
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-bottom: 7px;
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