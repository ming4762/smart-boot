import { ref, onMounted, Ref, watch, reactive } from 'vue'

import { errorMessage } from '@/components/notice/SystemNotice'
import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import StoreUtil from '@/common/utils/StoreUtil'

import TimeTaskUtil from '@/common/utils/TimeTaskUtil'
import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'

const meter_key = 'smart_monitor_select_metrics'
const meter_measurements_key = 'smart_monitor_metrics_measurements'

/**
 * 加载指标列表
 * @param clientId 客户端ID
 */
export const useLoadMeterList = (clientId: string) => {
  const meterList = ref<Array<any>>([])
  const currentMeter = ref('')
  /**
   * 加载meter列表
   */
  const loadMeterList = async () => {
    try {
      const { names } = await loadActuator(clientId, 'metrics')
      meterList.value = names.map((item: string) => {
        return {
          value: item,
          label: item
        }
      })
    } catch (e) {
      errorMessage(e)
      return false
    }
    if (meterList.value.length > 0) {
      currentMeter.value = meterList.value[0].label
    }
  }
  onMounted(loadMeterList)
  return {
    meterList,
    currentMeter
  }
}

/**
 * 加载meter tag
 * @param clientId 客户端
 * @param currentMeter 指标
 */
export const useLoadMeterTagList = (clientId: string, currentMeter: Ref<string>) => {
  const tagList = ref<Array<any>>([])
  // 存储meter measurements
  const meterMeasurementsMap: any = StoreUtil.getStore(meter_measurements_key) || {}

  /**
   * 加载tag函数
   */
  const loadMeterTagList = async () => {
    try {
      const { availableTags, measurements } = await loadActuator(clientId, `metrics/${currentMeter.value}`)
      tagList.value = availableTags
      if (!meterMeasurementsMap[currentMeter.value]) {
        meterMeasurementsMap[currentMeter.value] = measurements.map((item: any) => item.statistic)
        StoreUtil.setStore(meter_measurements_key, meterMeasurementsMap)
      }
    } catch (e) {
      errorMessage(e)
    }
  }
  watch(currentMeter, loadMeterTagList)

  return {
    tagList,
    meterMeasurementsMap
  }
}

export const useShowMeterData = (clientId: string, currentMeter: Ref) => {
  // 存储获取到的数据
  const meterDataMap = reactive(new Map())
  const tagModel = ref<any>({})
  const selectMeterList = ref<Array<any>>([])

  const handleTagChange = (tag: string, value: string) => {
    tagModel.value[tag] = value
  }
  /**
   * 监控指标变更，指标变更后清楚tag
   */
  watch(currentMeter, () => {
    tagModel.value = {}
  })

  /**
   * 添加指标函数
   */
  const handleAddMeter = () => {
    const meterName = currentMeter.value
    // 获取meter数据
    const meterData = selectMeterList.value.filter(item => item.name === meterName)
    if (meterData.length === 0) {
      selectMeterList.value.push({
        name: meterName,
        tags: [Object.assign({}, tagModel.value)],
        types: {}
      })
    } else {
      // 判断是否已经包含指定的tag
      const tagModelStr = JSON.stringify(tagModel.value)
      // 防止重复添加tag
      if (!meterData[0].tags.some((item: any) => JSON.stringify(item) === tagModelStr)) {
        meterData[0].tags.push(Object.assign({}, tagModel.value))
      }
    }
    loadMeterData()
    saveSelectMeterList(selectMeterList.value)
  }

  /**
   * 移除meter
   * @param meterName
   * @param removeTag
   */
  const handleRemoveMeter = (meterName: string, removeTag: any) => {
    const result: Array<any> = []
    selectMeterList.value.forEach(meter => {
      if (meter.name !== meterName) {
        result.push(meter)
      } else {
        const { tags } = meter
        if (tags.length > 0) {
          for (let i=0; i<tags.length; i++) {
            const tag = tags[i]
            if (JSON.stringify(tag) === JSON.stringify(removeTag)) {
              tags.splice(i, 1)
              break
            }
          }
          if (tags.length > 0) {
            result.push(meter)
          }
        }
      }
    })
    saveSelectMeterList(result)
  }
  /**
   * 保存meter信息
   */
  const saveSelectMeterList = (meterList: any) => {
    selectMeterList.value = [].concat(meterList)
    // 保存到存储
    StoreUtil.setStore(meter_key, selectMeterList.value)
  }


  /**
   * 加载meter数据
   */
  const loadMeterData = () => {
    const promiseList: Array<Promise<any>> = []
    selectMeterList.value.forEach(({name, tags}: any) => {
      tags.forEach((tag: any) => {
        const key = `${name}/${JSON.stringify(tag)}`
        const tagParameter = Object.keys(tag)
          .map(item => `${item}:${tag[item]}`)
          .join(',')
        // 加载数据
        promiseList.push(
          loadActuator(clientId, `metrics/${name}?tag=${encodeURI(tagParameter)}`)
            .then(({ measurements }) => {
              return {
                key: key,
                data: measurements
              }
            })
        )
      })
    })
    Promise.all(promiseList)
      .then((data) => {
        // 将data转为map
        meterDataMap.clear()
        data.forEach(({ key, data }: any) => {
          meterDataMap.set(key, data)
        })
      })
  }

  /**
   * 修改 statistic显示类型
   * @param meterName
   * @param statistic
   * @param type
   */
  const handleChangeType= (meterName: string, statistic: string, type: string) => {
    // 获取meter
    const meterList = selectMeterList.value.filter(item => item.name === meterName)
    if (meterList.length > 0) {
      const meter = meterList[0]
      meter.types[statistic] = type
    }
    saveSelectMeterList(selectMeterList.value)
  }

  onMounted(() => {
    selectMeterList.value = StoreUtil.getStore(meter_key) || []
    loadMeterData()
    TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, 'loadMeterData', loadMeterData)
  })

  return {
    tagModel,
    handleTagChange,
    handleAddMeter,
    loadMeterData,
    selectMeterList,
    handleRemoveMeter,
    meterDataMap,
    handleChangeType
  }
}
