import { ButtonProps } from '/@/components/Button'

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
}

export interface SmartTableButton extends SmartTableBasicButtonDropdowns {
  placement?: string
  'destroy-on-close'?: boolean
  transfer?: boolean
  dropdowns?: SmartTableBasicButtonDropdowns[]
  buttonRender?: SmartTableButtonRender
  props?: ButtonProps
  // 是否是ant-design按钮，false：使用vxe-table原有的按钮，true使用VxeTableToolButtonRenderer进行渲染
  isAnt?: boolean
}