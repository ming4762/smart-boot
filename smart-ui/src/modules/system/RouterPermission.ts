import router from '@/router'
import NProgress from 'nprogress'

import { getToken, getMenuPermission } from '@/common/auth/AuthUtils'
import { LOGIN_PATH } from '@/modules/system/constants/SystemConstants'

NProgress.configure({ showSpinner: false })

const loginPath = LOGIN_PATH

const error404Path = '/error/404'

const error403Path = '/error/403'

/**
 * 白名单列表
 */
const whiteList = [
  loginPath,
  error404Path,
  error403Path
]

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (whiteList.includes(to.path)) {
    next()
    return
  }
  // 验证是否存在路由，不存在跳转到404
  if (to.matched.length === 0) {
    next({
      path: error404Path
    })
    return
  }
  const token = getToken()
  if (token === null || token === undefined) {
    const toPath = to.path
    next({
      path: `${loginPath}`,
      query: {
        redirect: toPath
      }
    })
    return
  }
  const redirectPath = (from.query && from.query.redirect) as string | null
  // 判断用户是否有权限访问路径
  const permission = to.meta && (to.meta.permission as string)
  let validate = true
  if (permission) {
    const userMenuPermissionList = getMenuPermission()
    if (!userMenuPermissionList.includes(permission)) {
      validate = false
    }
  }
  if (!validate) {
    // 无权限跳转到403
    next({
      path: error403Path
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
