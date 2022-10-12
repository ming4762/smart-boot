import ApiService from '@/common/utils/ApiService'

/**
 * 端点API
 * @param clientId 客户端ID
 * @param type 端点类型
 * @param urlHandler URL处理器
 * @param method 请求函数
 * @param parameter 参数
 * @param customParameter
 */
export const loadActuator = (
  clientId: string,
  type: string,
  urlHandler: Function | undefined | null = (url: string): string => url,
  method = 'GET',
  parameter?: { [index: string]: any },
  customParameter?: { [index: string]: any }
): Promise<any | void> => {
  let url = `/monitor/client/${clientId}/actuator/${type}`
  url = urlHandler ? urlHandler(url) : url

  return ApiService.ajax(url, method, parameter, customParameter)
}

/**
 * 下载接口
 * @param clientId 客户端ID
 * @param type 端点类型
 * @param urlHandler URL处理器
 * @param method 请求函数
 * @param parameter 参数
 * @param customParameter
 */
export const downActuator = (
  clientId: string,
  type: string,
  urlHandler: Function | undefined | null = (url: string): string => url,
  method = 'GET',
  parameter?: { [index: string]: any },
  customParameter?: { [index: string]: any }
): Promise<any | void> => {
  let url = `/monitor/clientDownload/${clientId}/actuator/${type}`
  url = urlHandler ? urlHandler(url) : url
  return ApiService.download(url, method, parameter, customParameter)
}

/**
 * 获取请求的URL
 * @param clientId 客户端ID
 * @param type 端点类型
 */
export const getActuatorUrl = (clientId: string, type: string): string => {
  return `monitor/client/${clientId}/actuator/${type}`
}

/**
 * 加载所有端点
 * @param clientId 客户端ID
 * @param baseUrl 端点URL
 */
export const loadActuatorList = (clientId: string, baseUrl = 'actuator'): Promise<any> => {
  const url = `/monitor/client/${clientId}/${baseUrl}`
  return ApiService.ajax(url, 'GET')
}
