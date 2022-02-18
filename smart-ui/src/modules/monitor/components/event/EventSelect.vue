<template>
  <a-select
    v-bind="$attrs"
    mode="tags"
    :options="options" />
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'

import ApiService from '@/common/utils/ApiService'

import { errorMessage } from '@/components/notice/SystemNotice'

export default defineComponent({
  name: 'EventSelect',
  setup () {
    const options = ref<Array<any>>([])
    /**
     * 加载数据函数
     */
    const loadData = async () => {
      try {
        const result = await ApiService.postAjax('monitor/manager/event/listBuiltInEventCode')
        options.value = result.map((item: string) => {
          return {
            label: item,
            value: item
          }
        })
      } catch (e) {
        errorMessage(e)
      }
    }

    onMounted(loadData)

    return {
      options
    }
  }
})
</script>

<style scoped>

</style>
