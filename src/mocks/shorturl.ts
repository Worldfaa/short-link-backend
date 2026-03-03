import Mock from 'mockjs'

// 模拟数据
const mockShortLinks = [
  {
    id: 1,
    name: 'Google',
    originalUrl: 'https://www.google.com',
    shortCode: 'a1b2c3',
    status: 'ENABLED',
    createdAt: '2024-01-01 10:00:00',
    updatedAt: '2024-01-01 10:00:00'
  },
  {
    id: 2,
    name: 'Baidu',
    originalUrl: 'https://www.baidu.com',
    shortCode: 'd4e5f6',
    status: 'ENABLED',
    createdAt: '2024-01-02 11:00:00',
    updatedAt: '2024-01-02 11:00:00'
  },
  {
    id: 3,
    name: 'GitHub',
    originalUrl: 'https://github.com',
    shortCode: 'g7h8i9',
    status: 'DISABLED',
    createdAt: '2024-01-03 12:00:00',
    updatedAt: '2024-01-03 12:00:00'
  }
]

// 生成随机短码
const generateShortCode = () => {
  return Math.random().toString(36).substring(2, 8)
}

// 模拟API拦截
Mock.mock('/api/shortlinks', 'get', (options: any) => {
  const { url } = options
  const name = new URL(url).searchParams.get('name')
  let result = [...mockShortLinks]
  
  if (name) {
    result = result.filter(item => item.name.includes(name))
  }
  
  return {
    code: 200,
    data: result,
    message: 'success'
  }
})

Mock.mock('/api/shortlinks', 'post', (options: any) => {
  const body = JSON.parse(options.body)
  const newShortLink = {
    id: mockShortLinks.length + 1,
    name: body.name,
    originalUrl: body.originalUrl,
    shortCode: generateShortCode(),
    status: 'ENABLED',
    createdAt: new Date().toLocaleString(),
    updatedAt: new Date().toLocaleString()
  }
  mockShortLinks.push(newShortLink)
  
  return {
    code: 200,
    data: newShortLink,
    message: 'success'
  }
})

Mock.mock(/\/api\/shortlinks\/\d+/, 'put', (options: any) => {
  const id = parseInt(options.url.split('/').pop())
  const body = JSON.parse(options.body)
  const index = mockShortLinks.findIndex(item => item.id === id)
  
  if (index !== -1) {
    mockShortLinks[index] = {
      ...mockShortLinks[index],
      name: body.name,
      originalUrl: body.originalUrl,
      updatedAt: new Date().toLocaleString()
    }
    
    return {
      code: 200,
      data: mockShortLinks[index],
      message: 'success'
    }
  }
  
  return {
    code: 404,
    message: 'Not found'
  }
})

Mock.mock(/\/api\/shortlinks\/\d+/, 'delete', (options: any) => {
  const id = parseInt(options.url.split('/').pop())
  const index = mockShortLinks.findIndex(item => item.id === id)
  
  if (index !== -1) {
    mockShortLinks.splice(index, 1)
    return {
      code: 200,
      message: 'success'
    }
  }
  
  return {
    code: 404,
    message: 'Not found'
  }
})

Mock.mock(/\/api\/shortlinks\/\d+\/enable/, 'patch', (options: any) => {
  const id = parseInt(options.url.split('/').slice(-2, -1)[0])
  const index = mockShortLinks.findIndex(item => item.id === id)
  
  if (index !== -1) {
    mockShortLinks[index].status = 'ENABLED'
    mockShortLinks[index].updatedAt = new Date().toLocaleString()
    return {
      code: 200,
      data: mockShortLinks[index],
      message: 'success'
    }
  }
  
  return {
    code: 404,
    message: 'Not found'
  }
})

Mock.mock(/\/api\/shortlinks\/\d+\/disable/, 'patch', (options: any) => {
  const id = parseInt(options.url.split('/').slice(-2, -1)[0])
  const index = mockShortLinks.findIndex(item => item.id === id)
  
  if (index !== -1) {
    mockShortLinks[index].status = 'DISABLED'
    mockShortLinks[index].updatedAt = new Date().toLocaleString()
    return {
      code: 200,
      data: mockShortLinks[index],
      message: 'success'
    }
  }
  
  return {
    code: 404,
    message: 'Not found'
  }
})

Mock.mock('/api/shortlinks/generate', 'post', () => {
  return {
    code: 200,
    data: {
      shortCode: generateShortCode()
    },
    message: 'success'
  }
})

export default Mock