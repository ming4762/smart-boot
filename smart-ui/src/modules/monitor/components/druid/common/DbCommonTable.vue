<template>
  <a-table
    class="db-common"
    :data-source="data"
    :columns="columns"
    bordered
    :pagination="false"
    :show-header="false"
    size="small">
  </a-table>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

const columns = [
  {
    dataIndex: 'key',
    width: 237
  },
  {
    dataIndex: 'value'
  }
]

export default defineComponent({
  name: 'DbCommonTable',
  props: {
    data: {
      type: Array,
      default: () => []
    },
    columns: {
      type: Array,
      default: () => columns
    }
  }
})
</script>

<style lang="less" scoped>
  .db-common {
    ::v-deep(td:first-child) {
      background: #E5E5E5;
    }
    ::v-deep(.ant-table) {
      color: black;
    }
  }
</style>
