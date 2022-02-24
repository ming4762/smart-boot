import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const searchSymbol: {[index: string]: string} = {
	applicationCode: 'like',
	clientId: 'like',
	datasourceName: 'like'
}

const timestampKey = 'timestamp'

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
	console.log('==========')
	const searchWithSymbol: any = {}
	Object.keys(searchParameter).forEach(key => {
		if (key === timestampKey) {
			const data: Array<any> = searchParameter[key]
			if (data && data.length > 0) {
				searchWithSymbol[`${timestampKey}@>=`] = data[0].toDate().getTime()
				searchWithSymbol[`${timestampKey}@<=`] = data[1].toDate().getTime()
			}
		} else if (searchSymbol[key]) {
			searchWithSymbol[`${key}@${searchSymbol[key]}`] = searchParameter[key]
		} else {
			searchWithSymbol[key] = searchParameter[key]
		}
	})
	try {
		return await ApiService.postAjax('/monitor/slowSql/list', {
			...params,
			parameter: {
				...searchWithSymbol,
				FILTER_BY_USER: true
			}
		})
  } catch (e) {
		errorMessage(e)
		throw e
  }
}
