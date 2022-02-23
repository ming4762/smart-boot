<template>
  <div>
    <h3>FULL SQL</h3>
    <h5>{{ data.SQL }}</h5>
    <h2>Format View</h2>
    <a-textarea
      v-model:value="data.formattedSql"
      auto-size />
    <br />
    <br />
    <h3 class="margin-title">ParseView:</h3>
    <DbCommonTable :data="computedParseViewData" />
    <h3 class="margin-title">LastSlowView:</h3>
    <DbCommonTable :data="computedLastShowViewData" />
    <h3 class="margin-title">LastErrorView:</h3>
    <DbCommonTable :data="computedLastErrorViewData" />
    <h3 class="margin-title">OtherView:</h3>
    <DbCommonTable :data="computedOtherViewData" />
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, onMounted, ref, computed, watch } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'

import DbCommonTable from '@/modules/monitor/components/druid/common/DbCommonTable.vue'

const parseViewKey: {[index: string]: string} = {
  Tables: 'parsedTable',
  Fields: 'parsedFields',
  Coditions: 'parsedConditions',
  Relationships: 'parsedRelationships',
  OrderByColumns: 'parsedOrderbycolumns'
}

const lastShowViewKey: {[index: string]: string} = {
  MaxTimespan: 'MaxTimespan',
  MaxTimespanOccurTime: 'MaxTimespanOccurTime',
  LastSlowParameters: 'LastSlowParameters'
}

const lastErrorViewKey: {[index: string]: string} = {
  LastErrorMessage: 'LastErrorMessage',
  LastErrorClass: 'LastErrorClass',
  LastErrorTime: 'LastErrorTime',
  LastErrorStackTrace: 'LastErrorStackTrace'
}

const otherViewKey: {[index: string]: string} = {
  BatchSizeMax: 'BatchSizeMax',
  BatchSizeTotal: 'BatchSizeTotal',
  BlobOpenCount: 'BlobOpenCount',
  ClobOpenCount: 'ClobOpenCount',
  ReaderOpenCount: 'ReaderOpenCount',
  InputStreamOpenCount: 'InputStreamOpenCount',
  ReadStringLength: 'ReadStringLength',
  ReadBytesLength: 'ReadBytesLength'
}

const formatData = (keys: {[index: string]: string}, data: any) => {
  return Object.keys(keys).map(key => {
    return {
      key: key,
      value: data[keys[key]]
    }
  })
}

/**
 * SQL详情页面
 */
export default defineComponent({
  name: 'SqlDetail',
  components: {
    DbCommonTable
  },
  props: {
    id: {
      type: Number as PropType<number>,
      required: true
    },
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {
    const { id, clientId } = toRefs(props)
    const data = ref<object>({})
    const doLoadData = async () => {
      data.value = await loadActuator(clientId.value, `druidSql/${id.value}`)
    }
    onMounted(doLoadData)
    watch([id, clientId], doLoadData)

    const computedParseViewData = computed(() => formatData(parseViewKey, data.value))
    const computedLastShowViewData = computed(() => formatData(lastShowViewKey, data.value))
    const computedLastErrorViewData = computed(() => formatData(lastErrorViewKey, data.value))
    const computedOtherViewData = computed(() => formatData(otherViewKey, data.value))

    return {
      data,
      computedParseViewData,
      computedLastShowViewData,
      computedLastErrorViewData,
      computedOtherViewData
    }
  }
})
</script>

<style lang="less" scoped>
  h3 {
    font-size: 24px;
    line-height: 40px;
  }
  h5 {
    font-size: 14px;
    line-height: 20px;
  }
  h2 {
    font-size: 30px;
    line-height: 40px;
  }
  .margin-title {
    margin-top: 10px;
  }
</style>
