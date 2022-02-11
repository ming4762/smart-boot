<template>
  <ClientCard class="full-height" title="Garbage Collection">
    <a-row>
      <a-col v-for="item in computedData" :key="'gc-title' + item.statistic" class="center" :span="span">
        {{ item.statistic }}
      </a-col>
    </a-row>
    <a-row style="margin-top: 5px">
      <a-col v-for="item in computedData" :key="'gc-value' + item.statistic" class="center" :span="span">
        {{ item.formatter ? item.formatter(item.value) : item.value }}
      </a-col>
    </a-row>
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, toRefs, PropType } from 'vue'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'
import { loadMetricsDataVue } from '@/modules/monitor/utils/ClientMetrisData'

/**
 * JAVA GC 信息
 */
export default defineComponent({
  name: 'ClientGc',
  components: {
    ClientCard
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
    const { time, clientId } = toRefs(props)
    const { computedData } = loadMetricsDataVue(clientId, time, ['jvm.gc.pause'])
    return {
      computedData
    }
  },
  data () {
    return {
      span: 8
    }
  }
})
</script>

<style lang="less" scoped>
  .center {
    text-align: center;
  }
</style>
