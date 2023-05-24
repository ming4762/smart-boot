import sha256 from 'crypto-js/sha256'
import { useUserStore } from '@/store/modules/user'

export function getToken() {
  const userStore = useUserStore()
  return userStore.getToken
}

/**
 * 创建密码
 * @param username
 * @param password
 */
export const createPassword = (username: string, password: string) => {
  return sha256(sha256(`${username}${password}888888$#@`)).toString()
}
