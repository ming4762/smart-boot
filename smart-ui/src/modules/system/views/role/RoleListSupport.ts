import { ref, onMounted, reactive } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { message } from 'ant-design-vue'

import { errorMessage } from '@/components/notice/SystemNotice'

const defaultSearchModel = {
  roleName: '',
  roleCode: '',
  roleType: ''
}

/**
 * 加载用户数据vue支持
 */
export const vueLoadRoleData = () => {
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
  /**
   * 加载数据函数
   */
  const loadRoleList = async () => {
    // 构建参数
    const allParameter: any = {
      limit: tablePage.pageSize,
      page: tablePage.currentPage
    }
    // 自定义参数
    const customParameter: any = {
      QUERY_CREATE_UPDATE_USER: true
    }
    Object.keys(searchModel.value).forEach((key) => {
      const value = searchModel.value[key]
      if (value !== null && value !== '') {
        customParameter[key + '@like'] = value
      }
    })
    allParameter.parameter = customParameter
    try {
      loading.value = true
      const { rows, total } = await ApiService.postAjax('sys/role/list', allParameter)
      tablePage.total = total
      data.value = rows
    } catch (e) {
      errorMessage(e)
    } finally {
      loading.value = false
    }
  }
  /**
   * 分页触发事件
   */
  const handlePageChange = ({ currentPage, pageSize }: any) => {
    tablePage.currentPage = currentPage
    tablePage.pageSize = pageSize
    loadRoleList()
  }
  /**
   * 重置搜索表单
   */
  const resetSearch = () => {
    searchModel.value = Object.assign({}, defaultSearchModel)
  }
  onMounted(loadRoleList)
  return {
    data,
    loading,
    searchModel,
    loadRoleList,
    resetSearch,
    tablePage,
    handlePageChange
  }
}

/**
 * 添加修改编码验证规则
 */
const addEditFormRules = {
  roleName: [{ required: true, trigger: 'blur', message: '请输入角色名称' }],
  roleCode: [{ required: true, trigger: 'blur', message: '请输入角色编码' }]
}

const defaultAddEditModel = {
  useYn: true,
  seq: 1
}

/**
 * 添加修改支持
 */
export const vueAddUpdate = (loadData: any) => {
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
   * 保存操作
   */
  const handleSave = async () => {
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/role/saveUpdate', addEditModel.value)
      addEditModalVisible.value = false
      loadData()
    } finally {
      saveLoading.value = false
    }
  }
  /**
   * 编辑操作
   * @param roleId
   */
  const handleShowEdit = async (roleId: number) => {
    isAdd.value = false
    addEditModalVisible.value = true
    getLoading.value = true
    try {
      addEditModel.value = await ApiService.postAjax('sys/role/getById', roleId)
    } finally {
      getLoading.value = false
    }
  }
  /**
   * 添加操作
   */
  const handleShowAdd = () => {
    addEditModel.value = Object.assign({}, defaultAddEditModel)
    isAdd.value = true
    addEditModalVisible.value = true
  }
  return {
    saveLoading,
    getLoading,
    addEditModalVisible,
    handleShowAdd,
    isAdd,
    addEditModel,
    formRules: reactive(addEditFormRules),
    handleSave,
    handleShowEdit
  }
}

/**
 * 设置用户支持
 */
export const vueSetUser = () => {
  const setUserModalVisible = ref(false)
  const getUserLoading = ref(false)
  const setUserLoading = ref(false)
  // 所有用户数据
  const allUserData = ref<Array<any>>([])
  const targetKeysModel = ref<Array<string>>([])
  let currentRoleId: number | null = null

  /**
   * 设置用户弹窗打开
   * @param roleId
   */
  const handleShowSetUser = async (roleId: number) => {
    currentRoleId = roleId
    setUserModalVisible.value = true
    getUserLoading.value = true
    try {
      if (allUserData.value.length === 0) {
        const result: Array<any> = await ApiService.postAjax('sys/user/list', {
          sortName: 'seq',
          parameter: {
            'useYn@=': true
          }
        })
        allUserData.value = result.map(({ userId, fullName, username }: any) => {
          return {
            key: userId + '',
            title: `${fullName}[${username}]`
          }
        })
      }
      // 查询角色对应的用户信息
      const result: Array<any> = await ApiService.postAjax('sys/user/listUserByRoleId', [roleId])
      targetKeysModel.value = result.map((item) => item.userId + '')
    } finally {
      getUserLoading.value = false
    }
  }
  /**
   * 设置用户
   */
  const handleSetUser = async () => {
    if (currentRoleId == null) {
      throw new Error('系统发生错误')
    }
    setUserLoading.value = true
    try {
      await ApiService.postAjax('sys/role/setRoleUser', {
        roleId: currentRoleId,
        userIdList: targetKeysModel.value
      })
      message.success('保存成功')
      setUserModalVisible.value = false
    } finally {
      setUserLoading.value = false
    }
  }
  const handleTransChange = (targetKeyList: Array<string>) => {
    targetKeysModel.value = targetKeyList
  }
  return {
    setUserModalVisible,
    handleShowSetUser,
    getUserLoading,
    setUserLoading,
    handleSetUser,
    targetKeysModel,
    handleTransChange,
    allUserData
  }
}
