<template>
  <a-config-provider :locale="locale">
    <a-spin :spinning="globalLoading">
      <router-view />
    </a-spin>
  </a-config-provider>
</template>

<script lang="ts">
import { defineComponent, computed, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

import { domTitle, setDocumentTitle } from '@/common/utils/domUtil'
import ApiService from '@/common/utils/ApiService'

const domTitleVueSupport = (route: any, locale: any, i18nRender: Function) => {
  watch([route, locale], () => {
    const matchedList = route.matched
    let matchedMeta = null
    for (let i = matchedList.length - 1; i>= 0; i--) {
      const matched = matchedList[i]
      if (matched.meta.locales) {
        matchedMeta = matched.meta
        break
      }
    }
    if (matchedMeta) {
      setDocumentTitle(`${matchedMeta.locales[locale.value]} - ${domTitle}`)
    } else {
      let metaTitle = null
      const matchedList = route.matched
      for (let i = matchedList.length - 1; i>= 0; i--) {
        const matched = matchedList[i]
        if (matched.meta.title) {
          metaTitle = matched.meta.title
          break
        }
      }
      if (metaTitle) {
        let routeTitle = metaTitle as string
        if (routeTitle.startsWith('{') && routeTitle.endsWith('}')) {
          routeTitle = routeTitle.replace('{', '').replace('}', '')
          routeTitle = i18nRender(routeTitle)
        }
        setDocumentTitle(`${routeTitle} - ${domTitle}`)
      } else {
        setDocumentTitle(domTitle)
      }
    }
  })
}

export default defineComponent({
  setup () {
    const i18n = useI18n()
    const route = useRoute()
    const store = useStore()
    const computedLocale = computed(() => {
      return i18n.getLocaleMessage(store.getters['app/lang']).antLocale
    })
    domTitleVueSupport(route, i18n.locale, i18n.t)

    // 动态设置语言信息
    const computedLang = computed(() => store.getters['app/lang'])
    ApiService.setLang(computedLang.value)
    watch(computedLang, () => {
      ApiService.setLang(computedLang.value)
    })

    return {
      locale: computedLocale,
      globalLoading: computed(() => store.getters['app/globalLoading'])
    }
  },
  watch: {
    globalLoading () {
      console.log(this.globalLoading)
    }
  }
})
</script>

<style lang="less" scoped>
#app {
  .ant-spin-nested-loading {
    height: 100%;
    ::v-deep(.ant-spin-container) {
      height: 100%;
    }
  }
}
</style>
