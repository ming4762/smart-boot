<template>
  <div style="padding: 10px" class="full-height">
    <vxe-grid
      v-bind="tableProps"
      :tree-config="treeConfig"
      :columns="columns"
      highlight-hover-row
      height="auto"
      stripe
      border></vxe-grid>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted} from 'vue'

import {useVxeTable} from '@/components/hooks'
import ApiService from '@/common/utils/ApiService'
import {errorMessage} from '@/components/notice/SystemNotice'
import {tableBooleanColumn} from '@/components/common/TableCommon'

const doLoadData = async () => {
  try {
    return await ApiService.postAjax('db/code/main/getTemplateDataDocument')
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
      treeConfig: {
        children: 'fieldList'
      },
      columns: [
        {
          title: '属性',
          field: 'name',
          width: 240,
          fixed: 'left',
          treeNode: true
        },
        {
          title: '说明',
          field: 'remark',
          minWidth: 240
        },
        {
          title: '参数|返回值',
          field: 'type',
          width: 120
        },
        {
          title: '可选值',
          field: 'optional',
          width: 180
        },
        {
          title: '默认值',
          field: 'defaultValue',
          width: 200
        },
        {
          ...tableBooleanColumn(this.$t, '是否可null', 'nullable').createColumn(),
          width: 120
        }
      ]
    }
  }
})
</script>

<style scoped>

</style>
