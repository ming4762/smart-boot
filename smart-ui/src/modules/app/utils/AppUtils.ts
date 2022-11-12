import { useAppSettingStore, useAppStateStore } from '../store/index'
/**
 * 是否显示tab
 */
export const hasTab = (): boolean => {
  const appSettingStore = useAppSettingStore()

  return appSettingStore.hasMultiTab
}

/**
 * 开启关闭全局加载状态
 * @param loading 加载状态
 */
export const globalLoading = (loading: boolean) => {
  const appStateStore = useAppStateStore()
  appStateStore.setGlobalLoading(loading)
}
