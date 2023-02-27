import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = '/sys/parameter/list',
  getById = '/sys/parameter/getById',
  batchSaveUpdate = '/sys/parameter/saveUpdateBatch',
  delete = '/sys/parameter/batchDeleteById',
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

export const getByIdApi = (id) => {
  return defHttp.post({
    url: Api.getById,
    data: id,
  })
}
