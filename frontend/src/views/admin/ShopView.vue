<template>
  <div class="shop-management">
    <h2>商店管理</h2>
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>商品列表</span>
          <el-button type="primary" @click="handleAddShop">添加商品</el-button>
        </div>
      </template>
      <div class="search-bar">
        <el-form :model="searchForm" inline>
          <el-form-item label="商品名称">
            <el-input v-model="searchForm.item" placeholder="请输入商品名称"></el-input>
          </el-form-item>
          <el-form-item label="商品分类">
            <el-select v-model="searchForm.category" placeholder="请选择商品分类">
              <el-option label="全部" value=""></el-option>
              <el-option label="默认" value="默认"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品状态">
            <el-select v-model="searchForm.stop" placeholder="请选择商品状态">
              <el-option label="全部" value=""></el-option>
              <el-option label="在售" :value="false"></el-option>
              <el-option label="停售" :value="true"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table :data="shops" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="item" label="商品名称"></el-table-column>
        <el-table-column prop="image" label="商品图片" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.image" :src="scope.row.image" :preview-src-list="[scope.row.image]" style="width: 50px; height: 50px;" fit="cover"></el-image>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="category" label="商品分类" width="120"></el-table-column>
        <el-table-column prop="stop" label="商品状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.stop ? 'danger' : 'success'">
              {{ scope.row.stop ? '停售' : '在售' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditShop(scope.row)">编辑</el-button>
            <el-button v-if="!scope.row.stop" type="warning" size="small" @click="handleStopShop(scope.row.id, true)">停售</el-button>
            <el-button v-else type="success" size="small" @click="handleStopShop(scope.row.id, false)">上架</el-button>
            <el-button type="danger" size="small" @click="handleDeleteShop(scope.row.id)">删除</el-button>
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

    <!-- 添加/编辑商品对话框 -->
    <el-dialog v-model="dialogVisible" title="{{ isEdit ? '编辑商品' : '添加商品' }}" width="600px">
      <el-form :model="shopForm" :rules="shopRules" ref="shopFormRef" label-width="80px">
        <el-form-item label="商品名称" prop="item">
          <el-input v-model="shopForm.item" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :http-request="handleImageUpload"
            :before-upload="beforeImageUpload"
          >
            <el-image v-if="shopForm.image" :src="shopForm.image" class="avatar" fit="cover"></el-image>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="shopForm.price" :min="0.01" :step="0.01" :precision="2" placeholder="请输入商品价格"></el-input-number>
        </el-form-item>
        <el-form-item label="商品分类" prop="category">
          <el-input v-model="shopForm.category" placeholder="请输入商品分类"></el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input type="textarea" v-model="shopForm.description" placeholder="请输入商品描述" :rows="5"></el-input>
        </el-form-item>
        <el-form-item label="允许输入字段">
          <el-input type="textarea" v-model="shopForm.inputs" placeholder="请输入允许用户输入的字段名，多个字段用逗号分隔" :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="Webhook API">
          <el-input v-model="shopForm.api" placeholder="请输入Webhook API地址"></el-input>
        </el-form-item>
        <el-form-item label="商品状态">
          <el-switch v-model="shopForm.stop" active-text="停售" inactive-text="在售"></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

const shops = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const dialogVisible = ref(false)
const isEdit = ref(false)
const shopFormRef = ref()

const searchForm = reactive({
  item: '',
  category: '',
  stop: ''
})

const shopForm = reactive({
  id: '',
  item: '',
  image: '',
  description: '',
  price: 0.00,
  inputs: '',
  api: '',
  category: '默认',
  stop: false
})

const shopRules = {
  item: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '商品价格不能小于0.01元', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请输入商品分类', trigger: 'blur' }
  ]
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  loadShops()
}

// 处理重置
const handleReset = () => {
  searchForm.item = ''
  searchForm.category = ''
  searchForm.stop = ''
  currentPage.value = 1
  loadShops()
}

// 处理添加商品
const handleAddShop = () => {
  isEdit.value = false
  // 重置表单
  if (shopFormRef.value) {
    shopFormRef.value.resetFields()
  }
  // 清空表单数据
  shopForm.id = ''
  shopForm.item = ''
  shopForm.image = ''
  shopForm.description = ''
  shopForm.price = 0.00
  shopForm.inputs = ''
  shopForm.api = ''
  shopForm.category = '默认'
  shopForm.stop = false
  dialogVisible.value = true
}

// 处理编辑商品
const handleEditShop = (shop) => {
  isEdit.value = true
  // 填充表单数据
  shopForm.id = shop.id
  shopForm.item = shop.item
  shopForm.image = shop.image
  shopForm.description = shop.description
  shopForm.price = shop.price
  shopForm.inputs = shop.inputs
  shopForm.api = shop.api
  shopForm.category = shop.category
  shopForm.stop = shop.stop
  dialogVisible.value = true
}

// 处理商品上下架
const handleStopShop = (id, stop) => {
  ElMessageBox.confirm(stop ? '确定要停售这个商品吗？' : '确定要上架这个商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用上下架API
    ElMessage.success(stop ? '商品已停售' : '商品已上架')
    // 刷新列表
    loadShops()
  }).catch(() => {
    // 取消操作
  })
}

// 处理删除商品
const handleDeleteShop = (id) => {
  ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(() => {
    // 这里应该调用删除API
    ElMessage.success('删除成功')
    // 刷新列表
    loadShops()
  }).catch(() => {
    // 取消删除
  })
}

// 处理表单提交
const handleSubmit = async () => {
  if (!shopFormRef.value) return
  await shopFormRef.value.validate((valid) => {
    if (valid) {
      // 这里应该调用保存API
      dialogVisible.value = false
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      // 刷新列表
      loadShops()
    }
  })
}

// 处理图片上传
const handleImageUpload = (req) => {
  const file = req.file
  // 这里应该调用上传API
  // 模拟上传成功
  shopForm.image = URL.createObjectURL(file)
}

// 图片上传前校验
const beforeImageUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  loadShops()
}

// 处理当前页变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  loadShops()
}

// 加载商品列表
const loadShops = () => {
  // 这里应该调用API获取商品列表
  // 模拟数据
  shops.value = []
  total.value = 0
}

// 初始化加载商品列表
loadShops()
</script>

<style scoped>
.shop-management {
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

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
}
</style>