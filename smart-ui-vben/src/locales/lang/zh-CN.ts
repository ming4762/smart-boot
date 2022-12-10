import { generateModuleMessage, genMessage } from '../helper'
import antdLocale from 'ant-design-vue/es/locale/zh_CN'
import vxeZhCN from 'vxe-table/es/locale/lang/zh-CN'
import { deepMerge } from '/@/utils'

const modules = import.meta.globEager('./zh-CN/**/*.ts')
const modulesLocales = import.meta.globEager('../../modules/**/lang/zh_CN.ts')
export default {
  message: {
    ...deepMerge(genMessage(modules, 'zh-CN'), generateModuleMessage(modulesLocales)),
    antdLocale,
    ...vxeZhCN,
  },
}
