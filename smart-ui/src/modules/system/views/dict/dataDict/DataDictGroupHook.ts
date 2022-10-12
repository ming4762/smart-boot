import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

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
