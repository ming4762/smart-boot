import { ref } from 'vue'

import { loadActuator } from './ClientApiUtils'

/**
 * 加载数据库连接名称列表
 * @param clientId 客户端ID
 */
export const useLoadDbNameList = (clientId: string) => {
  const dbNameList = ref([])
  const loadDbNameList = async () => {
    dbNameList.value = await loadActuator(clientId, 'druidDatasource/names')
  }
  loadDbNameList()
  return {
    dbNameList
  }
}
