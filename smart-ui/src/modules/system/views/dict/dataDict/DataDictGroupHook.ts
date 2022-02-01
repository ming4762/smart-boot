import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
  console.log(searchParameter)
  const parameter: any = {}
  Object.keys(searchParameter).forEach(key => {
    let symbol = 'like'
    if (key === 'useYn') {
      symbol = '='
    }
    parameter[`${key}@${symbol}`] = searchParameter[key]
  })
  try {
    return await ApiService.postAjax('sys/dict/list', {
      ...params,
      parameter
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
export const handleGetById = async (id: string) => {
  try {
    return await ApiService.postAjax('sys/dict/getById', { id: id })
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
    await ApiService.postAjax('sys/dict/saveUpdate', model)
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
    await ApiService.postAjax('sys/dict/batchDeleteById', idList)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}
