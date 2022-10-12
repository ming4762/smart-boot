import { ref } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

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
      detailsData.value = await ApiService.postAjax('sys/log/getById', id)
    } catch (e) {
      errorMessage(e)
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
    handleCloseDetails
  }
}
