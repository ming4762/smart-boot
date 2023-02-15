import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = '/sys/fileStorage/list',
  getById = '/sys/fileStorage/getById',
  batchSaveUpdate = '/sys/fileStorage/saveUpdateBatch',
  delete = '/sys/fileStorage/batchDeleteById',
  setDefault = '/sys/fileStorage/setDefault',
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
