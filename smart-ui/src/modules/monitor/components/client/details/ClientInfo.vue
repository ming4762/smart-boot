<template>
  <ClientCard class="full-height" title="Info" />
</template>

<script lang="ts">
import { defineComponent, onMounted, toRefs, ref, watch } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'

/**
 * 客户端信息
 */
export default defineComponent({
  name: 'ClientInfo',
  components: {
    ClientCard
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup(props) {
    const { clientId } = toRefs(props)
    const data = ref({})
    // 加载数据函数
    const loadInfoData = async () => {
      data.value = await loadActuator(props.clientId, 'info')
    }
    // 页面加载执行
    watch([clientId], loadInfoData)
    onMounted(loadInfoData)
    return {
      data
    }
  }
})
</script>

<style scoped></style>
