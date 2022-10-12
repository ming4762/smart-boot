<template>
  <div :class="computedWrpClass">
    <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
    <select-lang :class="prefixCls" />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, toRefs } from 'vue'
import type { PropType } from 'vue'

import SelectLang from '../../SelectLang/SelectLang.vue'
import AvatarDropdown from './AvatarDropdown.vue'

import { getCurrentUser } from '@/common/auth/AuthUtils'

export default defineComponent({
  name: 'RightContent',
  components: {
    SelectLang,
    AvatarDropdown
  },
  props: {
    prefixCls: {
      type: String as PropType<string>,
      default: 'ant-pro-global-header-index-action'
    },
    isMobile: {
      type: Boolean as PropType<boolean>,
      default: () => false
    },
    topMenu: {
      type: Boolean as PropType<boolean>,
      required: true
    },
    theme: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup(props) {
    const { isMobile, theme, topMenu } = toRefs(props)
    const computedWrpClass = computed(() => {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${isMobile.value || !topMenu.value ? 'light' : theme.value}`]: true
      }
    })
    const currentUser = computed(() => {
      return getCurrentUser() || {}
    })
    return {
      computedWrpClass,
      showMenu: true,
      currentUser
    }
  }
})
</script>

<style lang="less" scoped></style>
