import { merge } from 'xe-utils'

import { transferI18n } from '@/i18n/LangVueSupport'

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
// 在线用户国际化
import onlineUser_zh_CN from '../views/auth/onlineUser/lang/zh_CN'
import onlineUser_en_US from '../views/auth/onlineUser/lang/en_US'
// 日志页面国际化
import log_zh_CN from '../views/log/lang/zh_CN'
import log_en_US from '../views/log/lang/en_US'

// 数据字典国际化
import {
  dictGroupI18n as dictGroupI18n_zh,
  dictItemI18n as dictItemI18n_zh
} from '../views/dict/dataDict/lang/zh_CN'
import {
  dictGroupI18n as dictGroupI18n_en,
  dictItemI18n as dictItemI18n_en
} from '../views/dict/dataDict/lang/en_US'

//异常信息国际化
import exception_zh_CN from '../views/exception/lang/zh_CN'
import exception_en_US from '../views/exception/lang/en_US'

// 文件管理国际化
import file_zh_CN from '../views/file/lang/zh-CN'
import file_en_US from '../views/file/lang/en-US'

// 部门管理国际化
import dept_zh_CN from '../views/dept/lang/zh-CN'
import dept_en_US from '../views/dept/lang/en-US'

export default {
  zh_CN: merge(
    {},
    systemZhCN,
    transferI18n(function_zh_CN),
    i18n_zh_CN,
    login_zh_CN,
    role_zh_CN,
    user_zh_CN,
    userGroup_zh_CN,
    onlineUser_zh_CN,
    log_zh_CN,
    transferI18n(dictGroupI18n_zh),
    transferI18n(dictItemI18n_zh),
    transferI18n(exception_zh_CN),
    transferI18n(file_zh_CN),
    transferI18n(dept_zh_CN)
  ),
  en_US: merge(
    {},
    systemEnUS,
    transferI18n(function_en_US),
    i18n_en_US,
    login_en_US,
    role_en_US,
    user_en_US,
    userGroup_en_US,
    onlineUser_en_US,
    log_en_US,
    transferI18n(dictGroupI18n_en),
    transferI18n(dictItemI18n_en),
    transferI18n(exception_en_US),
    transferI18n(file_en_US),
    transferI18n(dept_en_US)
  )
}
