<template>
  <div class="app-management">
    <h2>应用管理</h2>
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>应用列表</span>
          <el-button type="primary" @click="handleAddApp">添加应用</el-button>
        </div>
      </template>
      <el-table :data="apps" style="width: 100%" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="appName" label="应用名称"></el-table-column>
        <el-table-column prop="appKey" label="应用密钥" width="200"></el-table-column>
        <el-table-column prop="appImage" label="应用图片" width="100">
          <template #default="scope">
            <el-image v-if="scope.row.appImage" :src="scope.row.appImage" :preview-src-list="[scope.row.appImage]" style="width: 50px; height: 50px;" fit="cover"></el-image>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEditApp(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDeleteApp(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑应用对话框 -->
    <el-dialog v-model="dialogVisible" title="{{ isEdit ? '编辑应用' : '添加应用' }}" width="500px">
      <el-form :model="appForm" :rules="appRules" ref="appFormRef" label-width="80px">
        <el-form-item label="应用名称" prop="appName">
          <el-input v-model="appForm.appName" placeholder="请输入应用名称"></el-input>
        </el-form-item>
        <el-form-item label="应用图片">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :http-request="handleImageUpload"
            :before-upload="beforeImageUpload"
          >
            <el-image v-if="appForm.appImage" :src="appForm.appImage" class="avatar" fit="cover"></el-image>
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
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

const apps = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const appFormRef = ref()

const appForm = reactive({
  id: '',
  appName: '',
  appKey: '',
  appImage: ''
})

const appRules = {
  appName: [
    { required: true, message: '请输入应用名称', trigger: 'blur' }
  ]
}

// 处理添加应用
const handleAddApp = () => {
  isEdit.value = false
  // 重置表单
  if (appFormRef.value) {
    appFormRef.value.resetFields()
  }
  // 清空表单数据
  appForm.id = ''
  appForm.appName = ''
  appForm.appKey = ''
  appForm.appImage = ''
  dialogVisible.value = true
}

// 处理编辑应用
const handleEditApp = (app) => {
  isEdit.value = true
  // 填充表单数据
  appForm.id = app.id
  appForm.appName = app.appName
  appForm.appKey = app.appKey
  appForm.appImage = app.appImage
  dialogVisible.value = true
}

// 处理删除应用
const handleDeleteApp = (id) => {
  ElMessageBox.confirm('确定要删除这个应用吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该调用删除API
    ElMessage.success('删除成功')
    // 刷新列表
    loadApps()
  }).catch(() => {
    // 取消删除
  })
}

// 处理表单提交
const handleSubmit = async () => {
  if (!appFormRef.value) return
  await appFormRef.value.validate((valid) => {
    if (valid) {
      // 这里应该调用保存API
      dialogVisible.value = false
      ElMessage.success(isEdit.value ? '编辑成功' : '添加成功')
      // 刷新列表
      loadApps()
    }
  })
}

// 处理图片上传
const handleImageUpload = (req) => {
  const file = req.file
  // 这里应该调用上传API
  // 模拟上传成功
  appForm.appImage = URL.createObjectURL(file)
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

// 加载应用列表
const loadApps = () => {
  // 这里应该调用API获取应用列表
  // 模拟数据
  apps.value = []
}

// 初始化加载应用列表
loadApps()
</script>

<style scoped>
.app-management {
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