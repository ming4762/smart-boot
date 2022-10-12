<template>
  <div class="db-monitor">
    <vxe-grid
      :id="'dbsql' + clientId + dbName"
      show-overflow="ellipsis"
      :columns="vxeColumns"
      size="small"
      stripe
      height="auto"
      :data="data"
      border>
      <template #SQL="{ row }">
        <a @click="() => handleShowSqlDetail(row.ID)">{{ row.SQL }}</a>
      </template>
    </vxe-grid>
    <!--  sql详情弹窗  -->
    <a-modal
      v-model:visible="modalVisible"
      title="SQL Detail"
      width="90%"
      @ok="handleOk">
      <SqlDetail :id="currentSqlId" :client-id="clientId" />
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, toRefs, onBeforeUnmount } from 'vue'
import type { PropType } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'
import TimeTaskUtil from '@/common/utils/TimeTaskUtil'

import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'

import SqlDetail from './SqlDetail.vue'

const histogramFormatter = (data: Array<number>): string => {
  return `[${data.join(', ')}]`
}

export default defineComponent({
  name: 'DbSql',
  components: {
    SqlDetail
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    },
    dbName: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {
    const { clientId, dbName } = toRefs(props)
    // 加载数据
    const data = ref([] as Array<any>)
    const handleLoadData = async () => {
      try {
        data.value = await loadActuator(clientId.value, `druidSql?datasourceName=${dbName.value}`)
      } catch (e) {
        errorMessage(e)
      }
    }
    const lookKey = `druid_sql_${clientId.value}_${dbName.value}`
    // 添加循环
    TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, lookKey, handleLoadData)
    onBeforeUnmount(() => TimeTaskUtil.removeLoop(MONITOR_DETAIL_LOOP_GROUP, lookKey))
    onMounted(handleLoadData)
    // SQL弹窗
    const modalVisible = ref(false)
    const currentSqlId = ref(0)
    const handleShowSqlDetail = (id: number) => {
      currentSqlId.value = id
      modalVisible.value = true
    }
    const handleOk = () => {
      modalVisible.value = false
    }
    return {
      data,
      modalVisible,
      handleShowSqlDetail,
      currentSqlId,
      handleOk
    }
  },
  data () {
    return {
      vxeColumns: [
        {
          title: 'ID',
          field: 'ID',
          width: 100,
          fixed: 'left'
        },
        {
          title: 'SQL',
          field: 'SQL',
          width: 300,
          slots: {
            default: 'SQL'
          },
          fixed: 'left'
        },
        {
          title: '执行数',
          field: 'ExecuteCount',
          width: 120,
          sortable: true
        },
        {
          title: '执行时间',
          field: 'TotalTime',
          width: 140,
          sortable: true
        },
        {
          title: '最慢',
          field: 'MaxTimespan',
          width: 120,
          sortable: true
        },
        {
          title: '事务执行',
          field: 'Txn',
          width: 100
        },
        {
          title: '错误数',
          field: 'ErrorCount',
          width: 120,
          sortable: true
        },
        {
          title: '更新行数',
          field: 'UpdateCount',
          width: 120,
          sortable: true
        },
        {
          title: '读取行数',
          field: 'FetchRowCount',
          width: 120,
          sortable: true
        },
        {
          title: '读取行数',
          field: 'RunningCount',
          width: 120,
          sortable: true
        },
        {
          title: '最大并发',
          field: 'ConcurrentMax',
          width: 120,
          sortable: true
        },
        {
          title: '执行时间分布',
          field: 'Histogram',
          width: 150,
          customRender: ({ text }: any) => histogramFormatter(text)
        },
        {
          title: '执行+RS时分布',
          field: 'ExecuteAndResultHoldTimeHistogram',
          width: 150,
          customRender: ({ text }: any) => histogramFormatter(text)
        },
        {
          title: '读取行分布',
          field: 'FetchRowCountHistogram',
          width: 130,
          customRender: ({ text }: any) => histogramFormatter(text)
        },
        {
          title: '更新行分布',
          field: 'EffectedRowCountHistogram',
          width: 130,
          customRender: ({ text }: any) => histogramFormatter(text)
        }
      ]
    }
  }
})
</script>

<style scoped>

</style>
