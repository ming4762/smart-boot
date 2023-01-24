import type { FormProps } from '/@/components/Form'
import type { SmartColumn, TableHeightType } from './types/SmartTableType'
import { buildUUID } from '/@/utils/uuid'
import { propTypes } from '/@/utils/propTypes'

export const smartTableProps = {
  columns: {
    type: [Array] as PropType<SmartColumn[]>,
    default: () => [],
  },
  height: {
    type: [String, Number] as PropType<TableHeightType>,
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
  size: propTypes.string.def('small'),
  id: propTypes.string.def(buildUUID()),
}
