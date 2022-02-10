import { onBeforeUnmount } from 'vue'

import TimeTaskUtil from '@/common/utils/TimeTaskUtil'

/**
 * 存储handler
 */
const handlerMap: Map<string, Function> = new Map<string, Function>()

/**
 * 解除刷新
 * @param key
 */
export const relieveRefresh = (key: string) => {
  handlerMap.delete(key)
}

/**
 * 添加刷新
 * @param key
 * @param handler
 */
export const addRefresh = (key: string, handler: Function) => {
  handlerMap.set(key, handler)
  onBeforeUnmount(() => {
    relieveRefresh(key)
  })
}

let lookup: number | null = null

/**
 * 执行handler
 */
export const executeHandler = () => {
  handlerMap.forEach((handler) => {
    handler()
  })
}

export const changeRefresh = (refreshTime: number) => {
  if (lookup !== null) {
    clearInterval(lookup)
  }
  if (refreshTime !== -1) {
    lookup = TimeTaskUtil.loop(() => {
      executeHandler()
    }, -1, refreshTime * 1000)
  }
}

export const closeRefresh = () => {
  if (lookup !== null) {
    clearInterval(lookup)
  }
}
