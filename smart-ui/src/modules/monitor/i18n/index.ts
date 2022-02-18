import { merge } from 'xe-utils'
import { transferI18n } from '@/i18n/LangVueSupport'

import managerApplicationZhCN from '../views/manager/application/lang/zh_CN'
import managerApplicationEnUS from '../views/manager/application/lang/en_US'

import eventZhCN from '../views/manager/event/lang/zh_CN'
import eventEnUS from '../views/manager/event/lang/en_US'

export default {
  zh_CN: merge({},
    transferI18n(managerApplicationZhCN),
    transferI18n(eventZhCN)
  ),
  en_US: merge({},
    transferI18n(managerApplicationEnUS),
    transferI18n(eventEnUS)
  )
}
