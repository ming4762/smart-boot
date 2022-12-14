import { defHttp } from '/@/utils/http/axios'
import { RequestOptions } from '/#/axios'

export default class ApiService {
  public static postAjax<T = any>(url, data: any = {}, options?: RequestOptions): Promise<T> {
    return defHttp.post<T>(
      {
        url,
        data,
      },
      options,
    )
  }

  public static getApiUrl(): string {
    return defHttp.getApiUrl()
  }
}
