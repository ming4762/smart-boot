import { defHttp } from '/@/utils/http/axios'

enum Api {
  listBySystem = 'db/code/main/listBySystem',
  delete = 'db/code/main/batchDeleteById',
}

export const listBySystemApi = (parameter) =>
  defHttp.post({ url: Api.listBySystem, data: parameter })

export const deleteApi = (data) =>
  defHttp.post({ url: Api.delete, data: data.map((item: any) => item.id) })
