<template>
  <div class="shop-container">
    <div class="shop-header">
      <h1>商品商店</h1>
      <p>浏览我们的商品，选择您需要的服务</p>
    </div>
    
    <div class="shop-content">
      <!-- 分类筛选 -->
      <div class="category-filter">
        <el-button 
          v-for="category in categories" 
          :key="category"
          :type="activeCategory === category ? 'primary' : 'default'"
          @click="setActiveCategory(category)"
          size="small"
        >
          {{ category }}
        </el-button>
      </div>
      
      <!-- 商品列表 -->
      <div class="shop-grid">
        <el-card 
          v-for="item in filteredItems" 
          :key="item.id"
          shadow="hover"
          class="shop-item"
        >
          <div class="shop-item-image">
            <el-image 
              :src="item.image || 'https://picsum.photos/300/200'" 
              fit="cover"
              :alt="item.item"
            ></el-image>
          </div>
          <div class="shop-item-info">
            <h3 class="shop-item-title">{{ item.item }}</h3>
            <p class="shop-item-category">{{ item.category }}</p>
            <p class="shop-item-description">{{ item.description }}</p>
            <div class="shop-item-footer">
              <span class="shop-item-price">¥{{ item.price }}</span>
              <el-button 
                type="primary" 
                size="small"
                @click="buyItem(item)"
              >
                立即购买
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 空状态 -->
      <div v-if="filteredItems.length === 0" class="empty-state">
        <el-empty description="暂无商品"></el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const items = ref([])
const loading = ref(false)
const activeCategory = ref('全部')

// 分类列表
const categories = ref(['全部', '教育', '电子书', '软件', '服务', '云服务', '安全'])

// 获取商品列表
const fetchItems = async () => {
  loading.value = true
  try {
    // 这里应该调用实际的后端API，现在使用模拟数据
    // const response = await axios.get('/api/shop')
    // items.value = response.data
    
    // 模拟数据
    items.value = [
      { id: 1, item: '在线课程', description: '高级Java开发课程，包含Spring Boot、微服务等内容', price: '99.99', category: '教育', stop: false },
      { id: 2, item: '电子书', description: '《现代前端开发实战》，包含Vue 3、React等框架', price: '39.99', category: '电子书', stop: false },
      { id: 3, item: '软件许可', description: 'OnlinePay专业版许可，支持无限商户', price: '199.00', category: '软件', stop: false },
      { id: 4, item: '技术支持', description: '1小时专业技术支持服务', price: '150.00', category: '服务', stop: false },
      { id: 5, item: '定制开发', description: '根据需求定制开发支付系统', price: '5000.00', category: '服务', stop: false },
      { id: 6, item: '服务器租赁', description: '高性能云服务器，适合中小型应用', price: '299.00', category: '云服务', stop: false },
      { id: 7, item: '域名注册', description: '顶级域名注册服务，包含一年DNS管理', price: '59.99', category: '云服务', stop: false },
      { id: 8, item: 'SSL证书', description: '通配符SSL证书，保护所有子域名', price: '199.00', category: '安全', stop: false }
    ]
  } catch (error) {
    console.error('获取商品列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 设置活跃分类
const setActiveCategory = (category) => {
  activeCategory.value = category
}

// 筛选商品
const filteredItems = computed(() => {
  if (activeCategory.value === '全部') {
    return items.value
  }
  return items.value.filter(item => item.category === activeCategory.value)
})

// 购买商品
const buyItem = (item) => {
  // 跳转到支付页面，传入商品信息
  router.push({
    path: '/pay',
    query: {
      item: item.item,
      price: item.price
    }
  })
}

// 页面加载时获取商品列表
onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
.shop-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.shop-header {
  text-align: center;
  margin-bottom: 40px;
}

.shop-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  color: #2c3e50;
}

.shop-header p {
  font-size: 1.1rem;
  color: #7f8c8d;
}

.category-filter {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  flex-wrap: wrap;
  justify-content: center;
}

.shop-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.shop-item {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.shop-item-image {
  height: 200px;
  overflow: hidden;
  margin-bottom: 15px;
}

.shop-item-image img {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.shop-item:hover .shop-item-image img {
  transform: scale(1.05);
}

.shop-item-title {
  font-size: 1.2rem;
  margin-bottom: 5px;
  color: #2c3e50;
}

.shop-item-category {
  font-size: 0.9rem;
  color: #165dff;
  margin-bottom: 10px;
}

.shop-item-description {
  font-size: 0.95rem;
  color: #7f8c8d;
  margin-bottom: 20px;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.shop-item-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.shop-item-price {
  font-size: 1.5rem;
  font-weight: bold;
  color: #e74c3c;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}
</style>