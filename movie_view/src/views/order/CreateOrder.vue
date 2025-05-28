<template>
  <div class="glass-card">
    <h2 class="title">订单详情</h2>
    <table class="order-table" v-if="order">
      <tbody>
        <tr>
          <th>用户昵称</th>
          <td>{{ user?.data?.nickname || order.userId }}</td>
        </tr>
        <tr>
          <th>电影名</th>
          <td>{{ movie?.data?.title || schedule?.movieId || order.scheduleId }}</td>
        </tr>
        <tr>
          <th>放映厅</th>
          <td>{{ hall?.name || schedule?.hallId || '-' }}</td>
        </tr>
        <tr>
          <th>开场时间</th>
          <td>{{ schedule?.startTime || '-' }}</td>
        </tr>
        <tr>
          <th>座位号</th>
          <td>{{ order.seatNumbers }}</td>
        </tr>
        <tr>
          <th>总价</th>
          <td>{{ order.totalPrice }}</td>
        </tr>
        <tr>
          <th>订单状态</th>
          <td>{{ showStatus(order.status) }}</td>
        </tr>
        <tr>
          <th>创建时间</th>
          <td>{{ order.createdAt }}</td>
        </tr>
        <tr>
          <th>更新时间</th>
          <td>{{ order.updatedAt }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else class="no-data">暂无订单数据</div>
    <div class="action-buttons" v-if="order">
      <button
        class="glow-button"
        :disabled="order.status !== 'pending' || loading"
        @click="payOrder"
      >
        确认支付
      </button>
      <button
        class="cancel-button"
        :disabled="order.status !== 'pending' || loading"
        @click="cancelOrder"
      >
        撤回订单
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { createOrder, updateOrder, deleteOrder } from '@/services/OrderService';
import { getUserById } from '@/services/UserService';
import { getScheduleDetails } from '@/services/ScheduleService';
import { getMovieDetails } from '@/services/MovieService';
import { getHallDetails } from '@/services/hall';

function getNowStr() {
  const now = new Date();
  return now.toISOString().replace('T', ' ').substring(0, 19);
}

const route = useRoute();
const router = useRouter();
const scheduleId = Number(route.query.id) || 1;
const seatNumbers = route.query.seat_numbers || 'A1,A2';
const seatCount = parseInt(route.query.seat_count) || (seatNumbers ? seatNumbers.split(',').length : 1);
const userId = Number(localStorage.getItem('userId')) || 12345;

const order = ref(null);
const user = ref(null);
const schedule = ref(null);
const movie = ref(null);
const hall = ref(null);
const loading = ref(false);

// 记录表单创建时的时间
const formCreatedTime = getNowStr();

onMounted(async () => {
  user.value = await getUserById(userId);
  schedule.value = await getScheduleDetails(scheduleId);

  if (schedule.value) {
    if (schedule.value.movieId) {
      movie.value = await getMovieDetails(schedule.value.movieId);
    }
    if (schedule.value.hallId) {
      hall.value = await getHallDetails(schedule.value.hallId);
    }
  }

  // 使用表单创建时间
  const tmpOrder = {
    userId,
    scheduleId,
    seatNumbers,
    totalPrice: schedule.value && schedule.value.price ? schedule.value.price * seatCount : 0,
    status: 'pending',
    createdAt: formCreatedTime,
    updatedAt: formCreatedTime,
  };

  loading.value = true;
  try {
    const created = await createOrder(tmpOrder);
    console.log('createOrder 返回：', created);

    // 兼容后端返回结构，提取id
    let id = created.id;
    if (!id && created.data) id = created.data.id;
    if (!id && created.data?.data) id = created.data.data.id;

    // 组装 order，确保id一定有
    order.value = {
      ...(created.data?.data || created.data || created),
      id,
      createdAt: (created.data?.data?.createdAt || created.data?.createdAt || created.createdAt || formCreatedTime),
      updatedAt: (created.data?.data?.updatedAt || created.data?.updatedAt || created.updatedAt || formCreatedTime),
    };

  } catch (e) {
    alert('订单创建失败！');
  } finally {
    loading.value = false;
  }
});

function showStatus(status) {
  switch (status) {
    case 'pending': return '待支付';
    case 'paid': return '已支付';
    case 'cancelled': return '已撤回';
    case 'completed': return '已完成';
    default: return status;
  }
}

const payOrder = async () => {
  if (!order.value || order.value.status !== 'pending') return;
  if (!window.confirm('确认支付该订单吗？')) return;

  loading.value = true;
  try {
    const nowStr = getNowStr();
    const updated = await updateOrder(order.value.id, {
      ...order.value,
      status: 'paid',
      updatedAt: nowStr,
    });
    order.value = updated;
    order.value.updatedAt = nowStr;
    alert('支付成功！');
    router.push('/userhome'); // 支付成功后跳转
  } catch (e) {
    alert('支付失败，请重试');
  } finally {
    loading.value = false;
  }
};

const cancelOrder = async () => {
  if (!order.value || order.value.status !== 'pending') return;
  if (!window.confirm('是否撤回该订单？')) return;
  loading.value = true;
  try {
    await deleteOrder(order.value.id);
    order.value = null;
    alert('订单已撤回');
    router.push('/userhome'); // 撤回成功后跳转
  } catch (e) {
    alert('撤回失败，请重试');
  } finally {
    loading.value = false;
  }
};
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
.title {
  font-size: 24px;
  margin-bottom: 12px;
  color: #00f7ff;
  text-align: center;
}
.order-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 18px;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  overflow: hidden;
}
.order-table th, .order-table td {
  padding: 10px 14px;
  border-bottom: 1px solid #00f7ff22;
  color: #e0e0e0;
  text-align: left;
}
.order-table th {
  color: #00f7ff;
  background: rgba(0, 247, 255, 0.05);
  width: 120px;
  font-weight: 600;
}
.order-table tr:last-child td, .order-table tr:last-child th {
  border-bottom: none;
}
.no-data {
  text-align: center;
  color: #999;
  margin-top: 30px;
}
.action-buttons {
  margin-top: 20px;
  text-align: center;
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
  transition: 0.2s;
}
.glow-button:disabled {
  background: #00f7ff55;
  cursor: not-allowed;
}
.cancel-button {
  background: transparent;
  color: #ff6b6b;
  border: 1px solid #ff6b6b;
  padding: 8px 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: 0.2s;
}
.cancel-button:disabled {
  color: #ff6b6b55;
  border-color: #ff6b6b55;
  cursor: not-allowed;
}
</style>