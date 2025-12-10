<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px" class="aside">
        <div class="logo">
          <img src="../../assets/logo.svg" alt="OnlinePay" class="logo-img">
          <span class="logo-text">OnlinePay管理后台</span>
        </div>
        <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" router>
          <el-menu-item index="/admin/console">
            <template #title>
              <el-icon><Management /></el-icon>
              <span>控制台</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/app">
            <template #title>
              <el-icon><Grid /></el-icon>
              <span>应用管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/order">
            <template #title>
              <el-icon><Ticket /></el-icon>
              <span>订单管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/admin/shop">
            <template #title>
              <el-icon><ShoppingBag /></el-icon>
              <span>商店管理</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="header-content">
            <span>欢迎使用OnlinePay管理后台</span>
            <div class="user-info">
              <el-dropdown>
                <span class="user-dropdown">
                  <el-icon><User /></el-icon>
                  管理员
                  <el-icon class="el-icon--right"><arrow-down /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </el-header>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Management, Grid, Ticket, ShoppingBag, User, ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const activeMenu = computed(() => {
  return route.path
})

const handleLogout = () => {
  // 这里应该清除登录状态
  ElMessage.success('退出登录成功')
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.aside {
  background-color: #001529;
  color: #fff;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  line-height: 60px;
  font-size: 18px;
  font-weight: bold;
  background-color: #165dff;
}

.logo-img {
  width: 30px;
  height: 30px;
  vertical-align: middle;
}

.logo-text {
  vertical-align: middle;
  color: #fff;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.main {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>