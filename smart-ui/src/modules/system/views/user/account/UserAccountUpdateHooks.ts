import { ref } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage, successMessage } from '@/components/notice/SystemNotice'

/**
 * 显示账户hook
 */
export const useShowAccount = (t: Function) => {
  const modalVisible = ref(false)
  const userId = ref<number>()
  const dataLoading = ref(false)
  const userData = ref<any>({})
  const accountData = ref<any>({})
  const saveLoading = ref(false)
  /**
   * 显示账户信息
   * @param id 用户ID
   */
  const show = (id: number) => {
    userId.value = id
    modalVisible.value = true
    handleLoadUserAccount()
  }

  /**
   * 加载账户信息
   */
  const handleLoadUserAccount = async () => {
    dataLoading.value = true
    try {
      const result = await ApiService.postAjax('sys/user/getById', userId.value);
      if (result) {
        userData.value = result
        if (result.userAccount) {
          accountData.value = result.userAccount
        }
      } else {
        userData.value = {}
        accountData.value = {}
      }
    } catch (e) {
      errorMessage(e)
    } finally {
      dataLoading.value = false
    }
  }

  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    try {
      saveLoading.value = true
      await ApiService.postAjax('sys/user/saveAccountSetting', accountData.value)
      modalVisible.value = false
      successMessage(t('common.message.editSuccess'))
    } catch (e) {
      errorMessage(e)
    } finally {
      saveLoading.value = false
    }
  }

  return {
    modalVisible,
    show,
    userId,
    dataLoading,
    userData,
    accountData,
    handleSave,
    saveLoading
  }
}

