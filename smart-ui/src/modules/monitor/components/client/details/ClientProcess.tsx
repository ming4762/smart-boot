import { defineComponent, toRefs } from 'vue'
import type { PropType } from 'vue'

import ClientCard from '../../common/ClientCard.vue'
import { loadMetricsDataVue } from '@/modules/monitor/utils/ClientMetrisData'

/**
 * 进程相关组件
 */
export default defineComponent({
  name: 'ClientProcess',
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
  setup(props) {
    const { time, clientId } = toRefs(props)
    const { computedData } = loadMetricsDataVue(clientId, time, [
      'process.uptime',
      'process.cpu.usage',
      'system.cpu.usage',
      'system.cpu.count'
    ])
    return {
      computedData
    }
  },
  data() {
    return {
      span: 6,
      itemStyle: {
        'text-align': 'center'
      }
    }
  },
  render() {
    return (
      <client-card class="full-height" title="Process">
        <a-row>
          {
            this.computedData.map((item) => {
              return (
                <a-col style={this.itemStyle} span={this.span}>{item.statistic}</a-col>
              )
            })
          }
        </a-row>
        <a-row style="margin-top: 5px">
          {
            this.computedData.map(item => {
              return (
                <a-col style={this.itemStyle} span={this.span}>{item.formatter ? item.formatter(item.value) : item.value}</a-col>
              )
            })
          }
        </a-row>
      </client-card>
    )
  }
})
