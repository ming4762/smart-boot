<template>
  <vxe-grid
    v-bind="$attrs"
    :data="data"
    :columns="columns"></vxe-grid>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import type { PropType } from 'vue'

/**
 * Redis info
 */
export default defineComponent({
  name: 'RedisInfoGrid',
  props: {
    data: {
      type: Array as PropType<Array<any>>,
      default: () => []
    }
  },
  data () {
    return {
      columns: [
        {
          title: 'Group',
          field: 'group',
          width: 120
        },
        {
          title: 'Key',
          field: 'key',
          width: 210
        },
        {
          title: 'Value',
          field: 'value',
          width: 230
        },
        {
          title: 'Description',
          field: 'description',
          minWidth: 200
        }
      ]
    }
  }
})
</script>
