import { ref, createVNode, onMounted, computed, watch, reactive } from 'vue'

import { Modal, notification } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import { loadActuator, loadActuatorList } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'
import ApiService from '@/common/utils/ApiService'

import { changeRefresh, executeHandler } from '@/modules/monitor/utils/ClientRefresh'

const key = 'shutdownNotify'

/**
 * shut down
 */
export const useShutdown = (clientId: string) => {

  const shutdownLoading = ref(false)
  const shutdownCloseTime = ref(10)

  /**
   * 关闭应用函数
   */
  const handleShutdown = () => {
    Modal.confirm({
      title: '确定要关闭应用实例吗?',
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        shutdownLoading.value = true
        try {
          await loadActuator(clientId, 'shutdown')
        } catch (e) {
          errorMessage(e)
          return false
        } finally {
          shutdownLoading.value = false
        }
        notification.success({
          key: key,
          message: `应用实例已关闭，该页面将在${shutdownCloseTime.value}s后关闭`,
          duration: 10
        })
        setInterval(() => {
          shutdownCloseTime.value = shutdownCloseTime.value - 1
          if (shutdownCloseTime.value > 0) {
            notification.success({
              key: key,
              message: `应用实例已关闭，页面将在${shutdownCloseTime.value}s后关闭`,
              duration: shutdownCloseTime.value
            })
          } else {
            // window.close()
          }
        }, 1000)
      }
    })
  }

  return {
    shutdownLoading,
    handleShutdown
  }
}

/**
 * 加载应用hook
 * @param clientId 客户端ID
 */
export const useLoadApplication = (clientId: string) => {
  const applicationData = ref<any>({
    name: ''
  })
  onMounted(async () => {
    try {
      applicationData.value = await ApiService.postAjax('monitor/manager/client/getApplicationByClientId', clientId)
      document.title = applicationData.value.name
    } catch (e) {
      errorMessage(e)
    }
  })
  const computedApplicationName = computed(() => {
    const name = `${applicationData.value.name} [${applicationData.value.applicationCode}]`
    document.title = name
    return name
  })
  return {
    applicationData,
    computedApplicationName
  }
}

// 刷新时间
const refreshTimes = () => {
  const times = [5, 10, 20, 30, 60]
  const result: Array<any> = []
  result.push({
    key: -1,
    value: '不刷新'
  })
  times.forEach(item => {
    result.push({
      key: item,
      value: item + 'S'
    })
  })
  return result
}

/**
 * 刷新客户端hook
 */
export const useRefreshClient = () => {
  const refreshTimeModel = ref<number>(10)
  /**
   * 刷新时间变更时改变刷新
   */
  watch(refreshTimeModel, () => {
    changeRefresh(refreshTimeModel.value)
  })
  onMounted(() => {
    changeRefresh(refreshTimeModel.value)
    /**
     * 执行刷新函数
     */
    executeHandler()
  })
  return {
    refreshTimes: reactive(refreshTimes()),
    refreshTimeModel
  }
}

/**
 * 加载客户端端点
 * @param clientId 客户端ID
 */
export const useLoadActuator = (clientId: string) => {
  const actuators = ref<any>({})
  /**
   * 加载端点函数
   */
  const loadActuators = async () => {
    try {
      const result = await loadActuatorList(clientId)
      if (result && result._links) {
        actuators.value = result._links
      }
    } catch (e) {
      errorMessage(e)
    }
  }
  return {
    actuators,
    loadActuators
  }
}
