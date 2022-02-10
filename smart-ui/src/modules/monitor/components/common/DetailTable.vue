<template>
  <table class="health-table">
    <!--  表体  -->
    <tr>
      <th colspan="2" class="nested">
        <span style="font-weight: bold">{{ data.title }}</span>
        <span class="status" :class="data.status === 'UP' ? 'up' : 'down'">{{ data.status }}</span>
      </th>
    </tr>
    <a-divider />
    <tr v-if="Object.keys(data.details).length > 0">
      <td colspan="2" class="nested">
        <CommonTable :data="data" />
      </td>
    </tr>
  </table>
</template>

<script>
import CommonTable from '@/modules/monitor/components/common/CommonTable.vue'

export default {
  name: 'DetailTable',
  components: {
    CommonTable
  },
  props: {
    data: {
      type: Object,
      required: true
    }
  }
}
</script>

<style lang="less" scoped>
.health-table {
  width: 100%;
  .status {
    float: right;
    font-weight: bold;
  }
  .up {
    color: #4EC677;
  }
  .down {
    color: red;
  }
  .td-divider {
    .ant-divider-horizontal {
      margin: 8px 0 !important;
    }
  }
  .ant-divider-horizontal {
    margin: 18px 0 !important;
  }
  td {
    .nested {
      padding: 0 0 0 .75em;
    }
  }
}
</style>
