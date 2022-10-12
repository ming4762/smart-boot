import { merge } from 'xe-utils'
import { transferI18n } from '@/i18n/LangVueSupport'

import managerApplicationZhCN from '../views/manager/application/lang/zh_CN'
import managerApplicationEnUS from '../views/manager/application/lang/en_US'

import eventZhCN from '../views/manager/event/lang/zh_CN'
import eventEnUS from '../views/manager/event/lang/en_US'

import slowSqlZhCN from '../views/manager/slowSql/lang/zh_CN'
import slowSqlEnUS from '../views/manager/slowSql/lang/en_US'

import logZhCN from '../views/manager/log/lang/zh_CN'
import logEnUS from '../views/manager/log/lang/en_US'

import httpTraceZhCN from '../views/manager/httpTrace/lang/zh_CN'
import httpTraceEnUS from '../views/manager/httpTrace/lang/en_US'

export default {
  zh_CN: merge(
    {},
    transferI18n(managerApplicationZhCN),
    transferI18n(eventZhCN),
    transferI18n(slowSqlZhCN),
    transferI18n(logZhCN),
    transferI18n(httpTraceZhCN)
  ),
  en_US: merge(
    {},
    transferI18n(managerApplicationEnUS),
    transferI18n(eventEnUS),
    transferI18n(slowSqlEnUS),
    transferI18n(logEnUS),
    transferI18n(httpTraceEnUS)
  )
}
