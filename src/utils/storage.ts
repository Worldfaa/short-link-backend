import type { ShortLink } from '../types'

const STORAGE_KEY = 'short_links'

export const storageService = {
  getShortLinks(): ShortLink[] {
    const data = localStorage.getItem(STORAGE_KEY)
    if (!data) return []
    return JSON.parse(data)
  },

  addShortLink(shortLink: Omit<ShortLink, 'id' | 'createdAt' | 'updatedAt'>): ShortLink {
    const shortLinks = this.getShortLinks()
    // 检查是否已经存在相同短码的记录
    const existingIndex = shortLinks.findIndex(link => link.shortCode === shortLink.shortCode)
    
    if (existingIndex !== -1) {
      // 如果存在相同短码的记录，更新它
      shortLinks[existingIndex] = {
        ...shortLinks[existingIndex],
        ...shortLink,
        updatedAt: new Date().toLocaleString('zh-CN', { hour12: false })
      }
      localStorage.setItem(STORAGE_KEY, JSON.stringify(shortLinks))
      return shortLinks[existingIndex]
    } else {
      // 如果不存在相同短码的记录，添加新的记录
      const newShortLink: ShortLink = {
        ...shortLink,
        id: Date.now(),
        createdAt: new Date().toLocaleString('zh-CN', { hour12: false }),
        updatedAt: new Date().toLocaleString('zh-CN', { hour12: false })
      }
      shortLinks.unshift(newShortLink)
      localStorage.setItem(STORAGE_KEY, JSON.stringify(shortLinks))
      return newShortLink
    }
  },

  updateShortLink(shortCode: string, updates: Partial<ShortLink>): void {
    const shortLinks = this.getShortLinks()
    const index = shortLinks.findIndex(link => link.shortCode === shortCode)
    if (index !== -1) {
      shortLinks[index] = { ...shortLinks[index], ...updates, updatedAt: new Date().toLocaleString('zh-CN', { hour12: false }) }
      localStorage.setItem(STORAGE_KEY, JSON.stringify(shortLinks))
    }
  },

  deleteShortLink(shortCode: string): void {
    const shortLinks = this.getShortLinks().filter(link => link.shortCode !== shortCode)
    localStorage.setItem(STORAGE_KEY, JSON.stringify(shortLinks))
  },

  clearAll(): void {
    localStorage.removeItem(STORAGE_KEY)
  }
}