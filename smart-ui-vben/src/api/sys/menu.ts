import { defHttp } from '/@/utils/http/axios'
import { getMenuListResultModel, RouteItem } from './model/menuModel'
import { useLocaleStore } from '/@/store/modules/locale'
import TreeUtils from '/@/utils/TreeUtils'

enum Api {
  GetMenuList = '/sys/user/listUserMenu',
}

const humpToLine = (camelCaseName: string): string => {
  camelCaseName = camelCaseName.replace(camelCaseName[0], camelCaseName[0].toLowerCase())
  return camelCaseName.replace(/([A-Z])/g, function (match) {
    return '-' + match.toLowerCase()
  })
}

/**
 * @description: Get user menu based on id
 */
export const getMenuList = async () => {
  const locales = useLocaleStore().localInfo.availableLocales
  const menuList: Array<any> = await defHttp.post({
    url: Api.GetMenuList,
    data: locales,
  })
  const routeMenuList: getMenuListResultModel = menuList.map((item) => {
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
    } = item
    // 兼容icon
    let compatibleIcon = icon
    if (compatibleIcon && compatibleIcon.indexOf(':') === -1) {
      compatibleIcon = humpToLine(compatibleIcon)
      compatibleIcon = 'ant-design:' + compatibleIcon
    }
    const routeItem: RouteItem = {
      path: url,
      name: componentName || functionName,
      component,
      meta: {
        title: functionName,
        locales,
        icon: compatibleIcon,
        key: functionId,
        parentKey: parentId,
      },
    }
    if (redirect) {
      routeItem.redirect = redirect
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
