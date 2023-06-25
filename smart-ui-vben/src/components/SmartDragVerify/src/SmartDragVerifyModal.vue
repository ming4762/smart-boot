<template>
  <BasicModal
    :width="computedModalWidth"
    centered
    @visible-change="handleVisibleChange"
    wrapClassName="drag-verify"
    @register="registerModal"
    v-bind="$attrs"
    :footer="null">
    <SmartDragVerify ref="dragRef" v-bind="$attrs" :type="props.type" />
  </BasicModal>
</template>

<script lang="ts" setup>
import type { VerifyType } from '/@/components/SmartDragVerify'

import { useModalInner, BasicModal } from '/@/components/Modal'
import SmartDragVerify from './SmartDragVerify.vue'
import { propTypes } from '/@/utils/propTypes'
import { computed, ref } from 'vue'

const props = defineProps({
  type: {
    type: String as PropType<VerifyType>,
    required: true,
  },
  // 图片宽度
  width: propTypes.number.def(320),
})

const dragRef = ref()

const computedModalWidth = computed(() => {
  return props.width + 28
})

const [registerModal] = useModalInner()

const handleVisibleChange = (visible: boolean) => {
  if (visible) {
    dragRef.value?.refresh()
  }
}
</script>

<style lang="less">
.drag-verify {
  .ant-modal-header {
    display: none;
  }
  .ant-modal-close {
    display: none;
  }
  .scrollbar__wrap {
    margin-bottom: 0 !important;
  }
}
</style>
