import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const equalField = ['useYn']

/**
 * 加载数据
 * @param params 参数
 * @param searchParameter 查询参数
 */
export const handleLoadData = async (params: any, searchParameter: any) => {
  try {
		const parameter: any = {}
		Object.keys(searchParameter).forEach(key => {
			const value = searchParameter[key]
			if (value != undefined) {
				if (equalField.includes(key)) {
					parameter[`${key}@=`] = value
				} else {
					parameter[`${key}@like`] = value
				}
			}
		})
		return await ApiService.postAjax('monitor/manager/application/list', {
			...params,
			parameter: {
				...parameter,
				QUERY_CREATE_UPDATE_USER: true
			}
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
		return await ApiService.postAjax('monitor/manager/application/getById', id)
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
		await ApiService.postAjax('monitor/manager/application/saveUpdate', model)
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
		await ApiService.postAjax('monitor/manager/application/batchDeleteById', idList)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
