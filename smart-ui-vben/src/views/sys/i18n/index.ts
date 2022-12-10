import { transferI18n } from '/@/locales/useLocale'
import { deepMerge } from '/@/utils'

import loginZhCN from '../login/lang/zh_CN'
import loginEnUs from '../login/lang/en_US'

export const systemZhCN = deepMerge(
  {},
  transferI18n(loginZhCN)
)

export const systemEnUs = deepMerge(
  {},
  transferI18n(loginEnUs)
)
