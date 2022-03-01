<template>
  <div class="full-height">
    <a-row style="margin: 0" :gutter="gutter" class="chart-row">
      <a-col :span="12" style="padding-left: 0">
        <RedisMemoryChart :data="computedMemoryData" :time="time" />
      </a-col>
      <a-col :span="12" style="padding-right: 0">
        <redis-cpu-chart :time="time" :data="computedCpuSysData" />
      </a-col>
    </a-row>
    <a-row style="margin: 10px 0 0 0" :gutter="gutter" class="chart-row">
      <a-col :span="12" style="padding-left: 0">
        <redis-key-chart :data="computedData" />
      </a-col>
      <a-col :span="12" style="padding-right: 0">
        <RedisClientChart :data="computedConnectedClientsData" :time="time" />
      </a-col>
    </a-row>

    <a-row style="margin: 10px 0 0 0" :gutter="gutter" class="chart-row">
      <a-col :span="12" style="padding-left: 0">
        <RedisOpsPerChart :data="computedOpsPerData" :time="time" />
      </a-col>
      <a-col :span="12" style="padding-right: 0">
        <RedisInOutKpsChart :time="time" :data="computedInputOutputData" />
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import {computed, defineComponent, PropType, ref, toRefs} from 'vue'

import TimeTaskUtil from '@/common/utils/TimeTaskUtil'
import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'
import { loadMetricsDataVue } from '@/modules/monitor/utils/ClientMetrisData'

import RedisKeyChart from './charts/RedisKeyChart.vue'
import RedisMemoryChart from './charts/RedisMemoryChart.vue'
import RedisCpuChart from './charts/RedisCpuChart.vue'
import RedisClientChart from './charts/RedisClientChart.vue'
import RedisOpsPerChart from './charts/RedisOpsPerChart.vue'
import RedisInOutKpsChart from './charts/RedisInOutKpsChart.vue'


export default defineComponent({
  name: 'RedisInfoCharts',
  components: {
    RedisKeyChart,
    RedisMemoryChart,
    RedisCpuChart,
    RedisClientChart,
    RedisOpsPerChart,
    RedisInOutKpsChart
  },
  props: {
    data: {
      type: Array as PropType<Array<any>>,
      default: () => []
    },
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {
    const { clientId, data } = toRefs(props)
    const time = ref(0)
    TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, 'redisKeyMeter', () => time.value++)
    // 加载指标数据
    const { computedData } = loadMetricsDataVue(clientId, time, ['redis.keys'])

    /**
     * 内存使用情况
     */
    const computedMemoryData = computed(() => {
      const memory = data.value.filter(item => item.key === 'used_memory_rss')
      if (memory && memory.length > 0) {
        return parseInt(memory[0].value)
      }
      return 0
    })

    /**
     * redis cpu
     */
    const computedCpuSysData = computed(() => {
      const cpuData = data.value.filter(item => item.key === 'used_cpu_sys')
      if (cpuData && cpuData.length > 0) {
        return parseFloat(cpuData[0].value)
      }
      return 0
    })

    const computedConnectedClientsData = computed(() => {
      const cpuData = data.value.filter(item => item.key === 'connected_clients')
      if (cpuData && cpuData.length > 0) {
        return parseInt(cpuData[0].value)
      }
      return 0
    })

    const computedOpsPerData = computed(() => {
      const cpuData = data.value.filter(item => item.key === 'instantaneous_ops_per_sec')
      if (cpuData && cpuData.length > 0) {
        return parseInt(cpuData[0].value)
      }
      return 0
    })

    const computedInputOutputData = computed(() => {
      const inputData = data.value.filter(item => item.key === 'instantaneous_input_kbps')
      const outputData = data.value.filter(item => item.key === 'instantaneous_ops_per_sec')

      return {
        input: inputData.length > 0 ? inputData[0].value : 0,
        output: outputData.length > 0 ? outputData[0].value : 0
      }
    })

    return {
      computedData,
      computedMemoryData,
      computedCpuSysData,
      time,
      computedConnectedClientsData,
      computedOpsPerData,
      computedInputOutputData
    }
  },
  data () {
    return {
      gutter: 16
    }
  }
})
</script>
<style lang="less" scoped>
.chart-row {
  height: 350px;
}
</style>
