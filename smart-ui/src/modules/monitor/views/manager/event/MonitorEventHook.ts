import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const searchSymbol: any = {
	applicationCode: '=',
	clientId: '=',
	eventCode: '='
}

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
	const searchWithSymbol: any = {}
	Object.keys(searchParameter).forEach(key => {
		if (searchSymbol[key]) {
			searchWithSymbol[`${key}@${searchSymbol[key]}`] = searchParameter[key]
		} else {
			searchWithSymbol[key] = searchParameter[key]
		}
	})
  try {
		return await ApiService.postAjax('monitor/manager/event/list', {
			...params,
			parameter: searchWithSymbol
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
		return await ApiService.postAjax('monitor/manager/event/getById', id)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}

