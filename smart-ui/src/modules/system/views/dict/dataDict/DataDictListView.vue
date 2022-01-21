<template>
  <LeftRightLayout right-width="60%"></LeftRightLayout>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import LeftRightLayout from '@/components/layouts/LeftRightLayout.vue'

/**
 * 数据字典页面
 */
export default defineComponent({
  name: 'DataDictListView',
  components: {
    LeftRightLayout
  }
})
</script>

<style scoped>

</style>
