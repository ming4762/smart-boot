import { PropType, defineComponent } from 'vue'
import { defaultRenderLogo } from '@/components/SiderMenu/SiderMenu'
import { isFun, triggerEvent, inBrowser } from '@/common/utils/utils'
// @ts-ignore
import debounce from 'lodash/debounce'
import { MenuFoldOutlined, MenuUnfoldOutlined } from '@ant-design/icons-vue'

import './GlobalHeader.less'

export const GlobalHeaderProps = {
  collapsed: {
    type: Boolean as PropType<boolean>
  },
  handleCollapse: Function as PropType<Function>,
  isMobile: {
    type: Boolean as PropType<boolean>,
    default: false
  },
  fixedHeader: Boolean as PropType<boolean>,
  logo: {
    type: null
  },
  menuRender: Object as PropType<object>,
  collapsedButtonRender: null,
  headerContentRender: null,
  rightContentRender: null
}

const defaultRenderCollapsedButton = (collapsed: boolean) => {
  return collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />
}

const renderCollapsedButton = (props: any, triggerResizeEvent: Function) => {
  const toggle = () => {
    const { collapsed, handleCollapse } = props
    if (handleCollapse) {
      handleCollapse(!collapsed)
    }
    triggerResizeEvent()
  }
  const {  menuRender, collapsed } = props
  const collapsedButtonRender = props.collapsedButtonRender === void 0 ? defaultRenderCollapsedButton : props.collapsedButtonRender
  if (collapsedButtonRender !== false && menuRender !== false) {
    return (
      <span onClick={() => toggle()} class="ant-pro-global-header-trigger">
        {
          isFun(collapsedButtonRender) && collapsedButtonRender(collapsed) || collapsedButtonRender
        }
      </span>
    )
  }
  return null
}

export default defineComponent({
  name: 'GlobalHeader',
  props: GlobalHeaderProps,
  methods: {
    triggerResizeEvent: debounce(() => {
      // TODO:待处理
      () => {
        // @ts-ignore
        inBrowser && triggerEvent(document, 'resize')
      }
    })
  },
  // eslint-disable-next-line vue/order-in-components
  beforeUnmount() {
    this.triggerResizeEvent.cancel && this.triggerResizeEvent.cancel()
  },
  render () {
    const { isMobile, logo, headerContentRender, rightContentRender } = this
    const headerCls = 'ant-pro-global-header'
    return (
      <div class={headerCls}>
        {
          // 移动端渲染
          isMobile ? <a class={[''.concat(headerCls, '-logo')]} key="logo" href="/">
            { defaultRenderLogo(logo) }
          </a> : null
        }
        {
          renderCollapsedButton(this.$props, this.triggerResizeEvent)
        }
        {
          headerContentRender && <div class={''.concat(headerCls, '-content')}>
            {
              isFun(headerContentRender) && headerContentRender(this.$props) || headerContentRender
            }
          </div>
        }
        {
          isFun(rightContentRender) && rightContentRender(this.$props) || rightContentRender
        }
      </div>
    )
  }
})
