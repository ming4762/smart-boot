import router from '@/router'
import { useSystemMenuStore } from '@/modules/system/store'

const initRouterTabs = () => {
  router.afterEach((to) => {
    const { tab } = to.meta
    if (!tab) {
      return false
    }
    const menuId = (to.query && to.query.menuId) || to.meta.menuId
    const menuStore = useSystemMenuStore()
    menuStore.addMenu({
      ...to.meta,
      menuId
    })
  })
}

export default initRouterTabs
