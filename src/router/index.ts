import { createRouter, createWebHistory } from 'vue-router'
import { authService } from '../utils/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../pages/home/HomePage.vue'),
      meta: { public: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../pages/auth/LoginPage.vue'),
      meta: { public: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../pages/auth/RegisterPage.vue'),
      meta: { public: true }
    },
    {
      path: '/demo',
      name: 'demo',
      component: () => import('../pages/demo/DemoPage.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('../pages/admin/ListPage.vue')
    }
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 如果路由是公开的，直接放行
  if (to.meta.public) {
    next()
    return
  }
  
  // 检查是否已登录
  if (authService.isLoggedIn()) {
    next()
  } else {
    // 未登录，跳转到登录页
    next('/login')
  }
})

export default router