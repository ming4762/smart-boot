import { ref, onMounted } from 'vue'
import type { Ref } from 'vue'

import ApiService from '/@/common/utils/ApiService'
import { errorMessage } from '/@/common/utils/SystemNotice'

/**
 * 加载字典项hook
 * @param dictCode
 */
export const useLoadDictItem = (dictCode: Ref<string>) => {
  const dictData = ref<Array<any>>([])

  /**
   * 加载数据函数
   */
  const loadDictData = async () => {
    if (!dictCode.value || dictCode.value === '') {
      dictData.value = []
    } else {
      try {
        dictData.value = await ApiService.postAjax('sys/dictItem/list', {
          sortName: 'seq',
          sortOrder: 'asc',
          parameter: {
            'dictCode@=': dictCode.value,
          },
        })
      } catch (e) {
        errorMessage(e)
      }
    }
  }

  onMounted(loadDictData)

  return {
    dictData,
    loadDictData,
  }
}
