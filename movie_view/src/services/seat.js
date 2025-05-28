import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/seat';

// 根据影厅ID获取座位列表
export const getSeatsByHallId = async (hallId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/hall/${hallId}`);
    return response.data.data || [];
  } catch (error) {
    console.error('获取座位列表失败:', error);
    throw error;
  }
};

// 占用座位，seatNumbers 是字符串数组，比如 ["A1", "A2"]
export const occupySeats = async (hallId, seatNumbers) => {
  try {
    const params = new URLSearchParams();
    params.append('hallId', hallId);
    seatNumbers.forEach(seat => params.append('seatNumbers', seat));

    const response = await axios.post(`${API_BASE_URL}/occupy`, params);
    return response.data;
  } catch (error) {
    console.error('座位占用失败:', error);
    throw error;
  }
};

// 释放座位，seatNumbers 是字符串数组
export const releaseSeats = async (hallId, seatNumbers) => {
  try {
    const params = new URLSearchParams();
    params.append('hallId', hallId);
    seatNumbers.forEach(seat => params.append('seatNumbers', seat));

    const response = await axios.post(`${API_BASE_URL}/release`, params);
    return response.data;
  } catch (error) {
    console.error('座位释放失败:', error);
    throw error;
  }
};
