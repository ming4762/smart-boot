import { defHttp } from '/@/utils/http/axios'

enum Api {
  queryDbTable = 'db/connection/queryDbTable',
  getConfigById = 'db/code/main/getConfigById',
  saveConfig = 'db/code/main/save',
}

/**
 * 查询数据库信息
 * @param connectionId
 * @param tableName
 */
export const queryDbTableApi = (connectionId: number, tableName: number) =>
  defHttp.post({ url: Api.queryDbTable, data: { dbConnectionId: connectionId, tableName } })

export const getConfigByIdApi = (configId: number) => defHttp.post({ url: Api.getConfigById, data: configId })

export const saveConfigApi = (model: Recordable) => {
  return defHttp.post({
    url: Api.saveConfig,
    data: model,
  })
}
