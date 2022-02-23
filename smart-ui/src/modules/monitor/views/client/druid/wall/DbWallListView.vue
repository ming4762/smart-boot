<template>
  <div class="full-height" style="padding: 10px">
    <div class="full-height" style="background: white; padding: 10px; overflow: auto">
      <DbWallList :db-name-list="dbNameList" :client-id="clientId" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import { useLoadDbNameList } from '@/modules/monitor/utils/DruidPointUtils'

import DbWallList from '@/modules/monitor/components/druid/wall/DbWallList.vue'

export default defineComponent({
  name: 'DbWallListView',
  components: {
    DbWallList
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

<style lang="less" scoped>

</style>
