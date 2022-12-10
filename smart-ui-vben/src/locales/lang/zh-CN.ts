import { genMessage } from '../helper'
import antdLocale from 'ant-design-vue/es/locale/zh_CN'

import { systemZhCN } from '/@/views/sys/i18n'
const modules = import.meta.globEager('./zh-CN/**/*.ts')
export default {
  message: {
    ...genMessage(modules, 'zh-CN'),
    antdLocale,
    ...systemZhCN,
  },
}
