import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'
import {
  ChangePasswordParams,
  LoginParams,
  LoginResultModel,
  MobileLoginParams,
} from './model/userModel'

import { ErrorMessageMode } from '/#/axios'

enum Api {
  Login = '/auth/login',
  Logout = '/auth/logout',
  GetUserInfo = '/getUserInfo',
  GetPermCode = '/getPermCode',
  TestRetry = '/testRetry',
  changePassword = 'sys/auth/changePassword',

  smsSendCode = '/auth/sms/createCode',

  mobileLogin = '/auth/sms/login',
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

/**
 * 修改密码
 * @param params 参数
 */
export const changePasswordApi = (params: ChangePasswordParams) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.changePassword,
    data: params,
  })
}

/**
 * 发送短信验证码API
 * @param phone 电话号码
 */
export const smsSendCodeApi = (phone: string) => {
  return defHttp.postForm({
    service: ApiServiceEnum.SMART_AUTH,
    url: Api.smsSendCode,
    data: {
      phone,
    },
  })
}

export const mobileLoginApi = (params: MobileLoginParams, mode: ErrorMessageMode = 'modal') => {
  return defHttp.postForm(
    {
      service: ApiServiceEnum.SMART_AUTH,
      url: Api.mobileLogin,
      data: params,
    },
    {
      errorMessageMode: mode,
    },
  )
}
