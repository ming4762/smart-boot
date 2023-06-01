import type { VxeTableDefines, VxeColumnPropTypes } from 'vxe-table'

export type SmartTableColumnComponent = 'switch' | 'tag' | 'button' | 'booleanTag'

// @ts-ignore
export interface SmartColumn extends VxeTableDefines.ColumnOptions {
  flag?: 'INDEX' | 'DEFAULT' | 'CHECKBOX' | 'RADIO' | 'ACTION'
  component?: SmartTableColumnComponent
  componentProps?: Recordable | ((params: VxeColumnPropTypes.DefaultSlotParams) => Recordable)
}
