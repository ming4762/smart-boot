import { merge } from 'xe-utils'

// 数据库管理模块I18N
import db_zh_CN from '../views/database/lang/zh_CN'
import db_en_US from '../views/database/lang/en_US'
// 模板管理模块国际化
import template_zh_CN from '../views/template/lang/zh_CN'
import template_en_US from '../views/template/lang/en_US'

export default {
  zh_CN: merge({}, db_zh_CN, template_zh_CN),
  en_US: merge({}, db_en_US, template_en_US)
}
