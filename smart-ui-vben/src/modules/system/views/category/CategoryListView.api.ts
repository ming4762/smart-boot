import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = '/sys/category/list',
  saveUpdate = '/sys/category/saveUpdate',
  batchDeleteById = '/sys/category/batchDeleteById',
  getById = '/sys/category/getById',
}

export const listApi = (params: Recordable = {}, parentId = 0) => {
  if (params) {
    params = {
      ...params,
      parameter: {
        ...params.parameter,
        'parentId@=': parentId,
      },
    }
  }
  return defHttp.post({
    url: Api.list,
    data: {
      sortName: 'seq',
      ...params,
    },
  })
}

export const saveUpdateApi = (data) => {
  return defHttp.post({
    url: Api.saveUpdate,
    data,
  })
}

export const getByIdApi = async (id: number) => {
  const result = await defHttp.post({
    url: Api.getById,
    data: id,
  })
  result.parentName = result.parent?.categoryName || '根目录'
  return result
}

export const deleteApi = (idList: number[]) => {
  return defHttp.post({
    url: Api.batchDeleteById,
    data: idList,
  })
}
