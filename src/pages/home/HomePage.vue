<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1>短链接生成系统</h1>
        <p>快速生成、管理和分享简洁的短链接</p>
        <div class="hero-buttons">
          <el-button type="primary" size="large" @click="goToLogin" class="login-button">
            <el-icon><User /></el-icon>
            用户登录
          </el-button>
          <el-button type="info" size="large" @click="goToRegister" class="register-button">
            <el-icon><UserFilled /></el-icon>
            注册账号
          </el-button>
        </div>
      </div>
    </div>
    
    <div class="features-section">
      <h2>系统功能</h2>
      <div class="features-grid">
        <el-card shadow="hover" class="feature-card">
          <div class="feature-icon">
            <el-icon><Link /></el-icon>
          </div>
          <h3>短链接生成</h3>
          <p>将长链接转换为简洁易记的短链接，方便分享和传播</p>
        </el-card>
        
        <el-card shadow="hover" class="feature-card">
          <div class="feature-icon">
            <el-icon><Management /></el-icon>
          </div>
          <h3>链接管理</h3>
          <p>查看、启用、禁用和删除已生成的短链接</p>
        </el-card>
        
        <el-card shadow="hover" class="feature-card">
          <div class="feature-icon">
            <el-icon><Lock /></el-icon>
          </div>
          <h3>用户认证</h3>
          <p>安全的用户登录和注册系统，保护您的链接</p>
        </el-card>
      </div>
    </div>
    
    <div class="footer">
      <p>© 2026 短链接生成系统</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Link, Management, Lock, User, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()

const goToLogin = () => {
  router.push('/login')
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.hero-section {
  min-height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
}

.hero-content h1 {
  font-size: 48px;
  font-weight: 600;
  margin-bottom: 20px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.hero-content p {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.login-button,
.register-button {
  padding: 12px 32px;
  font-size: 18px;
  font-weight: 500;
  border-radius: 8px;
}

.login-button .el-icon,
.register-button .el-icon {
  margin-right: 8px;
}

.features-section {
  padding: 60px 20px;
  background: rgba(255, 255, 255, 0.95);
  color: #333;
}

.features-section h2 {
  text-align: center;
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 40px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  border-radius: 12px;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 20px;
  color: #667eea;
}

.feature-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
}

.feature-card p {
  font-size: 16px;
  color: #666;
  line-height: 1.5;
}

.footer {
  background: rgba(0, 0, 0, 0.1);
  padding: 30px 20px;
  text-align: center;
  font-size: 14px;
  opacity: 0.8;
}

@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 36px;
  }
  
  .hero-content p {
    font-size: 18px;
  }
  
  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .login-button,
  .register-button {
    width: 200px;
  }
  
  .features-section {
    padding: 40px 20px;
  }
  
  .features-section h2 {
    font-size: 28px;
  }
}
</style>