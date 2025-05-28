<template>
  <div class="order-details" v-if="order">
    <h2>订单详情</h2>
    <p>电影: {{ order.movieTitle }}</p>
    <p>观影时间: {{ order.showTime }}</p>
  </div>
  <p v-else>加载订单详情中...</p>
</template>

<script>
import { getOrderDetails } from '@/services/OrderService';

export default {
  name: 'OrderDetails',
  data() {
    return {
      order: null,
    };
  },
  methods: {
    async fetchOrderDetails() {
      const { id } = this.$route.params;
      try {
        const response = await getOrderDetails(id);
        this.order = response.data || response || null;
      } catch (error) {
        console.error('获取订单详情失败:', error);
        alert('获取订单详情失败，请稍后重试');
        this.$router.push('/orders');
      }
    },
  },
  mounted() {
    this.fetchOrderDetails();
  },
};
</script>

<style scoped>
.order-details {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fafafa;
  max-width: 600px;
  margin: 30px auto;
  color: #444;
}
</style>
