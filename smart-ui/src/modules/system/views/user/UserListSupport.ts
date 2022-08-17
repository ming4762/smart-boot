import { onMounted, reactive, Ref, ref, createVNode } from 'vue'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 用户操作
 */
export const userOperationHoops = (tableRef: Ref, t: Function, loadData: Function) => {
  // 获取选中的用户IDList
  const getSelectUserList = (): Array<any> => tableRef.value.getCheckboxRecords()
  const validateMessage = (userIdList: Array<number>) => {
    if (userIdList.length === 0) {
      message.warn(t('system.views.user.validate.selectUser'))
      return false
    }
    return true
  }
  /**
   * 启停用户
   * @param useYn
   */
  const handleSetUseYn = (useYn: boolean) => {
    const userList = getSelectUserList()
    // 验证用户
    const result = validateMessage(userList)
    if (!result) {
      return false
    }
    // 验证是否包含系统用户
    const sysUserValidate = userList.some((item: any) => item.userType === '10')
    if (sysUserValidate) {
      message.error(t('system.views.user.validate.sysUserNoDelete'))
      return false
    }
    Modal.confirm({
      title: t('system.views.user.validate.setUserUseYn', { useYn: useYn ? t('common.button.use') : t('common.button.noUse') }),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        try {
          await ApiService.postAjax('system/user/setUseYn', {
            idList: userList.map(item => item.userId),
            useYn
          })
          // 重新加载数据
          loadData()
        } catch (e) {
          errorMessage(e)
        }
      }
    })
  }
  /**
   * 删除用户
   */
  const handleDeleteUser = () => {
    // 获取要删除的用户
    const userList = getSelectUserList()
    // 验证用户
    const result = validateMessage(userList)
    if (!result) {
      return false
    }
    // 验证是否包含系统用户
    const sysUserValidate = userList.some((item: any) => item.userType === '10')
    if (sysUserValidate) {
      message.error(t('system.views.user.validate.sysUserNoDelete'))
      return false
    }
    Modal.confirm({
      title: t('common.notice.deleteConfirm'),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        const userIdList = userList.map(item => item.userId)
        try {
          await ApiService.postAjax('sys/user/batchDeleteById', userIdList)
          loadData()
        } catch (e) {
          errorMessage(e)
        }
      }
    })
  }
  return {
    handleDeleteUser,
    handleSetUseYn
  }
}

/**
 * vue加载数据
 */
export const vueLoadData = () => {
  const sortData = reactive({
    sortName: 'seq',
    sortOrder: 'asc'
  })
  const data = ref<Array<any>>([])
  // table加载状态
  const loading = ref(false)
  // 搜索from
  const searchModel = reactive({
    username: '',
    fullName: '',
    email: '',
    useYn: 1,
    deleteYn: 0
  })
  // 分页数据
  const tablePage = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 500
  })
  // 加载数据函数
  const loadData = async () => {
    const allParameter: any = {
      ...sortData,
      limit: tablePage.pageSize,
      page: tablePage.currentPage
    }
    const parameter: any = {
      QUERY_CREATE_UPDATE_USER: true
    }
    if (searchModel.username != null && searchModel.username.trim() !== '') {
      parameter['username@like'] = searchModel.username
    }
    if (searchModel.fullName != null && searchModel.fullName.trim() !== '') {
      parameter['fullName@like'] = searchModel.fullName
    }
    if (searchModel.email != null && searchModel.email.trim() !== '') {
      parameter['email@like'] = searchModel.email
    }
    if (searchModel.useYn !== -1) {
      parameter['useYn@='] = searchModel.useYn !== 0
    }
    if (searchModel.deleteYn !== -1) {
      parameter['deleteYn@='] = searchModel.deleteYn !== 0
    }
    allParameter.parameter = parameter
    loading.value = true
    try {
      const result = await ApiService.postAjax('sys/user/list', allParameter)
      tablePage.total = result.total
      data.value = result.rows
    } catch (e) {
      errorMessage(e)
    } finally {
      loading.value = false
    }
  }
  /**
   * 排序变化时触发
   */
  const handleSortChange = ({ property, order }: any) => {
    sortData.sortOrder = order
    sortData.sortName = property
    loadData()
  }
  // 分页操作
  const handlePageChange = ({ currentPage, pageSize }: any) => {
    tablePage.currentPage = currentPage
    tablePage.pageSize = pageSize
    loadData()
  }
  onMounted(loadData)
  return {
    data,
    loading,
    searchModel,
    tablePage,
    loadData,
    handlePageChange,
    handleSortChange
  }
}

/**
 * 默认的添加修改表单数据
 */
const defaultAddEditModel = {
  userType: '20',
  seq: 1
}
/**
 * 添加修改
 * @param loadData
 */
export const vueAddEdit = (loadData: any) => {
  const formRef = ref()
  // 添加修改 modal显示状态
  const modalVisible = ref(false)
  const addEditModel = ref(Object.assign({}, defaultAddEditModel))
  // 保存加载状态
  const saveLoading = ref(false)
  // form加载状态
  const formLoading = ref(false)
  // 是否添加
  const isAdd = ref(false)

  /*
     * 点击保存触发
     */
  const handleOk = async () => {
    saveLoading.value = true
    formRef.value
      .validate()
      .then(async () => {
        try {
          await ApiService.postAjax('sys/user/saveUpdate', addEditModel.value)
          loadData()
          modalVisible.value = false
        } catch (e) {
          // TODO:待处理
          errorMessage(e)
        } finally {
          saveLoading.value = false
        }
      })
  }
  // 保存
  const handleShowSave = () => {
    isAdd.value = true
    modalVisible.value = true
    addEditModel.value = Object.assign({}, defaultAddEditModel)
  }
  // 修改操作
  const handleShowUpdate = async (id: number) => {
    isAdd.value = false
    modalVisible.value = true
    formLoading.value = true
    try {
      addEditModel.value = await ApiService.postAjax('sys/user/getById', id)
    } finally {
      formLoading.value = false
    }
  }
  return {
    formRef,
    modalVisible,
    addEditModel,
    saveLoading,
    formLoading,
    isAdd,
    handleOk,
    handleShowSave,
    handleShowUpdate
  }
}

/**
 * 创建用户账户hook
 * @param tableRef
 * @param t
 */
export const useCreateAccount = (tableRef: Ref, t: Function) => {
  // 获取选中的用户IDList
  const getSelectUserList = (): Array<any> => tableRef.value.getCheckboxRecords()

  /**
   * 创建账户
   */
  const handleCreateAccount = () => {
    const userList = getSelectUserList()
    if (userList.length === 0) {
      message.warn(t('system.views.user.validate.selectUser'))
      return false
    }
    // 判断是否有用户已经删除
    const hasDelete = userList.some(item => item.deleteYn === true)
    if (hasDelete) {
      message.warn(t('system.views.user.message.deleteUserNotCreateAccount'))
      return false
    }
    // 判断是否有停用用户
    const hasNoUse = userList.some(item => item.useYn === false)
    if (hasNoUse) {
      message.warn(t('system.views.user.message.noUseUserNotCreateAccount'))
      return false
    }
    Modal.confirm({
      title: t('system.views.user.validate.createAccountConfirm'),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        try {
          await ApiService.postAjax('sys/user/createAccount', userList.map(item => item.userId))
          message.success(t('system.views.user.message.createAccountSuccess'))
        } catch (e) {
          errorMessage(e)
        }
      }
    })
  }

  return {
    handleCreateAccount
  }
}
