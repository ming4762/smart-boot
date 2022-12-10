import { genMessage, generateModuleMessage } from '../helper'
import antdLocale from 'ant-design-vue/es/locale/en_US'
import vxeEnUS from 'vxe-table/es/locale/lang/en-US'
import { deepMerge } from '/@/utils'

const modules = import.meta.globEager('./en/**/*.ts')
const modulesLocales = import.meta.globEager('../../modules/**/lang/en_US.ts')
export default {
  message: {
    ...deepMerge(genMessage(modules, 'en'), generateModuleMessage(modulesLocales)),
    antdLocale,
    ...vxeEnUS,
  },
  dateLocale: null,
  dateLocaleName: 'en',
}
