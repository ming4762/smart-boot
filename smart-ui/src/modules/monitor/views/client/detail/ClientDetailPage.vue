<template>
  <div class="full-height" style="padding: 10px; background: white; overflow: auto">
    <ClientDetail :client-id="clientId" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import ClientDetail from '@/modules/monitor/components/client/details/ClientDetail.vue'

/**
 * 客户端详情页
 */
export default defineComponent({
  name: 'ClientDetailPage',
  components: {
    ClientDetail
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  }
})
</script>

<style scoped>

</style>
