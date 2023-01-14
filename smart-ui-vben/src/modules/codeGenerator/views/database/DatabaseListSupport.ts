import { ref, onMounted, reactive } from 'vue'
import type { Ref } from 'vue'
import type { Result } from '/#/axios'

import { message, Modal } from 'ant-design-vue'

import ApiService from '/@/common/utils/ApiService'

import { isSuperAdmin, getCurrentUserId, applyTempToken } from '/@/common/auth/AuthUtils'
import { useMessage } from '/@/hooks/web/useMessage'
import { searchForm } from './DatabaseListView.data'
import { useForm } from '/@/components/Form'

const defaultSearchModel = {
  connectionName: '',
  databaseName: '',
  project: '',
}

/**
 * 加载数据支持
 */
export const vueLoadData = (t: Function) => {
  // 数据
  const data = ref([])
  // 数据加载状态
  const loading = ref(false)
  // 搜索表单
  const searchModel = ref<any>(Object.assign({}, defaultSearchModel))
  const [registerSearch] = useForm({
    schemas: searchForm(t),
    showAdvancedButton: true,
    layout: 'inline',
  })
  // 分页信息
  const tablePage = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 500,
  })
  const loadData = async () => {
    // 构建参数
    const allParameter: any = {
      limit: tablePage.pageSize,
      page: tablePage.currentPage,
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
    resetSearch,
    registerSearch,
  }
}

/**
 * 编辑修改支持
 */
export const vueAddEdit = () => {
  /**
   * 编辑是否可以操作
   * @param row
   */
  const getEditDisable = (row: any) => {
    return !(isSuperAdmin() || row.createUserId === getCurrentUserId())
  }

  return {
    getEditDisable,
  }
}

/**
 * 删除操作
 */
export const vueAction = (t: Function) => {
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
          content: result.message,
        })
      }
    } finally {
      pageLoading.value = false
    }
  }
  return {
    handleTestConnected,
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
    selectGroupIds.value = await ApiService.postAjax('db/connection/listUserGroupId', row.id)
  }
  /**
   * 执行保存操作
   */
  const handleSetGroup = async (selectedRowKeys: Array<number>) => {
    await ApiService.postAjax('db/connection/setUserGroup', {
      connectionId: currentRow.value.id,
      userGroupIdList: selectedRowKeys,
    })
    message.success(t('common.message.saveSuccess'))
  }
  return {
    currentRow,
    handleCurrentChange,
    selectGroupIds,
    handleSetGroup,
    saveButtonVisible,
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
        const url = `${ApiService.getApiUrl()}/public/db/createDic?connectionId=${
          row.value.id
        }&templateId=${templateId}&access-token=${tempToken}`
        window.open(url)
      })
    } catch (e) {
      const { errorMessage } = useMessage()
      errorMessage(e as Result)
    }
  }
  return {
    handleTemplateChange,
    handleCreate,
    creatDicModalVisible,
    handleShowCreateDictModal,
  }
}
