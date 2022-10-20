import { useAppStateStore } from '@/modules/app/store'

export default class AppUtils {
  /**
   * 全局加载状态
   * @param loading 状态
   */
  public static globalLoading(loading: boolean): void {
    const appStateStore = useAppStateStore()
    appStateStore.setGlobalLoading(loading)
  }
}
