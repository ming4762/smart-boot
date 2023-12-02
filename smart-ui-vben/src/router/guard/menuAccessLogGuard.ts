import type { Router } from 'vue-router'
import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'

export const createMenuAccessLogGuard = (router: Router) => {
  router.afterEach((to, from) => {
    if (to.path !== from.path && to.meta.key) {
      defHttp.post({
        service: ApiServiceEnum.SMART_SYSTEM,
        url: 'sys/menuAccessLog/save',
        data: {
          functionId: to.meta.key,
        },
      })
    }
  })
}
