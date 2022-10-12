import { reactive } from 'vue'
import { storeToRefs } from 'pinia'
import { useI18n } from 'vue-i18n'

import { useAppI18nStore } from '@/store/modules/AppStore2'

import defaultSetting from '@/config/defaultSetting'

export default () => {
  const appI18nStore = useAppI18nStore()
  const { lang } = storeToRefs(appI18nStore)
  const i18n = useI18n()
  // 当前语言
  const currentLang = lang
  /**
   * 设置语言
   * @param lang 语言
   */
  const setLang = (lang: string) => {
    i18n.locale.value = lang
    appI18nStore.setLang(lang)
  }
  const languageList = reactive(defaultSetting.languageList)
  return {
    currentLang,
    setLang,
    languageList
  }
}

type I18nTransfer = {
  trans: boolean
  key: string
  data: { [index: string]: any }
}

/**
 * 转换国际化信息
 */
export const transferI18n = (data: I18nTransfer | any) => {
  if (!data.trans) {
    return data
  }
  const keySplit = data.key.split('.')
  let object = {}
  for (let i = keySplit.length - 1; i >= 0; i--) {
    const key = keySplit[i]
    const itemData: any = {}
    if (i === keySplit.length - 1) {
      itemData[key] = data.data
    } else {
      itemData[key] = object
    }
    object = itemData
  }
  return object
}
