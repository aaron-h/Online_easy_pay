<template>
  <div class="pay-success-container">
    <el-card shadow="hover" class="success-card">
      <div class="success-content">
        <div class="success-icon">
          <el-icon><CircleCheck /></el-icon>
        </div>
        <h2>支付成功</h2>
        <p class="success-message">感谢您的购买，订单支付成功！</p>
        <div class="order-info" v-if="orderId">
          <p>订单号：{{ orderId }}</p>
        </div>
        <div class="success-actions">
          <el-button type="primary" @click="handleReturnHome">返回首页</el-button>
          <el-button @click="handleViewOrder">查看订单</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CircleCheck } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const orderId = ref(route.query.orderId || '')

// 返回首页
const handleReturnHome = () => {
  router.push('/')
}

// 查看订单
const handleViewOrder = () => {
  router.push(`/pay?orderId=${orderId.value}`)
}
</script>

<style scoped>
.pay-success-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background-color: #f5f7fa;
}

.success-card {
  width: 100%;
  max-width: 500px;
}

.success-content {
  text-align: center;
  padding: 40px 20px;
}

.success-icon {
  margin-bottom: 20px;
}

.success-icon .el-icon {
  font-size: 80px;
  color: #67c23a;
}

h2 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.success-message {
  color: #606266;
  margin-bottom: 30px;
  font-size: 16px;
}

.order-info {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #fafafa;
  border-radius: 8px;
}

.order-info p {
  margin: 0;
  color: #2c3e50;
  font-weight: 500;
}

.success-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}
</style>