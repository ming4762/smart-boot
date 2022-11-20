import type { RouteRecord } from 'vue-router'

import router from '@/router'
import { useSystemMenuStore } from '@/modules/system/store'

/**
 * 设置路由元数据
 */
const setRouteMeta = (routes: Array<RouteRecord>, menuList: Array<any>) => {
  const pathMetaMap = new Map()
  if (menuList.length === 0) {
    return false
  }
  menuList.forEach((menu) => {
    if (menu.path) {
      pathMetaMap.set(menu.path, menu.meta)
    }
  })
  routes.forEach((route) => {
    const path = route.path
    const menuMeta = pathMetaMap.get(path)
    if (menuMeta) {
      route.meta = {
        ...menuMeta,
        ...route.meta
      }
    }
  })
}

/**
 * 设置router meta
 */
const useSetRouteMeta = () => {
  const { userMenuList } = useSystemMenuStore()
  setRouteMeta(router.getRoutes(), userMenuList)
}

export default useSetRouteMeta
