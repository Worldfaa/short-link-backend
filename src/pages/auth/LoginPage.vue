<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="header">
        <el-icon class="icon-user"><User /></el-icon>
        <h1>用户登录</h1>
        <p class="subtitle">欢迎回来，请登录您的账号</p>
      </div>
      
      <el-card class="auth-card" shadow="hover">
        <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
          <el-form-item label="用户名" prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名"
              size="large"
              clearable
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input 
              v-model="form.password" 
              type="password"
              placeholder="请输入密码"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleLogin" 
              :loading="loading"
              size="large"
              class="submit-btn"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="auth-footer">
          <p>还没有账号？<router-link to="/register" class="link">立即注册</router-link></p>

        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { authApi } from '../../api/auth'
import { authService } from '../../utils/auth'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度应在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 尝试连接后端服务
        const response = await authApi.login({
          username: form.username,
          password: form.password
        })
        
        authService.setAuth(response.data)
        ElMessage.success('登录成功')
        router.push('/demo')
      } catch (error) {
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.auth-container {
  max-width: 400px;
  width: 100%;
}

.header {
  text-align: center;
  color: white;
  margin-bottom: 30px;
}

.icon-user {
  font-size: 48px;
  margin-bottom: 16px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.header h1 {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.auth-card {
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.submit-btn {
  width: 100%;
  font-size: 16px;
  font-weight: 500;
  padding: 12px;
  border-radius: 8px;
  margin-top: 10px;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.auth-footer p {
  margin: 0 0 8px 0;
  color: #606266;
}

.auth-footer p:last-child {
  margin-bottom: 0;
}

.demo-link-text {
  font-size: 13px;
}

.link {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.link:hover {
  text-decoration: underline;
}
</style>