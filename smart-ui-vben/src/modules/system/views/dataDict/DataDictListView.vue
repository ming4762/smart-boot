<template>
  <LayoutSeparate :show-line="false" first-size="55%" class="full-height">
    <template #first>
      <DataDictGroup @code-change="handleCodeChange" />
    </template>
    <template #second>
      <DataDictItem :dict-code="dictCode" />
    </template>
  </LayoutSeparate>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

import { LayoutSeparate } from '/@/components/LayoutSeparate'

import DataDictGroup from './DataDictGroup.vue'
import DataDictItem from './DataDictItem.vue'

/**
 * 数据字典页面
 */
export default defineComponent({
  name: 'DataDictListView',
  components: {
    LayoutSeparate,
    DataDictGroup,
    DataDictItem,
  },
  setup() {
    const dictCode = ref<string | null>(null)

    const handleCodeChange = (code: string | null) => {
      dictCode.value = code
    }
    return {
      dictCode,
      handleCodeChange,
    }
  },
})
</script>

<style scoped></style>
