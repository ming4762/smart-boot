import type { App } from 'vue'
import { i18n } from '/@/locales/setupI18n'

import 'xe-utils'
import { VXETable, Table } from 'vxe-table'

export default (app: App) => {
  // @ts-ignore
  VXETable.setup({
    // @ts-ignore
    i18n: (key, args) => i18n.global.t(key, args),
    translate(key: string, args?: any): string {
      if (key.startsWith('{') && key.endsWith('}')) {
        const i18nKey = key.replace('{', '').replace('}', '')
        // @ts-ignore
        return i18n.global.t(i18nKey, args)
      }
      return key
    },
  })
  app.use(Table)
}
