import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'smart/license/listBySystem',
  getById = 'smart/license/getById',
  saveUpdateBatch = 'smart/license/saveUpdateBatch',
  delete = 'smart/license/batchDeleteById',
  generator = 'smart/license/generator',
}

export const listApi = (params) => {
  return defHttp.post({
    url: Api.list,
    data: params,
  })
}

export const getByIdApi = (data) => {
  return defHttp.post({
    url: Api.getById,
    data: data.id,
  })
}

export const saveUpdateBatchApi = (dataList: any[]) => {
  return defHttp.post({
    url: Api.saveUpdateBatch,
    data: dataList,
  })
}

export const deleteApi = (deleteDataList: any[]) => {
  return defHttp.post({
    url: Api.delete,
    data: deleteDataList.map((item) => item.id),
  })
}

export const generatorApi = (id: number) => {
  return defHttp.post({
    url: Api.generator,
    data: id,
  })
}