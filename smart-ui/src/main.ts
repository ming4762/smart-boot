import { createApp, App } from 'vue'
import AppVue from './App.vue'

import './style/index.less'

import Antd from 'ant-design-vue'
// 引入antd css
import 'ant-design-vue/dist/antd.css'

import router from './router'
import i18n from './i18n'
import store from './store'

import systemInit from '@/modules/system/SystemInit'

import themePluginConfig from './config/themePluginConfig'
// @ts-ignore
window['umi_plugin_ant_themeVar'] = themePluginConfig.theme

// 引入vxe-table
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

const i18nStartList = ['generator.', 'system.', 'common.']

VXETable.setup({
  i18n: (key, args) => i18n.global.t(key, args),
  translate(key: string, args?: any): string {
    if (i18nStartList.some(item => key.startsWith(item))) {
      return i18n.global.t(key, args)
    }
    return key
  }
})
const useVxe = (app: App) => {
  app.use(VXETable)
}

const app = createApp(AppVue)

// 初始化系统模块
systemInit(app)

app.use(router)
  .use(Antd)
  .use(useVxe)
  .use(i18n)
  .use(store)
  .mount('#app')
