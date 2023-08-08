<template>
  <SmartTable v-bind="$attrs" @register="register" />
</template>

<script lang="ts" setup>
import type { SmartTableProps } from '/@/components/SmartTable'
import { SmartTable, useSmartTable } from '/@/components/SmartTable'
import { ApiServiceEnum, defHttp } from '/@/utils/http/axios'
import { propTypes } from '/@/utils/propTypes'
import { watch } from 'vue'
import { getTableColumns, ColumnOption } from './SmartChangeLog.config'
import { useI18n } from '/@/hooks/web/useI18n'

const props = defineProps({
  tableProps: Object as PropType<SmartTableProps>,
  ident: propTypes.string.isRequired,
  businessId: propTypes.number,
  fieldOptions: {
    type: Object as PropType<Record<string, string>>,
    default: () => {
      return {}
    },
  },
  columnOptions: {
    type: Object as PropType<ColumnOption[]>,
    default: () => {
      return []
    },
  },
})

const { t } = useI18n()

/**
 * 标志位和业务ID变更重新加载数据
 */
watch([() => props.ident, () => props.businessId], () => query())

const mergeRowMethod = ({ row, _rowIndex, column, visibleData }) => {
  const fields = ['seq', 'createTime', 'createBy', 'operateType']
  const cellValue = row.id
  if (cellValue && fields.includes(column.field)) {
    const prevRow = visibleData[_rowIndex - 1]
    let nextRow = visibleData[_rowIndex + 1]
    if (prevRow && prevRow['id'] === cellValue) {
      return { rowspan: 0, colspan: 0 }
    } else {
      let countRowspan = 1
      while (nextRow && nextRow['id'] === cellValue) {
        nextRow = visibleData[++countRowspan + _rowIndex]
      }
      if (countRowspan > 1) {
        return { rowspan: countRowspan, colspan: 1 }
      }
    }
  }
}

const [register, { query }] = useSmartTable({
  columns: getTableColumns(t, props.columnOptions),
  border: true,
  stripe: true,
  pagerConfig: false,
  highlightHoverRow: true,
  spanMethod: mergeRowMethod,
  proxyConfig: {
    ajax: {
      query: async () => {
        const dataList = await defHttp.post({
          service: ApiServiceEnum.SMART_SYSTEM,
          url: 'smart/changeLog/list',
          data: {
            ident: props.ident,
            businessId: props.businessId,
          },
        })
        const result: any[] = []
        let seq = 1
        dataList.forEach((item) => {
          const detailList = item.detailList
          if (!detailList) {
            result.push({ seq, ...item })
          } else {
            detailList.forEach((detail) => {
              result.push({
                seq,
                ...detail,
                ...item,
              })
            })
          }
          seq++
        })
        return result
      },
    },
  },
  ...(props.tableProps || {}),
})
</script>

<style scoped></style>
