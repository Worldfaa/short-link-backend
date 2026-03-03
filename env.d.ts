/// <reference types="vite/client" />

declare module 'mockjs' {
  interface Mockjs {
    mock: (rurl: string | RegExp, rtype?: string, template?: any) => any
    Random: any
  }
  const Mock: Mockjs
  export default Mock
}
