import { onMounted, ref, reactive, Ref, createVNode } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

import TreeUtils from '@/common/utils/TreeUtils'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

/**
 * 加载功能列表
 */
export const vueLoadFunctionList = () => {
  const functionList = ref<Array<any>>([])
  const dataLoading = ref(false)

  /**
   * 加载数据函数
   */
  const loadData = async () => {
    dataLoading.value = true
    try {
      const result: Array<any> = await ApiService.postAjax('sys/function/list', {
        parameter: {
          QUERY_CREATE_UPDATE_USER: true
        },
        sortName: 'seq'
      })
      functionList.value = TreeUtils.convertList2Tree(result, ['functionId', 'parentId'], 0) || []
    } catch (e) {
      errorMessage(e)
    } finally {
      dataLoading.value = false
    }
  }
  onMounted(loadData)
  return {
    functionList,
    dataLoading,
    loadData
  }
}

/**
 * 默认的保存model
 */
const defaultSaveModel = {
  parentId: 0,
  functionType: '10',
  seq: 1,
  menuIs: true,
  internalOrExternal: false
}

const saveFormRules = (t: Function) => {
  return {
    functionName: [{ required: true, message: t('system.views.function.validate.functionName'), trigger: 'blur' }],
    functionType: [{ required: true, message: t('system.views.function.validate.functionType'), trigger: 'change' }]
  }
}

const defaultFunctionTypes = (t: Function) => {
  return {
    catalogue: {
      value: 'CATALOG',
      label: t('system.views.function.common.catalogue'),
      disabled: false
    },
    menu: {
      value: 'MENU',
      label: t('system.views.function.common.menu'),
      disabled: false
    },
    function: {
      value: 'FUNCTION',
      label: t('system.views.function.common.function'),
      disabled: false
    }
  }
}

/**
 * 添加编辑操作
 */
export const vueAddEdit = (loadData: any, t: Function) => {
  const currentRow = ref<any>()
  // 添加编辑弹窗显示状态
  const addEditModalVisible = ref(false)
  // 是否是添加
  const isAdd = ref(false)
  const saveLoading = ref(false)
  const getLoading = ref(false)
  const saveModel = ref<any>({})
  const formRules = reactive(saveFormRules(t))
  const functionTypes: any = reactive(defaultFunctionTypes(t))
  /**
   * 显示编辑
   */
  const handleShowEdit = async (row: any) => {
    currentRow.value = row
    isAdd.value = false
    addEditModalVisible.value = true
    setTypeDisabled(['catalogue', 'menu', 'function'])
    // 加载数据
    getLoading.value = true
    try {
      saveModel.value = await ApiService.postAjax('sys/function/getById', row.functionId)
    } finally {
      getLoading.value = false
    }
  }
  /**
   * 显示添加
   */
  const handleShowAdd = (row: any | null) => {
    currentRow.value = row
    isAdd.value = true
    addEditModalVisible.value = true
    saveModel.value = Object.assign({}, defaultSaveModel)
    if (row !== null) {
      saveModel.value.parentId = row.functionId
      saveModel.value.parentName = row.functionName
      const functionType = row.functionType
      switch (functionType) {
        case 'CATALOG': {
          setTypeDisabled(['function'])
          break
        }
        case 'MENU': {
          setTypeDisabled(['catalogue', 'menu'])
          saveModel.value.functionType = '30'
          break
        }
        case 'FUNCTION': {
          setTypeDisabled(['catalogue', 'menu', 'function'])
          break
        }
      }
    } else {
      saveModel.value.parentName = t('system.views.function.common.rootCatalogue')
      setTypeDisabled(['function'])
    }
  }
  /**
   * 设置不可用类型
   * @param keys
   */
  const setTypeDisabled = (keys: Array<string>) => {
    Object.keys(functionTypes).forEach(key => {
      functionTypes[key].disabled = keys.includes(key)
    })
  }
  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/function/saveUpdate', saveModel.value)
      addEditModalVisible.value = false
      loadData()
    } catch (e) {
      errorMessage(e)
    } finally {
      saveLoading.value = false
    }
  }
  return {
    handleShowEdit,
    handleShowAdd,
    addEditModalVisible,
    isAdd,
    handleSave,
    saveLoading,
    getLoading,
    saveModel,
    formRules,
    functionTypes
  }
}

export const vueDelete = (loadData: any, gridRef: Ref, t: Function) => {
  const handleDelete = () => {
    // 获取选中行
    const selectRows: Array<any> = gridRef.value.getCheckboxRecords()
    if (selectRows.length === 0) {
      message.error(t('common.notice.deleteChoose'))
      return false
    }
    Modal.confirm({
      title: t('common.notice.confirm'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('common.notice.deleteConfirm'),
      onOk: async () => {
        await ApiService.postAjax('sys/function/batchDeleteById', selectRows.map(item => item.functionId))
        loadData()
      }
    })
  }
  return {
    handleDelete
  }
}
