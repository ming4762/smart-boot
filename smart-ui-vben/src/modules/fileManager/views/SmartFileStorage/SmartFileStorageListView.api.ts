import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = '/smart/fileStorage/list',
  getById = '/smart/fileStorage/getById',
  batchSaveUpdate = '/smart/fileStorage/saveUpdateBatch',
  delete = '/smart/fileStorage/batchDeleteById',
  setDefault = '/smart/fileStorage/setDefault',
}

export const listApi = (params) => {
  return defHttp.post({
    url: Api.list,
    data: {
      ...params,
    },
  })
}

export const batchSaveUpdateApi = (modelList: any[]) => {
  return defHttp.post({
    url: Api.batchSaveUpdate,
    data: modelList,
  })
}

export const deleteApi = (removeRecords: Recordable[]) => {
  return defHttp.post({
    url: Api.delete,
    data: removeRecords.map((item) => item.id),
  })
}

export const getByIdApi = (model: Recordable) => {
  return defHttp.post({
    url: Api.getById,
    data: model.id,
  })
}

export const setDefaultApi = (id) => {
  return defHttp.post({
    url: Api.setDefault,
    data: {
      id,
    },
  })
}
