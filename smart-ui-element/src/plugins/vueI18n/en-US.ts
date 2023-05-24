import { generateModuleMessage, genMessage } from './helper'
import { deepMerge } from '@/utils'

import vxeZhCN from 'vxe-table/lib/locale/lang/zh-CN'

const modules = import.meta.glob('../../locales/en-US/**/*.ts', { eager: true })
const modulesLocales = import.meta.glob('../../modules/**/lang/en_US.ts', { eager: true })

export default {
  message: {
    ...deepMerge(genMessage(modules), generateModuleMessage(modulesLocales)),
    ...vxeZhCN,
  },
}
