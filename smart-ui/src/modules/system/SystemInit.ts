import type { App } from 'vue'

import { hasPermission } from '@/common/auth/AuthUtils'

import { subscribe } from '@/common/utils/PublishUtils'
import { EVENT_SYMBOLS } from '@/common/constants/CommonConstants'
import ApiService from '@/common/utils/ApiService'
import { useAppSettingStore } from '@/modules/app/store'
import { NoPermissionModeEnum } from '@/common/enums'
import initRouterTabs from './router/RouterTabs'
import useSetRouteMeta from '@/modules/system/router/RouteMetaSet'

import './RouterPermission'

/**
 * 初始化函数
 */
export default function init(app: App) {
  initDirectivePermission(app)
  handleInitEvent()
  initRouterTabs()
  useSetRouteMeta()
}

/**
 * 初始化权限指令
 * @param app
 */
const initDirectivePermission = (app: App) => {
  const appSettingStore = useAppSettingStore()
  app.directive('permission', {
    beforeMount(el, binding) {
      const permission = binding.value
      const has = hasPermission(permission)
      if (!has) {
        const noPermissionMode = appSettingStore.noPermissionMode
        if (el.type === 'button') {
          if (noPermissionMode === NoPermissionModeEnum.disabled) {
            el.disabled = true
          } else if (noPermissionMode === NoPermissionModeEnum.hide) {
            el.style.display = 'none'
          }
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
      functionId: menu.menuId
    })
  })
}
