// 只在开发环境启用 Mock
if (import.meta.env.DEV) {
  import('./shorturl')
}