<template>
  <a-spin :spinning="dataLoading">
    <a-transfer
      class="group-transfer"
      :style="computedStyle"
      v-bind="$attrs"
      :render="item => item.title"
      :target-keys="targetKeys"
      :data-source="dataSource"
      @change="handleChange" />
  </a-spin>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, PropType, toRefs, computed } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 用户组穿梭框
 */
export default defineComponent({
  name: 'UserGroupTransfer',
  props: {
    targetKeys: {
      type: Array as PropType<Array<number>>,
      default: () => []
    },
    height: {
      type: String
    }
  },
  emits: ['update:targetKeys'],
  setup (props, { emit }) {
    const { height } = toRefs(props)

    const computedStyle = computed(() => {
      if (height.value) {
        return {
          height: height.value
        }
      }
      return  {}
    })

    const dataSource = ref<Array<any>>([])
    const dataLoading = ref(false)
    /**
     * 记载数据函数
     */
    const handleLoadData = async () => {
      try {
        const result = await ApiService.postAjax('sys/userGroup/list', {
          sortName: 'seq'
        })
        dataSource.value = result.map(({ groupId, groupName, groupCode }: any) => {
          return {
            key: groupId + '',
            title: `${groupCode} [ ${groupName} ]`
          }
        })
      } catch (e) {
        errorMessage(e)
      } finally {
        dataLoading.value = false
      }
    }

    const handleChange = (targetKeys: Array<any>) => {
      emit('update:targetKeys', targetKeys)
    }

    onMounted(handleLoadData)
    return {
      dataSource,
      dataLoading,
      handleLoadData,
      handleChange,
      computedStyle
    }
  }
})
</script>

<style lang="less" scoped>
.group-transfer {
  ::v-deep(.ant-transfer-list) {
    width: 47%;
    height: 100%;
  }
}
</style>
