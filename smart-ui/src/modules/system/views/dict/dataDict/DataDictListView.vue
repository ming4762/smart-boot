<template>
  <LeftRightLayout right-width="55%">
    <DataDictGroup @code-change="handleCodeChange" />
    <template #right>
      <DataDictItem :dict-code="dictCode" />
    </template>
  </LeftRightLayout>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

import LeftRightLayout from '@/components/layouts/LeftRightLayout.vue'

import DataDictGroup from './DataDictGroup.vue'
import DataDictItem from './DataDictItem.vue'

/**
 * 数据字典页面
 */
export default defineComponent({
  name: 'DataDictListView',
  components: {
    LeftRightLayout,
    DataDictGroup,
    DataDictItem
  },
  setup() {
    const dictCode = ref<string | null>(null)

    const handleCodeChange = (code: string | null) => {
      dictCode.value = code
    }
    return {
      dictCode,
      handleCodeChange
    }
  }
})
</script>

<style scoped></style>
