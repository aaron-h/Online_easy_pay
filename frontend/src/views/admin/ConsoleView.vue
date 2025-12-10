<template>
  <div class="console">
    <h2>控制台</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-title">今日订单数</div>
            <div class="stat-value">0</div>
            <div class="stat-desc">较昨日 <span class="stat-up">+0%</span></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-title">今日交易额</div>
            <div class="stat-value">¥0.00</div>
            <div class="stat-desc">较昨日 <span class="stat-up">+0%</span></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-title">总订单数</div>
            <div class="stat-value">0</div>
            <div class="stat-desc">累计订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-title">总交易额</div>
            <div class="stat-value">¥0.00</div>
            <div class="stat-desc">累计交易额</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近订单</span>
              <el-button type="text" size="small">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentOrders" style="width: 100%" stripe>
            <el-table-column prop="orderId" label="订单号" width="180"></el-table-column>
            <el-table-column prop="appName" label="应用名称"></el-table-column>
            <el-table-column prop="price" label="金额" width="100"></el-table-column>
            <el-table-column prop="state" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getOrderStateTagType(scope.row.state)">
                  {{ getOrderStateText(scope.row.state) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>应用列表</span>
              <el-button type="text" size="small">查看全部</el-button>
            </div>
          </template>
          <el-table :data="apps" style="width: 100%" stripe>
            <el-table-column prop="appName" label="应用名称"></el-table-column>
            <el-table-column prop="appKey" label="应用密钥" width="200"></el-table-column>
            <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 模拟数据
const recentOrders = ref([
  // { orderId: '1234567890', appName: '测试应用', price: 100.00, state: 1, createTime: '2023-01-01 12:00:00' }
])

const apps = ref([
  // { appName: '测试应用', appKey: 'test_key_123456', createdAt: '2023-01-01 12:00:00' }
])

// 获取订单状态文本
const getOrderStateText = (state) => {
  switch (state) {
    case 1: return '等待支付'
    case 2: return '已支付'
    case 3: return '订单成功'
    case -1: return '已关闭'
    default: return '未知状态'
  }
}

// 获取订单状态标签类型
const getOrderStateTagType = (state) => {
  switch (state) {
    case 1: return 'warning'
    case 2: return 'success'
    case 3: return 'success'
    case -1: return 'danger'
    default: return 'info'
  }
}
</script>

<style scoped>
.console {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  color: #2c3e50;
}

.stat-card {
  height: 150px;
}

.stat-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.stat-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 5px;
}

.stat-desc {
  font-size: 12px;
  color: #909399;
}

.stat-up {
  color: #67c23a;
}

.stat-down {
  color: #f56c6c;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>