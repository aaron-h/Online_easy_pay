<template>
  <div class="pay-container">
    <el-card shadow="hover" class="pay-card">
      <div class="pay-header">
        <h2>订单支付</h2>
      </div>
      <div class="pay-content" v-if="order">
        <div class="order-info">
          <div class="order-item">
            <span class="label">订单号：</span>
            <span class="value">{{ order.orderId }}</span>
          </div>
          <div class="order-item">
            <span class="label">商品名称：</span>
            <span class="value">{{ order.appItem }}</span>
          </div>
          <div class="order-item">
            <span class="label">支付金额：</span>
            <span class="value price">¥{{ order.price.toFixed(2) }}</span>
          </div>
          <div class="order-item">
            <span class="label">创建时间：</span>
            <span class="value">{{ formatDateTime(order.createTime) }}</span>
          </div>
        </div>
        <div class="pay-method">
          <h3>请选择支付方式</h3>
          <el-radio-group v-model="payType">
            <el-radio :label="2">
              <div class="pay-method-item">
                <el-icon><Money /></el-icon>
                <span>支付宝</span>
              </div>
            </el-radio>
            <el-radio :label="3">
              <div class="pay-method-item">
                <el-icon><VideoCamera /></el-icon>
                <span>微信支付</span>
              </div>
            </el-radio>
          </el-radio-group>
        </div>
        <div class="qrcode-container" v-if="order.payImage">
          <h3>{{ payTitle }}</h3>
          <div class="qrcode">
            <el-image :src="order.payImage" style="width: 250px; height: 250px;" fit="cover"></el-image>
          </div>
          <div class="countdown">
            <span>订单将在 <span class="countdown-time">{{ countdown }} 秒后过期</span></span>
          </div>
          <el-progress :percentage="progress" :stroke-width="6" status="warning"></el-progress>
        </div>
        <div class="pay-actions">
          <el-button type="primary" @click="handleQueryOrder">查询支付结果</el-button>
          <el-button @click="handleCloseOrder">关闭订单</el-button>
        </div>
      </div>
      <div class="empty-state" v-else>
        <el-icon class="empty-icon"><DocumentDelete /></el-icon>
        <p>订单不存在或已失效</p>
        <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Money, DocumentDelete, VideoCamera } from '@element-plus/icons-vue'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const order = ref(null)
const payType = ref(2)
const countdown = ref(600)
const progress = ref(100)
let countdownTimer = null

// 根据支付类型显示不同的支付说明
const payTitle = computed(() => {
  return payType.value === 2 ? '请使用支付宝扫描二维码支付' : '请使用微信扫描二维码支付'
})

// 根据支付类型生成不同的二维码
const getQrCodeUrl = (orderId) => {
  if (payType.value === 2) {
    return `https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://www.alipay.com&orderId=${orderId}`
  } else {
    return `https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://www.weixin.qq.com&orderId=${orderId}`
  }
}

// 监听支付类型变化，更新二维码
watch(payType, (newType, oldType) => {
  if (order.value) {
    order.value.payImage = getQrCodeUrl(order.value.orderId)
    ElMessage.info(newType === 2 ? '已切换到支付宝支付' : '已切换到微信支付')
  }
})

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 创建订单
const createOrder = async (item, price) => {
  try {
    // 这里应该调用API创建订单
    // 模拟数据
    // const response = await axios.post('/api/pay/create', {
    //   appItem: item,
    //   price: parseFloat(price),
    //   payType: payType.value
    // })
    // order.value = response.data
    
    const orderId = 'ORD' + Date.now()
    // 暂时使用模拟数据
    order.value = {
      orderId: orderId,
      appItem: item,
      price: parseFloat(price),
      realPrice: parseFloat(price),
      createTime: new Date().toISOString(),
      payImage: getQrCodeUrl(orderId)
    }
    ElMessage.success('订单创建成功')
  } catch (error) {
    ElMessage.error('创建订单失败：' + error.message)
  }
}

// 查询订单
const queryOrder = async () => {
  try {
    const orderId = route.query.orderId
    if (!orderId) {
      throw new Error('订单号不能为空')
    }
    // 这里应该调用API查询订单
    // 模拟数据
    // order.value = await axios.post('/api/pay/query', { orderId })
    // 暂时使用模拟数据
    order.value = {
      orderId: orderId,
      appItem: '测试商品',
      price: 100.00,
      createTime: new Date().toISOString(),
      payImage: getQrCodeUrl(orderId)
    }
  } catch (error) {
    ElMessage.error('查询订单失败：' + error.message)
  }
}

// 查询支付结果
const handleQueryOrder = async () => {
  try {
    // 这里应该调用API查询支付结果
    // 模拟支付成功
    ElMessage.success('支付成功')
    router.push({ path: '/pay/success', query: { orderId: order.value.orderId } })
  } catch (error) {
    ElMessage.error('查询支付结果失败：' + error.message)
  }
}

// 关闭订单
const handleCloseOrder = async () => {
  try {
    // 这里应该调用API关闭订单
    ElMessage.success('订单已关闭')
    order.value = null
  } catch (error) {
    ElMessage.error('关闭订单失败：' + error.message)
  }
}

// 启动倒计时
const startCountdown = () => {
  countdownTimer = setInterval(() => {
    countdown.value--
    progress.value = (countdown.value / 600) * 100
    if (countdown.value <= 0) {
      clearInterval(countdownTimer)
      handleCloseOrder()
    }
  }, 1000)
}

// 清除倒计时
const clearCountdown = () => {
  if (countdownTimer) {
    clearInterval(countdownTimer)
    countdownTimer = null
  }
}

onMounted(() => {
  const orderId = route.query.orderId
  const item = route.query.item
  const price = route.query.price
  
  if (orderId) {
    // 如果有订单号，查询订单
    queryOrder()
  } else if (item && price) {
    // 如果有商品信息，创建订单
    createOrder(item, price)
  } else {
    // 否则显示订单不存在
    ElMessage.warning('订单信息不完整')
  }
  
  startCountdown()
})

onUnmounted(() => {
  clearCountdown()
})
</script>

<style scoped>
.pay-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background-color: #f5f7fa;
}

.pay-card {
  width: 100%;
  max-width: 600px;
}

.pay-header {
  text-align: center;
  margin-bottom: 20px;
}

.pay-header h2 {
  color: #2c3e50;
  margin: 0;
}

.pay-content {
  padding: 20px;
}

.order-info {
  background-color: #fafafa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.order-item {
  display: flex;
  margin-bottom: 10px;
}

.order-item:last-child {
  margin-bottom: 0;
}

.label {
  width: 100px;
  color: #909399;
}

.value {
  color: #2c3e50;
  font-weight: 500;
}

.value.price {
  color: #f56c6c;
  font-size: 24px;
  font-weight: bold;
}

.pay-method {
  margin-bottom: 20px;
}

.pay-method h3 {
  margin-bottom: 10px;
  color: #2c3e50;
  font-size: 16px;
}

.pay-method-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
}

.pay-method-item:hover {
  border-color: #409eff;
}

.pay-method-item .el-icon {
  margin-right: 10px;
  font-size: 24px;
  color: #165dff;
}

.qrcode-container {
  text-align: center;
  margin-bottom: 20px;
}

.qrcode-container h3 {
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 16px;
}

.qrcode {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.countdown {
  margin-bottom: 10px;
  color: #f56c6c;
}

.countdown-time {
  font-weight: bold;
}

.pay-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 50px 0;
}

.empty-icon {
  font-size: 64px;
  color: #dcdfe6;
  margin-bottom: 20px;
}

.empty-state p {
  color: #909399;
  margin-bottom: 20px;
}
</style>