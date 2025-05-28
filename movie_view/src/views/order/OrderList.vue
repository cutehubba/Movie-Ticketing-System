<template>
  <div class="order-list-bg">
    <div class="order-list-container glass-card">
      <div class="header-bar">
        <h1 class="page-title">订单列表</h1>
        <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
      </div>
      <ul class="order-list">
        <li v-for="order in orderList" :key="order.id" class="order-item glass-card">
          <div class="order-info">
            <div class="order-meta">
              <span>订单号：<strong>{{ order.id }}</strong></span>
              <span>用户昵称：{{ order.userNickname }}</span>
              <!-- 展示排片的详细信息 -->
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
import { getOrders } from '@/services/OrderService'
import { getUserById } from '@/services/UserService'
import { getSchedules } from '@/services/ScheduleService'
import { getCinemaDetails } from "@/services/CinemaService";
import { getMovieDetails } from "@/services/MovieService";
import { getHallDetails } from "@/services/hall";

export default {
  data() {
    return {
      orderList: []
    }
  },
  async created() {
    await this.loadOrders()
  },
  methods: {
    async loadOrders() {
      try {
        const orders = await getOrders()
        const mappedOrders = (orders || []).map(o => ({
          id: o.id,
          userId: o.userId || o.user_id,
          scheduleId: o.scheduleId || o.schedule_id,
          seatNumbers: o.seatNumbers || o.seat_numbers,
          totalPrice: o.totalPrice || o.total_price,
          status: o.status,
          createdAt: o.createdAt || o.created_at,
          updatedAt: o.updatedAt || o.updated_at,
        }))

        // 1. 并行获取昵称
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

        // 2. 收集所有用到的排片ID
        const scheduleIds = [
          ...new Set(ordersWithNickname.map(o => o.scheduleId).filter(Boolean))
        ]

        // 3. 批量获取排片信息
        let scheduleList = []
        try {
          scheduleList = await getSchedules()
        } catch (e) {
          scheduleList = []
        }
        // 筛选用到的排片
        const scheduleMap = {}
        for (const s of scheduleList) {
          scheduleMap[s.id] = s
        }

        // 4. 收集所有用到的影院/电影/影厅ID
        const cinemaIds = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.cinemaId).filter(Boolean))]
        const movieIds  = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.movieId).filter(Boolean))]
        const hallIds   = [...new Set(scheduleIds.map(sid => scheduleMap[sid]?.hallId).filter(Boolean))]

        // 5. 批量请求影院、电影、影厅详情（带简单缓存）
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

        // 6. 合成排片详细信息
        for (const sid of scheduleIds) {
          const s = scheduleMap[sid]
          if (!s) continue
          s.cinemaName = cinemaCache[s.cinemaId]?.name || `影院${s.cinemaId}`
          s.movieName = movieCache[s.movieId]?.title || `电影${s.movieId}`
          s.hallName = hallCache[s.hallId]?.name || `影厅${s.hallId}`
        }

        // 7. 最终把排片详细信息挂到订单上
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
      return d.toLocaleString()
    }
  }
}
</script>

<style scoped>
/* 样式同前略 */
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
  margin-bottom: 20px;
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

/* 响应式适配 */
@media (max-width: 900px) {
  .order-list-container {
    padding: 16px 3vw;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
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