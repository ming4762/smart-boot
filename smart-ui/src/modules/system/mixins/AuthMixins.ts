import { defineComponent } from 'vue'

import { hasPermission } from '@/common/auth/AuthUtils'

export default defineComponent({
  methods: {
    /**
     * 判断是否拥有权限
     * @param permission
     */
    hasPermission(permission: string): boolean {
      return hasPermission(permission)
    }
  }
})
