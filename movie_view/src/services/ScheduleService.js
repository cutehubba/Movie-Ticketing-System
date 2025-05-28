import axios from 'axios';

// 设置基础地址，带端口8083
const API_BASE_URL = 'http://localhost:8080/api/schedule';

// 获取所有排片信息
export const getSchedules = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data.data || [];  // 只返回 data 字段里的排片数组
  } catch (error) {
    console.error('获取排片信息失败:', error);
    throw error;
  }
};

// 获取单个电影排期详情
export const getScheduleDetails = async (scheduleId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${scheduleId}`);
    return response.data.data || null;  // 只返回 data 字段里的排期详情
  } catch (error) {
    console.error('获取排片详情失败:', error);
    throw error;
  }
};

// 创建排片
export const createSchedule = async (scheduleData) => {
  try {
    const response = await axios.post(API_BASE_URL, scheduleData);
    return response.data.data || null;  // 返回创建成功的排片数据
  } catch (error) {
    console.error('创建排片失败:', error);
    throw error;
  }
};

// 更新排片
export const updateSchedule = async (scheduleId, scheduleData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${scheduleId}`, scheduleData);
    return response.data.data || null;  // 返回更新后的排片数据
  } catch (error) {
    console.error('更新排片失败:', error);
    throw error;
  }
};

// 删除排片
export const deleteSchedule = async (scheduleId) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${scheduleId}`);
    return response.data;  // 删除一般只需要返回整体响应信息即可
  } catch (error) {
    console.error('删除排片失败:', error);
    throw error;
  }
};
