import axios from 'axios'

const Accept_Language = 'Accept-Language'

/**
 * 获取API地址
 */
const getApiUrl = (): string => {
  return localStorage.getItem('API_URL') || (import.meta.env.VITE_API_URL as string)
}

// axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

export const API_SERVICE = axios.create({
  baseURL: getApiUrl(),
  timeout: 100000
})

/**
 * API工具类
 */
export default class ApiService {
  public static POST = 'POST'

  public static GET = 'GET'

  private static apiLang: string

  /**
   * 获取后台地址
   */
  public static getApiUrl(url?: string): string {
    if (url) {
      return getApiUrl() + url
    }
    return getApiUrl()
  }

  public static getLang(): string | null {
    return this.apiLang
  }

  public static setLang(lang: string) {
    this.apiLang = lang
  }

  /**
   * 发送ajax请求
   * @param url 请求地址
   * @param ajaxType 请求类型
   * @param parameter 参数
   * @param customParameter 自定义参数
   */
  public static ajax(url: string, ajaxType: string, parameter?: { [index: string]: any }, customParameter?: { [index: string]: any }) {
    const params = customParameter || {}
    if (!params.headers) {
      params.headers = {}
      if (!params.headers['Content-Type']) {
        params.headers['Content-Type'] = 'application/json;charset=UTF-8'
      }
    }
    return this.request(url, ajaxType, parameter, params)
      .then((result: any) => {
        const data = result.data
        if (data.success === false) {
          return Promise.reject(data)
        }
        return data.data
      })
  }

  /**
   * 下载函数
   * @param url 请求地址
   * @param ajaxType 请求类型
   * @param parameter 参数
   * @param customParameter 自定义参数
   */
  public static download (url: string, ajaxType: string, parameter?: {[index: string]: any}, customParameter?: {[index: string]: any}) {
    return this.request(url, ajaxType, parameter, customParameter)
      .then((result: any) => {
        return result.data
      })
  }

  /**
   * 发送ajax请求
   * @param url 请求地址
   * @param method 请求类型
   * @param parameter 参数
   * @param customParameter 自定义参数
   * @private
   */
  public static request(url: string, method: string, parameter?: {[index: string]: any}, customParameter?: {[index: string]: any}) {
    const serverParameter: any = Object.assign({
      method: method,
      url: url,
      data: parameter || {},
      headers: {},
      validateStatus: (status: number) => status >= 200 && status < 300
    }, customParameter)

    const lang = this.getLang()
    if (lang) {
      serverParameter.headers[Accept_Language] = lang
    }
    return API_SERVICE(serverParameter)
  }

  /**
   * 发送post请求
   * @param url
   * @param parameter
   * @param customParameter
   */
  public static postAjax(url: string, parameter?: any, customParameter?: any) {
    return this.ajax(url, this.POST, parameter, customParameter)
  }

  /**
   * 发送get请求
   * @param url
   * @param parameter
   * @param customParameter
   */
  public static getAjax(url: string, parameter?: any, customParameter?: any) {
    return this.ajax(url, this.GET, parameter, customParameter)
  }

  /**
   * 发送FORM请求
   * @param url
   * @param parameter
   * @param customParameter
   */
  public static postForm(url: string, parameter?: any, customParameter?: any) {
    // 创建formData
    const formData = new FormData()
    if (parameter) {
      Object.keys(parameter).forEach((key) => {
        const value = parameter[key]
        if (value) {
          formData.append(key, value)
        }
      })
    }
    // 创建请求参数
    const serverParameter = Object.assign({
      method: this.POST,
      url: url,
      headers: {},
      data: formData,
      validateStatus: (status: number) => status >= 200 && status < 300
    }, customParameter)
    const lang = this.getLang()
    if (lang) {
      serverParameter.headers[Accept_Language] = lang
    }
    return API_SERVICE(serverParameter)
      .then((result: any) => {
        const data = result.data
        if (data.success === false) {
          return Promise.reject(data)
        }
        return data.data
      })
  }

  /**
   * 上传文件
   * @param url 文件URL
   * @param fileList 文件列表
   * @param parameter 参数信息
   * @param customParameter 自定义参数信息
   */
  public static postWithFiles(url: string, fileList: Array<File>, parameter?: any, customParameter?: any) {
    // 创建formData
    const formData = new FormData()
    fileList.forEach((file) => {
      formData.append('files', file)
    })
    // 添加其他参数
    if (parameter) {
      Object.keys(parameter)
        .forEach(key => {
          if (parameter[key])
            formData.append(key, parameter[key])
        })
    }
    // 创建请求参数
    const serverParameter = Object.assign({
      method: this.POST,
      url: url,
      headers: {},
      data: formData,
      validateStatus: (status: number) => status >= 200 && status < 300
    }, customParameter)

    const lang = this.getLang()
    if (lang) {
      serverParameter.headers[Accept_Language] = lang
    }
    return API_SERVICE(serverParameter)
      .then((result: any) => {
        const data = result.data
        if (data.success === false) {
          return Promise.reject(data)
        }
        return data.data
      })
  }
}
