import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const searchSymbol: { [index: string]: string } = {
  fileName: 'like',
  type: '=',
  handlerType: 'like'
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
    return await ApiService.postAjax('sys/file/list', {
      ...params,
      parameter: searchWithSymbol
    })
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
    await ApiService.postAjax('sys/file/batchDeleteById', idList)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}
