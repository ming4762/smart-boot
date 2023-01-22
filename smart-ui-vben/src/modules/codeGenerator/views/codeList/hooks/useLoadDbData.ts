import { computed, ref } from 'vue'
import ApiService from '/@/common/utils/ApiService'
import { isNotBlank } from '/@/utils/is'

export const useLoadDbData = ({ validateAddEdit }) => {
  // 数据库数据加载状态
  const dbDataLoading = ref(false)
  const dbData = ref<Recordable>({})

  /**
   * 加载数据库数据
   */
  const loadDbData = async () => {
    const { connectionId, tableName } = await validateAddEdit()
    if (isNotBlank(connectionId) && isNotBlank(tableName)) {
      dbDataLoading.value = true
      try {
        dbData.value = await ApiService.postAjax('db/connection/queryDbTable', {
          dbConnectionId: connectionId,
          tableName: tableName,
        })
      } finally {
        dbDataLoading.value = false
      }
    }
  }
  /**
   * 表格table计算属性
   */
  const computedTableData = computed(() => {
    if (!dbData.value.tableName) {
      return []
    }
    const primaryKeyList = dbData.value.primaryKeyList || []
    const baseColumnList = dbData.value.baseColumnList || []
    return [...primaryKeyList, ...baseColumnList]
  })
  /**
   * 同步表
   */
  const handleSyncTableData = () => {
    loadDbData()
  }
  return {
    dbData,
    dbDataLoading,
    computedTableData,
    handleSyncTableData,
    loadDbData,
  }
}