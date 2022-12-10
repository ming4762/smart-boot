import { useUserStore } from '/@/store/modules/user'
import { usePermission } from '/@/hooks/web/usePermission'

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
