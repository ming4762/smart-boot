import { defHttp } from '/@/utils/http/axios'

enum Api {
  getById = 'sys/dept/getById',
  saveUpdateBatch = 'sys/dept/saveUpdateBatch',
  delete = 'sys/dept/batchDeleteById',
}

export const getByIdApi = (params) => {
  return defHttp.post({
    url: Api.getById,
    data: params,
  })
}

export const saveUpdateBatchApi = (modelList: any[]) => {
  return defHttp.post({
    url: Api.saveUpdateBatch,
    data: modelList,
  })
}

export const deleteApi = (ids: number[]) => {
  return defHttp.post({
    url: Api.delete,
    data: ids,
  })
}
