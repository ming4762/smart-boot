import { defHttp } from '/@/utils/http/axios'

enum Api {
  list = 'db/code/main/list',
  delete = 'db/code/main/batchDeleteById',
}

export const listApi = (parameter) => defHttp.post({ url: Api.list, data: parameter })

export const deleteApi = (data) =>
  defHttp.post({ url: Api.delete, data: data.map((item: any) => item.id) })
