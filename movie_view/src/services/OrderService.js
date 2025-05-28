import axios from 'axios';

// 设置基础地址，带端口8084
const API_BASE_URL = 'http://localhost:8080/api/order';

/**
 * 获取所有订单
 * GET /api/order
 */
export const getOrders = async () => {
  try {
    const response = await axios.get(API_BASE_URL);
    return response.data.data || [];
  } catch (error) {
    console.error('获取订单列表失败:', error);
    throw error;
  }
};

/**
 * 获取指定订单详情
 * GET /api/order/{id}
 */
export const getOrderDetails = async (orderId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${orderId}`);
    return response.data.data || null;
  } catch (error) {
    console.error('获取订单详情失败:', error);
    throw error;
  }
};

/**
 * 创建订单
 * POST /api/order
 */
export const createOrder = async (orderData) => {
  try {
    console.log('创建订单参数:', orderData);
    const response = await axios.post(API_BASE_URL, orderData);
    return response.data.data || null;
  } catch (error) {
    console.error('创建订单失败:', error);
    throw error;
  }
};

/**
 * 更新订单
 * PUT /api/order/{id}
 */
export const updateOrder = async (orderId, orderData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/${orderId}`, orderData);
    return response.data.data || null;
  } catch (error) {
    console.error('更新订单失败:', error);
    throw error;
  }
};

/**
 * 删除订单
 * DELETE /api/order/{id}
 */
export const deleteOrder = async (orderId) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/${orderId}`);
    return response.data;
  } catch (error) {
    console.error('删除订单失败:', error);
    throw error;
  }
};

/**
 * 创建支付订单
 * POST /api/order/payment/{orderId}
 */
export const createPayment = async (orderId, paymentData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/payment/${orderId}`, paymentData);
    return response.data.data || null;
  } catch (error) {
    console.error('创建支付订单失败:', error);
    throw error;
  }
};

/**
 * 处理支付回调
 * POST /api/order/payment/callback
 */
export const handlePaymentCallback = async (paymentCallbackData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/payment/callback`, paymentCallbackData);
    return response.data.data || null;
  } catch (error) {
    console.error('支付回调处理失败:', error);
    throw error;
  }
};