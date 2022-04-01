<template>
  <div style="margin-bottom: 0; margin-left: 0" class="ant-pro-multi-tab">
    <div class="ant-pro-multi-tab-wrapper">
      <Navigation
        :active-value="computedActiveValue"
        :lang="computedLang"
        v-bind="$props">
        <template #dropdown-menu>
          <a-menu :selected-keys="[]" class="multi-drop-menu" @click="handleClickItem">
            <a-menu-item key="location"><aim-outlined class="icon" />{{ i18nRender('app.multiTab.dropdownMenu.location') }} </a-menu-item>
            <a-divider />
            <a-menu-item key="closeAll"><close-outlined class="icon" />{{ i18nRender('app.multiTab.dropdownMenu.closeAll') }} </a-menu-item>
            <a-menu-item key="closeOther"><close-circle-outlined class="icon" />{{ i18nRender('app.multiTab.dropdownMenu.closeOther') }} </a-menu-item>
            <a-menu-item key="refreshCurrent"><reload-outlined class="icon" />{{ i18nRender('app.multiTab.dropdownMenu.refreshCurrent') }}</a-menu-item>
          </a-menu>
        </template>
      </Navigation>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useStore } from 'vuex'

import { AimOutlined, ReloadOutlined, CloseOutlined, CloseCircleOutlined } from '@ant-design/icons-vue'

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
    Navigation,
    AimOutlined,
    ReloadOutlined,
    CloseOutlined,
    CloseCircleOutlined
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
.icon {
  margin-right: 5px;
}
</style>
