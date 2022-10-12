import type { App } from 'vue'

import { hasPermission } from '@/common/auth/AuthUtils'

import { subscribe } from '@/common/utils/PublishUtils'
import { EVENT_SYMBOLS } from '@/common/constants/CommonConstants'
import ApiService from '@/common/utils/ApiService'

import './RouterPermission'

/**
 * 初始化函数
 */
export default function init(app: App) {
  initDirectivePermission(app)
  handleInitEvent()
}

/**
 * 初始化权限指令
 * @param app
 */
const initDirectivePermission = (app: App) => {
  app.directive('permission', {
    beforeMount(el, binding) {
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

/**
 * 初始化事件
 */
const handleInitEvent = () => {
  // 订阅添加菜单事件
  subscribe(EVENT_SYMBOLS.SYSTEM_ADD_MENU, (key: string | symbol, menu: any) => {
    ApiService.postAjax('sys/menuAccessLog/save', {
      functionId: menu.id
    })
  })
}
