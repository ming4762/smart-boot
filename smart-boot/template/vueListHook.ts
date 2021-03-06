import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const searchSymbol: {[index: string]: string} = {
	<#list mainTable.codeSearchConfigList as item>
	${item.javaProperty}: '${item.searchSymbol}'<#sep>,
	</#list>

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
			searchWithSymbol[`${r'${key}@${searchSymbol[key]}'}`] = searchParameter[key]
		} else {
			searchWithSymbol[key] = searchParameter[key]
		}
	})
	try {
		return await ApiService.postAjax('${controllerBasePath}list', {
			...params,
			parameter: searchWithSymbol
		})
  } catch (e) {
		errorMessage(e)
		throw e
  }
}


<#if (mainTable.codeFormConfigList?size>0)>
/**
 * 通过ID查询
 * @param id ID
 */
export const handleGetById = async (id: number) => {
	try {
		return await ApiService.postAjax('${controllerBasePath}getById', id)
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
		await ApiService.postAjax('${controllerBasePath}saveUpdate', model)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
</#if>


/**
 * 删除函数
 * @param idList ID列表
 */
export const handleDelete = async (idList: Array<any>) => {
	try {
		await ApiService.postAjax('${controllerBasePath}batchDeleteById', idList)
	} catch (e) {
		errorMessage(e)
		throw e
	}
}
