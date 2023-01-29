import type { SmartTableBasicButtonDropdowns } from '/@/components/SmartTable'
import { isString } from '/@/utils/is'
import { hasPermission } from '/@/common/auth/AuthUtils'

export const hasButtonPermission = (props: SmartTableBasicButtonDropdowns): boolean => {
  const auth = props.auth
  if (!auth) {
    return true
  }
  if (isString(auth)) {
    return hasPermission(auth)
  }
  const { permission, multipleMode } = auth
  if (isString(permission)) {
    return hasPermission(permission)
  }
  if (multipleMode === 'or') {
    return permission.some((item) => hasPermission(item))
  } else {
    return permission.every((item) => hasPermission(item))
  }
}
