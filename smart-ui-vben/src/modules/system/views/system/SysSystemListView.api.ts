import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'sys/system/list',
  saveUpdate = 'sys/system/saveUpdate',
  delete = 'sys/system/batchDeleteById',
  getById = 'sys/system/getById',
}

export const listApi = (params) => {
  return defHttp.post({
    url: Api.list,
    data: {
      sortName: 'seq',
      ...params,
    },
  })
}

export const saveUpdateApi = (insertRecords, updateRecords) => {
  return defHttp.post({
    url: Api.saveUpdate,
    data: [...insertRecords, ...updateRecords][0],
  })
}

export const deleteApi = (removeRecords: Recordable[]) => {
  return defHttp.post({
    url: Api.delete,
    data: removeRecords.map((item) => item.id),
  })
}
export const getByIdApi = (id: number) => {
  return defHttp.post({
    url: Api.getById,
    data: id,
  })
}
