import GlobalHeader, { GlobalHeaderProps } from './GlobalHeader'
import { SiderMenuProps, defaultRenderLogoAntTitle } from '../../SiderMenu/SiderMenu'
import { computed, createVNode, defineComponent, PropType, toRefs } from 'vue'
import { isFun } from '@/common/utils/utils'
import RouteMenu from '@/components/SiderMenu/RouteMenu'

import './Header.less'

export const HeaderViewProps = Object.assign({} , GlobalHeaderProps, SiderMenuProps, {
  hasSiderMenu: Boolean as PropType<boolean>,
  autoHideHeader: Boolean as PropType<boolean>,
  headerRender: null,
  visible: {
    type: Boolean as PropType<boolean>,
    default: true
  }
})

/**
 * 渲染头部内容
 */
const renderHeaderContent = (props: any) => {
  const { headerRender, layout, isMobile, title } = props
  const isTop = layout === 'topmenu'
  if (headerRender) {
    return headerRender(props)
  }
  const createDefaultDom = () => createVNode(GlobalHeader, props)
  const createTopDom = () => {
    const maxWidth = 1200 - 280 - 120
    const { theme, menuHeaderRender, logo, menuRender, rightContentRender } = props
    const baseCls = 'ant-pro-top-nav-header'
    const contentWidth = props.contentWidth === 'Fixed'
    console.log(contentWidth)
    const rightContentProps = {
      theme: theme,
      isTop: isTop,
      isMobile: isMobile
    }
    return createVNode('div', {
      'class': [baseCls, theme]
    }, [createVNode('div', {
      'class': [''.concat(baseCls, '-main'), contentWidth ? 'wide' : '']
    }, [menuHeaderRender && createVNode('div', {
      'class': ''.concat(baseCls, '-left')
    }, [createVNode('div', {
      'class': ''.concat(baseCls, '-logo'),
      key: '',
      attrs: {
        id: 'logo'
      }
    }, [defaultRenderLogoAntTitle({
      logo: logo,
      title: title,
      menuHeaderRender: menuHeaderRender
    })])]), createVNode('div', {
      'class': ''.concat(baseCls, '-menu'),
      style: {
        maxWidth: ''.concat(maxWidth.toString(), 'px'),
        flex: 1
      }
    }, [menuRender && (isFun(menuRender) && menuRender(props) || menuRender) || createVNode(RouteMenu, props)]), isFun(rightContentRender) && rightContentRender(rightContentProps) || rightContentRender])])
  }
  return isTop && !isMobile ? createTopDom() : createDefaultDom()
}

/**
 * 渲染头部
 */
export default defineComponent({
  name: 'HeaderView',
  props: HeaderViewProps,
  setup (props) {
    const { fixedHeader, hasSiderMenu, layout, isMobile, collapsed, siderWidth } = toRefs(props)
    const computedIsTop = computed(() => {
      return layout.value === 'topmenu'
    })
    const computedHeaderStyle = computed(() => {
      return {
        padding: 0,
        zIndex: 9,
        width: (fixedHeader.value && hasSiderMenu.value && !computedIsTop.value && !isMobile.value) ? `calc(100% - ${collapsed.value ? 80 : siderWidth.value}px)` : '100%',
        right: fixedHeader.value ? 0 : undefined
      }
    })
    const computedHeaderClass = computed(() => {
      return {
        'ant-pro-fixed-header': fixedHeader.value,
        'ant-pro-top-menu': computedIsTop.value
      }
    })
    return {
      computedHeaderStyle,
      computedHeaderClass
    }
  },
  render () {
    const { visible, computedHeaderStyle, computedHeaderClass, fixedHeader } = this
    if (!visible) {
      return null
    }
    return (
      <div>
        {
          fixedHeader && <a-layout-header />
        }
        <a-layout-header style={computedHeaderStyle} class={computedHeaderClass}>
          {
            renderHeaderContent(this.$props)
          }
        </a-layout-header>
      </div>
    )
  }
})


