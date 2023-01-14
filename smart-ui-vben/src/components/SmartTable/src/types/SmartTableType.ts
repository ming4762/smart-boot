import type { VxeTableDefines, VxeGridProps, VxeGridPropTypes } from 'vxe-table'
import type { SmartSearchFormProps } from './SmartSearchFormType'
import type { SmartTableButton } from './SmartTableButton'
import type { SmartTableAddEditConfig } from './SmartTableAddEditType'

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
  page?: number
  sortInfo?: Recordable
  filterInfo?: Recordable
}

export interface SmartTableToolbarConfig extends VxeGridPropTypes.ToolbarConfig {
  buttons?: SmartTableButton[]
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
}

// @ts-ignore
export interface SmartTableProps<T = any> extends VxeGridProps<T> {
  columns: SmartColumn[]
  // 默认的排序参数
  // 请求接口配置
  height?: TableHeightType
  // 表格加载状态
  loading?: boolean
  // 搜索表单配置
  searchFormConfig?: Partial<SmartSearchFormProps>
  // 额外的请求参数
  searchInfo?: Recordable
  // 是否使用搜索表单
  useSearchForm?: boolean
  // 查询条件请求之前处理
  handleSearchInfoFn?: Fn
  // 搜索是否带有符号
  searchWithSymbol?: boolean
  toolbarConfig: SmartTableToolbarConfig
  // 分页配置
  pagerConfig?: boolean | VxeGridPropTypes.PagerConfig
  proxyConfig?: SmartTableProxyConfig
  // 添加修改配置
  addEditConfig?: SmartTableAddEditConfig
}

/**
 * 表格操作函数
 */
export interface TableActionType {
  reload: (opt?: FetchParams) => Promise<void>
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

  openAddEditModal: <T = any>(props?: boolean, data?: T, openOnSet?: boolean) => void
  showAddModal: () => void
  editByCheckbox: () => Promise<boolean> | boolean
  editByRow: (data) => Promise<boolean> | boolean
  getAddEditFieldsValue: () => Recordable
  resetAddEditFields: () => Promise<void>
  setAddEditFieldsValue: (data: any) => Promise<void>
  deleteByRow: (rows: any | any[]) => Promise<any>
}
