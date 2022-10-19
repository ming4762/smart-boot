import { defineComponent, toRefs, computed, createVNode } from 'vue'
import type { PropType } from 'vue'
import VuePropsTypes from '@/common/utils/VueTypes'
import RouteMenu, { RouteMenuProps } from './RouteMenu'
import './SideMenu.less'
import { isFun } from '@/common/utils/utils'

export const SiderMenuProps = {
  contentWidth: {
    type: String as PropType<string>,
    validator: VuePropsTypes.oneOf(['Fluid', 'Fixed']).validator,
    default: 'Fluid'
  },
  collapsible: {
    type: Boolean as PropType<boolean>
  },
  handleCollapse: {
    type: Function as PropType<Function>
  },
  siderWidth: {
    type: Number as PropType<number>,
    default: 256
  },
  isMobile: {
    type: Boolean as PropType<boolean>
  },
  layout: {
    type: String as PropType<string>,
    default: 'inline'
  },
  fixSiderbar: {
    type: Boolean as PropType<boolean>
  },
  logo: {
    type: null
  },
  title: {
    type: String as PropType<string>,
    default: ''
  },
  menuHeaderRender: {
    type: [Function, Array, Object, Boolean] as PropType<Function | Array<any> | object | boolean>
  },
  menuRender: {
    validator: VuePropsTypes.oneOfType([Function, Array, Object, Boolean]).validator
  },
  ...RouteMenuProps
}

export const defaultRenderLogo = (logo: any) => {
  if (typeof logo === 'string') {
    return createVNode('img', {
      attrs: {
        src: logo,
        alt: 'logo'
      }
    })
  }
  if (typeof logo === 'function') {
    return logo()
  }
  return createVNode(logo)
}

export const defaultRenderLogoAntTitle = (props: any) => {
  const { logo, title, menuHeaderRender } = props
  if (menuHeaderRender === false) {
    return null
  }
  const logoDom = defaultRenderLogo(
    logo || 'https://gw.alipayobjects.com/zos/antfincdn/PmY%24TNNDBI/logo.svg'
  )
  const titleDom = createVNode('h1', null, [title])
  if (menuHeaderRender) {
    return (
      (isFun(menuHeaderRender) &&
        menuHeaderRender(logoDom, props.collapsed ? null : titleDom, props)) ||
      menuHeaderRender
    )
  }
  return createVNode('span', null, [logoDom, titleDom])
}

export default defineComponent({
  name: 'SiderMenu',
  props: SiderMenuProps,
  setup(props) {
    const { fixSiderbar, theme } = toRefs(props)
    const computedSiderClass = computed(() => {
      const result = ['ant-pro-sider-menu-sider']
      if (fixSiderbar.value === true) {
        result.push('fix-sider-bar')
      }
      if (theme.value === 'light') {
        result.push('light')
      }
      return result
    })
    return {
      computedSiderClass
    }
  },
  render() {
    const {
      logo,
      siderWidth,
      theme,
      collapsible,
      collapsed,
      computedSiderClass,
      title,
      menuHeaderRender,
      menuRender,
      menus,
      mode,
      i18nRender,
      menuClick,
      lang
    } = this
    const headerDom = defaultRenderLogoAntTitle({
      logo,
      title,
      menuHeaderRender,
      collapsed
    })
    return (
      <a-layout-sider
        {...{
          breakpoint: 'lg',
          trigger: null,
          width: siderWidth,
          theme,
          collapsible,
          collapsed
        }}
        class={computedSiderClass}>
        <div class="ant-pro-sider-menu-logo" id="logo">
          <router-link to="/">{headerDom}</router-link>
        </div>
        {
          /*  渲染侧边栏*/
          // @ts-ignore
          (menuRender && ((isFun(menuRender) && menuRender(this.$props)) || menuRender)) ||
            createVNode(RouteMenu, {
              collapsed,
              menus,
              mode,
              theme,
              i18nRender,
              menuClick,
              lang
            })
        }
      </a-layout-sider>
    )
  }
})
