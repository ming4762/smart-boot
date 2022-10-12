<template>
  <ClientCard class="full-height" title="Health">
    <table class="health-table">
      <tr>
        <td colspan="2">
          <span style="font-weight: bold">Client</span>
          <span class="status" :class="data.status === 'UP' ? 'up' : 'down'">
            {{ data.status }}
          </span>
        </td>
      </tr>
      <a-divider />
      <tr v-if="computedDetails.length > 0">
        <td colspan="2">
          <DetailTable
            v-for="detail in computedDetails"
            :key="'health-detail' + detail.title"
            :data="detail" />
        </td>
      </tr>
    </table>
  </ClientCard>
</template>

<script lang="ts">
import { defineComponent, ref, toRefs, watch, onMounted } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'
import DetailTable from '@/modules/monitor/components/common/DetailTable.vue'

/**
 * 健康状态页面
 */
export default defineComponent({
  name: 'ClientHealth',
  components: {
    ClientCard,
    DetailTable
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    },
    time: {
      required: true,
      type: Number
    }
  },
  setup(props) {
    const { clientId, time } = toRefs(props)
    const data = ref<any>({})
    // 加载数据函数
    const doLoadData = async () => {
      data.value = await loadActuator(props.clientId, 'health')
    }
    // 页面加载执行
    watch([clientId, time], doLoadData)
    onMounted(() => {
      doLoadData()
    })
    return {
      data
    }
  },
  computed: {
    computedDetails() {
      const details = this.data.components
      if (!details) {
        return []
      }
      const result: Array<any> = []
      Object.keys(details).forEach((key) => {
        const detailData = details[key]
        result.push({
          title: key,
          status: detailData.status,
          details: detailData.details ? detailData.details : {}
        })
      })
      return result
    }
  }
})
</script>

<style lang="less" scoped>
.health-table {
  width: 100%;
  .status {
    float: right;
    font-weight: bold;
  }
  .up {
    color: #4ec677;
  }
  .down {
    color: red;
  }
  .ant-divider-horizontal {
    margin: 18px 0 !important;
  }
}
</style>
