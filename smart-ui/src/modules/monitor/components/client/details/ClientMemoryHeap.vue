<template>
  <ClientCard class="full-height client-card" title="Memory Heap">
    <a-row style="margin-top: 10px">
      <a-col class="center" :span="8">
        <div style="width: 11px; height: 11px; display: inline-block; background: #3ba272"> </div>
        <div style="display: inline-block; ">已用</div>
      </a-col>
      <a-col class="center" :span="8">
        <div style="width: 11px; height: 11px; display: inline-block; background: #fc8452"> </div>
        <div style="display: inline-block; ">当前可用</div>
      </a-col>
      <a-col class="center" :span="8">
        最大
      </a-col>
    </a-row>
    <a-row style="margin-top: 5px">
      <a-col class="center" :span="8">{{ currentUsed }} MB</a-col>
      <a-col class="center" :span="8">{{ currentCommitted }} MB</a-col>
      <a-col class="center" :span="8">{{ currentMax }} MB</a-col>
    </a-row>
    <a-row style="height: 370px">
      <a-col :span="24">
        <Echarts ref="chartRef" manual-update autoresize />
      </a-col>
    </a-row>
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, ref, watch } from 'vue'

import { Echarts } from 'vue-echart5'
import { loadMetricsDataVue } from '@/modules/monitor/utils/ClientMetrisData'
import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

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
      type: 'value',
      name: '单位：Mb',
      nameLocation: 'end'
    },
    color: ['#fc8452', '#3ba272'],
    series: [

      {
        data: dataDaemon,
        type: 'line',
        name: '当前可用',
        areaStyle: {}
      },
      {
        data: dataLive,
        type: 'line',
        name: '已用',
        areaStyle: {}
      }
    ]
  }
}

const converToMb = (value: number) => {
  return Math.floor(value / 1024 / 1024 * 100) /100
}

export default defineComponent({
  name: 'ClientMemoryHeap',
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
    const { computedData } = loadMetricsDataVue(clientId, time, ['jvm.memory.used', 'jvm.memory.committed', 'jvm.memory.max'])
    const allData = ref<Array<any>>([])

    const currentUsed = ref(0)
    const currentCommitted = ref(0)
    const currentMax = ref(0)
    const addData = () => {
      if (computedData.value.length > 0) {
        currentUsed.value = converToMb(computedData.value[0].value)
        currentCommitted.value = converToMb(computedData.value[1].value)
        currentMax.value = converToMb(computedData.value[2].value)

        if (allData.value.length >= 100) {
          allData.value.shift()
        }
        allData.value.push({
          time: dayjs().format('HH:mm:ss'),
          data: computedData.value.map(item => {
            return Object.assign(item, {
              value: converToMb(item.value)
            })
          })
        })
      }
      chartRef.value.mergeOptions(createOption(allData.value), false)
    }
    // addData()
    watch(computedData, addData)
    return {
      allData,
      chartRef,
      currentUsed,
      currentCommitted,
      currentMax
    }
  }
})
</script>

<style lang="less" scoped>
  .client-card ::v-deep(.ant-card-body) {
    height: calc(100% - 58px);
    padding: 0;
  }
  .center {
    text-align: center;
  }
</style>
