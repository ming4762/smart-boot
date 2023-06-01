import { computed } from 'vue'

import { useAppStore } from '@/store/modules/app'
import { usePermissionStore } from '@/store/modules/permission'
import { useUserStore } from '@/store/modules/user'

import router from '@/router'
// import { RootRoute } from '@/router/routes';

import { RoleEnum } from '@/enums/roleEnum'

import { intersection } from 'lodash-es'
import { isArray } from '@/utils/is'

// User permissions related operations
export function usePermission() {
  const userStore = useUserStore()
  const appStore = useAppStore()
  const permissionStore = usePermissionStore()
  const getNoPermissionMode = computed(() => appStore.getProjectConfig.noPermissionMode)

  /**
   * Determine whether there is permission
   */
  function hasPermission(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    if (isSuperAdmin()) {
      return true
    }
    // Visible by default
    if (!value) {
      return def
    }
    const allCodeList = permissionStore.getPermCodeList as string[]
    if (!isArray(value)) {
      return allCodeList.includes(value)
    }
    return (intersection(value, allCodeList) as string[]).length > 0
  }

  /**
   * 是否是超级管理员
   */
  const isSuperAdmin = (): boolean => {
    const userStore = useUserStore()
    return userStore.getRoleList.includes('SUPERADMIN')
  }

  return {
    hasPermission,
    getNoPermissionMode,
    isSuperAdmin,
  }
}
