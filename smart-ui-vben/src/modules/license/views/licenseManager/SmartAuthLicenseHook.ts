import ApiService from '/@/common/utils/ApiService'
import { errorMessage } from '/@/common/utils/SystemNotice'
import dayjs from 'dayjs'
import { message, Modal } from 'ant-design-vue'
import { createVNode } from 'vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

const searchSymbol: { [index: string]: string } = {
  licenseCode: 'like',
  version: '=',
  status: '=',
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
    return await ApiService.postAjax('/smart/license/list', {
      ...params,
      parameter: searchWithSymbol,
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
    const result = await ApiService.postAjax('/smart/license/getById', id)
    return {
      ...result,
      times: [dayjs(result.effectiveTime), dayjs(result.expirationTime)],
    }
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
  const { times } = model
  const parameter = {
    ...model,
    effectiveTime: times[0],
    expirationTime: times[1],
  }
  delete parameter.times
  return ApiService.postAjax('/smart/license/saveUpdate', parameter)
}

/**
 * 删除函数
 * @param idList ID列表
 */
export const handleDelete = async (idList: Array<any>) => {
  try {
    await ApiService.postAjax('/smart/license/batchDeleteById', idList)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

export const useGeneratorLicense = (t: Function, afterHandler: Function) => {
  const handleGenerator = async (id: number, status: string) => {
    let i18nMessage = 'smart.license.message.generatorConfirm'
    if (status === 'GENERATOR') {
      i18nMessage = 'smart.license.message.reGeneratorConfirm'
    }
    Modal.confirm({
      content: t(i18nMessage),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        await ApiService.postAjax('smart/license/generator', id)
        message.success(t('smart.license.message.generatorSuccess'))
        afterHandler && afterHandler()
      },
    })
  }

  return {
    handleGenerator,
  }
}
