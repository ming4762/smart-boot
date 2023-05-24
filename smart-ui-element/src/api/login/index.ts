import request from '@/config/axios/indexOkd'
import type { UserType } from './types'
import defHttp, { ApiServiceEnum } from '@/config/axios'
import { useLocaleStore } from '@/store/modules/locale'
import StringUtils from '@/utils/StringUtils'
import TreeUtils from '@/utils/TreeUtils'

enum Api {
  Login = '/auth/login',
  Logout = '/auth/logout',
  GetUserInfo = '/getUserInfo',
  GetPermCode = '/getPermCode',
  TestRetry = '/testRetry',
  changePassword = 'sys/auth/changePassword',
  GetMenuList = '/sys/user/listUserMenu',
}

interface RoleParams {
  roleName: string
}

interface LoginParams {
  username: string
  password: string
  codeKey?: string
  code?: string
}

export const loginApi = (data: LoginParams): Promise<IResponse<UserType>> => {
  return defHttp.postForm(
    {
      service: ApiServiceEnum.SMART_AUTH,
      url: Api.Login,
      params: data,
    },
    {
      errorMessageMode: 'modal',
    },
  )
}

export const loginOutApi = (): Promise<IResponse> => {
  return defHttp.post({ url: Api.Logout, service: ApiServiceEnum.SMART_AUTH })
}

export const getUserListApi = ({ params }: AxiosConfig) => {
  return request.get<{
    code: string
    data: {
      list: UserType[]
      total: number
    }
  }>({ url: '/user/list', params })
}

/**
 * 加载用户菜单
 */
export const listUserMenuApi = async () => {
  const localeStore = useLocaleStore()
  const locales = localeStore.getLocaleMap.map((item) => item.lang)
  const menuList: any[] = await defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.GetMenuList,
    data: locales,
  })
  const routeMenuList = menuList.map((item) => {
    const {
      url,
      functionName,
      locales,
      icon,
      functionId,
      parentId,
      component,
      componentName,
      redirect,
      isMenu,
      cached,
    } = item

    // 兼容icon
    let compatibleIcon = icon
    if (compatibleIcon && compatibleIcon.indexOf(':') === -1) {
      compatibleIcon = StringUtils.humpToLine(compatibleIcon)
      compatibleIcon = 'ant-design:' + compatibleIcon
    }
    const routeItem: any = {
      path: url,
      name: componentName || functionName,
      component,
      meta: {
        hideMenu: isMenu === false,
        title: functionName,
        locales,
        icon: compatibleIcon,
        key: functionId,
        parentKey: parentId,
        queryToProps: true,
      },
    }
    // LAYOUT特殊处理
    if (routeItem.component === 'LAYOUT') {
      routeItem.component = '#'
    }
    if (redirect) {
      routeItem.redirect = redirect
    }
    if (cached === false) {
      routeItem.meta.noCache = true
    }
    return routeItem
  })

  // 构建树
  return TreeUtils.convertList2Tree(
    routeMenuList,
    (data) => data.meta.key,
    (data) => data.meta.parentKey,
    0,
  )
}
