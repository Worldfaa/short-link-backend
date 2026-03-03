<template>
  <div class="demo-page">
    <div class="demo-container">
      <div class="header">
        <div class="header-main">
          <el-icon class="icon-link"><Link /></el-icon>
          <h1>短链接生成器</h1>
          <p class="subtitle">快速生成简洁的短链接</p>
        </div>
        <el-dropdown @command="handleCommand">
          <el-button size="large" class="user-btn">
            <el-icon><User /></el-icon>
            {{ username }}
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="admin">
                <el-icon><Management /></el-icon>
                管理后台
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      
      <el-card class="input-card" shadow="hover">
        <el-form :model="form" label-position="top">
          <el-form-item label="原始链接">
            <el-input 
              v-model="form.originalUrl" 
              placeholder="请输入长链接，例如：https://www.example.com"
              size="large"
              clearable
              class="url-input"
            >
              <template #prefix>
                <el-icon><Link /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              @click="generateShortUrl" 
              :loading="loading"
              size="large"
              class="generate-btn"
            >
              <el-icon><MagicStick /></el-icon>
              生成短链接
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
      
      <el-card v-if="shortUrl" class="result-card" shadow="hover">
        <div class="result-header">
          <el-icon class="success-icon"><CircleCheck /></el-icon>
          <h3>生成成功</h3>
        </div>
        <div class="result-content">
          <div class="short-url-container">
            <el-input 
              v-model="shortUrl" 
              readonly 
              size="large"
              class="short-url-input"
            >
              <template #prefix>
                <el-icon><Link /></el-icon>
              </template>
              <template #append>
                <el-button 
                  @click="copyShortUrl"
                  :loading="copyLoading"
                  size="large"
                  type="primary"
                >
                  <el-icon><CopyDocument /></el-icon>
                  复制
                </el-button>
              </template>
            </el-input>
          </div>
          <div class="original-url">
            <span class="label">原始链接：</span>
            <el-tooltip :content="form.originalUrl" placement="top">
              <span class="url">{{ form.originalUrl }}</span>
            </el-tooltip>
          </div>
        </div>
      </el-card>
      
      <div class="tips">
        <el-icon class="tips-icon"><InfoFilled /></el-icon>
        <p>提示：生成的短链接可在 <a href="/admin" class="admin-link">管理后台</a> 中查看和管理</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Link, MagicStick, CircleCheck, CopyDocument, InfoFilled, User, ArrowDown, SwitchButton, Management } from '@element-plus/icons-vue'
import { shorturlApi } from '../../api/shorturl'
import { storageService } from '../../utils/storage'
import { authService } from '../../utils/auth'

const router = useRouter()
const username = ref(authService.getUser()?.username || '用户')

const form = ref({
  originalUrl: ''
})

const shortUrl = ref('')
const loading = ref(false)

const handleCommand = (command: string) => {
  if (command === 'admin') {
    router.push('/admin')
  } else if (command === 'logout') {
    ElMessageBox.confirm(
      '确定要退出登录吗？',
      '退出确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    ).then(() => {
      authService.clearAuth()
      ElMessage.success('已退出登录')
      router.push('/login')
    }).catch(() => {})
  }
}
const copyLoading = ref(false)

const generateShortUrl = async () => {
  if (!form.value.originalUrl) {
    ElMessage.warning('请输入原始链接')
    return
  }
  
  // URL格式校验
  try {
    new URL(form.value.originalUrl)
  } catch {
    ElMessage.error('请输入正确的URL格式')
    return
  }
  
  loading.value = true
  try {
    const response = await shorturlApi.generate({
      url: form.value.originalUrl
    })
    
    // 使用后端返回的短链接地址或构建短链接地址
    if (response.data.shortUrl) {
      shortUrl.value = response.data.shortUrl
    } else {
      const baseUrl = window.location.origin
      shortUrl.value = `${baseUrl}/${response.data.shortCode}`
    }
    
    // 自动保存到管理后台
    const urlObj = new URL(form.value.originalUrl)
    const name = urlObj.hostname || '未命名链接'
    storageService.addShortLink({
      name,
      originalUrl: form.value.originalUrl,
      shortCode: response.data.shortCode,
      status: 'ENABLED'
    })
    
    ElMessage.success('短链接生成成功')
  } catch (error) {
    ElMessage.error('生成失败，请重试')
  } finally {
    loading.value = false
  }
}

const copyShortUrl = async () => {
  if (!shortUrl.value) return
  
  copyLoading.value = true
  try {
    await navigator.clipboard.writeText(shortUrl.value)
    ElMessage.success('复制成功')
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  } finally {
    copyLoading.value = false
  }
}
</script>

<style scoped>
.demo-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.demo-container {
  max-width: 600px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.header {
  text-align: center;
  color: white;
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.header-main {
  flex: 1;
}

.user-btn {
  border-radius: 8px;
  font-weight: 500;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
}

.user-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  color: white;
}

.user-btn .el-icon {
  margin-right: 6px;
}

.icon-link {
  font-size: 48px;
  margin-bottom: 16px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.header h1 {
  font-size: 32px;
  font-weight: 600;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.subtitle {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.input-card {
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
}

.url-input {
  margin-bottom: 16px;
}

.generate-btn {
  width: 100%;
  font-size: 16px;
  font-weight: 500;
  padding: 12px;
  border-radius: 8px;
}

.generate-btn .el-icon {
  margin-right: 8px;
}

.result-card {
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: #67c23a;
}

.success-icon {
  font-size: 24px;
}

.result-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.result-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.short-url-container {
  margin-bottom: 8px;
}

.short-url-input {
  border-radius: 8px;
}

.original-url {
  font-size: 14px;
  color: #606266;
}

.label {
  font-weight: 500;
  margin-right: 8px;
}

.url {
  color: #409eff;
  text-decoration: underline;
  word-break: break-all;
}

.tips {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  padding: 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  backdrop-filter: blur(10px);
}

.tips-icon {
  font-size: 20px;
  color: #409eff;
  margin-top: 2px;
  flex-shrink: 0;
}

.tips p {
  margin: 0;
  color: #606266;
  line-height: 1.4;
}

.admin-link {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.admin-link:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .demo-container {
    padding: 0 16px;
  }
  
  .header h1 {
    font-size: 24px;
  }
  
  .input-card,
  .result-card {
    border-radius: 8px;
  }
}
</style>