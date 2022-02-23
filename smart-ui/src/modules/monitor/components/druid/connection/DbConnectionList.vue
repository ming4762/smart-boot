<template>
  <a-tabs>
    <a-tab-pane
      v-for="dbName in dbNameList"
      :key="'connection-pane' + dbName"
      :tab="dbName">
      <DbConnection :client-id="clientId" :db-name="dbName" />
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import DbConnection from './DbConnection.vue'

/**
 * 数据库连接列表
 */
export default defineComponent({
  name: 'DbConnectionList',
  components: {
    DbConnection
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

<style scoped>

</style>
