import { defHttp } from '/@/utils/http/axios'

export default class ApiService {
  public static postAjax<T = any>(url, data: any = {}): Promise<T> {
    return defHttp.post<T>({
      url,
      data,
    })
  }
}
