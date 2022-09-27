<template>
  <div style="margin-bottom: 0; margin-left: 0" class="ant-pro-multi-tab">
    <div class="ant-pro-multi-tab-wrapper">
      <Navigation
        :active-value="computedActiveValue"
        :lang="computedLang"
        v-bind="$props">
      </Navigation>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

import Navigation, { NavigationProps } from './Navigation.vue'

export const MultiTabProps = Object.assign({
  dataList: {
    type: Array as PropType<Array<any>>,
    default: () => []
  }
}, NavigationProps)

/**
 * 菜单栏组件
 */
export default defineComponent({
  name: 'MultiTab',
  components: {
    Navigation
  },
  props: MultiTabProps,
  setup () {
    const route = useRoute()
    const store = useStore()
    const computedActiveValue = computed(() => {
      return route.fullPath
    })
    const computedLang = computed(() => {
      return store.getters['app/lang']
    })
    const handleClickItem = ({ key }: any) => {
      switch (key) {
        case 'refreshCurrent': {
          break
        }
      }
    }
    return {
      computedActiveValue,
      computedLang,
      handleClickItem
    }
  }
})
</script>

<style lang="less" scoped>
.multi-drop-menu {
  .ant-divider-horizontal {
    margin: 5px 0 !important;
  }
}

</style>
