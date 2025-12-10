import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'

const app = createApp(App)
const pinia = createPinia()

// 配置axios
axios.defaults.baseURL = '/api'
axios.defaults.timeout = 10000

// 请求拦截器
axios.interceptors.request.use(config => {
  // 可以在这里添加token等认证信息
  return config
}, error => {
  return Promise.reject(error)
})

// 响应拦截器
axios.interceptors.response.use(response => {
  return response.data
}, error => {
  return Promise.reject(error)
})

app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.mount('#app')