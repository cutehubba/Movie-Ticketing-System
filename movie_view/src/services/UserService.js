import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api/user', // 统一前缀
  timeout: 5000,
})

// 注册用户
export const registerUser = async (userData) => {
  try {
    const response = await api.post('/register', userData)
    return response.data
  } catch (error) {
    // 如果后端返回字符串类型的错误信息，帮它转换成对象
    if (error.response && typeof error.response.data === 'string') {
      try {
        error.response.data = JSON.parse(error.response.data)
      } catch {
        // 忽略解析失败
      }
    }
    throw error
  }
}



// 用户登录
export const loginUser = async (credentials) => {
  try {
    const response = await api.post('/login', credentials)
    return response.data
  } catch (error) {
    console.error('登录失败:', error)
    throw error
  }
}

// 获取所有用户
export const getAllUsers = async () => {
  try {
    const response = await api.get('')
    return response.data
  } catch (error) {
    console.error('获取用户列表失败:', error)
    throw error
  }
}

// 根据 ID 获取用户
export const getUserById = async (id) => {
  try {
    const response = await api.get(`/${id}`)
    return response.data
  } catch (error) {
    console.error('获取用户信息失败:', error)
    throw error
  }
}

// 更新用户
export const updateUser = async (id, userData) => {
  try {
    const response = await api.put(`/${id}`, userData)
    return response.data
  } catch (error) {
    console.error('更新用户失败:', error)
    throw error
  }
}

// 删除用户
export const deleteUser = async (id) => {
  try {
    const response = await api.delete(`/${id}`)
    return response.data
  } catch (error) {
    console.error('删除用户失败:', error)
    throw error
  }
}
