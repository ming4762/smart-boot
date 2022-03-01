<template>
  <ClientCard title="Redis CPU" class="full-height client-card">
    <Echarts ref="chartRef" manual-update autoresize />
  </ClientCard>
</template>

<script lang="ts">
import {defineComponent, PropType, ref, toRefs, watch} from 'vue'

import dayjs from 'dayjs'

import { Echarts } from 'vue-echart5'
import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

const pasrseToSecond = (date: Date) => {
  return parseInt((date.getTime()/1000).toFixed())
}

const createChartOption = (data: Array<any>) => {
  const xdata = data.map(item => {
    return item.time
  })
  const valueData = data.map(item => {
    return item.data
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
      type: 'value',
      name: 'CPU使用率(%)',
      nameLocation: 'end'
    },
    series: [
      {
        symbol: 'none',
        data: valueData,
        type: 'line',
        areaStyle: {}
      }
    ]
  }
}

/**
 * redis cpu使用率图标
 */
export default defineComponent({
  name: 'RedisCpuChart',
  components: {
    Echarts,
    ClientCard
  },
  props: {
    data: {
      type: Number as PropType<number>,
      default: 0
    },
    time: {
      type: Number as PropType<number>,
      default: -1
    }
  },
  setup (props) {
    const chartRef = ref()
    const { data, time } = toRefs(props)

    let beforeData = 0
    let beforeTime: Date | null = null
    const allData: Array<any> = []

    /**
     * 添加数据函数
     * @param currentData
     */
    const addData = (currentData: number) => {
      const currentTime = new Date()
      if (beforeData === 0 || beforeTime === null) {
        beforeTime = currentTime
        beforeData = currentData
        return
      }
      if (allData.length >= 200) {
        allData.shift()
      }
      allData.push({
        time: dayjs().format('HH:mm:ss'),
        data: ((currentData - beforeData)/(pasrseToSecond(currentTime) - pasrseToSecond(beforeTime)) * 100).toFixed(3)
      })
      beforeTime = currentTime
      beforeData = currentData
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

<style  lang="less" scoped>
.client-card ::v-deep(.ant-card-body) {
  height: calc(100% - 58px);
  padding: 0;
}
</style>
