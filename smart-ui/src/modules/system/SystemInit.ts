import { App } from 'vue'

import { hasPermission } from '@/common/auth/AuthUtils'

import './RouterPermission'

/**
 * 初始化函数
 */
export default function init (app: App) {
  initDirectivePermission(app)
}

/**
 * 初始化权限指令
 * @param app
 */
const initDirectivePermission = (app: App) => {
  app.directive('permission', {
    beforeMount (el, binding) {
      const permission = binding.value
      const has = hasPermission(permission)
      if (!has) {
        if (el.type === 'button') {
          el.disabled = true
        }
        // TODO:其他情况未处理
      }
    }
  })
}
