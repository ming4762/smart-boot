<template>
  <ClientCard class="full-height" title="Metadata">
    <CommonTable :data="computedMetadata" />
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'
import CommonTable from '@/modules/monitor/components/common/CommonTable.vue'

export default defineComponent({
  name: 'ClientMetadata',
  components: {
    ClientCard,
    CommonTable
  },
  props: {
    data: {
      type: Object,
      required: false
    }
  },
  computed: {
    /**
     * 元数据计算属性
     * @returns {*|{}}
     */
    computedMetadata() {
      if (this.data && this.data.application) {
        const metadata = this.data.application.metadata || {}
        metadata.startupTime = new Date(this.data.application.startupTime)
        return {
          details: metadata
        }
      }
      return {}
    }
  }
})
</script>
