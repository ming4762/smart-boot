<template>
  <div style="padding: 10px" class="full-height">
    <vxe-grid
      v-bind="tableProps"
      :columns="columns"
      highlight-hover-row
      height="auto"
      border
      stripe></vxe-grid>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from 'vue'

import { useVxeTable } from '@/components/hooks'
import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const doLoadData = async () => {
  try {
    const data = await ApiService.postAjax('db/code/main/getTemplateDataDocument')
    console.log(data)
    return data
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

export default defineComponent({
  name: 'TemplateDataDocumentView',
  setup () {
    const { tableProps, loadData} = useVxeTable(doLoadData, {
      paging: false
    })

    onMounted(loadData)

    return {
      tableProps
    }
  },
  data () {
    return {
      columns: [

      ]
    }
  }
})
</script>

<style scoped>

</style>
