import type { Ref } from 'vue'

import { message } from 'ant-design-vue'

import ApiService from '/@/common/utils/ApiService'
import { errorMessage } from '/@/common/utils/SystemNotice'

const searchSymbol: { [index: string]: string } = {
  deptCode: '=',
  deptName: 'like',
}

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
  const searchWithSymbol: any = {}
  Object.keys(searchParameter).forEach((key) => {
    if (searchSymbol[key]) {
      searchWithSymbol[`${key}@${searchSymbol[key]}`] = searchParameter[key]
    } else {
      searchWithSymbol[key] = searchParameter[key]
    }
  })
  try {
    return await ApiService.postAjax('sys/dept/list', {
      ...params,
      parameter: searchWithSymbol,
    })
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

/**
 * 通过ID查询
 * @param id ID
 */
export const handleGetById = async (id: number) => {
  try {
    return await ApiService.postAjax('sys/dept/getById', id)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

/**
 * 添加保存函数
 * @param model 添加保存参数
 */
export const handleSaveUpdate = async (model: any) => {
  try {
    await ApiService.postAjax('sys/dept/saveUpdate', model)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

/**
 * 删除函数
 * @param idList ID列表
 */
export const handleDelete = async (idList: Array<any>) => {
  try {
    await ApiService.postAjax('sys/dept/batchDeleteById', idList)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

export const useAddChild = (
  t: Function,
  setModel: Function,
  addEditHandler: Function,
  currentDeptCode: Ref<number | null>,
  parentFieldVisible: Ref<boolean>,
) => {
  /**
   * 添加下级函数
   */
  const handleAddChild = () => {
    if (currentDeptCode.value === null) {
      message.error(t('system.views.dept.message.selectDeptError'))
      return false
    }
    // 设置上级编码
    parentFieldVisible.value = true
    addEditHandler(true, null)
    setModel({
      parentId: currentDeptCode.value,
      seq: 1,
    })
  }
  return {
    handleAddChild,
  }
}
