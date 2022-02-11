<template>
  <a-table
    class="db-common"
    :data-source="data"
    :columns="columns"
    bordered
    :pagination="false"
    :show-header="false"
    size="small">
    <template #bodyCell="{text, column}">
      <template v-if="column.dataIndex === 'value'">
        <div style="width: 600px">{{ text }}</div>
      </template>
      <template v-if="column.dataIndex === 'key'">
        <div :style="{ width: column.width }">{{ text }}</div>
      </template>
    </template>
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
  name: 'KeyValueTable',
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

<style scoped>
  .db-common ::v-deep(td:first-child) {
    background: #E5E5E5;
  }
</style>
