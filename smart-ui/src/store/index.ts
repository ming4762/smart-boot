import { createPinia, PiniaPluginContext } from 'pinia'
import { toRaw } from 'vue'

import StoreUtil from '@/common/utils/StoreUtil'

/**
 * 需要持久化的 store
 */
const STORAGE_STORE_LIST: {[index: string]: string | null} = {
  'systemMenu': StoreUtil.SESSION_TYPE,
  'appSetting': null,
  'appI18n': null,
  'appState': null
}


type Options = {
  key: string
}

const storagePlugin = (options: Options) => {
  console.log(options)
  return ({ store }: PiniaPluginContext) => {
    const id = store.$id
    if (Object.keys(STORAGE_STORE_LIST).includes(id)) {
      const key = `${options.key}_${id}`
      /**
       * 订阅更改
       */
      store.$subscribe(() => {
        StoreUtil.setStore(key, toRaw(store.$state), STORAGE_STORE_LIST[id])
      })
      const data = StoreUtil.getStore(key)
      return {
        ...data
      }
    }
  }
}

const store = createPinia()

store.use(storagePlugin({
  key: 'smart_store'
}))

export default store
