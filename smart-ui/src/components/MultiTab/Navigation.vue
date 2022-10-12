<template>
  <div ref="outContainerRef" class="content-tabs" :style="{ height: navHeight + 'px', 'line-height': `${navHeight - 2}px` }">
    <div :style="computedRightButtonStyle" class="roll-nav roll-left direction-button" @click="handleMoveLeft">
      <BackwardOutlined />
    </div>
    <nav style="width: 12000px" class="page-tabs s-menu-tabs">
      <div ref="tabContainerRef" class="page-tabs-content" :style="computedTabContainerStyle">
        <a v-for="(item, i) in dataList" :key="'navigation-menu_' + i" :class="['s-menu-tab', item.path === activeValue ? 'active' : '']" href="javascript:" @click="() => handleClick(item)">
          {{ getTitle(item) }}
          <CloseCircleOutlined v-if="i !== 0" class="menu-close-icon" :style="{ color: '#CCCCCC' }" @click.stop="() => handleRemove(item, $event)" />
        </a>
      </div>
    </nav>
    <div :style="computedRightButtonStyle" class="roll-nav roll-right direction-button s-menu-right" @click="handleMoveRight">
      <ForwardOutlined />
    </div>
    <a-dropdown>
      <div class="btn-group roll-nav roll-right">
        <span>{{ i18nRender('app.multiTab.closeOperation') }}</span>
        <CaretDownOutlined />
      </div>
      <template #overlay>
        <a-menu :selected-keys="[]" class="multi-drop-menu" @click="handleClickItem">
          <a-menu-item key="location">
            <aim-outlined class="icon" />
            {{ i18nRender('app.multiTab.dropdownMenu.location') }}
          </a-menu-item>
          <a-divider />
          <a-menu-item key="closeAll">
            <close-outlined class="icon" />
            {{ i18nRender('app.multiTab.dropdownMenu.closeAll') }}
          </a-menu-item>
          <a-menu-item key="closeOther">
            <close-circle-outlined class="icon" />
            {{ i18nRender('app.multiTab.dropdownMenu.closeOther') }}
          </a-menu-item>
          <a-menu-item key="refreshCurrent">
            <reload-outlined class="icon" />
            {{ i18nRender('app.multiTab.dropdownMenu.refreshCurrent') }}
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, toRefs } from 'vue'

import type { PropType } from 'vue'

import { AimOutlined, BackwardOutlined, CaretDownOutlined, CloseCircleOutlined, CloseOutlined, ForwardOutlined, ReloadOutlined } from '@ant-design/icons-vue'

import { useTabsMove, useTabContainerStyle } from './NavigationHooks'

export const NavigationProps = {
  i18nRender: {
    type: Function as PropType<Function>,
    required: true
  },
  navHeight: {
    type: Number as PropType<number>,
    default: 35
  },
  tabClick: Function as PropType<Function>,
  tabRemove: Function as PropType<Function>
}

export default defineComponent({
  name: 'NavigationComponent',
  components: {
    BackwardOutlined,
    CaretDownOutlined,
    CloseCircleOutlined,
    ForwardOutlined,
    AimOutlined,
    ReloadOutlined,
    CloseOutlined
  },
  props: Object.assign(
    {
      activeValue: String as PropType<string>,
      dataList: {
        type: Array as PropType<Array<any>>,
        default: () => []
      },
      lang: {
        type: String as PropType<string>,
        required: true
      }
    },
    NavigationProps
  ),
  setup(props) {
    const { navHeight, lang, activeValue, dataList } = toRefs(props)
    // tab nav ref
    const tabContainerRef = ref()
    const firstElementRef = ref<Element | null>(null)
    const tableMoveHook = useTabsMove(firstElementRef, tabContainerRef, activeValue)
    const contentHeight = computed(() => {
      return navHeight.value - 2
    })
    const computedRightButtonStyle = computed(() => {
      const pxValue = `${navHeight.value - 2}px`
      return {
        height: pxValue,
        width: pxValue
      }
    })
    const handleClickItem = ({ key }: any) => {
      switch (key) {
        case 'location': {
          tableMoveHook.locationCurrent()
        }
      }
    }
    const handleRemove = (item: any) => {
      if (item.path !== activeValue.value) {
        // 删除的不是当前菜单
      }
      if (props.tabRemove) {
        props.tabRemove(item)
      }
    }
    const handleClick = (item: any) => {
      if (props.tabClick) {
        props.tabClick(item)
      }
    }
    const getTitle = (menu: any) => {
      if (!menu.meta.locales) {
        return menu.meta.title
      }
      return menu.meta.locales[lang.value] || menu.meta.title
    }
    return {
      handleRemove,
      handleClickItem,
      handleClick,
      contentHeight,
      computedRightButtonStyle,
      getTitle,
      ...tableMoveHook,
      ...useTabContainerStyle(firstElementRef, tabContainerRef)
    }
  }
})
</script>

<style lang="less" scoped>
@hover-color: #777777;
@hover-bg-color: #f2f2f2;

.content-tabs {
  border-bottom: solid 2px #2f4050;
  position: relative;
  overflow: hidden;
  background: #fafafa;
  .roll-nav {
    position: absolute;
    text-align: center;
    color: #999;
    z-index: 2;
    top: 0;
  }
  .roll-left {
    left: 0;
    border-right: solid 1px #eee;
  }
  .roll-right {
    right: 0;
    border-left: solid 1px #eee;
    &.s-menu-right {
      right: 85px;
    }
    // 更多操作按钮样式
    &.btn-group {
      //right: 5px;
      width: 85px;
      padding: 0 4px;
      cursor: pointer;
      background-color: #ffffff;
      &:hover {
        color: @hover-color;
        background-color: @hover-bg-color;
      }
    }
  }
  .direction-button {
    background: #fff;
    outline: none;
    cursor: pointer;
    &:hover {
      color: @hover-color;
      background-color: @hover-bg-color;
    }
  }
  nav.page-tabs {
    margin-left: 33px;
    //width: 10000px;
    //height: 40px;
    overflow: hidden;
    .page-tabs-content {
      float: left;
      transition: margin-left 0.5s linear;
    }
  }
}

.ant-dropdown-menu {
  .ant-divider.ant-divider-horizontal {
    margin: 5px 0 !important;
  }
}

.page-tabs {
  a {
    display: block;
    color: #999;
    float: left;
    border-right: solid 1px #eee;
    padding: 0 15px;
    &.active {
      background: #2f4050;
      color: #a7b1c2;
      &:hover {
        color: #ffffff;
        background-color: #2a3846;
      }
    }
    &:hover {
      color: @hover-color;
      background-color: @hover-bg-color;
    }
  }
}

.s-menu-tab {
  -webkit-transition: all 0.3s ease-out 0s;
  transition: all 0.3s ease-out 0s;
  font-size: 12px;
  .menu-close-icon {
    :hover {
      color: red;
    }
  }
}

.icon {
  margin-right: 5px;
}
</style>
