import ApiService from '@/common/utils/ApiService'
import {errorMessage} from '@/components/notice/SystemNotice'
import {applyTempToken} from '@/common/auth/AuthUtils'

const searchSymbol: {[index: string]: string} = {
	applicationCode: 'likeLeft',
	clientId: 'likeLeft',
	timestamp: '=',
	level: '='
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
		if (key === 'timestamp') {
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
		return await ApiService.postAjax('monitor/client/log/listFilterUser', {
			...params,
			parameter: searchWithSymbol
		})
  } catch (e) {
		errorMessage(e)
		throw e
  }
}

/**
 * 执行下载函数
 * @param searchParameter 搜索函数
 */
export const handleDownload = async (searchParameter: any) => {

	const parameter: any = {}
	Object.keys(searchParameter).forEach(key => {
		const data = searchParameter[key]
		if (key === 'timestamp') {
			if (data && data.length > 0) {
				searchParameter.startTime = data[0]
				searchParameter.endTime = data[1]
			}
		} else {
			if (data && data !== '') {
				parameter[key] = searchParameter[key]
			}
		}
	})
	try {
		parameter['access-token'] = await applyTempToken('monitor:client:log')
	} catch (e) {
		errorMessage(e)
		return false
	}
	// 拼接URL
	const parameterStr = Object.keys(parameter).map(key => {
		return `${key}=${parameter[key]}`
	}).join('&')
	const url = `${ApiService.getApiUrl()}public/monitor/downloadFilterUser${parameterStr.length > 0 ? '?' + parameterStr : ''}`
	window.open(url)
}

