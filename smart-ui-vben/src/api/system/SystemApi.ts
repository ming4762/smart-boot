import { defHttp } from '/@/utils/http/axios'

enum Api {
  listUser = 'sys/user/list',
  listSystem = 'sys/system/list',
  listSystemFilterByUser = 'sys/system/listAuthUser',
}

/**
 * 查询用户列表
 * @param params 参数
 * @param useYn
 */
export const listUserApi = (params: Recordable = {}, useYn = true) => {
  let parameter = params.parameter
  if (useYn) {
    parameter = {
      ...parameter,
      'useYn@=': true,
    }
  }
  return defHttp.post({
    url: Api.listUser,
    data: {
      ...params,
      parameter,
    },
  })
}

export const listSystemApi = (params, filterByUser = false) => {
  return defHttp.post({
    url: filterByUser ? Api.listSystemFilterByUser : Api.listSystem,
    data: params,
  })
}
