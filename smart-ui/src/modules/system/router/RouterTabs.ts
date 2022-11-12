import router from '@/router'
import { useSystemMenuStore } from '@/modules/system/store'
import { hasTab } from '@/modules/app/utils/AppUtils'

const initRouterTabs = () => {
  router.afterEach((to) => {
    if (!hasTab()) {
      // 不显示tab栏，不处理
      return false
    }
    console.log(to)
  })
}

export default initRouterTabs
