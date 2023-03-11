import { ref } from 'vue'

import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'

/**
 * 显示日志详情
 */
export const useShowDetails = () => {
  const detailsModalVisible = ref(false)
  // 加载状态
  const getDetailLoading = ref(false)
  const detailsData = ref<any>({})

  const handleShowDetails = async (id: number) => {
    detailsModalVisible.value = true
    getDetailLoading.value = true
    try {
      detailsData.value = await defHttp.post({
        service: ApiServiceEnum.SMART_SYSTEM,
        url: 'sys/log/getById',
        data: id,
      })
    } finally {
      getDetailLoading.value = false
    }
  }
  /**
   * 执行关闭操作
   */
  const handleCloseDetails = () => {
    detailsModalVisible.value = false
  }
  return {
    detailsModalVisible,
    handleShowDetails,
    getDetailLoading,
    detailsData,
    handleCloseDetails,
  }
}
