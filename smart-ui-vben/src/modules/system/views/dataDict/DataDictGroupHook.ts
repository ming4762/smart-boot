import ApiService from '/@/common/utils/ApiService'

/**
 * 通过ID查询
 * @param id ID
 */
export const handleGetById = async (id: string) => {
  return await ApiService.postAjax('sys/dict/getById', { id: id })
}

/**
 * 添加保存函数
 * @param model 添加保存参数
 */
export const handleSaveUpdate = async (model: any) => {
  await ApiService.postAjax('sys/dict/saveUpdate', model)
}

/**
 * 删除函数
 * @param idList ID列表
 */
export const handleDelete = async (idList: Array<any>) => {
  await ApiService.postAjax('sys/dict/batchDeleteById', idList, { errorMessageMode: 'modal' })
}
