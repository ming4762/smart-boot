import ApiService from '@/common/utils/ApiService'

/**
 * 端点API
 * @param clientId 客户端ID
 * @param type 端点类型
 * @param urlHandler URL处理器
 * @param method 请求函数
 * @param parameter 参数
 */
export const loadActuator = (
  clientId: string,
  type: string,
  urlHandler: Function | undefined | null = (url: string): string => url,
  method = 'GET',
  parameter?: {[index: string]: any}
): Promise<any | void> => {
  let url = `/monitor/client/${clientId}/actuator/${type}`
  url = urlHandler ? urlHandler(url) : url

  return ApiService.ajax(url, method, parameter)
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
