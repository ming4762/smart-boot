import { ref } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 用户关联的应用名字
 */
export const useUserApplicationName = () => {
  const applicationNameList = ref<Array<string>>([])

  /**
   * 获取用户关联的应用名字列表
   */
  const loadUserApplicationName = async () => {
    try {
      applicationNameList.value = await ApiService.postAjax('monitor/manager/application/listUserApplicationName')
    } catch (e) {
      errorMessage(e)
    }
  }
  loadUserApplicationName()
  return {
    applicationNameList,
    loadUserApplicationName
  }
}
