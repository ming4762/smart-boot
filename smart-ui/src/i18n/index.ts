import { createI18n } from 'vue-i18n'
import { merge } from 'xe-utils'
import store from '@/store'

// ant design国际化
import zhCN from 'ant-design-vue/es/locale-provider/zh_CN'
import enUS from 'ant-design-vue/es/locale/en_US'

// vxe-table 国际化
import vxeTableZhCN from 'vxe-table/lib/locale/lang/zh-CN'
import vxeTableEnUS from 'vxe-table/lib/locale/lang/en-US'

// 系统模块国际化
import systemI18n from '../modules/system/i18n'
// 代码生成器模块国际化
import generatorI18n from '@/modules/generator/i18n'

// app 模块
import appI18n from '@/modules/app/i18n'
// 监控模块
import monitorI18n from '@/modules/monitor/i18n'

/**
 * 创建I18N
 */
const i18n = createI18n({
  // locale: localStorage.getItem('smart_locale') || DefaultSetting.lang,
  locale: store.getters['app/lang'],
  messages: {
    'zh-CN': merge({},
      systemI18n.zh_CN,
      appI18n.zh_CN,
      generatorI18n.zh_CN,
      monitorI18n.zh_CN,
      vxeTableZhCN,
      { antLocale: zhCN }),
    'en-US': merge({},
      systemI18n.en_US,
      appI18n.en_US,
      generatorI18n.en_US,
      monitorI18n.en_US,
      vxeTableEnUS,
      { antLocale: enUS })
  }
})


export default i18n
