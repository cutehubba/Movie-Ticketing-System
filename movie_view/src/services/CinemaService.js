import axios from 'axios';

// 设置基本的 API 路径
const API_BASE_URL = 'http://localhost:8080/api/cinema';

// 获取所有影院
export const getCinemas = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data.data;
  } catch (error) {
    console.error('获取影院列表失败:', error);
    throw error;
  }
};

// 获取影院详情
export const getCinemaDetails = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`获取影院${id}详情失败:`, error);
    throw error;
  }
};

// 添加影院
export const addCinema = async (cinemaData) => {
  try {
    const response = await axios.post(API_BASE_URL, cinemaData);
    return response.data;
  } catch (error) {
    console.error('添加影院失败:', error);
    throw error;
  }
};

// 更新影院
export const updateCinema = async (id, cinemaData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${id}`, cinemaData);
    return response.data;
  } catch (error) {
    console.error(`更新影院${id}失败:`, error);
    throw error;
  }
};

// 删除影院
export const deleteCinema = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`删除影院${id}失败:`, error);
    throw error;
  }
};
