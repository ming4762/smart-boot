import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'
import { LoginParams, LoginResultModel } from './model/userModel'

import { ErrorMessageMode } from '/#/axios'

enum Api {
  Login = '/auth/login',
  Logout = '/auth/logout',
  GetUserInfo = '/getUserInfo',
  GetPermCode = '/getPermCode',
  TestRetry = '/testRetry',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  return defHttp.postForm<LoginResultModel>(
    {
      service: ApiServiceEnum.SMART_AUTH,
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
    },
  )
}

/**
 * @description: getUserInfo
 */
// export function getUserInfo() {
//   return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' })
// }

export function getPermCode() {
  return defHttp.get<string[]>({ url: Api.GetPermCode, service: ApiServiceEnum.SMART_AUTH })
}

export function doLogout() {
  return defHttp.post({ url: Api.Logout, service: ApiServiceEnum.SMART_AUTH })
}

export function testRetry() {
  return defHttp.get(
    { url: Api.TestRetry, service: ApiServiceEnum.SMART_AUTH },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  )
}
