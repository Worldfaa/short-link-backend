import { request } from './http'
import type { GenerateShortCodeResponse, ApiResponse } from '../types'

export const shorturlApi = {
  generate: (data: { url: string; appId?: string }) => {
    return request.post<ApiResponse<GenerateShortCodeResponse>>(
      '/short-url/generate',
      data
    )
  },

  disable: (shortCode: string) => {
    return request.put<ApiResponse<void>>(`/short-url/disable/${shortCode}`)
  },

  enable: (shortCode: string) => {
    return request.put<ApiResponse<void>>(`/short-url/enable/${shortCode}`)
  },

  delete: (shortCode: string) => {
    return request.delete<ApiResponse<void>>(`/short-url/${shortCode}`)
  }
}

export default shorturlApi