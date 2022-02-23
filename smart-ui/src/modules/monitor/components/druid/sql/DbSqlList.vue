<template>
  <a-tabs class="tab-container full-height">
    <a-tab-pane
      v-for="dbName in dbNameList"
      :key="'connection-pane' + dbName"
      :tab="dbName">
      <DbSql class="full-height" :client-id="clientId" :db-name="dbName" />
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import DbSql from './DbSql.vue'

/**
 * SQL列表
 */
export default defineComponent({
  name: 'DbSqlList',
  components: {
    DbSql
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    },
    dbNameList: {
      type: Array as PropType<string[]>,
      required: true
    }
  }
})
</script>

<style lang="less" scoped>
  .tab-container {
    ::v-deep(.ant-tabs-content) {
      height: 100%;
    }
    ::v-deep(.ant-tabs-nav) {
      padding: 0 10px;
    }
  }

</style>
