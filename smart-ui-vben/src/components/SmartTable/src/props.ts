import { propTypes } from '/@/utils/propTypes'
import type { FormProps } from '/@/components/Form'
import type { SmartColumn, TableHeightType } from './types/SmartTableType'

export const smartTableProps = {
  columns: {
    type: [Array] as PropType<SmartColumn[]>,
    default: () => [],
  },
  height: {
    type: [String, Number] as PropType<TableHeightType>,
    default: 'auto',
  },
  loading: propTypes.bool,
  // 表单配置
  searchFormConfig: {
    type: Object as PropType<Partial<FormProps>>,
    default: null,
  },
  // 额外的请求参数
  searchInfo: {
    type: Object as PropType<Recordable>,
    default: null,
  },
  // 使用搜索表单
  useSearchForm: propTypes.bool,
  // 立即请求接口
  immediate: { type: Boolean, default: true },
  searchWithSymbol: propTypes.bool,
  size: propTypes.string.def('small'),
}
