// Mock认证数据 - 用于前端测试
export const mockUsers = [
  {
    id: 1,
    username: 'test',
    password: '123456',
    token: 'mock_token_test_user_001'
  },
  {
    id: 2,
    username: 'admin',
    password: 'admin123',
    token: 'mock_token_admin_user_002'
  }
]

// 测试账号信息
export const testAccounts = {
  // 普通测试账号
  testUser: {
    username: 'test',
    password: '123456',
    description: '普通测试用户'
  },
  // 管理员测试账号
  adminUser: {
    username: 'admin',
    password: 'admin123',
    description: '管理员测试用户'
  }
}

// 验证用户登录
export const mockLogin = (username: string, password: string) => {
  const user = mockUsers.find(u => u.username === username && u.password === password)
  if (user) {
    return {
      code: 200,
      data: {
        token: user.token,
        user: {
          id: user.id,
          username: user.username
        }
      },
      message: '登录成功'
    }
  }
  return {
    code: 401,
    data: null,
    message: '用户名或密码错误'
  }
}

// 注册用户
export const mockRegister = (username: string, password: string) => {
  const existingUser = mockUsers.find(u => u.username === username)
  if (existingUser) {
    return {
      code: 400,
      data: null,
      message: '用户名已存在'
    }
  }
  
  const newUser = {
    id: mockUsers.length + 1,
    username,
    password,
    token: `mock_token_${username}_${Date.now()}`
  }
  mockUsers.push(newUser)
  
  return {
    code: 200,
    data: {
      id: newUser.id,
      username: newUser.username
    },
    message: '注册成功'
  }
}

export default {
  mockUsers,
  testAccounts,
  mockLogin,
  mockRegister
}