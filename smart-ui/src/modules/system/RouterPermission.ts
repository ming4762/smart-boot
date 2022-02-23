import router from '@/router'
import store from '@/store'

import NProgress from 'nprogress'

import { getToken } from '@/common/auth/AuthUtils'


NProgress.configure({ showSpinner: false })

const loginPath = '/user/login'

const error404Path = '/error/404'

const error403Path = '/error/403'

/**
 * 前台映射的权限
 * 例如访问/codeCreateView，需要查看用户是否拥有/code/codeList权限
 */
const permissionMappingPaths: any = {
  '/codeCreateView': '/code/codeList',
  // 客户端监控
  '/monitor/client/detail': '/monitor/manager/client',
  '/monitor/client/environment': '/monitor/manager/client',
  '/monitor/client/beans': '/monitor/manager/client',
  '/monitor/client/loggerConfig': '/monitor/manager/client',
  '/monitor/client/httpMapping': '/monitor/manager/client',
  '/monitor/client/metrics': '/monitor/manager/client',
  '/monitor/client/druid/dbConnection': '/monitor/manager/client',
  '/monitor/client/druid/dbSql': '/monitor/manager/client'
}

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
  const token = getToken()
  if (whiteList.includes(to.path)) {
    next()
    return
  }
  // 验证是否存在路由，不存在跳转到404
  if (to.matched.length === 0) {
    next({
      path: error404Path
    })
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
  const permissionPath = permissionMappingPaths[path] ? permissionMappingPaths[path] : path
  const validate = userMenuList.some(menu => menu.path === permissionPath)
  if (!validate) {
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
