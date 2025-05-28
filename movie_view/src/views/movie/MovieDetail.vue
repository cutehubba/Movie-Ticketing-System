<template>
  <div class="movie-detail-bg">
    <div class="movie-detail-glass">
      <!-- 加载中状态 -->
      <div v-if="loading" class="loading">
        <span class="loading-spinner"></span>
        加载中...
      </div>
      <!-- 电影详情 -->
      <div v-else class="movie-content">
        <div class="movie-header">
          <h1 class="movie-title">{{ movie.title }}</h1>
          <span class="badge" :class="badgeClass">{{ statusText }}</span>
          <p class="movie-rating">
            <span class="star">★</span>
            {{ movie.rating !== undefined && movie.rating !== null ? movie.rating : '暂无评分' }}
          </p>
        </div>
        <div class="movie-main">
          <div class="movie-poster">
            <img :src="movie.poster_url" alt="电影海报" v-if="movie.poster_url" />
            <div class="no-poster" v-else>暂无海报</div>
          </div>
          <div class="movie-info">
            <p>
              <span class="info-label">简介：</span>
              <span>{{ movie.description || '暂无简介' }}</span>
            </p>
            <p>
              <span class="info-label">导演：</span>
              <span>{{ movie.director || '未知' }}</span>
            </p>
            <p>
              <span class="info-label">演员：</span>
              <span>{{ movie.actors?.join(', ') || '暂无演员信息' }}</span>
            </p>
            <p>
              <span class="info-label">类型：</span>
              <span>{{ movie.genre || '未知' }}</span>
            </p>
            <p>
              <span class="info-label">上映日期：</span>
              <span>{{ formatReleaseDate(movie.release_date) || '未知' }}</span>
            </p>
            <p>
              <span class="info-label">时长：</span>
              <span>{{ movie.duration || '未知' }} 分钟</span>
            </p>
            <div class="movie-trailer" v-if="movie.trailer_url">
              <span class="info-label">预告片：</span>
              <a :href="movie.trailer_url" target="_blank">
                <span class="trailer-btn">🎬 点击观看</span>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getMovieDetails } from '@/services/MovieService';

export default {
  data() {
    return {
      movie: {},
      loading: true,
    };
  },
  async mounted() {
    const movieId = this.$route.params.id;
    try {
      const response = await getMovieDetails(movieId);
      this.movie = response.data;
    } catch (error) {
      console.error('加载电影详情失败', error);
    } finally {
      this.loading = false;
    }
  },
  computed: {
    badgeClass() {
      if (this.movie.release_status === 'released') return 'badge-released';
      if (this.movie.release_status === 'upcoming') return 'badge-upcoming';
      return 'badge-not-released';
    },
    statusText() {
      if (this.movie.release_status === 'released') return '已上映';
      if (this.movie.release_status === 'upcoming') return '即将上映';
      return '未上映';
    }
  },
  methods: {
    formatReleaseDate(date) {
      if (!date) return '未知';
      const releaseDate = new Date(date);
      if (isNaN(releaseDate)) return date;
      return releaseDate.toLocaleDateString();
    }
  }
};
</script>

<style scoped>
.movie-detail-bg {
  min-height: 100vh;
  background: linear-gradient(120deg, #141e30 0%, #243b55 100%);
  padding: 40px 0;
  display: flex;
  align-items: flex-start;
  justify-content: center;
}
.movie-detail-glass {
  margin: 0 auto;
  max-width: 820px;
  width: 98vw;
  border-radius: 30px;
  background: rgba(36, 56, 95, 0.92);
  backdrop-filter: blur(10px);
  box-shadow: 0 10px 40px #1863c066, 0 1.5px 12px #70aaff33;
  padding: 44px 44px 30px 44px;
  color: #e2f1ff;
  animation: fadeInUp 0.7s;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(40px);}
  to { opacity: 1; transform: none;}
}

.loading {
  text-align: center;
  font-size: 1.3em;
  color: #70aaff;
  margin: 60px 0;
  letter-spacing: 2px;
  font-weight: 600;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.loading-spinner {
  margin-bottom: 16px;
  width: 40px;
  height: 40px;
  border: 5px solid #70aaff88;
  border-top: 5px solid #70aaff;
  border-radius: 50%;
  animation: spin 1.1s linear infinite;
}
@keyframes spin {
  100% { transform: rotate(360deg);}
}

.movie-header {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}
.movie-title {
  font-size: 2.5em;
  font-weight: bold;
  color: #70f2ff;
  letter-spacing: 2px;
  margin-bottom: 10px;
  text-shadow: 0 3px 20px #70aaff66;
  line-height: 1.2;
}
.badge {
  display: inline-block;
  margin-top: 10px;
  margin-bottom: 0;
  padding: 4px 20px;
  font-size: 1.06em;
  font-weight: bold;
  border-radius: 18px;
  box-shadow: 0 2px 14px #fff4;
  letter-spacing: 1.2px;
  vertical-align: middle;
  margin-left: 8px;
}
.badge-released {
  background: linear-gradient(90deg, #a8e063 0%, #56ab2f 100%);
  color: #fff;
}
.badge-upcoming {
  background: linear-gradient(90deg, #f7971e, #ffd200 100%);
  color: #223;
}
.badge-not-released {
  background: linear-gradient(90deg, #e43a15 0%, #e65245 100%);
  color: #fff;
}

.movie-rating {
  font-size: 1.3em;
  color: #ffe066;
  margin: 22px 0 0 0;
  letter-spacing: 1px;
  font-weight: 700;
}
.star {
  color: #f39c12;
  font-size: 1.25em;
  margin-right: 6px;
  vertical-align: middle;
}

.movie-main {
  display: flex;
  align-items: flex-start;
  gap: 34px;
  margin-top: 25px;
}
.movie-poster {
  flex: 0 0 240px;
  text-align: center;
  min-height: 340px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.movie-poster img {
  width: 220px;
  max-width: 96vw;
  max-height: 330px;
  border-radius: 14px;
  box-shadow: 0 4px 18px #70aaff33, 0 2px 7px #fff4;
  background: #fff3;
  object-fit: cover;
}
.no-poster {
  width: 200px;
  height: 320px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(120deg, #2a3752 0%, #466e9c 100%);
  color: #e2eaf7cc;
  border-radius: 14px;
  font-size: 1.1em;
  box-shadow: 0 2px 10px #70aaff33;
  letter-spacing: 2px;
  font-weight: 600;
}

.movie-info {
  flex: 1 1 auto;
  font-size: 1.15em;
  color: #e2f1ff;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  gap: 8px;
}
.movie-info p {
  margin: 0 0 8px 0;
  line-height: 1.7;
}
.info-label {
  color: #70f2ff;
  font-weight: 600;
  margin-right: 7px;
  letter-spacing: 1px;
}
.movie-trailer {
  margin-top: 16px;
}
.trailer-btn {
  display: inline-block;
  background: linear-gradient(90deg, #00c6ff 0%, #0072ff 100%);
  color: #fff;
  font-weight: 600;
  border-radius: 16px;
  padding: 7px 22px;
  text-decoration: none;
  margin-left: 6px;
  font-size: 1.02em;
  transition: background 0.3s;
}
.trailer-btn:hover {
  background: linear-gradient(90deg, #0072ff 0%, #00c6ff 100%);
  box-shadow: 0 2px 12px #00c6ff77;
  color: #ffe066;
}

@media (max-width: 900px) {
  .movie-detail-glass {
    padding: 24px 1vw 18px 1vw;
    max-width: 99vw;
  }
  .movie-main {
    flex-direction: column;
    align-items: center;
    gap: 18px;
  }
  .movie-poster {
    min-height: 200px;
  }
}
@media (max-width: 480px) {
  .movie-detail-glass {
    padding: 11px 0 7px 0;
    border-radius: 10px;
  }
  .movie-title {
    font-size: 1.2em;
  }
  .movie-info {
    font-size: 0.98em;
  }
}
</style>