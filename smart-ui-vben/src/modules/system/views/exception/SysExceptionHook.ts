import ApiService from '/@/common/utils/ApiService'
import { ref } from 'vue'

const searchSymbol: { [index: string]: string } = {
  exceptionMessage: 'like',
  requestIp: 'like',
  serverIp: 'like',
  userFeedback: '=',
  resolved: '=',
}

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
  const searchWithSymbol: any = {}
  Object.keys(searchParameter).forEach((key) => {
    let value = searchParameter[key]
    if ((key === 'userFeedback' || key === 'resolved') && value !== '') {
      value = value === 'true'
    }
    if (searchSymbol[key]) {
      searchWithSymbol[`${key}@${searchSymbol[key]}`] = value
    } else {
      searchWithSymbol[key] = value
    }
  })
  return await ApiService.postAjax('sys/exception/list', {
    ...params,
    parameter: searchWithSymbol,
  })
}

/**
 * 显示堆栈信息
 */
export const useShowStackTrace = () => {
  // 堆栈信息
  const stackTrace = ref<string>()
  const stackTraceModalVisible = ref(false)
  const stackTraceLoading = ref(false)
  /**
   * 显示堆栈信息
   * @param id
   */
  const handleShowStackTrace = async (id: number) => {
    // errorMessage({
    // 	exceptionNo: new Date().getTime(),
    // 	code: 500
    // })
    stackTraceLoading.value = true
    stackTrace.value = ''
    try {
      stackTraceModalVisible.value = true
      const result = await ApiService.postAjax('sys/exception/getById', id)
      stackTrace.value = result.stackTrace
    } finally {
      stackTraceLoading.value = false
    }
  }
  return {
    stackTrace,
    handleShowStackTrace,
    stackTraceModalVisible,
    stackTraceLoading,
  }
}
