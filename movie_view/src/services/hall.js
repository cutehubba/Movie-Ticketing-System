import axios from 'axios';

// 设置基础地址
const API_BASE_URL = 'http://localhost:8080/api/hall';

// 获取所有影厅
export const getHalls = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data.data || [];
  } catch (error) {
    console.error('获取影厅列表失败:', error);
    throw error;
  }
};

// 获取单个影厅详情
export const getHallDetails = async (hallId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${hallId}`);
    return response.data.data || null;
  } catch (error) {
    console.error('获取影厅详情失败:', error);
    throw error;
  }
};

// 创建影厅
export const createHall = async (hallData) => {
  try {
    const response = await axios.post(API_BASE_URL, hallData);
    return response.data.data || null;
  } catch (error) {
    console.error('创建影厅失败:', error);
    throw error;
  }
};

// 更新影厅
export const updateHall = async (hallId, hallData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${hallId}`, hallData);
    return response.data.data || null;
  } catch (error) {
    console.error('更新影厅失败:', error);
    throw error;
  }
};

// 删除影厅
export const deleteHall = async (hallId) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${hallId}`);
    return response.data;
  } catch (error) {
    console.error('删除影厅失败:', error);
    throw error;
  }
};

// 根据影院 ID 获取影厅列表
export const getHallsByCinemaId = async (cinemaId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/cinema/${cinemaId}`);
    return response.data.data || [];
  } catch (error) {
    console.error(`获取影院 ID 为 ${cinemaId} 的影厅列表失败:`, error);
    throw error;
  }
};