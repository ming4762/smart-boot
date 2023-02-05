import { defHttp } from '/@/utils/http/axios'

enum Api {
  listOnlineUser = 'auth/listOnlineUser',
  offline = 'auth/offline',
}

export const listOnlineUserApi = (params) =>
  defHttp.post({
    url: Api.listOnlineUser,
    data: params,
  })

export const offlineApi = (username, token) => {
  return defHttp.post({
    url: Api.offline,
    data: { username, token },
  })
}
