<template>
  <div class="full-height" style="padding: 10px">
    <div class="full-height" style="background: white">
      <DbSqlList :db-name-list="dbNameList" :client-id="clientId" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import type { PropType } from 'vue'

import { useLoadDbNameList } from '@/modules/monitor/utils/DruidPointUtils'

import DbSqlList from '@/modules/monitor/components/druid/sql/DbSqlList.vue'

export default defineComponent({
  name: 'DbSqlListView',
  components: {
    DbSqlList
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup(props) {
    return {
      ...useLoadDbNameList(props.clientId)
    }
  }
})
</script>

<style lang="less" scoped></style>
