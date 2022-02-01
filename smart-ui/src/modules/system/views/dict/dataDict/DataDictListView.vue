<template>
  <LeftRightLayout right-width="55%">
    <DataDictGroup />
  </LeftRightLayout>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import LeftRightLayout from '@/components/layouts/LeftRightLayout.vue'

import DataDictGroup from './DataDictGroup.vue'

/**
 * 数据字典页面
 */
export default defineComponent({
  name: 'DataDictListView',
  components: {
    LeftRightLayout,
    DataDictGroup
  }
})
</script>

<style scoped>

</style>
