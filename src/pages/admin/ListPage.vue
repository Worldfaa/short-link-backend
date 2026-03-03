<template>
  <div class="admin-page">
    <div class="admin-container">
      <div class="header">
        <div class="header-content">
          <div class="title-section">
            <el-icon class="header-icon"><Management /></el-icon>
            <div>
              <h1>短链接管理</h1>
              <p class="subtitle">管理您的短链接，查看访问统计</p>
            </div>
          </div>
          <div class="header-actions">
            <el-button 
              type="success" 
              @click="goToDemo"
              size="large"
              class="generate-btn"
            >
              <el-icon><Plus /></el-icon>
              生成短链接
            </el-button>
            <el-dropdown @command="handleCommand">
              <el-button size="large" class="user-btn">
                <el-icon><User /></el-icon>
                {{ username }}
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>

      <el-card class="search-card" shadow="never">
        <div class="search-section">
          <el-select 
            v-model="searchType" 
            placeholder="选择搜索类型"
            size="large"
            class="search-select search-type-select"
          >
            <el-option label="按名称" value="name" />
            <el-option label="按短码" value="shortCode" />
            <el-option label="按原始链接" value="originalUrl" />
          </el-select>
          <el-input 
            v-model="searchValue" 
            :placeholder="getPlaceholder()"
            size="large"
            clearable
            class="search-input"
            @keyup.enter="search"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-select 
            v-model="searchForm.status" 
            placeholder="按状态筛选"
            size="large"
            clearable
            class="search-select"
          >
            <el-option label="启用" value="ENABLED" />
            <el-option label="禁用" value="DISABLED" />
          </el-select>
          <el-button 
            type="primary" 
            @click="search"
            size="large"
            class="search-btn"
          >
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button 
            @click="resetSearch"
            size="large"
            class="reset-btn"
          >
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
      </el-card>

      <el-card class="table-card" shadow="never">
        <el-table 
          :data="shortLinks" 
          style="width: 100%" 
          v-loading="loading"
          :header-cell-style="headerCellStyle"
          row-class-name="table-row"
        >
          <el-table-column prop="name" label="名称" min-width="150">
            <template #default="scope">
              <div class="name-cell">
                <el-icon class="link-icon"><Link /></el-icon>
                <span class="name-text">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="shortCode" label="短码" width="120">
            <template #default="scope">
              <el-tag type="info" class="short-code-tag">
                {{ scope.row.shortCode }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="originalUrl" label="原始链接" min-width="250">
            <template #default="scope">
              <el-tooltip 
                :content="scope.row.originalUrl" 
                placement="top" 
                :show-after="500"
              >
                <div class="url-cell">{{ scope.row.originalUrl }}</div>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="ENABLED"
                inactive-value="DISABLED"
                @change="(val) => handleStatusChange(scope.row, val)"
                class="status-switch"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="160">
            <template #default="scope">
              <div class="time-cell">
                <el-icon><Clock /></el-icon>
                <span>{{ scope.row.createdAt }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="scope">
              <div class="action-buttons">
                <el-button 
                  type="danger" 
                  link
                  @click="deleteShortLink(scope.row)"
                  class="action-btn"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        
        <el-empty 
          v-if="!loading && shortLinks.length === 0" 
          description="暂无短链接数据"
          class="empty-state"
        >
          <p class="empty-tip">您可以在 <a href="/demo" class="demo-link">功能演示页</a> 生成短链接</p>
        </el-empty>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Management, Plus, Search, Refresh, Link, Delete, 
  Clock, User, ArrowDown, SwitchButton
} from '@element-plus/icons-vue'
import { shorturlApi } from '../../api/shorturl'
import type { ShortLink } from '../../types'
import { storageService } from '../../utils/storage'
import { authService } from '../../utils/auth'

const router = useRouter()

const searchType = ref('name')
const searchValue = ref('')
const searchForm = ref({
  status: ''
})

const shortLinks = ref<ShortLink[]>([])
const loading = ref(false)
const username = ref(authService.getUser()?.username || '用户')

const goToDemo = () => {
  router.push('/demo')
}

const getPlaceholder = () => {
  switch (searchType.value) {
    case 'name':
      return '请输入短链接名称'
    case 'shortCode':
      return '请输入短码'
    case 'originalUrl':
      return '请输入原始链接'
    default:
      return '请输入搜索内容'
  }
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
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

const headerCellStyle = {
  background: '#f5f7fa',
  color: '#606266',
  fontWeight: 600
}

onMounted(() => {
  loadShortLinks()
})

const loadShortLinks = () => {
  loading.value = true
  try {
    shortLinks.value = storageService.getShortLinks()
  } catch (error) {
    ElMessage.error('加载失败，请重试')
  } finally {
    loading.value = false
  }
}

const search = () => {
  loading.value = true
  try {
    const allLinks = storageService.getShortLinks()
    shortLinks.value = allLinks.filter(link => {
      // 按选择的类型搜索
      if (searchValue.value) {
        switch (searchType.value) {
          case 'name':
            if (!link.name.toLowerCase().includes(searchValue.value.toLowerCase())) {
              return false
            }
            break
          case 'shortCode':
            if (!link.shortCode.toLowerCase().includes(searchValue.value.toLowerCase())) {
              return false
            }
            break
          case 'originalUrl':
            if (!link.originalUrl.toLowerCase().includes(searchValue.value.toLowerCase())) {
              return false
            }
            break
        }
      }
      // 按状态筛选
      if (searchForm.value.status && link.status !== searchForm.value.status) {
        return false
      }
      return true
    })
  } catch (error) {
    ElMessage.error('搜索失败，请重试')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchType.value = 'name'
  searchValue.value = ''
  searchForm.value.status = ''
  loadShortLinks()
}

const deleteShortLink = (row: ShortLink) => {
  ElMessageBox.confirm(
    `确定要删除短链接 "${row.name}" 吗？`, 
    '删除确认', 
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger'
    }
  )
  .then(async () => {
    await shorturlApi.delete(row.shortCode)
    storageService.deleteShortLink(row.shortCode)
    ElMessage.success('删除成功')
    loadShortLinks()
  })
  .catch(() => {})
}

const handleStatusChange = async (row: ShortLink, newStatus: string) => {
  try {
    if (newStatus === 'ENABLED') {
      await shorturlApi.enable(row.shortCode)
    } else {
      await shorturlApi.disable(row.shortCode)
    }
    storageService.updateShortLink(row.shortCode, { status: newStatus })
    ElMessage.success(newStatus === 'ENABLED' ? '短链接已启用' : '短链接已禁用')
  } catch (error) {
    // 恢复原状态
    row.status = newStatus === 'ENABLED' ? 'DISABLED' : 'ENABLED'
    ElMessage.error('操作失败，请重试')
  }
}
</script>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: #f0f2f5;
  padding: 24px;
}

.admin-container {
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  margin-bottom: 24px;
}

.search-section {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-wrap: wrap;
}

.search-input {
  flex: 1;
  min-width: 200px;
}

.search-select {
  min-width: 100px;
  flex: 0 0 auto;
}

.search-type-select {
  min-width: 120px;
  max-width: 150px;
}

.search-btn,
.reset-btn {
  white-space: nowrap;
  flex: 0 0 auto;
}

@media (max-width: 768px) {
  .search-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-input,
  .search-select {
    width: 100%;
  }
  
  .search-btn,
  .reset-btn {
    width: 100%;
  }
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  padding: 24px 32px;
  border-radius: 12px;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.title-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-icon {
  font-size: 40px;
  color: #409eff;
}

.header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.generate-btn {
  border-radius: 8px;
  font-weight: 500;
}

.generate-btn .el-icon {
  margin-right: 6px;
}

.user-btn {
  border-radius: 8px;
  font-weight: 500;
}

.user-btn .el-icon {
  margin-right: 6px;
}

.search-card {
  margin-bottom: 16px;
  border-radius: 12px;
}

.search-section {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.search-btn .el-icon,
.reset-btn .el-icon {
  margin-right: 4px;
}

.table-card {
  border-radius: 12px;
  min-height: 400px;
}

.table-row {
  transition: background-color 0.2s;
}

.table-row:hover {
  background-color: #f5f7fa;
}

.name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.link-icon {
  color: #409eff;
}

.name-text {
  font-weight: 500;
  color: #1f2937;
}

.short-code-tag {
  font-family: 'Courier New', monospace;
  font-weight: 600;
}

.url-cell {
  color: #6b7280;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 250px;
}

.status-switch {
  --el-switch-on-color: #67c23a;
  --el-switch-off-color: #f56c6c;
}

.time-cell {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #6b7280;
  font-size: 13px;
}

.time-cell .el-icon {
  font-size: 14px;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  font-size: 13px;
}

.action-btn .el-icon {
  margin-right: 4px;
  font-size: 14px;
}

.empty-state {
  padding: 60px 0;
}

.empty-tip {
  color: #606266;
  margin: 0;
}

.demo-link {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.demo-link:hover {
  text-decoration: underline;
}
</style>