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
            <ClientMetadata />
          </a-col>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, ref, onMounted } from 'vue'

import { addRefresh } from '@/modules/monitor/utils/ClientRefresh'
import ApiService from '@/common/utils/ApiService'

import ClientInfo from './ClientInfo.vue'
import ClientMetadata from './ClientMetadata.vue'

/**
 * 客户端详情
 */
export default defineComponent({
  name: 'ClientDetail',
  components: {
    ClientInfo,
    ClientMetadata
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
    addRefresh('detail', () => {
      time.value++
    })
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
