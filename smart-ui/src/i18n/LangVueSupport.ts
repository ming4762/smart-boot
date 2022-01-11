import { computed, reactive } from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'

import defaultSetting from '@/config/defaultSetting'

export default () => {
  const store = useStore()
  const i18n = useI18n()
  // 当前语言
  const currentLang = computed(() => store.state['app/lang'])
  /**
   * 设置语言
   * @param lang 语言
   */
  const setLang = (lang: string) => {
    i18n.locale.value = lang
    store.dispatch('app/setLang', lang)
  }
  const languageList = reactive(defaultSetting.languageList)
  return {
    currentLang,
    setLang,
    languageList
  }
}
