import { onMounted, reactive, ref } from 'vue'
import ApiService from '@/common/utils/ApiService'
import { extensionLanguageMap } from '@/modules/generator/views/codeCreate/CodeCreateSupport'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 关联用户组
 */
export const vueRelateUserGroup = () => {
  // modal显示状态
  const relateModalVisible = ref(false)
  const userGroupLoading = ref(false)
  // 所有用户信息
  const allUserGroup = ref<Array<any>>([])
  const targetKeysModel = ref<Array<string>>([])
  /**
   * 显示设置用户组弹窗
   */
  const handleShowRelateModal = async () => {
    relateModalVisible.value = true
    userGroupLoading.value = true
    try {
      // 加载用户组信息
      if (allUserGroup.value.length === 0) {
        const result: Array<any> = await ApiService.postAjax('sys/userGroup/list', {
          sortName: 'seq',
          parameter: {
            'useYn@=': true
          }
        })
        allUserGroup.value = result.map(({ groupId, groupName, groupCode }: any) => {
          return {
            key: groupId + '',
            title: `${groupName}[${groupCode}]`
          }
        })
      }
    } finally {
      userGroupLoading.value = false
    }
  }
  const handleTransChange = (targetKeyList: Array<string>) => {
    targetKeysModel.value = targetKeyList
  }
  return {
    relateModalVisible,
    handleShowRelateModal,
    allUserGroup,
    userGroupLoading,
    targetKeysModel,
    handleTransChange
  }
}

/**
 * 加载数据支持
 */
export const vueLoadData = () => {
  const data = ref<Array<any>>([])
  // 数据加载状态
  const tableDataLoading = ref(false)
  // 搜索表单吗
  const searchModel = reactive({
    name: ''
  })
  // 分页信息
  const tablePage = reactive({
    total: 0,
    currentPage: 1,
    pageSize: 500
  })
  /**
   * 加载表格数据函数
   */
  const loadData = async () => {
    tableDataLoading.value = true
    const parameter = {
      limit: tablePage.pageSize,
      page: tablePage.currentPage,
      parameter: {
        'name@like': searchModel.name,
        queryCreateUpdateUser: true
      }
    }
    try {
      const { rows, total } = await ApiService.postAjax('db/code/template/list', parameter)
      tablePage.total = total
      data.value = rows
    } catch (e) {
      errorMessage(e)
    } finally {
      tableDataLoading.value = false
    }
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
    tableDataLoading,
    searchModel,
    loadData,
    tablePage,
    handlePageChange
  }
}

const defaultSaveModel = {
  language: 'text/x-java',
  template: '',
  filenameSuffix: ''
}

export const vueAddEdit = (loadData: any, t: Function) => {
  const addEditModalVisible = ref(false)
  const currentRow = ref<any>({})
  const isAdd = ref(true)
  const isShow = ref(false)
  const saveLoading = ref(false)
  const saveFormRef = ref()
  const codemirror = ref()
  const code = ref('')
  const formModel = ref(Object.assign({}, defaultSaveModel))
  const formRules = reactive({
    name: [
      { required: true, message: t('generator.views.template.validate.name'), trigger: 'blur' }
    ]
  })
  /**
   * 执行保存
   */
  const handleSave = () => {
    // 表单验证
    saveFormRef.value.validate()
      .then(async () => {
        // 获取代码内容
        const template = codemirror.value.getCode()
        const saveModel = Object.assign({}, formModel.value, {
          template
        })
        saveLoading.value = true
        try {
          await ApiService.postAjax('db/code/template/saveUpdate', saveModel)
        } catch (e) {
          errorMessage(e)
        } finally {
          saveLoading.value = false
        }
        addEditModalVisible.value = false
        loadData()
      })
  }
  /**
   * 当前行变更事件
   */
  const handleCurrentChange = ({ row }: any) => {
    currentRow.value = row
  }
  /**
   * 显示edit操作
   */
  const handleShowEdit = async (id: number) => {
    isAdd.value = false
    isShow.value = false
    addEditModalVisible.value = true
    // 加载数据 TODO:加载状态
    formModel.value = await ApiService.postAjax('db/code/template/getById', id)
    codemirror.value.setCode(formModel.value.template)
  }
  /**
   * 显示模板
   */
  const handleShowTemplate = async (id: number) => {
    isShow.value = true
    addEditModalVisible.value = true
    formModel.value = await ApiService.postAjax('db/code/template/getById', id)
    codemirror.value.setCode(formModel.value.template)
  }
  return {
    addEditModalVisible,
    handleShowTemplate,
    isAdd,
    isShow,
    handleShowAdd () {
      isShow.value = false
      isAdd.value = true
      addEditModalVisible.value = true
      formModel.value = Object.assign({}, defaultSaveModel)
      if (codemirror.value) {
        codemirror.value.setCode('')
      }
    },
    handleShowEdit,
    handleSave,
    saveLoading,
    saveFormRef,
    formModel,
    formRules,
    codemirror,
    code,
    extensionLanguageMap: reactive(extensionLanguageMap),
    handleCurrentChange,
    currentRow
  }
}
