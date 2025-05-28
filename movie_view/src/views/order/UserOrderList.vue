<template>
  <div class="order-list-bg">
    <div class="order-list-container glass-card">
      <div class="header-bar">
        <h1 class="page-title">我的订单</h1>
        <router-link to="/userhome" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>
      <ul class="order-list">
        <li v-for="order in orderList" :key="order.id" class="order-item glass-card">
          <div class="order-info">
            <div class="order-meta">
              <span>订单号：<strong>{{ order.id }}</strong></span>
              <span>用户昵称：{{ order.userNickname }}</span>
              <span>
                排片信息：
                <template v-if="order.scheduleInfo">
                  电影: {{ order.scheduleInfo.movieName || ('电影' + order.scheduleInfo.movieId) }} |
                  影院: {{ order.scheduleInfo.cinemaName || ('影院' + order.scheduleInfo.cinemaId) }} |
                  影厅: {{ order.scheduleInfo.hallName || ('影厅' + order.scheduleInfo.hallId) }} |
                  时间: {{ formatDate(order.scheduleInfo.startTime) }} - {{ formatDate(order.scheduleInfo.endTime) }}
                </template>
                <template v-else>
                  (排片ID: {{ order.scheduleId }})
                </template>
              </span>
            </div>
            <div class="order-details">
              <span>座位：{{ order.seatNumbers }}</span>
              <span>总价：￥{{ order.totalPrice }}</span>
              <span>状态：<span :class="'status-' + order.status">{{ statusText(order.status) }}</span></span>
              <button
                v-if="order.status === 'pending'"
                class="pay-btn"
                @click="payOrder(order)"
                :disabled="payingOrderId === order.id"
              >
                {{ payingOrderId === order.id ? '支付中...' : '支付' }}
              </button>
              <button
                v-if="order.status !== 'cancelled' && order.status !== 'completed'"
                class="cancel-btn"
                @click="cancelOrder(order)"
                :disabled="cancellingOrderId === order.id"
              >
                {{ cancellingOrderId === order.id ? '取消中...' : '取消订单' }}
              </button>
            </div>
            <div class="order-time">
              <span>创建时间：{{ formatDate(order.createdAt) }}</span>
              <span>更新时间：{{ formatDate(order.updatedAt) }}</span>
            </div>
          </div>
        </li>
      </ul>
      <p v-if="orderList.length === 0" class="no-orders">没有订单数据</p>
    </div>
  </div>
</template>

<script>
import { getOrders, updateOrder } from '@/services/OrderService'
import { getUserById } from '@/services/UserService'
import { getSchedules } from '@/services/ScheduleService'
import { getCinemaDetails } from "@/services/CinemaService";
import { getMovieDetails } from "@/services/MovieService";
import { getHallDetails } from "@/services/hall";

function toDateTimeString(date) {
  const d = new Date(date)
  const pad = n => n < 10 ? '0' + n : '' + n
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
}

export default {
  data() {
    return {
      orderList: [],
      cancellingOrderId: null,
      payingOrderId: null
    }
  },
  async created() {
    await this.loadOrders()
  },
  methods: {
    async loadOrders() {
      try {
        const userIdStr = localStorage.getItem('userId')
        if (!userIdStr) {
          alert('请先登录！');
          return;
        }
        const orders = await getOrders()
        const myOrders = (orders || []).filter(o =>
          String(o.userId) === userIdStr
        )
        const mappedOrders = myOrders.map(o => ({
          id: o.id,
          userId: o.userId,
          scheduleId: o.scheduleId,
          seatNumbers: o.seatNumbers,
          totalPrice: o.totalPrice,
          status: o.status,
          createdAt: o.createdAt,
          updatedAt: o.updatedAt,
        }))

        const ordersWithNickname = await Promise.all(
          mappedOrders.map(async order => {
            try {
              const user = await getUserById(order.userId)
              return {
                ...order,
                userNickname: user.data.nickname || order.userId
              }
            } catch (e) {
              return {
                ...order,
                userNickname: order.userId
              }
            }
          })
        )

        const scheduleIds = [
          ...new Set(ordersWithNickname.map(o => o.scheduleId).filter(Boolean))
        ]

        let scheduleList = []
        try {
          scheduleList = await getSchedules()
        } catch (e) {
          scheduleList = []
        }
        const scheduleMap = {}
        for (const s of scheduleList) {
          scheduleMap[s.id] = s
        }

        const cinemaIds = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.cinemaId).filter(Boolean))]
        const movieIds  = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.movieId).filter(Boolean))]
        const hallIds   = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.hallId).filter(Boolean))]

        const cinemaCache = {}
        const movieCache = {}
        const hallCache = {}

        const cinemaPromises = cinemaIds.map(async cid => {
          try {
            const res = await getCinemaDetails(cid)
            cinemaCache[cid] = res.data.data || res.data || {}
          } catch(e) {
            cinemaCache[cid] = { name: `影院${cid}` }
          }
        })
        const moviePromises = movieIds.map(async mid => {
          try {
            const res = await getMovieDetails(mid)
            movieCache[mid] = res.data.data || res.data || {}
          } catch(e) {
            movieCache[mid] = { title: `电影${mid}` }
          }
        })
        const hallPromises = hallIds.map(async hid => {
          try {
            const res = await getHallDetails(hid)
            hallCache[hid] = res.data.data || res.data || {}
          } catch(e) {
            hallCache[hid] = { name: `影厅${hid}` }
          }
        })
        await Promise.all([...cinemaPromises, ...moviePromises, ...hallPromises])

        for (const sid of scheduleIds) {
          const s = scheduleMap[sid]
          if (!s) continue
          s.cinemaName = cinemaCache[s.cinemaId]?.name || `影院${s.cinemaId}`
          s.movieName = movieCache[s.movieId]?.title || `电影${s.movieId}`
          s.hallName = hallCache[s.hallId]?.name || `影厅${s.hallId}`
        }

        this.orderList = ordersWithNickname.map(order => ({
          ...order,
          scheduleInfo: scheduleMap[order.scheduleId]
        }))
      } catch (error) {
        console.error('加载订单列表失败', error)
      }
    },
    statusText(status) {
      switch (status) {
        case 'pending': return '待支付'
        case 'paid': return '已支付'
        case 'cancelled': return '已取消'
        case 'completed': return '已完成'
        default: return status
      }
    },
    formatDate(time) {
      if (!time) return '-'
      const d = typeof time === 'string' ? new Date(time.replace(' ', 'T')) : new Date(time)
      if (isNaN(d.getTime())) return time
      return toDateTimeString(d)
    },
    async cancelOrder(order) {
      if (this.cancellingOrderId) return;
      if (order.status === 'cancelled' || order.status === 'completed') return;
      if (!confirm('确认要取消该订单吗？')) return;
      this.cancellingOrderId = order.id;
      try {
        const userId = Number(localStorage.getItem('userId'));
        const updateData = {
          id: order.id,
          userId: userId,
          scheduleId: order.scheduleId,
          seatNumbers: order.seatNumbers,
          totalPrice: order.totalPrice,
          status: 'cancelled',
          createdAt: toDateTimeString(order.createdAt),
          updatedAt: toDateTimeString(new Date())
        };
        await updateOrder(order.id, updateData);
        this.orderList = this.orderList.map(o =>
          o.id === order.id ? { ...o, status: 'cancelled', updatedAt: new Date().toISOString() } : o
        );
      } catch (e) {
        alert('取消订单失败，请重试');
      } finally {
        this.cancellingOrderId = null;
      }
    },
    async payOrder(order) {
      if (this.payingOrderId) return;
      if (order.status !== 'pending') return;
      this.payingOrderId = order.id;
      try {
        const userId = Number(localStorage.getItem('userId'));
        const updateData = {
          id: order.id,
          userId: userId,
          scheduleId: order.scheduleId,
          seatNumbers: order.seatNumbers,
          totalPrice: order.totalPrice,
          status: 'paid',
          createdAt: toDateTimeString(order.createdAt),
          updatedAt: toDateTimeString(new Date())
        };
        await updateOrder(order.id, updateData);
        this.orderList = this.orderList.map(o =>
          o.id === order.id ? { ...o, status: 'paid', updatedAt: new Date().toISOString() } : o
        );
      } catch (e) {
        alert('支付失败，请重试');
      } finally {
        this.payingOrderId = null;
      }
    }
  }
}
</script>

<style scoped>
.order-list-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #181a21 0%, #23272f 100%);
  padding: 40px 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}
.glass-card {
  background: rgba(32, 34, 43, 0.92);
  box-shadow: 0 8px 32px #11131744, 0 1.5px 12px #6c3d2444;
  border-radius: 22px;
  border: 1.5px solid rgba(255, 200, 112, 0.08);
  backdrop-filter: blur(8px);
  transition: box-shadow 0.25s, border 0.2s;
}
.glass-card:hover {
  box-shadow: 0 12px 40px #ffd37033, 0 2px 18px #bbaa6822;
  border: 1.5px solid #ffd37033;
}

.order-list-container {
  max-width: 950px;
  width: 97vw;
  margin: 0 auto;
  padding: 34px 36px 34px 36px;
  color: #e5e5e5;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  user-select: none;
  position: relative;
  overflow: visible;
}

/* 新增 header bar */
.header-bar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 32px;
  gap: 16px;
}

.back-home-btn {
  display: inline-block;
  margin-left: 20px;
  padding: 9px 20px;
  background: linear-gradient(90deg, #ffd370 0%, #ff4b2b 100%);
  color: #222;
  font-size: 1em;
  font-weight: 700;
  border: none;
  border-radius: 16px;
  box-shadow: 0 2px 10px #ffd37033;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.17s, color 0.15s, box-shadow 0.21s;
  user-select: none;
  white-space: nowrap;
}
.back-home-btn:hover {
  background: linear-gradient(90deg, #fff176 0%, #ffd370 100%);
  color: #ff4b2b;
  box-shadow: 0 4px 18px #fff17644;
}

.page-title {
  font-size: 2.1em;
  font-weight: 900;
  text-align: center;
  margin-bottom: 0;
  text-shadow: 0 2px 22px #181a21b0, 1px 1px 8px #0008;
  letter-spacing: 2px;
  background: linear-gradient(90deg,#ffd370,#ff8c70 60%,#ff4b2b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.order-list {
  display: flex;
  flex-wrap: wrap;
  gap: 22px 22px;
  padding: 8px 0 0 0;
  list-style: none;
  margin: 0;
  justify-content: flex-start;
}
.order-item {
  flex: 0 0 400px;
  display: flex;
  flex-direction: column;
  min-height: 155px;
  margin-bottom: 10px;
  position: relative;
  transition: transform 0.23s, box-shadow 0.23s;
  overflow: hidden;
  box-sizing: border-box;
  border: 2px solid #ffd37018;
  background: rgba(38, 41, 54, 0.97);
}
.order-item:hover {
  transform: translateY(-8px) scale(1.01);
  z-index: 2;
  box-shadow: 0 10px 40px #ffd37022, 0 2px 12px #ffb44b22;
  border-color: #ffd37044;
}
.order-info {
  padding: 18px 22px 16px 22px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  color: #f5f5fa;
  box-sizing: border-box;
}
.order-meta {
  font-size: 1.08em;
  display: flex;
  gap: 20px;
  margin-bottom: 8px;
  font-weight: 700;
  flex-wrap: wrap;
  color: #ffd370;
  text-shadow: 0 1px 3px #2a1621aa;
}
.order-details {
  font-size: 1em;
  display: flex;
  gap: 20px;
  color: #ffcf6b;
  font-weight: 600;
  margin-bottom: 5px;
  flex-wrap: wrap;
  text-shadow: 0 1px 3px #23272f88;
  align-items: center;
}
.order-time {
  font-size: 0.92em;
  color: #b8a885;
  display: flex;
  gap: 20px;
  font-weight: 500;
  flex-wrap: wrap;
  text-shadow: 0 1px 2px #23272f77;
}

.status-pending { color: #ffd370; filter: drop-shadow(0 1px 4px #ffd37033);}
.status-paid { color: #47e699; filter: drop-shadow(0 1px 4px #47e69933);}
.status-cancelled { color: #ff6565; filter: drop-shadow(0 1px 4px #ff656533);}
.status-completed { color: #41c6ff; filter: drop-shadow(0 1px 4px #41c6ff33);}

.no-orders {
  text-align: center;
  font-size: 1.3em;
  margin-top: 50px;
  color: #ffd370;
  letter-spacing: 1.2px;
  background: rgba(255, 211, 112, 0.07);
  border-radius: 12px;
  padding: 28px 0;
  box-shadow: 0 2px 6px #2224;
}

.cancel-btn {
  background: #ff6565;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 4px 15px;
  font-size: 1em;
  font-weight: 700;
  cursor: pointer;
  margin-left: 12px;
  transition: background 0.21s;
  box-shadow: 0 1px 6px #ff656533;
}
.cancel-btn:hover {
  background: #ff3939;
}
.cancel-btn:disabled {
  background: #aaa;
  cursor: not-allowed;
}

/* 响应式适配 */
@media (max-width: 900px) {
  .order-list-container {
    padding: 16px 3vw;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
  }
  .order-list {
    gap: 10px 0;
    justify-content: center;
  }
  .order-item {
    flex: 0 0 96vw;
    min-width: 96vw;
    max-width: 99vw;
  }
  .back-home-btn {
    padding: 6px 12px;
    font-size: 0.93em;
    border-radius: 9px;
    margin-left: 0;
    margin-top: 4px;
  }
}
@media (max-width: 480px) {
  .order-list-container {
    padding: 7px 1vw;
    border-radius: 10px;
  }
  .order-meta,
  .order-details,
  .order-time {
    font-size: 0.96em;
    gap: 10px;
  }
  .order-info {
    padding: 8px 7px 8px 7px;
  }
  .order-item {
    padding: 0;
  }
}
</style>