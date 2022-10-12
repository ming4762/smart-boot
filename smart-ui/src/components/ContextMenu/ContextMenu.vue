<template>
  <div ref="divRef">
    <slot />
    <div v-show="visible" :style="computedMenuStyle" class="content-menu">
      <slot name="menu" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, reactive, computed } from 'vue'
import type { PropType } from 'vue'

const closeEvents = ['click', 'dblclick', 'contextmenu']

/**
 * 点击菜单
 */
export default defineComponent({
  name: 'ContextMenu',
  props: {
    event: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup(props) {
    const divRef = ref()
    const visible = ref(false)
    // 菜单位置
    const menuPosition = reactive({
      x: 0,
      y: 0
    })
    onMounted(() => {
      divRef.value.addEventListener(props.event, (event: any) => {
        visible.value = true
        menuPosition.x = event.clientX
        menuPosition.y = event.clientY
        event.preventDefault()
      })
      closeEvents.forEach((item: any) => {
        document.addEventListener(item, (event: any) => {
          if (props.event !== item || event.target !== divRef.value) {
            visible.value = false
          }
        })
      })
    })
    const computedMenuStyle = computed(() => {
      return {
        top: `${menuPosition.y - 10}px`,
        left: `${menuPosition.x + 10}px`
      }
    })
    return {
      divRef,
      visible,
      computedMenuStyle
    }
  }
})
</script>

<style lang="less" scoped>
.content-menu {
  position: fixed;
  z-index: 1000;
  border-radius: 2px;
  box-shadow: 0 2px 8px #00000026;
  outline: none;
  ::v-deep(.ant-menu-item) {
    height: 25px;
    line-height: 25px;
  }
}
</style>
