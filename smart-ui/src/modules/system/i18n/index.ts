import { merge } from 'xe-utils'

import systemZhCN from './lang/zh_CN'
import systemEnUS from './lang/en_US'

// 登录页面国际化
import login_zh_CN from '../views/login/lang/zh_CN'
import login_en_US from '../views/login/lang/en_US'

// 功能模块国际化
import function_zh_CN from '../views/function/lang/zh_CN'
import function_en_US from '../views/function/lang/en_US'

import i18n_zh_CN from '../views/i18n/lang/zh_CN'
import i18n_en_US from '../views/i18n/lang/en_US'

import role_zh_CN from '../views/role/lang/zh_CN'
import role_en_US from '../views/role/lang/en_US'

import user_zh_CN from '../views/user/lang/zh_CN'
import user_en_US from '../views/user/lang/en_US'

import userGroup_zh_CN from '../views/userGroup/lang/zh_CN'
import userGroup_en_US from '../views/userGroup/lang/en_US'

export default {
  zh_CN: merge({},
    systemZhCN,
    function_zh_CN,
    i18n_zh_CN,
    login_zh_CN,
    role_zh_CN,
    user_zh_CN,
    userGroup_zh_CN
  ),
  en_US: merge({},
    systemEnUS,
    function_en_US,
    i18n_en_US,
    login_en_US,
    role_en_US,
    user_en_US,
    userGroup_en_US
  )
}
