<template>
  <a-modal
    class="modal-diag"
    :get-container="getContainer"
    v-bind='$attrs'>
    <template #closeIcon />
    <template #title>
      <slot></slot>
      <div class="top-button-container">
        <a-button class="button">
          <template #icon>
            <fullscreen-outlined />
          </template>
        </a-button>
        <a-button class="button">
          <CloseOutlined />
        </a-button>
      </div>
    </template>
    这是一个示例
  </a-modal>
</template>

<script lang='ts' setup>
/**
 * 可拖拽的modal
 */
import type { PropType } from 'vue'

import { FullscreenOutlined, FullscreenExitOutlined, CloseOutlined } from '@ant-design/icons-vue'

const props = defineProps({
  title: {
    type: String as PropType<string>
  }
})

/**
 * 获取挂载点
 */
const getContainer = () => document.getElementById('modal_container')
</script>

<style lang='less'>
.top-button-container {
  position: absolute;
  right: 0;
  top: 0;
  button {
    border: 0;
    height: 54px;
    padding: 0;
    width: 35px;
  }
}
.modal-container {
  .ant-modal-close {
    display: none;
  }
}
</style>
