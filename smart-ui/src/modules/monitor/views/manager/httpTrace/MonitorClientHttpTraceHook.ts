import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const searchSymbol: {[index: string]: string} = {
	applicationCode: '=',
	clientId: '=',
	httpMethod: '=',
	responseStatus: '=',
	timestamp: '='
}

const timestampKey = 'timestamp'

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
	const searchWithSymbol: any = {}
	Object.keys(searchParameter).forEach(key => {
		if (key === timestampKey) {
			const timeData: Array<any> = searchParameter[key]
			if (timeData && timeData.length > 0) {
				searchWithSymbol[`${timestampKey}@>=`] = timeData[0].toDate().getTime()
				searchWithSymbol[`${timestampKey}@<=`] = timeData[1].toDate().getTime()
			}
		} else if (searchSymbol[key]) {
			searchWithSymbol[`${key}@${searchSymbol[key]}`] = searchParameter[key]
		} else {
			searchWithSymbol[key] = searchParameter[key]
		}
	})
	try {
		return await ApiService.postAjax('/monitor/client/httpTrace/listByCurrentUserNoDetail', {
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
		await ApiService.postAjax('/monitor/client/httpTrace/batchDeleteById', idList)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
