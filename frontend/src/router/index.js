import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    children: [
      {
        path: 'console',
        name: 'admin-console',
        component: () => import('../views/admin/ConsoleView.vue')
      },
      {
        path: 'app',
        name: 'admin-app',
        component: () => import('../views/admin/AppView.vue')
      },
      {
        path: 'order',
        name: 'admin-order',
        component: () => import('../views/admin/OrderView.vue')
      },
      {
        path: 'shop',
        name: 'admin-shop',
        component: () => import('../views/admin/ShopView.vue')
      }
    ]
  },
  {
    path: '/pay',
    name: 'pay',
    component: () => import('../views/PayView.vue')
  },
  {
    path: '/pay/success',
    name: 'pay-success',
    component: () => import('../views/PaySuccessView.vue')
  },
  {
    path: '/shop',
    name: 'shop',
    component: () => import('../views/ShopView.vue')
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router