import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'sys/exception/list',
  getById = 'sys/exception/getById',
}

export const listApi = (params) => {
  return defHttp.post({
    url: Api.list,
    data: params,
  })
}

export const getById = (id) => {
  return defHttp.post({
    url: Api.getById,
    data: id,
  })
}
