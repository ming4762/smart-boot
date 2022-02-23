<template>
  <a-tabs>
    <a-tab-pane
      v-for="dbName in dbNameList"
      :key="'connection-pane' + dbName"
      :tab="dbName">
      <DbWall :client-id="clientId" :db-name="dbName" />
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import DbWall from './DbWall.vue'

export default defineComponent({
  name: 'DbWallList',
  components: {
    DbWall
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
