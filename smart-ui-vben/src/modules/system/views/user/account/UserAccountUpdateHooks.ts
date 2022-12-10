import { ref } from 'vue'

import { message } from 'ant-design-vue'

import { globalLoading } from '/@/modules/app/utils/AppUtils'
import { defHttp } from '/@/utils/http/axios'

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
    userData.value = {}
    accountData.value = {}
    handleLoadUserAccount()
  }

  /**
   * 加载账户信息
   */
  const handleLoadUserAccount = async () => {
    globalLoading(true)
    try {
      const result = await defHttp.post({
        url: '/sys/user/getById',
        data: userId.value,
      })
      if (result) {
        userData.value = result
        if (result.userAccount) {
          accountData.value = result.userAccount
          modalVisible.value = true
        } else {
          message.error(t('system.views.user.message.noAccount'))
        }
      } else {
        userData.value = {}
        accountData.value = {}
      }
    } finally {
      globalLoading(false)
    }
  }

  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    try {
      saveLoading.value = true
      await defHttp.post({
        url: '/sys/user/saveAccountSetting',
        data: accountData.value,
      })
      modalVisible.value = false
      message.success(t('common.message.editSuccess'))
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
    saveLoading,
  }
}
