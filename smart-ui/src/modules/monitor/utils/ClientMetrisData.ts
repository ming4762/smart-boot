import { onMounted, watch, ref, computed } from 'vue'
import type { Ref } from 'vue'

import type { Measurement, MeterData } from '@/modules/monitor/typing'
import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import TimeUtils from '@/common/utils/TimeUtils'

const usageFormatter = (value: number) => {
  return (value * 100).toFixed(3) + '%'
}

/**
 * 指标数据信息
 */
const metricsMap: { [index: string]: MeterData } = {
  'process.uptime': {
    key: 'process.uptime',
    name: 'Uptime',
    measurements: [
      {
        statistic: 'Uptime',
        value: 0
      }
    ],
    formatter: (value: number) => {
      return TimeUtils.convertDuration(value * 1000)
    }
  },
  'process.cpu.usage': {
    key: 'process.cpu.usage',
    name: 'Process CPU Usage',
    measurements: [
      {
        statistic: 'Process CPU Usage',
        value: 0
      }
    ],
    formatter: usageFormatter
  },
  'system.cpu.usage': {
    key: 'system.cpu.usage',
    name: 'System CPU Usage',
    measurements: [
      {
        statistic: 'System CPU Usage',
        value: 0
      }
    ],
    formatter: usageFormatter
  },
  'system.cpu.count': {
    key: 'system.cpu.count',
    name: 'System CPU Count',
    measurements: [
      {
        statistic: 'System CPU Count',
        value: 0
      }
    ]
  },
  'jvm.gc.pause': {
    key: 'jvm.gc.pause',
    name: '',
    measurements: [
      {
        statistic: 'COUNT',
        value: 0
      },
      {
        statistic: 'TOTAL TIME',
        value: 0
      },
      {
        statistic: 'MAX TIME',
        value: 0
      }
    ],
    formatter: (value: number) => {
      if (value.toString().split('.').length > 1) {
        return value.toFixed(3)
      }
      return value
    }
  },
  'jvm.threads.live': {
    key: 'jvm.threads.live',
    name: '',
    measurements: [
      {
        statistic: 'live',
        value: 0
      }
    ]
  },
  'jvm.threads.daemon': {
    key: 'jvm.threads.daemon',
    name: '',
    measurements: [
      {
        statistic: 'daemon',
        value: 0
      }
    ]
  },
  'jvm.threads.peak': {
    key: 'jvm.threads.peak',
    name: '',
    measurements: [
      {
        statistic: 'peak',
        value: 0
      }
    ]
  },
  'jvm.threads.states': {
    key: 'jvm.threads.states',
    name: '',
    measurements: [
      {
        statistic: 'states',
        value: 0
      }
    ]
  },
  'jvm.memory.committed': {
    key: 'jvm.memory.committed',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ],
    formatter: (value: number) => {
      return value / 1024 / 1024
    }
  },
  'jvm.memory.max': {
    key: 'jvm.memory.max',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ],
    formatter: (value: number) => {
      return value / 1024 / 1024
    }
  },
  'jvm.memory.used': {
    key: 'jvm.memory.used',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ],
    formatter: (value: number) => {
      return value / 1024 / 1024
    }
  },
  'druid.datasource.poolingPeak': {
    key: 'druid.datasource.poolingPeak',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ]
  },
  'druid.datasource.poolingCount': {
    key: 'druid.datasource.poolingCount',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ]
  },
  'druid.datasource.activeCount': {
    key: 'druid.datasource.activeCount',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ]
  },
  'druid.datasource.activePeak': {
    key: 'druid.datasource.activePeak',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ]
  },
  'redis.keys': {
    key: 'redis.keys',
    name: '',
    measurements: [
      {
        statistic: 'value',
        value: 0
      }
    ]
  }
}

interface MeterTag {
  meter: string
  tag: string
}

/**
 * 转换为meter tag
 * @param metrics
 */
const convertMeterTag = (metrics: Array<string>): Array<MeterTag> => {
  return metrics.map((meter) => {
    const meterTagArray = meter.split('?')
    const tag = meterTagArray.length > 1 ? meterTagArray[1] : ''
    return {
      meter: meterTagArray[0],
      tag: tag
    }
  })
}

/**
 * 加载指标数据
 * @param clientId 客户端ID
 * @param metrics 指标列表
 */
export const loadMetricsData = async (
  clientId: string,
  metrics: Array<string>
): Promise<Array<MeterData>> => {
  // 分离tag
  const meterTags = convertMeterTag(metrics)
  // 获取所有指标
  const metricsData: Array<MeterData> = meterTags.map((meterTag) => {
    return Object.assign({}, metricsMap[meterTag.meter])
  })
  const promiseList = meterTags.map((meterTag) => {
    let type = meterTag.meter
    if (meterTag.tag.length > 0) {
      type += `?${meterTag.tag}`
    }
    // 获取类型
    return loadActuator(clientId, `metrics/${type}`).then((data: any) => {
      return Promise.resolve(data.measurements)
    })
  })
  return Promise.all(promiseList).then((data) => {
    for (let i = 0; i < metricsData.length; i++) {
      const measurements: Array<Measurement> = metricsData[i].measurements
      for (let j = 0; j < measurements.length; j++) {
        const item = data[i][j]
        metricsData[i].measurements[j] = {
          statistic: measurements[j].statistic === '' ? item.statistic : measurements[j].statistic,
          value: item.value
        }
      }
    }
    return Promise.resolve(metricsData)
  })
}

/**
 * 加载指标数据
 * @param clientId
 * @param time
 * @param metrics
 */
export const loadMetricsDataVue = (
  clientId: Ref<string>,
  time: Ref<number>,
  metrics: Array<string>
) => {
  const meterData: Array<MeterData> = []
  const data = ref(meterData)
  const doLoadData = async () => {
    data.value = await loadMetricsData(clientId.value, metrics)
  }
  onMounted(doLoadData)
  watch([time, clientId], doLoadData)
  const computedData = computed(() => {
    const result: Array<any> = []
    data.value.forEach((item) => {
      item.measurements.forEach((measurement) => {
        result.push(
          Object.assign(
            {
              formatter: item.formatter
            },
            measurement
          )
        )
      })
    })
    return result
  })
  return {
    data,
    computedData
  }
}
