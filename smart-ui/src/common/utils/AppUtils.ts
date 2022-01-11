import store from '@/store'

import { STORE_APP_MUTATION } from '@/common/constants/CommonConstants'

export default class AppUtils {

  /**
   * 全局加载状态
   * @param loading 状态
   */
  public static globalLoading (loading: boolean): void {
    store.commit(`app/${STORE_APP_MUTATION.GLOBAL_LOADING}`, loading)
  }
}
