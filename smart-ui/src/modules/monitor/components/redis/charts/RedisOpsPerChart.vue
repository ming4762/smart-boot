<template>
  <ClientCard title="每秒执行命令" class="full-height client-card">
    <Echarts ref="chartRef" manual-update autoresize />
  </ClientCard>
</template>

<script lang="ts">
import {defineComponent, PropType, ref, toRefs, watch} from 'vue'

import dayjs from 'dayjs'

import { Echarts } from 'vue-echart5'
import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

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
      name: '次/S',
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

export default defineComponent({
  name: 'RedisOpsPerChart',
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

    const allData: Array<any> = []

    const addData = (data: number) => {
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

<style scoped lang="less">
.client-card ::v-deep(.ant-card-body) {
  height: calc(100% - 58px);
  padding: 0;
}
</style>
