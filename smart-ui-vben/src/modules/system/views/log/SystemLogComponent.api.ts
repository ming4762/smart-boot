import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'sys/log/list',
  getById = 'sys/log/getById',
}

export const listApi = (parameter) => {
  return defHttp.post({
    url: Api.list,
    data: {
      sortName: 'createTime',
      sortOrder: 'desc',
      ...parameter,
    },
  })
}

export const getByIdApi = (id: number) => {
  return defHttp.post({
    url: Api.getById,
    data: id,
  })
}
