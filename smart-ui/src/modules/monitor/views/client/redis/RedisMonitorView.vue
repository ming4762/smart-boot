<template>
  <div class="full-height" style="padding: 10px">
    <div class="full-height" style="overflow: auto">
      <div>
        <RedisInfoCharts :data="data" :client-id="clientId" />
      </div>
      <div class="grid-container">
        <RedisInfoGrid stripe highlight-hover-row border :data="data" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, onBeforeUnmount } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'
import TimeTaskUtil from '@/common/utils/TimeTaskUtil'
import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'

import RedisInfoGrid from '@/modules/monitor/components/redis/RedisInfoGrid.vue'
import RedisInfoCharts from '@/modules/monitor/components/redis/RedisInfoCharts.vue'

/**
 * redis info页面
 */
export default defineComponent({
  name: 'RedisMonitorView',
  components: {
    RedisInfoGrid,
    RedisInfoCharts
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup(props) {
    const data = ref<Array<any>>([])
    /**
     * 加载redis信息
     */
    const loadRedisInfo = async () => {
      try {
        data.value = await loadActuator(props.clientId, 'redisInfo/all')
      } catch (e) {
        errorMessage(e)
      }
    }
    onMounted(() => {
      loadRedisInfo()
      // 添加循环获取数据
      TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, 'redisInfo', loadRedisInfo)
    })
    // 页面销毁时，移除定时任务
    onBeforeUnmount(() => TimeTaskUtil.removeLoop(MONITOR_DETAIL_LOOP_GROUP, 'redisInfo'))
    return {
      data
    }
  }
})
</script>

<style lang="less" scoped>
.grid-container {
  margin-top: 10px;
}
</style>
