<template>
  <SmartTable
    v-bind="$attrs"
    @register="registerTable"
    @current-change="handleCurrentChange"
    @after-load="handleAfterLoad" />
</template>

<script lang="ts" setup>
import { propTypes } from '/@/utils/propTypes'

import { listSystemApi } from '/@/api/system/SystemApi'

import { SmartTable, useSmartTable } from '/@/components/SmartTable'

const props = defineProps({
  // 是否自动选中第一行
  autoSelected: propTypes.bool.def(true),
})

const emit = defineEmits(['current-change'])

/**
 * 数据加载完成时间
 */
const handleAfterLoad = () => {
  if (props.autoSelected) {
    const dataList = getData()
    if (dataList.length > 0) {
      getTableInstance().setCurrentRow(dataList[0])
      emit('current-change', dataList[0])
    }
  }
}

const handleCurrentChange = ({ row }) => {
  emit('current-change', row)
}

const [registerTable, { getTableInstance, getData }] = useSmartTable({
  rowConfig: {
    isHover: true,
    isCurrent: true,
    keyField: 'id',
  },
  toolbarConfig: {
    refresh: true,
  },
  proxyConfig: {
    ajax: {
      query: (params) => {
        return listSystemApi({
          ...params.ajaxParameter,
          sortName: 'seq',
        })
      },
    },
  },
  columns: [
    {
      field: 'name',
      title: '{system.views.system.title.name}',
      minWidth: 160,
      formatter: ({ row }) => {
        return `${row.name}(${row.code})`
      },
    },
  ],
})
</script>

<style scoped></style>
