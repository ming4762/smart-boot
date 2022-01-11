<template>
  <div class="content-tabs" :style="{height: navHeight + 'px', 'line-height': `${navHeight - 2}px`}">
    <div :style="computedRightButtonStyle" class="roll-nav roll-left direction-button">
      <BackwardOutlined />
    </div>
    <nav class="page-tabs s-menu-tabs">
      <div class="page-tabs-content" style="margin-left: 0">
        <a
          v-for="(item, i) in dataList"
          :key="'navigation-menu_' + i"
          :class="['s-menu-tab', item.path === activeValue ? 'active' : '']"
          href="javascript:"
          @click="() => handleClick(item)">
          {{ getTitle(item) }}
          <CloseCircleOutlined
            v-if="i !== 0"
            class="menu-close-icon"
            :style="{color: '#CCCCCC'}"
            @click.stop="($event) => handleRemove(item, $event)" />
        </a>
      </div>
    </nav>
    <div :style="computedRightButtonStyle" class="roll-nav roll-right direction-button s-menu-right">
      <ForwardOutlined />
    </div>
    <a-dropdown>
      <div class="btn-group roll-nav roll-right">
        <span>{{ i18nRender('app.multiTab.closeOperation') }}</span>
        <CaretDownOutlined />
      </div>
      <template #overlay>
        <slot name="dropdown-menu" />
      </template>
    </a-dropdown>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, PropType, toRefs } from 'vue'

import { BackwardOutlined, CaretDownOutlined, CloseCircleOutlined, ForwardOutlined } from '@ant-design/icons-vue'

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
  name: 'Navigation',
  components: {
    BackwardOutlined,
    CaretDownOutlined,
    CloseCircleOutlined,
    ForwardOutlined
  },
  props: Object.assign({
    activeValue: String as PropType<string>,
    dataList: {
      type: Array as PropType<Array<any>>,
      default: () => []
    },
    lang: String
  }, NavigationProps),
  setup (props) {
    const { navHeight, lang } = toRefs(props)
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
    const handleRemove = (item: any) => {
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
      return  menu.meta.locales[lang.value] || menu.meta.title
    }
    return {
      handleRemove,
      handleClick,
      contentHeight,
      computedRightButtonStyle,
      getTitle
    }
  }
})
</script>

<style lang="less" scoped>
@hover-color: #777777;
@hover-bg-color: #F2F2F2;

.content-tabs {
  border-bottom: solid 2px #2f4050;
  position: relative;
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
      right: 90px;
    }
    // 更多操作按钮样式
    &.btn-group {
      right: 5px;
      width: 85px;
      padding: 0 4px;
      cursor: pointer;
      background-color: #FFFFFF;
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
    margin-left: 40px;
    //width: 10000px;
    //height: 40px;
    overflow: hidden;
    .page-tabs-content {
      float: left;
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
        color: #FFFFFF;
        background-color: #2A3846;
      }
    }
    &:hover {
      color: @hover-color;
      background-color: @hover-bg-color;
    }
  }
}

.s-menu-tab {
  -webkit-transition: all .3s ease-out 0s;
  transition: all .3s ease-out 0s;
  font-size: 12px;
  .menu-close-icon {
    :hover {
      color: red;
    }
  }
}
</style>
