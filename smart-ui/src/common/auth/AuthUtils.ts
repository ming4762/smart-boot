import StoreUtil from '@/common/utils/StoreUtil'

import ApiService, { API_SERVICE } from '@/common/utils/ApiService'
import sha256 from 'crypto-js/sha256'

const USER_KEY = 'smart_auth_user'
const PERMISSION_KEY = 'smart_auth_permission'
const MENU_PERMISSION_KEY = 'smart_auth_menu_permission'
const ROLE_KEY = 'smart_auth_role'
const TOKEN_KEY = 'smart_authorization'
const REQUEST_TOKEN_KEY = 'Authorization'

// API_SERVICE 添加token
API_SERVICE.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    if (!config.headers) {
      config.headers = {}
    }
    config.headers[REQUEST_TOKEN_KEY] = token
  }
  return config
})

/**
 * 保存token信息
 * @param token
 */
export const saveToken = (token: string) => {
  StoreUtil.setStore(TOKEN_KEY, token, StoreUtil.SESSION_TYPE)
}

/**
 * 获取token
 */
export const getToken = (): string | null => {
  return StoreUtil.getStore(TOKEN_KEY)
}

export const saveUser = (user: any): void => {
  StoreUtil.setStore(USER_KEY, user, StoreUtil.SESSION_TYPE)
}

export const getCurrentUser = (): any | null => {
  return StoreUtil.getStore(USER_KEY)
}

/**
 * 获取当前用户ID
 */
export const getCurrentUserId = (): number | null => {
  const user = getCurrentUser()
  return (user && user.userId) || null
}

export const saveUserPermission = (permissions: Array<string>): void => {
  StoreUtil.setStore(PERMISSION_KEY, permissions, StoreUtil.SESSION_TYPE)
}

/**
 * 保存菜单权限
 * @param permissions 菜单权限
 */
export const saveMenuPermission = (permissions: Array<string>) => {
  StoreUtil.setStore(MENU_PERMISSION_KEY, permissions, StoreUtil.SESSION_TYPE)
}

/**
 * 获取菜单权限
 */
export const getMenuPermission = (): Array<string> => {
  return StoreUtil.getStore(MENU_PERMISSION_KEY)
}

export const getUserPermission = (): Array<string> => {
  return StoreUtil.getStore(PERMISSION_KEY)
}

export const saveUserRole = (roleList: Array<string>): void => {
  StoreUtil.setStore(ROLE_KEY, roleList, StoreUtil.SESSION_TYPE)
}

export const getUserRole = (): Array<string> => {
  return StoreUtil.getStore(ROLE_KEY)
}

/**
 * 判断当前用户是否是超级管理员
 */
export const isSuperAdmin = () => {
  const roles = getUserRole()
  return roles !== null && roles.includes('SUPERADMIN')
}

/**
 * 判断用户是否拥有权限
 * @param permission
 */
export const hasPermission = (permission: string | null | undefined): boolean => {
  let hasPermission = true
  if (isSuperAdmin()) {
    return true
  }
  if (permission) {
    const permissions = getUserPermission()
    if (!permissions || permissions.length === 0 || !permissions.includes(permission)) {
      hasPermission = false
    }
  }
  return hasPermission
}

/**
 * 申请临时 token
 * @param resource 申请资源
 * @param once 是否只使用一次
 */
export const applyTempToken = async (resource: string, once = true): Promise<string> => {
  return await ApiService.postAjax('auth/tempToken/apply', { resource, once })
}

/**
 * 创建加密密码
 * @param username 用户名
 * @param password 密码明文
 */
export const createPassword = (username: string, password: string) => {
  return sha256(sha256(`${username}${password}888888$#@`)).toString()
}
