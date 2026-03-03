# 短链接管理系统前端

## 环境要求
- Node.js (建议使用较新LTS版本)
- 包管理器：npm/pnpm/yarn任意（推荐pnpm）
- IDE：VSCode（推荐装ESLint/Prettier插件）

## 启动命令
1. 安装依赖
   ```bash
   npm install
   ```

2. 启动开发服务器
   ```bash
   npm run dev
   ```

3. 构建生产版本
   ```bash
   npm run build
   ```

## 联调模式配置
- 项目已配置代理，后端服务地址为：http://localhost:8081
- 代理配置在 `vite.config.ts` 文件中

## 页面入口
- **主页**：http://localhost:8080/
- **登录页**：http://localhost:8080/login
- **注册页**：http://localhost:8080/register
- **功能演示页**：http://localhost:8080/demo（需要登录）
- **管控后台**：http://localhost:8080/admin（需要登录）

## 项目结构
```
src/
  api/            # 接口封装
    http.ts       # HTTP请求封装
    shorturl.ts   # 短链接业务API
    auth.ts       # 认证业务API
  pages/          # 页面
    home/         # 主页
    auth/         # 认证相关页面
      LoginPage.vue     # 登录页
      RegisterPage.vue  # 注册页
    demo/         # 功能演示页
    admin/        # 管控后台
  router/         # 路由配置
  utils/          # 工具类
  types/          # 类型定义
```

## 功能说明
1. **主页**：
   - 系统功能介绍
   - 用户登录和注册入口

2. **登录/注册**：
   - 用户认证系统
   - 表单验证
   - 错误提示

3. **功能演示页**：
   - 输入长链接 → 生成短码 → 复制短链接
   - 支持URL格式校验
   - 需要登录才能访问

4. **管控后台**：
   - 短链接列表展示
   - 按名称搜索
   - 新增/编辑/删除短链接
   - 启用/禁用短链接
   - 操作后自动刷新列表
   - 按钮防重复提交
   - 需要登录才能访问

## 技术栈
- Vue 3 + TypeScript
- Element Plus
- Axios
- Vite