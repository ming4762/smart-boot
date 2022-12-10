import { genMessage } from '../helper'
import antdLocale from 'ant-design-vue/es/locale/en_US'

const modules = import.meta.globEager('./en/**/*.ts')
import { systemEnUs } from '/@/views/sys/i18n'
export default {
  message: {
    ...genMessage(modules, 'en'),
    antdLocale,
    ...systemEnUs,
  },
  dateLocale: null,
  dateLocaleName: 'en',
}
