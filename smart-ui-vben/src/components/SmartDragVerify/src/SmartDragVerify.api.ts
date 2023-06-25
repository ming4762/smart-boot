import type { VerifyType } from '/@/components/SmartDragVerify'
import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'

enum Api {
  loadCaptcha = 'public/auth/generateCaptcha',
  validateCaptcha = 'public/auth/validateCaptcha',
}

export const loadCaptchaApi = (type: VerifyType) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_AUTH,
    url: Api.loadCaptcha,
    data: {
      type,
    },
  })
}

export const validateCaptchaApi = (parameter: any) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_AUTH,
    url: Api.validateCaptcha,
    data: parameter,
  })
}
