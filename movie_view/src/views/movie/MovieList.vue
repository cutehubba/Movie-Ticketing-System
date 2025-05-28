<template>
  <div class="movie-list-container">
    <div class="header-bar">
      <h1 class="page-title">电影列表</h1>
      <router-link to="/home" class="back-home-btn" title="返回主页">返回主页</router-link>
    </div>

    <!-- 添加电影按钮 -->
    <button @click="showAddMovieForm = !showAddMovieForm" class="add-movie-btn">
      {{ showAddMovieForm ? '取消添加' : '添加电影' }}
      <span :class="['arrow-icon', showAddMovieForm ? 'expanded' : '']">▼</span>
    </button>

    <!-- 添加电影表单 -->
    <transition name="fade-slide">
      <div v-if="showAddMovieForm" class="form-container glass-card">
        <form class="movie-form" @submit.prevent="addMovie">
          <input v-model="newMovie.title" placeholder="电影标题" class="form-input" required />
          <input v-model="newMovie.releaseDate" type="date" class="form-input" required />
          <input v-model="newMovie.duration" type="number" placeholder="时长（分钟）" class="form-input" />
          <input v-model="newMovie.genre" placeholder="类型" class="form-input" />
          <input v-model="newMovie.director" placeholder="导演" class="form-input" required />
          <textarea v-model="newMovie.description" placeholder="简介" class="form-input"></textarea>
          <select v-model="newMovie.releaseStatus" class="form-input">
            <option value="released">已上映</option>
            <option value="upcoming">即将上映</option>
            <option value="not_released">未上映</option>
          </select>
          <input v-model="newMovie.rating" type="number" step="0.1" max="10" min="0" placeholder="评分（0-10）" class="form-input" />
          <input v-model="newMovie.posterUrl" placeholder="海报图片链接" class="form-input" />
          <input v-model="newMovie.trailerUrl" placeholder="预告片链接" class="form-input" />
          <button type="submit" class="submit-btn">提交</button>
        </form>
      </div>
    </transition>

    <!-- 显示加载状态或无数据提示 -->
    <p v-if="movieList.length === 0" class="no-movies">没有电影数据</p>

    <!-- 电影列表 -->
    <ul class="movie-list">
      <li v-for="movie in movieList" :key="movie.id" class="movie-item glass-card">
        <div class="movie-poster" v-if="movie.posterUrl" :style="{ backgroundImage: 'url(' + movie.posterUrl + ')' }"></div>
        <div class="movie-info">
          <router-link :to="'/movie/' + movie.id" class="movie-title" :title="movie.title">{{ movie.title }}</router-link>
          <div class="movie-meta">
            <span>🎬 {{ movie.director || '未知' }}</span>
            <span>🏷️ {{ movie.genre || '未知' }}</span>
            <span>⭐ {{ movie.rating || '-' }}/10</span>
          </div>
          <div class="movie-summary" v-if="movie.description">{{ movie.description }}</div>
        </div>
        <div class="movie-actions">
          <button @click="openEditModal(movie)" class="edit-btn" title="编辑电影">✏️</button>
          <button @click="deleteMovie(movie.id)" class="delete-btn" title="删除电影">🗑️</button>
        </div>
      </li>
    </ul>

    <!-- 编辑电影弹出框 -->
    <transition name="modal-fade">
      <div v-if="editingMovie" class="modal" @click.self="closeModal">
        <div class="modal-content glass-card">
          <h3>编辑电影</h3>
          <form @submit.prevent="updateMovie(editingMovie.id, editingMovie)">
            <input v-model="editingMovie.title" placeholder="电影标题" class="form-input" required />
            <input v-model="editingMovie.releaseDate" type="date" class="form-input" required />
            <input v-model="editingMovie.duration" type="number" placeholder="时长（分钟）" class="form-input" />
            <input v-model="editingMovie.genre" placeholder="类型" class="form-input" />
            <input v-model="editingMovie.director" placeholder="导演" class="form-input" required />
            <textarea v-model="editingMovie.description" placeholder="简介" class="form-input"></textarea>
            <select v-model="editingMovie.releaseStatus" class="form-input">
              <option value="released">已上映</option>
              <option value="upcoming">即将上映</option>
              <option value="not_released">未上映</option>
            </select>
            <input v-model="editingMovie.rating" type="number" step="0.1" max="10" min="0" placeholder="评分（0-10）" class="form-input" />
            <input v-model="editingMovie.posterUrl" placeholder="海报图片链接" class="form-input" />
            <input v-model="editingMovie.trailerUrl" placeholder="预告片链接" class="form-input" />
            <div class="modal-buttons">
              <button type="submit" class="submit-btn">更新</button>
              <button type="button" @click="closeModal" class="cancel-btn">关闭</button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { getMovies, addMovie, updateMovie, deleteMovie } from '@/services/MovieService';

export default {
  data() {
    return {
      movieList: [],
      showAddMovieForm: false,
      newMovie: {
        title: '',
        releaseDate: '',
        duration: '',
        genre: '',
        director: '',
        description: '',
        releaseStatus: 'released',
        rating: '',
        posterUrl: '',
        trailerUrl: ''
      },
      editingMovie: null
    };
  },
  async created() {
    await this.loadMovies();
  },
  methods: {
    async loadMovies() {
      try {
        const response = await getMovies();
        if (response && response.data && Array.isArray(response.data)) {
          this.movieList = response.data;
        } else {
          console.error('电影数据格式不正确');
        }
      } catch (error) {
        console.error('加载电影列表失败', error);
      }
    },
    async addMovie() {
      if (!this.newMovie.title || !this.newMovie.releaseDate || !this.newMovie.director) {
        alert('请确保所有必填字段（标题、上映日期、导演）都已填写');
        return;
      }
      try {
        const response = await addMovie({ ...this.newMovie });
        if (response && response.data) {
          this.movieList.push(response.data);
          this.showAddMovieForm = false;
          this.newMovie = { title: '', releaseDate: '', duration: '', genre: '', director: '', description: '', releaseStatus: 'released', rating: '', posterUrl: '', trailerUrl: '' };
        }
      } catch (error) {
        console.error('添加电影失败:', error);
      }
    },
    async deleteMovie(id) {
      const confirmDelete = confirm('确定要删除这个电影吗？');
      if (confirmDelete) {
        try {
          await deleteMovie(id);
          this.movieList = this.movieList.filter(movie => movie.id !== id);
        } catch (error) {
          console.error('删除电影失败:', error);
        }
      }
    },
    openEditModal(movie) {
      this.editingMovie = { ...movie };
    },
    async updateMovie(id, updatedMovie) {
      if (!updatedMovie.title || !updatedMovie.releaseDate || !updatedMovie.director) {
        alert('请确保所有必填字段（标题、上映日期、导演）都已填写');
        return;
      }
      try {
        const response = await updateMovie(id, updatedMovie);
        const index = this.movieList.findIndex(movie => movie.id === id);
        if (index !== -1) {
          this.movieList.splice(index, 1, response.data);
        }
        this.closeModal();
      } catch (error) {
        console.error('更新电影失败:', error);
      }
    },
    closeModal() {
      this.editingMovie = null;
    }
  }
};
</script>

<style scoped>
.movie-list-container {
  max-width: 1220px;
  margin: 44px auto;
  padding: 38px 36px;
  background: linear-gradient(120deg, #161c2a 0%, #243b55 100%);
  border-radius: 22px;
  box-shadow: 0 18px 54px rgba(16, 32, 72, 0.27);
  color: #f0f0f0;
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
  margin-bottom: 10px;
  gap: 16px;
}

/* 返回主页按钮 */
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

.page-title {
  font-size: 3em;
  font-weight: 900;
  text-align: center;
  margin-bottom: 28px;
  text-shadow: 0 2px 22px #12c2e9b0, 1px 1px 8px #0008;
  letter-spacing: 2px;
  background: linear-gradient(90deg,#70f2ff,#fff176 60%,#ff4b2b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.add-movie-btn {
  display: inline-flex;
  align-items: center;
  background: linear-gradient(90deg, #ff416c 0%, #ff4b2b 100%);
  color: white;
  font-size: 1.22em;
  padding: 12px 34px;
  border-radius: 44px;
  border: none;
  cursor: pointer;
  box-shadow: 0 6px 15px #ff4b2b55;
  transition: transform 0.3s, box-shadow 0.3s, background 0.3s;
  margin: 0 auto 36px;
  user-select: none;
  font-weight: 700;
}
.add-movie-btn:hover {
  transform: scale(1.08);
  box-shadow: 0 10px 28px #ff4b2b99;
  background: linear-gradient(90deg, #ff4b2b 0%, #ff416c 100%);
}
.arrow-icon {
  margin-left: 14px;
  transition: transform 0.35s cubic-bezier(.44,.4,.2,1.5);
  display: inline-block;
  font-size: 1.1em;
}
.arrow-icon.expanded {
  transform: rotate(180deg);
}

.form-container {
  background: rgba(255,255,255,0.07);
  padding: 28px 36px 18px;
  border-radius: 18px;
  box-shadow: 0 4px 18px #70aaff33 inset;
  margin-bottom: 44px;
  animation: fadeInCard 0.7s;
}
@keyframes fadeInCard {
  from { opacity: 0; transform: translateY(-18px);}
  to   { opacity: 1; transform: none;}
}

/* 玻璃拟态风格卡片 */
.glass-card {
  background: rgba(36, 56, 95, 0.87);
  box-shadow: 0 4px 22px #0f7bff22, 0 1.5px 10px #4062bb22;
  border-radius: 22px;
  border: 1.5px solid rgba(112,170,255,0.09);
  backdrop-filter: blur(7px);
  transition: box-shadow 0.25s;
}
.glass-card:hover {
  box-shadow: 0 8px 30px #0f7bff55, 0 2px 18px #4062bb44;
}

.form-input {
  display: block;
  width: 100%;
  margin: 12px 0;
  padding: 12px 18px;
  border-radius: 14px;
  border: none;
  font-size: 1.1em;
  background: linear-gradient(90deg,rgba(255,255,255,0.13) 0%,rgba(112,170,255,0.10) 100%);
  color: #1b263b;
  box-shadow: 0 1.5px 8px #70aaff22 inset;
  transition: background 0.3s, box-shadow 0.2s;
  outline: none;
}
.form-input:focus {
  background: #fff;
  box-shadow: 0 0 12px #70aaff99;
  color: #14213d;
}

.submit-btn {
  background: linear-gradient(90deg,#56ab2f 0%,#a8e063 100%);
  color: #fff;
  font-weight: 700;
  font-size: 1.2em;
  border-radius: 40px;
  padding: 14px 40px;
  border: none;
  cursor: pointer;
  box-shadow: 0 8px 20px #a8e06344;
  margin-top: 16px;
  user-select: none;
  transition: background 0.3s, box-shadow 0.3s;
}
.submit-btn:hover {
  background: linear-gradient(90deg,#a8e063 0%,#56ab2f 100%);
  box-shadow: 0 12px 30px #a8e063bb;
}

.movie-list {
  display: flex;
  flex-wrap: wrap;
  gap: 26px 24px;
  padding: 12px 0 0 0;
  list-style: none;
  margin: 0;
}

.movie-item {
  flex: 0 0 310px;
  display: flex;
  flex-direction: column;
  min-height: 470px;
  margin-bottom: 10px;
  position: relative;
  transition: transform 0.23s;
  overflow: hidden;
}
.movie-item:hover {
  transform: translateY(-10px) scale(1.025);
  z-index: 2;
}

.movie-poster {
  height: 340px;
  background-size: cover;
  background-position: center center;
  border-radius: 18px 18px 0 0;
  border-bottom: 3px solid #ff4b2b;
  transition: box-shadow 0.24s;
  box-shadow: 0 6px 24px #ff416c33;
}
.movie-info {
  padding: 18px 22px 8px 22px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  color: #fff;
}
.movie-title {
  font-weight: 900;
  font-size: 1.37em;
  margin-bottom: 8px;
  color: #fff176;
  text-decoration: none;
  text-shadow: 1px 1px 7px #ff4b2baa,0 2px 10px #0008;
  user-select: text;
  transition: color 0.21s;
}
.movie-title:hover {
  color: #70f2ff;
  text-shadow: 1px 1px 10px #70f2ff, 0 0 12px #fff;
}
.movie-meta {
  font-size: 1em;
  display: flex;
  gap: 16px;
  color: #eaf6ff;
  font-weight: 600;
  margin-bottom: 7px;
  user-select: text;
}
.movie-summary {
  color: #b9e9f7;
  font-size: 0.98em;
  margin: 6px 0 2px 0;
  background: rgba(255,255,255,0.10);
  border-radius: 7px;
  padding: 7px 10px;
  min-height: 42px;
  max-height: 90px;
  overflow: auto;
  box-shadow: 0 2px 8px #70aaff11 inset;
}
.movie-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 18px;
  background: rgba(0,0,0,0.11);
  gap: 8px;
  margin-top: auto;
  border-radius: 0 0 18px 18px;
}
.edit-btn, .delete-btn {
  cursor: pointer;
  background: transparent;
  border: none;
  font-size: 1.22em;
  margin-left: 10px;
  color: #ffeb3b;
  transition: color 0.3s;
}
.edit-btn:hover {
  color: #1de9b6;
}
.delete-btn:hover {
  color: #ff1744;
}

.no-movies {
  text-align: center;
  font-size: 1.4em;
  margin-top: 40px;
  color: #b9e9f7;
  letter-spacing: 1.2px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg,#223,#1c283e 70%,#243b55 100%);
  background-color: rgba(0,0,0,0.64);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  user-select: none;
  animation: modalFadeIn 0.3s;
}
@keyframes modalFadeIn {
  from { opacity: 0; }
  to   { opacity: 1; }
}
.modal-content {
  background: rgba(36, 56, 95, 0.98);
  border-radius: 22px;
  width: 430px;
  max-width: 97vw;
  padding: 30px 36px 18px 36px;
  box-shadow: 0 8px 40px #70aaff55;
  color: #eee;
  animation: fadeInCard 0.7s;
}
.modal-content h3 {
  margin-bottom: 25px;
  font-weight: 900;
  font-size: 2em;
  text-align: center;
  color: #ff4b2b;
  text-shadow: 1px 1px 7px #ff4b2baa, 0 2px 8px #0008;
}
.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 22px;
  gap: 16px;
}
.cancel-btn {
  background: #555;
  color: #ddd;
  border: none;
  border-radius: 40px;
  padding: 12px 30px;
  cursor: pointer;
  font-weight: 600;
  transition: background 0.3s;
  user-select: none;
}
.cancel-btn:hover {
  background: #ff4b2b;
  color: #fff;
}

.fade-slide-enter-active, .fade-slide-leave-active {
  transition: all 0.5s cubic-bezier(.55,0,.1,1);
}
.fade-slide-enter-from, .fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-14px);
}
.modal-fade-enter-active, .modal-fade-leave-active {
  transition: opacity 0.22s cubic-bezier(.55,0,.1,1);
}
.modal-fade-enter-from, .modal-fade-leave-to {
  opacity: 0;
}

/* 响应式 */
@media (max-width: 800px) {
  .movie-list-container {
    padding: 16px 3vw;
  }
  .header-bar {
    flex-direction: column;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
  }
  .movie-list {
    gap: 14px 0;
  }
  .movie-item {
    flex: 0 0 98vw;
    min-width: 96vw;
    max-width: 99vw;
  }
  .modal-content {
    width: 97vw;
    padding: 14px 2vw 10px 2vw;
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
  .movie-list-container {
    padding: 7px 1vw;
  }
  .modal-content {
    padding: 7px 2vw 7px 2vw;
  }
  .form-container {
    padding: 14px 5vw 7px 5vw;
  }
}
</style>