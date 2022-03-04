<template>
  <ClientCard class="full-height client-card" title="Threads">
    <a-row style="margin-top: 10px">
      <a-col class="center" :span="8">
        <div style="width: 11px; height: 11px; display: inline-block; background: #3ba272"> </div>
        <div style="display: inline-block; ">活动线程</div>
      </a-col>
      <a-col class="center" :span="8">
        <div style="width: 11px; height: 11px; display: inline-block; background: #fc8452"> </div>
        <div style="display: inline-block; ">守护线程</div>
      </a-col>
      <a-col class="center" :span="8">
        最大
      </a-col>
    </a-row>
    <a-row style="margin-top: 5px">
      <a-col class="center" :span="8">{{ currentLive }}</a-col>
      <a-col class="center" :span="8">{{ currentDaemon }}</a-col>
      <a-col class="center" :span="8">{{ currentPeak }}</a-col>
    </a-row>
    <a-row style="height: 280px">
      <a-col :span="24">
        <Echarts ref="chartRef" manual-update autoresize />
      </a-col>
    </a-row>
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
    grid: {
      top: 20,
      bottom: 40
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: xdata
    },
    yAxis: {
      type: 'value'
    },
    color: ['#fc8452', '#3ba272'],
    series: [
      {
        data: dataLive,
        type: 'line',
        name: '活动线程',
        symbol: 'none',
        areaStyle: {}
      },
      {
        data: dataDaemon,
        type: 'line',
        name: '守护线程',
        symbol: 'none',
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
    const currentLive = ref(0)
    const currentDaemon = ref(0)
    const currentPeak = ref(0)
    const { computedData } = loadMetricsDataVue(clientId, time, ['jvm.threads.live', 'jvm.threads.daemon', 'jvm.threads.peak'])
    const allDataR: Array<any> = []
    const allData = ref(allDataR)
    watch(computedData, () => {
      if (computedData.value.length > 0) {
        currentLive.value = computedData.value[0].value
        currentDaemon.value = computedData.value[1].value
        currentPeak.value = computedData.value[2].value

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
      chartRef,
      currentLive,
      currentDaemon,
      currentPeak
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
.center {
  text-align: center;
}
</style>
