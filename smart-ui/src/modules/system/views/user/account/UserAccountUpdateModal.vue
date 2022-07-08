<template>
  <a-modal>

  </a-modal>
</template>

<script lang="ts">
import { defineComponent, PropType, ref, toRefs } from 'vue'

/**
 * 用户账户更新弹窗
 */
export default defineComponent({
  name: 'UserAccountUpdateModal',
  props: {
    userId: {
      type: Number as PropType<number>
    }
  },
  setup (props) {
    const { userId } = toRefs(props)
    // 弹窗显示状态
    const modalVisible = ref(false)
    /**
     * 显示弹窗
     */
    const handleShowAccountModal = () => {
      modalVisible.value = true
    }

    return {
      modalVisible,
      handleShowAccountModal
    }
  }
})
</script>

<style scoped>

</style>
