<template>
  <div class="cinema-detail-glass">
    <div class="cinema-detail-card">
      <h1 v-if="cinema.name" class="cinema-title">{{ cinema.name }}</h1>
      <p v-if="cinema.location" class="cinema-location">
        <span class="icon">📍</span>地址：{{ cinema.location }}
      </p>
      <p v-if="cinema.contact_number" class="cinema-contact">
        <span class="icon">📞</span>联系电话：{{ cinema.contact_number }}
      </p>
      <p v-if="cinema.capacity !== undefined && cinema.capacity !== null" class="cinema-capacity">
        <span class="icon">👥</span>容量：{{ cinema.capacity }} 人
      </p>
      <p v-if="cinema.description" class="cinema-desc">
        <span class="icon">🎬</span>介绍：{{ cinema.description }}
      </p>
      <p v-if="loading" class="loading-text">加载中...</p>
      <p v-if="error" class="error-text">加载失败：{{ error }}</p>
    </div>
  </div>
</template>

<script>
import { getCinemaDetails } from "@/services/CinemaService";

export default {
  name: "CinemaDetail",
  data() {
    return {
      cinema: {},
      loading: false,
      error: ""
    };
  },
  async created() {
    const cinemaId = this.$route.params.id;
    if (!cinemaId) {
      this.error = "没有传入影院ID";
      return;
    }
    this.loading = true;
    try {
      const response = await getCinemaDetails(cinemaId);
      this.cinema = response.data || {};
    } catch (err) {
      this.error = err.message || "请求影院详情出错";
      console.error(err);
    } finally {
      this.loading = false;
    }
  }
};
</script>

<style scoped>
.cinema-detail-glass {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  background: linear-gradient(135deg, #1f1f2e 0%, #121217 100%);
  padding: 24px 0;
}

.cinema-detail-card {
  background: rgba(32, 42, 66, 0.9);
  padding: 44px 38px 36px 38px;
  border-radius: 26px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.65), 0 1.5px 12px #1b426688;
  backdrop-filter: blur(10px);
  min-width: 320px;
  max-width: 440px;
  width: 95vw;
  margin: 0 auto;
  border: 1.5px solid rgba(100, 180, 255, 0.16);
  text-align: center;
  animation: fadeInUp 0.6s;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(40px);}
  to { opacity: 1; transform: none;}
}

.cinema-title {
  font-size: 2.3em;
  color: #5fd6ff;
  font-family: 'Segoe UI', 'Arial', sans-serif;
  text-shadow: 0 2px 16px #0f7bff33;
  margin-bottom: 20px;
  letter-spacing: 2px;
}

.cinema-location,
.cinema-contact,
.cinema-capacity,
.cinema-desc {
  font-size: 1.15em;
  margin: 18px 0;
  color: #e2f1ff;
  background: rgba(100, 180, 255, 0.09);
  border-radius: 12px;
  padding: 12px 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 12px #0f7bff11 inset;
  word-break: break-all;
}

.icon {
  margin-right: 8px;
  font-size: 1.2em;
  filter: drop-shadow(0 0 4px #0f7bff77);
}

.loading-text {
  color: #70aaff;
  font-size: 1.1em;
  margin-top: 24px;
  letter-spacing: 2px;
}

.error-text {
  color: #ff5a5a;
  font-weight: bold;
  font-size: 1.1em;
  margin-top: 24px;
  letter-spacing: 2px;
}

@media (max-width: 600px) {
  .cinema-detail-card {
    max-width: 97vw;
    padding: 28px 8px 18px 8px;
  }
  .cinema-title {
    font-size: 1.3em;
  }
  .cinema-location,
  .cinema-contact,
  .cinema-capacity,
  .cinema-desc {
    font-size: 1em;
    padding: 8px 4px;
  }
}
</style>