<template>
  <a-layout class="full-height">
    <a-layout-content :style="{ width: computedStyle.left }" class="full-height">
      <slot />
    </a-layout-content>
    <a-layout-sider :width="computedStyle.right" :style="{ width: computedStyle.right }" theme="light">
      <slot name="right" />
    </a-layout-sider>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, computed } from 'vue'

/**
 * 左右布局
 */
export default defineComponent({
  name: 'LeftRightLayout',
  props: {
    /**
     * 右侧宽度
     */
    rightWidth: {
      type: String as PropType<string>,
      default: '240px'
    }
  },
  setup (props) {
    const { rightWidth } = toRefs(props)
    const computedStyle = computed(() => {
      if (rightWidth.value.endsWith('px')) {
        return {
          left: `calc(100% - ${rightWidth.value})`,
          right: rightWidth.value
        }
      }
      if (rightWidth.value.endsWith('%')) {
        const rightWidthNumber = rightWidth.value.substring(0, rightWidth.value.length - 1)
        return {
          left: `${100 - Number(rightWidthNumber)}%`,
          right: rightWidth.value
        }
      }
      return {
        left: `calc(100% - ${rightWidth.value}px)`,
        right: rightWidth.value + 'px'
      }
    })
    return {
      computedStyle
    }
  }
})
</script>

<style lang="less" scoped>

</style>
