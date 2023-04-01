import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'

enum Api {
  listI18n = 'sys/i18n/list',
  getI18nById = 'sys/i18n/getById',
  i18nSaveUpdate = 'sys/i18n/saveUpdate',
  i18nDelete = 'sys/i18n/batchDeleteById',
}

export const listI18nApi = (params: Recordable) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.listI18n,
    data: params,
  })
}

export const getI18nByIdApi = (id: number) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.getI18nById,
    data: id,
  })
}

export const i18nSaveUpdateApi = (model: any) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.i18nSaveUpdate,
    data: model,
  })
}

export const i18nDeleteApi = (deleteData: any[]) => {
  return defHttp.post({
    service: ApiServiceEnum.SMART_SYSTEM,
    url: Api.i18nDelete,
    data: deleteData.map((item) => item.i18nId),
  })
}
