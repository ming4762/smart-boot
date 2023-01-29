import { ButtonProps } from '/@/components/Button'
import type { ComputedRef, Ref } from 'vue'

export type SmartTableButtonType = 'text' | 'submit' | 'reset' | 'button'

export type SmartTableButtonStatus = 'primary' | 'success' | 'info' | 'warning' | 'danger'

export type SmartTableButtonCode =
  | 'reload'
  | 'query'
  | 'insert'
  | 'insert_actived'
  | 'mark_cancel'
  | 'delete'
  | 'remove'
  | 'save'
  | 'import'
  | 'open_import'
  | 'export'
  | 'open_export'
  | 'reset_custom'
  | 'ModalAdd'
  | 'ModalEdit'
  | 'smartDelete'

export interface SmartTableButtonRender {
  name?: string
  props?: any
  events?: any
}

/**
 * 按钮权限
 */
export interface SmartTableButtonAuth {
  permission: string | string[]
  displayMode: 'hide' | 'disabled'
  multipleMode: 'and' | 'or'
}

export interface SmartTableBasicButtonDropdowns {
  name?: string | 'smart-auto'
  // 按钮类型
  type?: SmartTableButtonType
  // 按钮状态
  status?: SmartTableButtonStatus
  code?: SmartTableButtonCode
  visible?: boolean
  disabled?: boolean
  icon?: string
  round?: boolean
  circle?: boolean
  auth?: string | SmartTableButtonAuth
}

export interface SmartTableButton extends SmartTableBasicButtonDropdowns {
  placement?: string
  'destroy-on-close'?: boolean
  transfer?: boolean
  dropdowns?: SmartTableBasicButtonDropdowns[]
  buttonRender?: SmartTableButtonRender
  props?: ButtonProps | Ref<ButtonProps> | ComputedRef<ButtonProps>
  // 是否是ant-design按钮，false：使用vxe-table原有的按钮，true使用VxeTableToolButtonRenderer进行渲染
  isAnt?: boolean
  // 点击事件是否触发加载状态
  clickLoading?: boolean
  // 是否使用插槽
  slot?: string
}
