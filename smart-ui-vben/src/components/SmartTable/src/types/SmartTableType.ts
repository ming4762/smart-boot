import type { VxeTableDefines, VxeGridProps, VxeGridPropTypes, VxeGridInstance } from 'vxe-table'
import type { SmartSearchFormProps } from './SmartSearchFormType'
import type { SmartTableButton, SmartTableToolbarTool } from './SmartTableButton'
import type { SmartTableAddEditConfig } from './SmartTableAddEditType'
import type { NamePath } from 'ant-design-vue/es/form/interface'
import type { VxeTablePropTypes } from 'vxe-table/types/table'
import type { Options as SortableOptions } from 'sortablejs'

export interface SmartColumn extends VxeTableDefines.ColumnOptions {
  flag?: 'INDEX' | 'DEFAULT' | 'CHECKBOX' | 'RADIO' | 'ACTION'
}

export interface GetColumnsParams {
  ignoreIndex?: boolean
  ignoreAction?: boolean
  sort?: boolean
}

/**
 * 表格高度
 */
export type TableHeightType = 'auto' | number | string

export interface FetchParams {
  searchInfo?: Recordable
  page?: Partial<VxeGridPropTypes.ProxyAjaxQueryPageParams>
  sorts?: Partial<VxeGridPropTypes.ProxyAjaxQuerySortCheckedParams>[]
  filters?: Partial<VxeTableDefines.FilterCheckedParams>[]
}

export interface SmartTableToolbarConfig extends VxeGridPropTypes.ToolbarConfig {
  buttons?: SmartTableButton[]
  tools?: SmartTableToolbarTool[]
  // 是否显示搜索
  showSearch?: boolean | SmartTableToolbarTool
}

export interface SmartTableAjaxQueryParams extends VxeGridPropTypes.ProxyAjaxQueryParams {
  searchInfo?: Recordable
  searchFormSymbol?: Recordable
  searchForm?: Recordable
  ajaxParameter?: Recordable
}

export interface SmartTableProxyConfig<T = any> extends VxeGridPropTypes.ProxyConfig {
  ajax?: {
    query?(params: SmartTableAjaxQueryParams, ...args: any[]): Promise<any>
    queryAll?(params: VxeGridPropTypes.ProxyAjaxQueryAllParams, ...args: any[]): Promise<any>
    delete?(params: VxeGridPropTypes.ProxyAjaxDeleteParams, ...args: any[]): Promise<any>
    save?(params: VxeGridPropTypes.ProxyAjaxSaveParams, ...args: any[]): Promise<any>
    getById?(params: T): Promise<T>
  }
  // 删除回调
  afterDelete?: (result?: any) => void
  afterLoad?: (result: any) => any
}

/**
 * 拖拽配置
 */
export interface SmartTableRowDragConfig extends SortableOptions {
  icon?: string
  // 是否自定义
  custom?: boolean
}

export interface SmartTableRowConfig extends VxeTablePropTypes.RowConfig {
  dragConfig?: boolean | SmartTableRowDragConfig
}

// @ts-ignore
export interface SmartTableProps<T = any> extends VxeGridProps<T> {
  columns?: SmartColumn[]
  // 默认的排序参数
  // 请求接口配置
  height?: TableHeightType
  // 表格加载状态
  loading?: boolean
  // 搜索表单配置
  searchFormConfig?: Partial<SmartSearchFormProps>
  // 是否使用搜索表单
  useSearchForm?: boolean
  // 搜索是否带有符号
  toolbarConfig?: SmartTableToolbarConfig
  // 分页配置
  pagerConfig?: boolean | VxeGridPropTypes.PagerConfig
  proxyConfig?: SmartTableProxyConfig
  // 添加修改配置
  addEditConfig?: SmartTableAddEditConfig
  rowConfig?: SmartTableRowConfig
}

/**
 * 表格操作函数
 */
export interface TableActionType {
  reload: (opt?: FetchParams) => Promise<void>
  query: (opt?: FetchParams) => Promise<void>
  /**
   * 设置props
   * @param props
   */
  setProps: (props: Partial<SmartTableProps>) => void
  // 设置加载状态
  setLoading: (loading: boolean) => void
  setPagination: (info: Partial<VxeGridPropTypes.PagerConfig>) => void
  getPagination: () => VxeGridPropTypes.PagerConfig | boolean | undefined
  commitVxeProxy: (code: string, ...args) => void

  setShowPagination: (show: boolean) => Promise<void>
  getShowPagination: () => boolean

  deleteByCheckbox: () => void
  getCheckboxRecords: (isFull: boolean) => Array<any>
  getRadioRecord: (isFull: boolean) => any

  // openAddEditModal: <T = any>(props?: boolean, data?: T, openOnSet?: boolean) => void
  showAddModal: (formData?: Recordable) => void
  editByCheckbox: () => Promise<boolean> | boolean
  editByRowModal: <T = any>(data: T, formData?: Recordable) => Promise<boolean> | boolean
  getAddEditFieldsValue: () => Recordable
  resetAddEditFields: () => Promise<void>
  setAddEditFieldsValue: (data: any) => Promise<void>
  deleteByRow: (rows: any | any[]) => Promise<any>
  /**
   * 用于 type=radio，设置某一行为选中状态
   * @param row 指定行
   */
  setRadioRow(row: any): Promise<any>
  /**
   * 用于 type=checkbox，设置行为选中状态，第二个参数为选中与否
   * @param rows 指定行
   * @param checked 是否选中
   */
  setCheckboxRow(rows: any | any[], checked: boolean): Promise<any>
  validateAddEditFields: (nameList?: NamePath[]) => Promise<any>
  validateAddEdit: (nameList?: NamePath[]) => Promise<any>
  getTableInstance: () => VxeGridInstance
  getData: (rowIndex?: number) => any[]
}
