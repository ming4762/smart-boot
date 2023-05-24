import { createRouter, createWebHashHistory } from 'vue-router'
import type {
  Router,
  RouteLocationNormalized,
  RouteRecordNormalized,
  RouteMeta,
  RouteRecordRaw,
} from 'vue-router'
import { isUrl } from '@/utils/is'
import { omit, cloneDeep } from 'lodash-es'
import { warn } from '@/utils/log'

/* Layout */
export const Layout = () => import('@/layout/Layout.vue')

export const getParentLayout = () => {
  return () =>
    new Promise((resolve) => {
      resolve({
        name: 'ParentLayout',
      })
    })
}

export const getRawRoute = (route: RouteLocationNormalized): RouteLocationNormalized => {
  if (!route) return route
  const { matched, ...opt } = route
  return {
    ...opt,
    matched: (matched
      ? matched.map((item) => ({
          meta: item.meta,
          name: item.name,
          path: item.path,
        }))
      : undefined) as RouteRecordNormalized[],
  }
}

// 前端控制路由生成
export const generateRoutesFn1 = (
  routes: AppRouteRecordRaw[],
  keys: string[],
  basePath = '/',
): AppRouteRecordRaw[] => {
  const res: AppRouteRecordRaw[] = []

  for (const route of routes) {
    const meta = route.meta as RouteMeta
    // skip some route
    if (meta.hidden && !meta.canTo) {
      continue
    }

    let data: Nullable<AppRouteRecordRaw> = null

    let onlyOneChild: Nullable<string> = null
    if (route.children && route.children.length === 1 && !meta.alwaysShow) {
      onlyOneChild = (
        isUrl(route.children[0].path)
          ? route.children[0].path
          : pathResolve(pathResolve(basePath, route.path), route.children[0].path)
      ) as string
    }

    // 开发者可以根据实际情况进行扩展
    for (const item of keys) {
      // 通过路径去匹配
      if (isUrl(item) && (onlyOneChild === item || route.path === item)) {
        data = Object.assign({}, route)
      } else {
        const routePath = onlyOneChild ?? pathResolve(basePath, route.path)
        if (routePath === item || meta.followRoute === item) {
          data = Object.assign({}, route)
        }
      }
    }

    // recursive child routes
    if (route.children && data) {
      data.children = generateRoutesFn1(route.children, keys, pathResolve(basePath, data.path))
    }
    if (data) {
      res.push(data as AppRouteRecordRaw)
    }
  }
  return res
}

/**
 * 获取系统所有组件列表
 */
const getComponents = (): Record<string, () => Promise<Recordable>> => {
  const result: Record<string, () => Promise<Recordable>> = {}

  const viewsRecord = import.meta.glob('../views/**/*.{vue,tsx}')
  Object.keys(viewsRecord).forEach((item) => {
    result[item] = viewsRecord[item]
  })

  const modeuleViewsRecord = import.meta.glob('../modules/**/*.{vue,tsx}')
  Object.keys(modeuleViewsRecord).forEach((item) => {
    result[item] = modeuleViewsRecord[item]
  })

  return result
}

const modules = getComponents()

const dynamicImport = (
  dynamicViewsModules: Record<string, () => Promise<Recordable>>,
  component: string,
) => {
  const keys = Object.keys(dynamicViewsModules)
  const matchKeys = keys.filter((key) => {
    if (component.startsWith('@')) {
      const keyPath = '@' + key.replace('..', '')
      return keyPath === component
    }
    const k = key.replace('../views', '')
    const startFlag = component.startsWith('/')
    const endFlag = component.endsWith('.vue') || component.endsWith('.tsx')
    const startIndex = startFlag ? 0 : 1
    const lastIndex = endFlag ? k.length : k.lastIndexOf('.')
    return k.substring(startIndex, lastIndex) === component
  })
  if (matchKeys.length === 1) {
    const matchKey = matchKeys[0]
    return dynamicViewsModules[matchKey]
  } else if (matchKeys.length > 1) {
    warn(
      'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
    )
    return
  } else {
    warn('找不到`' + component + '.vue` 或 `' + component + '.tsx`, 请自行创建!')
    return
  }
}

// 后端控制路由生成
export const generateRoutesFn2 = (routes: AppCustomRouteRecordRaw[]): AppRouteRecordRaw[] => {
  const res: AppRouteRecordRaw[] = []
  for (const route of routes) {
    const data: AppRouteRecordRaw = {
      path: route.path,
      name: route.name,
      redirect: route.redirect,
      meta: route.meta,
    }
    if (route.component) {
      const component = route.component as string
      if (component.includes('#')) {
        // 动态加载路由文件，可根据实际情况进行自定义逻辑
        data.component =
          component === '#' ? Layout : component.includes('##') ? getParentLayout() : null
      } else {
        data.component = dynamicImport(modules, component)
      }
    }
    // recursive child routes
    if (route.children) {
      data.children = generateRoutesFn2(route.children)
    }
    res.push(data as AppRouteRecordRaw)
  }
  return res
}

export const pathResolve = (parentPath: string, path: string) => {
  if (isUrl(path)) return path
  const childPath = path.startsWith('/') || !path ? path : `/${path}`
  return `${parentPath}${childPath}`.replace(/\/\//g, '/')
}

// 路由降级
export const flatMultiLevelRoutes = (routes: AppRouteRecordRaw[]) => {
  const modules: AppRouteRecordRaw[] = cloneDeep(routes)
  for (let index = 0; index < modules.length; index++) {
    const route = modules[index]
    if (!isMultipleRoute(route)) {
      continue
    }
    promoteRouteLevel(route)
  }
  return modules
}

// 层级是否大于2
const isMultipleRoute = (route: AppRouteRecordRaw) => {
  if (!route || !Reflect.has(route, 'children') || !route.children?.length) {
    return false
  }

  const children = route.children

  let flag = false
  for (let index = 0; index < children.length; index++) {
    const child = children[index]
    if (child.children?.length) {
      flag = true
      break
    }
  }
  return flag
}

// 生成二级路由
const promoteRouteLevel = (route: AppRouteRecordRaw) => {
  let router: Router | null = createRouter({
    routes: [route as RouteRecordRaw],
    history: createWebHashHistory(),
  })

  const routes = router.getRoutes()
  addToChildren(routes, route.children || [], route)
  router = null

  route.children = route.children?.map((item) => omit(item, 'children'))
}

// 添加所有子菜单
const addToChildren = (
  routes: RouteRecordNormalized[],
  children: AppRouteRecordRaw[],
  routeModule: AppRouteRecordRaw,
) => {
  for (let index = 0; index < children.length; index++) {
    const child = children[index]
    const route = routes.find((item) => item.name === child.name)
    if (!route) {
      continue
    }
    routeModule.children = routeModule.children || []
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteRecordRaw)
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule)
    }
  }
}
