import { PageEnum } from '/@/enums/pageEnum'
import { useUserStore } from '/@/store/modules/user'
import type { Router } from 'vue-router'

export const createUserMenuLoadGuard = (router: Router) => {
  router.beforeEach(async function (to, _, next) {
    if (to.path === PageEnum.BASE_LOGIN) {
      next()
      return
    }
    const userStore = useUserStore()
    const routeInit = userStore.getRouteInit
    if (!routeInit) {
      await userStore.initRoute()
      const { path, params, query } = to
      next({
        path,
        params,
        query,
      })
      return
    }
    next()
  })
}
