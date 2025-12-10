<template>
  <div class="order-management">
    <h2>订单管理</h2>
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>订单列表</span>
        </div>
      </template>
      <div class="search-bar">
        <el-form :model="searchForm" inline>
          <el-form-item label="订单号">
            <el-input v-model="searchForm.orderId" placeholder="请输入订单号"></el-input>
          </el-form-item>
          <el-form-item label="应用名称">
            <el-input v-model="searchForm.appName" placeholder="请输入应用名称"></el-input>
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="searchForm.state" placeholder="请选择订单状态">
              <el-option label="全部" value=""></el-option>
              <el-option label="等待支付" :value="1"></el-option>
              <el-option label="已支付" :value="2"></el-option>
              <el-option label="订单成功" :value="3"></el-option>
              <el-option label="已关闭" :value="-1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="支付类型">
            <el-select v-model="searchForm.payType" placeholder="请选择支付类型">
              <el-option label="全部" value=""></el-option>
              <el-option label="支付宝" :value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="orders" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="orderId" label="订单号" width="180"></el-table-column>
        <el-table-column prop="appName" label="应用名称"></el-table-column>
        <el-table-column prop="appItem" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="金额" width="100">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="payType" label="支付类型" width="100">
          <template #default="scope">
            {{ getPayTypeText(scope.row.payType) }}
          </template>
        </el-table-column>
        <el-table-column prop="state" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getOrderStateTagType(scope.row.state)">
              {{ getOrderStateText(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="180"></el-table-column>
        <el-table-column prop="closeTime" label="关闭时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleViewOrder(scope.row)">查看</el-button>
            <el-button v-if="scope.row.state === 1" type="danger" size="small" @click="handleCloseOrder(scope.row.orderId)">关闭</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 查看订单对话框 -->
    <el-dialog v-model="orderDialogVisible" title="查看订单" width="600px">
      <el-descriptions :column="1" v-if="selectedOrder">
        <el-descriptions-item label="订单号">{{ selectedOrder.orderId }}</el-descriptions-item>
        <el-descriptions-item label="应用名称">{{ selectedOrder.appName }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ selectedOrder.appItem }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥{{ selectedOrder.price.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="实际支付金额">¥{{ selectedOrder.realPrice.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="支付类型">{{ getPayTypeText(selectedOrder.payType) }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getOrderStateTagType(selectedOrder.state)">
            {{ getOrderStateText(selectedOrder.state) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ selectedOrder.createTime }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ selectedOrder.payTime || '未支付' }}</el-descriptions-item>
        <el-descriptions-item label="关闭时间">{{ selectedOrder.closeTime || '未关闭' }}</el-descriptions-item>
        <el-descriptions-item label="附加参数">{{ selectedOrder.param || '无' }}</el-descriptions-item>
        <el-descriptions-item label="用户信息">{{ selectedOrder.user || '无' }}</el-descriptions-item>
        <el-descriptions-item label="支付二维码">
          <el-image v-if="selectedOrder.payImage" :src="selectedOrder.payImage" style="width: 200px; height: 200px;" fit="cover"></el-image>
          <span v-else>无</span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="orderDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const orders = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const orderDialogVisible = ref(false)
const selectedOrder = ref(null)

const searchForm = reactive({
  orderId: '',
  appName: '',
  state: '',
  payType: ''
})

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

// 获取支付类型文本
const getPayTypeText = (payType) => {
  switch (payType) {
    case 2: return '支付宝'
    case 3: return '微信App'
    case 4: return '支付宝App'
    default: return '未知类型'
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  loadOrders()
}

// 处理重置
const handleReset = () => {
  searchForm.orderId = ''
  searchForm.appName = ''
  searchForm.state = ''
  searchForm.payType = ''
  currentPage.value = 1
  loadOrders()
}

// 处理查看订单
const handleViewOrder = (order) => {
  selectedOrder.value = order
  orderDialogVisible.value = true
}

// 处理关闭订单
const handleCloseOrder = (orderId) => {
  ElMessageBox.confirm('确定要关闭这个订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用关闭订单API
    ElMessage.success('订单关闭成功')
    // 刷新列表
    loadOrders()
  }).catch(() => {
    // 取消关闭
  })
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  loadOrders()
}

// 处理当前页变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  loadOrders()
}

// 加载订单列表
const loadOrders = () => {
  // 这里应该调用API获取订单列表
  // 模拟数据
  orders.value = []
  total.value = 0
}

// 初始化加载订单列表
loadOrders()
</script>

<style scoped>
.order-management {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  color: #2c3e50;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>