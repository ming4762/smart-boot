import { createApp } from 'vue'
import type { App } from 'vue'
import AppVue from './App.vue'

import './style/index.less'

import Antd from 'ant-design-vue'
// 引入antd css
import 'ant-design-vue/dist/antd.variable.min.css'

import router from './router'
import i18nCreator from './i18n'

import systemInit from '@/modules/system/SystemInit'

import store from './store'

import themePluginConfig from './config/themePluginConfig'
// @ts-ignore
window['umi_plugin_ant_themeVar'] = themePluginConfig.theme

// 引入vxe-table
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

import { useAppI18nStore } from '@/store/modules/AppStore2'

const app = createApp(AppVue)

app.use(store)

// 初始化系统模块
systemInit(app)

const appI18nStore = useAppI18nStore()

const i18n = i18nCreator(appI18nStore)

/**
 * vxe-table国际化
 */
VXETable.setup({
  // @ts-ignore
  i18n: (key, args) => i18n.global.t(key, args),
  translate(key: string, args?: any): string {
    if (key.startsWith('{') && key.endsWith('}')) {
      const i18nKey = key.replace('{', '').replace('}', '')
      return i18n.global.t(i18nKey, args)
    }
    return key
  }
})
const useVxe = (app: App) => {
  app.use(VXETable)
}

app.use(router)
  .use(Antd)
  .use(useVxe)
  .use(i18n)
  .mount('#app')
