import axios from 'axios'

// 设置基本 API 路径
const API_BASE_URL = 'http://localhost:8080/api/movie'

// 获取电影列表
export const getMovies = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/list`)
    console.log("API 返回数据:", response.data);
    return response.data
  } catch (error) {
    console.error('获取电影列表失败:', error)
    throw error
  }
}

// 获取电影详情
export const getMovieDetails = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/details/${id}`)
    return response.data
  } catch (error) {
    console.error(`获取电影 ${id} 详情失败:`, error)
    throw error
  }
}

// 添加电影
export const addMovie = async (movieData) => {
  try {
    console.log("准备添加电影：", movieData)
    const response = await axios.post(`${API_BASE_URL}`, movieData)
    return response.data
  } catch (error) {
    console.error('添加电影失败:', error)
    throw error
  }
}

// 更新电影
export const updateMovie = async (id, movieData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${id}`, movieData)
    return response.data
  } catch (error) {
    console.error(`更新电影 ${id} 失败:`, error)
    throw error
  }
}

// 删除电影
export const deleteMovie = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${id}`)
    return response.data
  } catch (error) {
    console.error(`删除电影 ${id} 失败:`, error)
    throw error
  }
}
