import { useUserStore } from '/@/store/modules/user'
import { usePermission } from '/@/hooks/web/usePermission'
import ApiService from '/@/common/utils/ApiService'
import { UserInfo } from '/#/store'

/**
 * 是否是超级管理员
 */
export const isSuperAdmin = (): boolean => {
  const userStore = useUserStore()
  return userStore.getRoleList.includes('SUPERADMIN')
}

/**
 * 是否拥有权限
 * @param permission
 */
export const hasPermission = (permission: string | null | undefined | string[]): boolean => {
  if (permission) {
    const { hasPermission } = usePermission()
    return hasPermission(permission)
  }
  return true
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
 * 获取用户信息
 */
export const getUserInfo = (): UserInfo => {
  const userStore = useUserStore()
  return userStore.getUserInfo
}

/**
 * 获取当前用户ID
 */
export const getCurrentUserId = (): string | number => {
  return getUserInfo().userId
}

export const getUserRole = (): string[] => {
  const userStore = useUserStore()
  return userStore.getRoleList
}
