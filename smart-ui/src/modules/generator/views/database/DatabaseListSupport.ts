import { ref, onMounted, reactive, createVNode } from 'vue'
import type { Ref } from 'vue'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

import { isSuperAdmin, getCurrentUserId, applyTempToken } from '@/common/auth/AuthUtils'

const defaultSearchModel = {
  connectionName: '',
  databaseName: '',
  project: ''
}

/**
 * 加载数据支持
 */
export const vueLoadData = () => {
  // 数据
  const data = ref([])
  // 数据加载状态
  const loading = ref(false)
  // 搜索表单
  const searchModel = ref<any>(Object.assign({}, defaultSearchModel))
  // 分页信息
  const tablePage = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 500
  })
  const loadData = async () => {
    // 构建参数
    const allParameter: any = {
      limit: tablePage.pageSize,
      page: tablePage.currentPage
    }
    // 自定义参数
    const customParameter: any = {}
    Object.keys(searchModel.value).forEach((key) => {
      const value = searchModel.value[key]
      if (value !== null && value !== '') {
        customParameter[key + '@like'] = value
      }
    })
    allParameter.parameter = customParameter
    try {
      loading.value = true
      const { rows, total } = await ApiService.postAjax('db/connection/listByAuth', allParameter)
      tablePage.total = total
      data.value = rows
    } catch (e) {
      errorMessage(e)
    } finally {
      loading.value = false
    }
  }
  /**
   * 重置搜索表单
   */
  const resetSearch = () => {
    searchModel.value = Object.assign({}, defaultSearchModel)
  }
  /**
   * 分页触发事件
   */
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
    loadData,
    tablePage,
    handlePageChange,
    resetSearch
  }
}

const defaultAddEditModel = {
  connectionName: '',
  databaseName: '',
  type: '',
  project: ''
}

const createFormRules = (t: Function) => {
  return {
    connectionName: [
      {
        required: true,
        trigger: 'blur',
        message: t('generator.views.database.validate.connectionName')
      }
    ],
    databaseName: [
      {
        required: true,
        trigger: 'blur',
        message: t('generator.views.database.validate.databaseName')
      }
    ],
    type: [
      { required: true, trigger: 'blur', message: t('generator.views.database.validate.type') }
    ],
    project: [
      { required: true, trigger: 'blur', message: t('generator.views.database.validate.project') }
    ],
    url: [{ required: true, trigger: 'blur', message: t('generator.views.database.validate.url') }],
    username: [
      { required: true, trigger: 'blur', message: t('generator.views.database.validate.username') }
    ],
    password: [
      { required: true, trigger: 'blur', message: t('generator.views.database.validate.password') }
    ]
  }
}

/**
 * 编辑修改支持
 */
export const vueAddEdit = (loadData: any, formRef: Ref, t: Function) => {
  // 保存加载状态
  const saveLoading = ref(false)
  // 查询加载状态
  const getLoading = ref(false)
  // 弹窗状态
  const addEditModalVisible = ref(false)
  // 是否是添加
  const isAdd = ref(false)
  // 添加修改表单
  const addEditModel = ref<any>(Object.assign({}, defaultAddEditModel))
  /**
   * 添加操作
   */
  const handleShowAdd = () => {
    addEditModel.value = Object.assign({}, defaultAddEditModel)
    isAdd.value = true
    addEditModalVisible.value = true
  }
  /**
   * 编辑是否可以操作
   * @param row
   */
  const getEditDisable = (row: any) => {
    return !(isSuperAdmin() || row.createUserId === getCurrentUserId())
  }
  /**
   * 编辑操作
   */
  const handleShowEdit = async (id: number) => {
    isAdd.value = false
    addEditModalVisible.value = true
    getLoading.value = true
    try {
      addEditModel.value = await ApiService.postAjax('db/connection/getById', id)
    } finally {
      getLoading.value = false
    }
  }
  /**
   * 执行保存操作
   */
  const handleSave = () => {
    // 验证表单
    formRef.value.validate().then(async () => {
      saveLoading.value = true
      try {
        await ApiService.postAjax('db/connection/saveUpdate', addEditModel.value)
        addEditModalVisible.value = false
        loadData()
      } finally {
        saveLoading.value = false
      }
    })
  }
  return {
    saveLoading,
    getLoading,
    addEditModalVisible,
    handleShowAdd,
    isAdd,
    addEditModel,
    formRules: reactive(createFormRules(t)),
    handleShowEdit,
    handleSave,
    getEditDisable
  }
}

/**
 * 删除操作
 */
export const vueAction = (gridRef: Ref, loadData: any, t: Function) => {
  /**
   * 删除操作
   */
  const handleDelete = () => {
    // 获取选中行
    const selectRows: Array<any> = gridRef.value.getCheckboxRecords()
    if (selectRows.length === 0) {
      message.error(t('common.notice.deleteChoose'))
      return false
    }
    // 判断是否有权限删除
    if (!isSuperAdmin()) {
      const validateResult = selectRows.some(
        (item: any) => item.createUserId !== getCurrentUserId()
      )
      if (validateResult) {
        message.error(t('generator.views.database.message.deleteOwn'))
        return false
      }
    }
    Modal.confirm({
      title: t('common.notice.confirm'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('common.notice.deleteChoose'),
      onOk: async () => {
        await ApiService.postAjax(
          'db/connection/batchDeleteById',
          selectRows.map((item: any) => item.id)
        )
        loadData()
      }
    })
  }
  /**
   * 测试连接
   * @param row
   * @param pageLoading 页面加载状态
   */
  const handleTestConnected = async (row: any, pageLoading: Ref) => {
    try {
      pageLoading.value = true
      const result = await ApiService.postAjax('db/connection/testConnection', row.id)
      if (result.result === true) {
        message.success(t('generator.views.database.message.connectSuccess'))
      } else {
        Modal.error({
          title: t('generator.views.database.message.connectFail'),
          content: result.message
        })
      }
    } finally {
      pageLoading.value = false
    }
  }
  return {
    handleDelete,
    handleTestConnected
  }
}

/**
 * 设置用户组相关操作
 */
export const vueSetUserGroup = (t: Function) => {
  const currentRow = ref<any>({})
  const selectGroupIds = ref<Array<number>>([])
  // 保存按钮的显示状态
  const saveButtonVisible = ref(false)
  /**
   * 当前行变更事件
   */
  const handleCurrentChange = async ({ row }: any) => {
    saveButtonVisible.value = isSuperAdmin() || row.createUserId === getCurrentUserId()
    currentRow.value = row
    try {
      selectGroupIds.value = await ApiService.postAjax('db/connection/listUserGroupId', row.id)
    } catch (e) {
      errorMessage(e)
    }
  }
  /**
   * 执行保存操作
   */
  const handleSetGroup = async (selectedRowKeys: Array<number>) => {
    await ApiService.postAjax('db/connection/setUserGroup', {
      connectionId: currentRow.value.id,
      userGroupIdList: selectedRowKeys
    })
    message.success(t('common.message.saveSuccess'))
  }
  return {
    currentRow,
    handleCurrentChange,
    selectGroupIds,
    handleSetGroup,
    saveButtonVisible
  }
}

export const vueCreateDict = (row: Ref, t: Function) => {
  const creatDicModalVisible = ref(false)
  // 选择的模板数据
  let selectTemplateIdList: Array<string> = []
  const handleTemplateChange = (templateIds: Array<string>) => {
    selectTemplateIdList = templateIds
  }
  const handleShowCreateDictModal = () => {
    creatDicModalVisible.value = true
  }
  /**
   * 创建模板
   */
  const handleCreate = async () => {
    if (selectTemplateIdList.length === 0) {
      message.error(t('generator.views.database.validate.template'))
      return false
    }
    try {
      const tempToken = await applyTempToken('db:connection:createDic', false)
      selectTemplateIdList.forEach((templateId) => {
        const url = `${ApiService.getApiUrl()}public/db/createDic?connectionId=${
          row.value.id
        }&templateId=${templateId}&access-token=${tempToken}`
        window.open(url)
      })
    } catch (e) {
      errorMessage(e)
    }
  }
  return {
    handleTemplateChange,
    handleCreate,
    creatDicModalVisible,
    handleShowCreateDictModal
  }
}
