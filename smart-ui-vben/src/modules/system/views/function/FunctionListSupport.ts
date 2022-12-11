import { onMounted, ref, reactive, createVNode } from 'vue'
import type { Ref } from 'vue'

import ApiService from '/@/common/utils/ApiService'

import TreeUtils from '/@/utils/TreeUtils'

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
          QUERY_CREATE_UPDATE_USER: true,
        },
        sortName: 'seq',
      })
      functionList.value =
        TreeUtils.convertList2Tree(
          result,
          (item) => item.functionId,
          (item) => item.parentId,
          0,
        ) || []
    } finally {
      dataLoading.value = false
    }
  }
  onMounted(loadData)
  return {
    functionList,
    dataLoading,
    loadData,
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
  internalOrExternal: false,
}

const saveFormRules = (t: Function) => {
  return {
    functionName: [
      {
        required: true,
        message: t('system.views.function.validate.functionName'),
        trigger: 'blur',
      },
    ],
    functionType: [
      {
        required: true,
        message: t('system.views.function.validate.functionType'),
        trigger: 'change',
      },
    ],
  }
}

const defaultFunctionTypes = (t: Function) => {
  return {
    catalogue: {
      value: 'CATALOG',
      label: t('system.views.function.common.catalogue'),
      disabled: false,
    },
    menu: {
      value: 'MENU',
      label: t('system.views.function.common.menu'),
      disabled: false,
    },
    function: {
      value: 'FUNCTION',
      label: t('system.views.function.common.function'),
      disabled: false,
    },
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
  const formRef = ref()
  // 显示数据model，不传送到后台
  const showDataModel = ref<any>({})
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
    saveModel.value = {}
    showDataModel.value = {}
    try {
      const getModel = await ApiService.postAjax('sys/function/getById', row.functionId)
      if (getModel !== null) {
        saveModel.value = getModel.function
        // 设置上级显示
        const { parent, createUser, updateUser } = getModel
        if (parent === null) {
          showDataModel.value.parentName = t('system.views.function.common.rootCatalogue')
        } else {
          showDataModel.value.parentName = parent.functionName
        }
        // 创建人/更新人
        if (createUser !== null) {
          showDataModel.value.createUser = createUser.fullName
        }
        if (updateUser !== null) {
          showDataModel.value.updateUser = updateUser.fullName
        }
        showDataModel.value.createTime = getModel.function.createTime
        showDataModel.value.updateTime = getModel.function.updateTime
      }
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
      showDataModel.value.parentName = row.functionName
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
      showDataModel.value.parentName = t('system.views.function.common.rootCatalogue')
      setTypeDisabled(['function'])
    }
  }
  /**
   * 设置不可用类型
   * @param keys
   */
  const setTypeDisabled = (keys: Array<string>) => {
    Object.keys(functionTypes).forEach((key) => {
      functionTypes[key].disabled = keys.includes(key)
    })
  }
  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    try {
      await formRef.value.validateFields()
    } catch (e) {
      console.log(e)
      return false
    }
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/function/saveUpdate', saveModel.value)
      addEditModalVisible.value = false
      loadData()
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
    functionTypes,
    showDataModel,
    formRef,
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
        await ApiService.postAjax(
          'sys/function/batchDeleteById',
          selectRows.map((item) => item.functionId),
        )
        loadData()
      },
    })
  }
  return {
    handleDelete,
  }
}
