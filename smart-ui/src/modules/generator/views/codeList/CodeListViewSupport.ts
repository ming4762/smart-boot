import { onMounted, reactive, ref, Ref, createVNode, computed } from 'vue'

import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'
import StringUtils from '@/common/utils/StringUtils'

import { errorMessage } from '@/components/notice/SystemNotice'

const formModelI = {
  id: null,
  connectionId: '',
  tableName: '',
  type: '10',
  showCheckbox: true,
  page: true,
  formColNum: 1,
  searchColNum: 3,
  invented: false,
  remark: '',
  configName: '',
  columnSort: false,
  leftButtonList: ['SEARCH', 'RESET'],
  rightButtonList: ['ADD', 'EDIT', 'DELETE'],
  rowButtonType: 'NONE',
  rowButtonList: [],
  i18nPrefix: ''
}

/**
 * 生成代码操作
 */
const handleGetData = (getLoading: Ref, codeId: number, codeData: Ref) => {
  getLoading.value = true
  // 加载数据
  const doLoadData = async () => {
    codeData.value = await ApiService.postAjax('db/code/main/getById', codeId)
  }
  try {
    doLoadData()
  } finally {
    getLoading.value = false
  }
}

/**
 * 加载数据库数据
 * @param isSync 数据库数据是否同步
 * @param dbData 数据库数据
 * @param formModel form实体
 * @param formRef form引用
 */
export const vueLoadDbDataSupport = (isSync: Ref, dbData: Ref, formModel: Ref, formRef: Ref) => {
  // 数据库数据加载状态
  const dbDataLoading = ref(false)
  /**
   * 加载数据库数据
   */
  const loadDbData = async () => {
    if (StringUtils.hasLength(formModel.value.connectionId) && StringUtils.hasLength(formModel.value.tableName)) {
      dbDataLoading.value = true
      try {
        dbData.value = await ApiService.postAjax('db/connection/queryDbTable', {
          dbConnectionId: formModel.value.connectionId,
          tableName: formModel.value.tableName
        })
        isSync.value = true
      } catch (e) {
        errorMessage(e)
      } finally {
        dbDataLoading.value = false
      }
    }
  }
  /**
   * 表格table计算属性
   */
  const computedTableData = computed(() => {
    if (!dbData.value.tableName) {
      return []
    }
    const primaryKeyList = dbData.value.primaryKeyList || []
    const baseColumnList = dbData.value.baseColumnList || []
    return [
      ...primaryKeyList,
      ...baseColumnList
    ]
  })
  /**
   * 同步表
   */
  const handleSyncTableData = () => {
    formRef.value.validate()
      .then(() => {
        loadDbData()
      })
  }
  return {
    dbData,
    dbDataLoading,
    computedTableData,
    handleSyncTableData,
    loadDbData
  }
}

/**
 * 保存配置数据
 * @param t 国际化函数
 * @param isSync 是否同步
 * @param doLoadData 加载列表数据函数
 * @param addEditModalVisible 弹窗加载状态
 * @param dbData 数据库数据
 * @param editConfigData
 * @param choseTableList 附表信息
 */
export const vueSaveConfigSupport = (t: Function, isSync: Ref, doLoadData: any, addEditModalVisible: Ref, dbData: Ref, editConfigData: Ref, choseTableList: Ref) => {
  const pageTableSettingRef = ref()
  const pageSearchSettingRef = ref()
  const pageFormSettingRef = ref()
  // form实体
  const formModel = ref(Object.assign({}, formModelI))
  // 保存加载状态
  const saveLoading = ref(false)
  /**
   * 点击添加操作
   */
  const handleShowAdd = () => {
    // 重置数据
    formModel.value = Object.assign({}, formModelI)
    dbData.value = {}
    editConfigData.value = {}
    addEditModalVisible.value = true
    choseTableList.value = []
  }
  /**
   * 执行保存
   */
  const doSave = async () => {
    saveLoading.value = true
    const saveData = Object.assign({
      className: dbData.value.className,
      remarks: dbData.value.remarks,
      addendumTableList: choseTableList.value || []
    }, formModel.value, {
      codePageConfigList: pageTableSettingRef.value.getData(),
      codeFormConfigList: pageFormSettingRef.value.getData(),
      codeSearchConfigList: pageSearchSettingRef.value.getData()
    })
    try {
      await ApiService.postAjax('db/code/main/save', saveData)
    } catch (e: any) {
      if (e.code === 400) {
        e.data.forEach((item: string) => {
          message.error(item)
        })
      } else {
        errorMessage(e)
      }
      return false
    } finally {
      saveLoading.value = false
    }
    addEditModalVisible.value = false
    // 加载数据
    doLoadData()
  }
  /**
   * 保存操作
   */
  const handleSave = () => {
    // 执行保存操作
    if (!isSync.value) {
      message.warn(t('generator.views.code.validate.syncTable'))
      return false
    }
    // 处理数据
    const tableSettingVue = pageTableSettingRef.value
    if (!tableSettingVue) {
      message.warn(t('generator.views.code.validate.tableSetting'))
      return false
    }
    const pageFormSettingVue = pageFormSettingRef.value
    if (!pageFormSettingVue) {
      message.warn(t('generator.views.code.validate.formSetting'))
      return false
    }
    // 搜索配置实体
    const pageSearchSettingVue = pageSearchSettingRef.value
    if (!pageSearchSettingVue) {
      message.warn(t('generator.views.code.validate.searchSetting'))
      return false
    }
    // 验证必填字段是否设置表单
    const pageFormSettingData = pageFormSettingVue.getData() as Array<any>
    const nonNullField: Array<any> = []
    pageFormSettingData.forEach(item => {
      if (item.nullable === 0 && (item.visible === false || item.used === false)) {
        nonNullField.push(item.columnName)
      }
    })
    if (nonNullField.length > 0) {
      Modal.confirm({
        title: t('common.notice.confirmSave'),
        icon: createVNode(ExclamationCircleOutlined),
        content: t('generator.views.code.message.saveConfirmContent', nonNullField.join(',')),
        onCancel () {
          return false
        },
        onOk () {
          doSave()
        }
      })
    } else {
      doSave()
    }
  }
  return {
    pageTableSettingRef,
    pageFormSettingRef,
    pageSearchSettingRef,
    handleSave,
    saveLoading,
    formModel,
    handleShowAdd
  }
}

/**
 * codeList页面删除功能
 * @param gridRef grid ref
 * @param t 国际化函数
 * @param doLoadTableData 加载表格数据
 */
export const vueCodeListDeleteSupport = (gridRef: Ref, t: Function, doLoadTableData: any) => {
  /**
   * 删除操作
   */
  const handleDelete = () => {
    // 获取要删除的数据
    const selectData: Array<any> = gridRef.value.getCheckboxRecords()
    if (selectData.length === 0) {
      message.error(t('common.notice.deleteChoose'))
      return false
    }
    Modal.confirm({
      title: t('common.notice.deleteConfirm'),
      icon: createVNode(ExclamationCircleOutlined),
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk: async () => {
        // 执行删除
        await ApiService.postAjax('db/code/main/batchDeleteById', selectData.map(item => item.id))
        doLoadTableData()
      }
    })
  }
  return {
    handleDelete
  }
}

/**
 * 修改操作
 */
export const vueConfigEditSupport = (addEditModalVisible: Ref, formModel: Ref, loadDbData: any, editConfigData: Ref, choseTableList: Ref) => {
  // 配置数据加载状态
  const configDataLoading = ref(false)
  /**
   * 加载数据函数
   * @param id
   */
  const handleShowEdit = async (id: number) => {
    // 打开model
    addEditModalVisible.value = true
    configDataLoading.value = true
    try {
      const result = await ApiService.postAjax('db/code/main/getConfigById', id)
      choseTableList.value = result.relatedTableList || []
      editConfigData.value = result
      // 设置form参数
      const formModelValue: any = Object.assign({}, formModelI)
      Object.keys(formModelValue).forEach(key => {
        formModelValue[key] = result[key]
      })
      formModelValue.connectionId = formModelValue.connectionId + ''
      formModel.value = formModelValue
      loadDbData()
    } finally {
      configDataLoading.value = false
    }
  }
  return {
    configDataLoading,
    handleShowEdit
  }
}

/**
 * anction操作
 */
export const crateActionsVue = (handleShowEdit: any) => {
  // 查询加载状态
  const getLoading = ref(false)
  const codeData = ref({})
  const createModalVisible = ref(false)
  const handleActions = (codeId: number, action: string) => {
    switch (action) {
      case 'edit': {
        handleShowEdit(codeId)
        break
      }
      case 'create': {
        // 加载数据
        handleGetData(getLoading, codeId, codeData)
        createModalVisible.value = true
        break
      }
    }
  }
  return {
    handleActions,
    getLoading,
    createModalVisible,
    codeData
  }
}

/**
 * 加载表格数据
 */
export const loadTableData = () => {
  const tableLoading = ref(false)
  // 表格数据
  const searchModel = reactive({
    tableName: '',
    type: ''
  })
  // 分页信息
  const tablePage = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 500
  })
  const data = ref<any>([])
  const doLoadData = async () => {
    tableLoading.value = true
    // 设置参数
    const searchParameter = {
      'tableName@like': searchModel.tableName,
      'type@=': searchModel.type
    }
    const parameter: {[index: string]: any} = {
      limit: tablePage.pageSize,
      page: tablePage.currentPage,
      parameter: searchParameter
    }
    try {
      const { rows, total } = await ApiService.postAjax('db/code/main/list', parameter)
      tablePage.total = total
      data.value = rows
    } catch (e) {
      errorMessage(e)
    } finally {
      tableLoading.value = false
    }
  }
  /**
   * 分页触发事件
   */
  const handlePageChange = ({ currentPage, pageSize }: any) => {
    tablePage.currentPage = currentPage
    tablePage.pageSize = pageSize
    doLoadData()
  }
  onMounted(() => doLoadData())
  return {
    tableLoading,
    doLoadData,
    data,
    searchModel,
    tablePage,
    handlePageChange
  }
}

/**
 * 左侧按钮列表
 */
const commonButtonList = [
  {
    key: 'SEARCH',
    value: '搜索'
  },
  {
    key: 'RESET',
    value: '重置'
  },
  {
    key: 'ADD',
    value: '添加'
  },
  {
    key: 'EXCEL_IMPORT',
    value: 'excel导入'
  },
  {
    key: 'DOWNLOAD',
    value: '导出'
  },
  {
    key: 'DOWNLOAD_ALL',
    value: '全部导出'
  },
  {
    key: 'COLUMN_SETTING',
    value: '列设置'
  },
  {
    key: 'SET_VALID',
    value: '设置有效'
  },
  {
    key: 'SET_INVALID',
    value: '设置无效'
  }
]

/**
 * 行按钮
 */
const rowButtonList = [
  {
    key: 'EDIT',
    value: '修改'
  },
  {
    key: 'DELETE',
    value: '删除'
  },
  {
    key: 'save',
    value: '保存'
  }
]

/**
 * 按钮支持
 */
export const vueButtonSupport = () => {
  return {
    buttonList: reactive(commonButtonList.concat(rowButtonList)),
    rowButtonList: reactive(rowButtonList)
  }
}

/**
 * 主表关联附表
 */
export const vueRelateAddendumTable = (choseTableList: Ref) => {
  // 关联附表弹窗显示状态
  const choseAddendumModalVisible = ref(false)

  /**
   * 点击关联附表事件
   */
  const handleShowChoseAddendumModal = () => {
    choseAddendumModalVisible.value = true
  }
  /**
   * 选择附表后触发
   */
  const handleChoseTable = (tableList: Array<any>) => {
    choseTableList.value.push(...tableList)
  }
  /**
   * 移除附表操作
   */
  const handleRemoveRelateTable = (table: any, index: number) => {
    choseTableList.value.splice(index, 1)
  }
  return {
    choseAddendumModalVisible,
    handleShowChoseAddendumModal,
    handleChoseTable,
    handleRemoveRelateTable
  }
}
