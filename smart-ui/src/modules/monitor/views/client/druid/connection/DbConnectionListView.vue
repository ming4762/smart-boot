<template>
  <div class="full-height" style="padding: 10px;">
    <div class="full-height" style="overflow:auto; background: white; padding: 10px">
      <DbConnectionList :client-id="clientId" :db-name-list="dbNameList" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import { useLoadDbNameList } from '@/modules/monitor/utils/DruidPointUtils'

import DbConnectionList from '@/modules/monitor/components/druid/connection/DbConnectionList.vue'

export default defineComponent({
  name: 'DbConnectionListView',
  components: {
    DbConnectionList
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {

    return {
      ...useLoadDbNameList(props.clientId)
    }
  }
})
</script>

<style scoped lang="less"></style>
