import router from '@/router'
import store from '@/store'

import NProgress from 'nprogress'

import { getToken } from '@/common/auth/AuthUtils'


NProgress.configure({ showSpinner: false })

const loginPath = '/user/login'

const error404Path = '/error/404'

/**
 * 白名单列表
 */
const whiteList = [
  loginPath,
  error404Path
]

router.beforeEach((to, from, next) => {

  NProgress.start()
  const token = getToken()

  if (whiteList.includes(to.path)) {
    next()
    return
  }
  if (token === null || token === undefined) {
    const toPath = to.path
    next({
      path: `${loginPath}`,
      query:{
        redirect: toPath
      }
    })
    return
  }
  const redirectPath = (from.query && from.query.redirect) as string | null
  const path = redirectPath || to.path
  // 判断用户菜单列表是否包含Path
  const userMenuList: Array<any> = [{
    path: '/main'
  }].concat(store.getters['app/userMenuList'] || [])
  const validate = userMenuList.some(menu => menu.path === path)
  if (!validate) {
    next({
      path: error404Path
    })
  } else {
    if (redirectPath && from.path !== redirectPath) {
      if (to.path === redirectPath) {
        next()
      } else {
        next({
          path: redirectPath
        })
      }
    } else {
      next()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
