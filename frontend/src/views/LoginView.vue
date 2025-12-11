<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <div class="login-logo">
        <img src="../assets/logo.svg" alt="OnlinePay" class="logo-img">
      </div>
      <h2>登录管理后台</h2>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      // 这里应该调用登录API，暂时模拟登录成功
      setTimeout(() => {
        loading.value = false
        ElMessage.success('登录成功')
        router.push('/admin/console')
      }, 1000)
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--bg-color);
}

.login-form-wrapper {
  background-color: var(--bg-light);
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.3);
  width: 100%;
  max-width: 400px;
  border: 1px solid var(--border-color);
}

.login-logo {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.logo-img {
  width: 100px;
  height: 100px;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: var(--primary-color);
}
</style>