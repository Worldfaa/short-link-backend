export interface ShortLink {
  id: number
  name: string
  originalUrl: string
  shortCode: string
  status: 'ENABLED' | 'DISABLED'
  createdAt: string
  updatedAt: string
}

export interface GenerateShortCodeResponse {
  shortCode: string
  shortUrl?: string
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}