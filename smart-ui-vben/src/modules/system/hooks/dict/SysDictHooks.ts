import { computed, ref, unref, watch } from 'vue'
import type { Ref } from 'vue'

import ApiService from '/@/common/utils/ApiService'
import { errorMessage } from '/@/common/utils/SystemNotice'

/**
 * 加载字典项hook
 * @param dictCodeRef
 */
export const useLoadDictItem = (dictCodeRef: Ref<string> | string) => {
  const dictData = ref<Array<any>>([])

  const getDictItemMap = computed(() => {
    const result: { [index: string]: string } = {}
    unref(dictData).forEach((item) => {
      result[item.dictItemCode] = item.dictItemName
    })
    return result
  })

  /**
   * 加载数据函数
   */
  const loadDictData = async (dictCode?: string) => {
    if (!dictCode || dictCode === '') {
      dictData.value = []
    } else {
      try {
        dictData.value = await ApiService.postAjax('sys/dict/listItemByCode', dictCode)
      } catch (e) {
        errorMessage(e)
      }
    }
  }

  watch(
    () => unref(dictCodeRef),
    (value) => {
      loadDictData(value)
    },
    {
      immediate: true,
    },
  )

  return {
    dictData,
    loadDictData,
    getDictItemMap,
  }
}
