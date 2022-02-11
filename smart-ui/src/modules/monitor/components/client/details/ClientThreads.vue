<template>
  <ClientCard class="full-height client-card" title="Threads">
    <Echarts ref="chartRef" manual-update autoresize />
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, ref, toRefs, watch, PropType } from 'vue'

import { Echarts } from 'vue-echart5'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

import { loadMetricsDataVue } from '@/modules/monitor/utils/ClientMetrisData'

import dayjs from 'dayjs'

const createOption = (data: Array<any>) => {
  const xdata = data.map(item => {
    return item.time
  })
  const dataLive = data.map(item => {
    return item.data[0].value
  })
  const dataDaemon = data.map(item => {
    return item.data[1].value
  })
  return {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata
    },
    yAxis: {
      type: 'value'
    },
    legend: {
      data: ['活动线程', '守护线程']
    },
    series: [
      {
        data: dataLive,
        type: 'line',
        name: '活动线程',
        areaStyle: {}
      },
      {
        data: dataDaemon,
        type: 'line',
        name: '守护线程',
        areaStyle: {}
      }
    ]
  }
}

/**
 * 线程管理
 */
export default defineComponent({
  name: 'ClientThreads',
  components: {
    ClientCard,
    Echarts
  },
  props: {
    time: {
      type: Number as PropType<number>,
      required: true
    },
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {
    const chartRef = ref()
    const { time, clientId } = toRefs(props)
    const { computedData } = loadMetricsDataVue(clientId, time, ['jvm.threads.live', 'jvm.threads.daemon', 'jvm.threads.peak', 'jvm.threads.states'])
    const allDataR: Array<any> = []
    const allData = ref(allDataR)
    watch(computedData, () => {
      if (computedData.value.length > 0) {
        if (allData.value.length >= 50) {
          allData.value.shift()
        }
        allData.value.push({
          time: dayjs().format('HH:mm:ss'),
          data: computedData.value
        })
      }
      chartRef.value.mergeOptions(createOption(allData.value), false)
    })
    return {
      allData,
      chartRef
    }
  }
})
</script>

<style lang="less" scoped>
.client-card {
  ::v-deep(.ant-card-body) {
    padding: 0 !important;
    height: calc(100% - 58px);
  }
}
</style>
