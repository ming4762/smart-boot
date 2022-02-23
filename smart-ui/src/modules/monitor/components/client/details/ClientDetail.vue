<template>
  <div>
    <a-row :gutter="gutter">
      <a-col :span="12">
        <a-row>
          <a-col :span="24">
            <ClientInfo :client-id="clientId" />
          </a-col>
        </a-row>

        <a-row class="detail-span">
          <a-col :span="24">
            <ClientMetadata :data="clientData" />
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="12">
        <client-health :client-id="clientId" :time="time" />
      </a-col>
    </a-row>

    <a-row class="detail-span large" :gutter="gutter">
      <a-col :span="12">
        <a-row>
          <a-col :span="24">
            <ClientProcess :client-id="clientId" :time="time" />
          </a-col>
        </a-row>
        <a-row :span="24" class="detail-span">
          <a-col :span="24">
            <client-gc :client-id="clientId" :time="time" />
          </a-col>
        </a-row>
      </a-col>
      <a-col :span="12">
        <ClientThreads :client-id="clientId" :time="time" />
      </a-col>
    </a-row>

    <a-row class="detail-span large" style="height: 400px" :gutter="gutter">
      <a-col :span="12">
        <ClientMemoryHeap :client-id="clientId" :time="time" />
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, ref, onMounted, onBeforeUnmount } from 'vue'

import TimeTaskUtil from '@/common/utils/TimeTaskUtil'
import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'

import ApiService from '@/common/utils/ApiService'

import ClientInfo from './ClientInfo.vue'
import ClientMetadata from './ClientMetadata.vue'
import ClientHealth from './ClientHealth.vue'
import ClientProcess from './ClientProcess'
import ClientGc from './ClientGc.vue'
import ClientThreads from './ClientThreads.vue'
import ClientMemoryHeap from './ClientMemoryHeap.vue'

/**
 * 客户端详情
 */
export default defineComponent({
  name: 'ClientDetail',
  components: {
    ClientInfo,
    ClientMetadata,
    ClientHealth,
    ClientProcess,
    ClientGc,
    ClientThreads,
    ClientMemoryHeap
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup (props) {
    const { clientId } = toRefs(props)

    // 客户端数据
    const clientData = ref({})
    onMounted(async () => {
      clientData.value = await ApiService.postAjax(`monitor/manager/client/getClientById/${clientId.value}`, 'false')
    })
    // 设置定时执行任务
    const time = ref(0)
    const loopKey = 'monitor_detail'
    TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, loopKey, () => {
      time.value ++
    })
    onBeforeUnmount(() => TimeTaskUtil.removeLoop(MONITOR_DETAIL_LOOP_GROUP, loopKey))
    return {
      time,
      clientData
    }
  },
  data () {
    return {
      gutter: 16
    }
  }
})
</script>

<style lang="less" scoped>
.detail-span {
  margin-top: 15px;
  .large {
    margin-top: 25px;
  }
}
</style>
