import { ref } from 'vue'
import { loadActuatorList } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 端点hook
 * @param clientId 客户端ID
 */
export const useActuator = (clientId: string) => {
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
  loadActuators()

  /**
   * 是否有指定端点
   * @param actuator
   */
  const hasActuator = (actuator: string) => {
    return hasActuators([actuator])
  }

  /**
   * 是否包含所有指定的端点
   * @param actuatorList
   */
  const hasActuators = (actuatorList: Array<string>): boolean => {
    if (actuatorList.length === 0) {
      return true
    }
    return actuatorList.every(item => actuators.value[item] !== undefined)
  }
  return {
    actuators,
    loadActuators,
    hasActuator,
    hasActuators
  }
}
