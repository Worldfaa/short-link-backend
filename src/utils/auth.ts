import type { User, LoginResponse } from '../api/auth'

const TOKEN_KEY = 'auth_token'
const USER_KEY = 'auth_user'

export const authService = {
  // 保存登录信息
  setAuth(data: LoginResponse): void {
    localStorage.setItem(TOKEN_KEY, data.token)
    localStorage.setItem(USER_KEY, JSON.stringify(data.user))
  },

  // 获取 token
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY)
  },

  // 获取用户信息
  getUser(): User | null {
    const data = localStorage.getItem(USER_KEY)
    if (!data) return null
    return JSON.parse(data)
  },

  // 判断是否已登录
  isLoggedIn(): boolean {
    return !!this.getToken()
  },

  // 清除登录信息
  clearAuth(): void {
    localStorage.removeItem(TOKEN_KEY)
    localStorage.removeItem(USER_KEY)
  }
}