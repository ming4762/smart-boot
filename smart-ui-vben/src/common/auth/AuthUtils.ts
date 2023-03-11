import { useUserStore } from '/@/store/modules/user'
import { usePermission } from '/@/hooks/web/usePermission'
import { UserInfo } from '/#/store'
import type { SmartAuth } from '/#/utils'
import { isString } from '/@/utils/is'
import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'

/**
 * 是否是超级管理员
 */
export const isSuperAdmin = (): boolean => {
  const userStore = useUserStore()
  return userStore.getRoleList.includes('SUPERADMIN')
}

/**
 * 是否拥有权限
 * @param auth
 */
export const hasPermission = (auth?: SmartAuth | string): boolean => {
  if (!auth) {
    return true
  }
  const { hasPermission } = usePermission()
  if (isString(auth)) {
    return hasPermission(auth)
  }
  const { permission, multipleMode } = auth
  if (isString(permission)) {
    return hasPermission(permission)
  }
  if (multipleMode === 'or') {
    return permission.some((item) => hasPermission(item))
  } else {
    return permission.every((item) => hasPermission(item))
  }
}

/**
 * 申请临时 token
 * @param resource 申请资源
 * @param once 是否只使用一次
 */
export const applyTempToken = async (resource: string, once = true): Promise<string> => {
  return await defHttp.post({
    service: ApiServiceEnum.SMART_AUTH,
    url: 'auth/tempToken/apply',
    data: { resource, once },
  })
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
