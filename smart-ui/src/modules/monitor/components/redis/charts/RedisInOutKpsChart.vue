<template>
  <ClientCard title="Redis Kps" class="full-height client-card">
    <Echarts ref="chartRef" manual-update autoresize />
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, ref, toRefs, watch } from 'vue'
import type { PropType } from 'vue'

import dayjs from 'dayjs'

import { Echarts } from 'vue-echart5'
import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

const createChartOption = (data: Array<any>) => {
  const xdata = data.map((item) => {
    return item.time
  })
  const inputData = data.map((item) => {
    return item.data.input
  })
  const outputData = data.map((item) => {
    return item.data.output
  })
  return {
    tooltip: {
      trigger: 'axis'
    },
    legend: {},
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata
    },
    yAxis: {
      type: 'value',
      name: 'kbps',
      nameLocation: 'end'
    },
    series: [
      {
        symbol: 'none',
        data: inputData,
        name: 'input',
        type: 'line',
        areaStyle: {}
      },
      {
        symbol: 'none',
        data: outputData,
        name: 'output',
        type: 'line',
        areaStyle: {}
      }
    ]
  }
}

/**
 * redis网络入口/出口kps chart
 */
export default defineComponent({
  name: 'RedisInOutKpsChart',
  components: {
    Echarts,
    ClientCard
  },
  props: {
    data: {
      type: Object as PropType<object>,
      required: true
    },
    time: {
      type: Number as PropType<number>,
      default: -1
    }
  },
  setup(props) {
    const chartRef = ref()
    const { data, time } = toRefs(props)

    const allData: Array<any> = []

    const addData = (data: any) => {
      if (allData.length >= 200) {
        allData.shift()
      }
      allData.push({
        time: dayjs().format('HH:mm:ss'),
        data: data
      })
      chartRef.value.mergeOptions(createChartOption(allData), false)
    }

    watch(time, () => {
      addData(data.value)
    })

    return {
      chartRef
    }
  }
})
</script>

<style lang="less" scoped>
.client-card ::v-deep(.ant-card-body) {
  height: calc(100% - 58px);
  padding: 0;
}
</style>
