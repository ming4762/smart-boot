<template>
  <ClientCard title="Redis key" class="full-height client-card">
    <Echarts ref="chartRef" manual-update autoresize />
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, watch, toRefs, ref } from 'vue'
import type { PropType } from 'vue'

import { Echarts } from 'vue-echart5'
import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

import dayjs from 'dayjs'

const createChartOption = (data: Array<any>) => {
  const xdata = data.map((item) => {
    return item.time
  })
  const keyData = data.map((item) => {
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
      name: 'Key数量',
      nameLocation: 'end',
      minInterval: 1
    },
    series: [
      {
        symbol: 'none',
        data: keyData,
        type: 'line',
        areaStyle: {}
      }
    ]
  }
}

export default defineComponent({
  name: 'RedisKeyChart',
  components: {
    Echarts,
    ClientCard
  },
  props: {
    data: {
      type: Object as PropType<any>,
      default: () => {}
    }
  },
  setup(props) {
    const { data } = toRefs(props)
    const chartRef = ref<any>()

    const allData: Array<any> = []
    /**
     * 添加数据函数
     */
    const addData = (meterData: Array<any>) => {
      if (meterData.length > 0) {
        if (allData.length >= 200) {
          allData.shift()
        }
        allData.push({
          time: dayjs().format('HH:mm:ss'),
          data: meterData[0].value
        })
      }
      chartRef.value.mergeOptions(createChartOption(allData), false)
    }
    watch(data, () => {
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
