import { ref, reactive, watch, onMounted, onBeforeUnmount } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'
import TimeTaskUtil from '@/common/utils/TimeTaskUtil'
import { downActuator } from '@/modules/monitor/utils/ClientApiUtils'
import fileDownload from 'js-file-download'

/**
 * 加载数据函数
 */
export const handleLoadData = async () => {
  try {
    return await ApiService.postAjax('monitor/manager/client/listUserClient', 'false')
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

const refreshTimes = () => {
  const times = [5, 10, 20, 30, 60]
  const result = []
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
 * 刷新数据hook
 */
export const useRefreshData = (loadData: Function) => {
  // 刷新时间
  const refreshTimeModel = ref(10)
  let lookup: any = null

  /**
   * 更改刷新
   */
  const changeRefresh = () => {
    if (lookup !== null) {
      clearInterval(lookup)
    }
    const refreshTime = refreshTimeModel.value
    if (refreshTime !== -1) {
      lookup = TimeTaskUtil.loop(() => {
        loadData()
      }, -1, refreshTime * 1000)
    }
  }
  // 时间间隔变更后，重新刷新
  watch(refreshTimeModel, changeRefresh)

  onMounted(changeRefresh)
  onBeforeUnmount(() => {
    if (lookup != null) {
      clearInterval(lookup)
    }
  })

  return {
    refreshTimes: reactive(refreshTimes()),
    refreshTimeModel
  }
}

/**
 * 下载内存转储
 */
export const handlerDownloadThreaddump = async (id: string, applicationName: string) => {
  downActuator(id, 'threaddump', null, 'GET', {}, {
    headers: {'Accept': 'text/plain'}
  }).then(result => {
    const blob = new Blob([result], {type: 'text/plain;charset=utf-8'})
    fileDownload(blob, applicationName + '-threaddump.txt')
  })
}
