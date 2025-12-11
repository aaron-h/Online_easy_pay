<template>
  <div class="home">
    <div class="logo-container">
      <img src="../assets/logo.svg" alt="OnlinePay" class="home-logo">
    </div>
    <h1>OnlinePay个人收款平台</h1>
    <p>一款安全高效的个人收款解决方案</p>
    <div class="actions">
      <el-button type="primary" @click="$router.push('/login')">登录管理后台</el-button>
      <el-button @click="$router.push('/shop')">进入商店</el-button>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home {
  text-align: center;
  padding: 2rem;
  background-color: var(--bg-color);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--primary-color);
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  color: var(--text-secondary);
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.home-logo {
  width: 150px;
  height: 150px;
}

.actions {
  display: flex;
  justify-content: center;
  gap: 1rem;
}
</style>