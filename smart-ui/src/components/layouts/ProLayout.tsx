import { computed, createVNode, defineComponent, PropType, toRefs } from 'vue'
import VueContainerQuery from '@/components/container/VueContainerQuery'
import SiderMenuWrapper from '../SiderMenu'
import { SiderMenuProps } from '@/components/SiderMenu/SiderMenu'
import { getComponentFromProp } from '@/common/utils/utils'
import WrapContent from './content/WrapContent'
import HeaderView, { HeaderViewProps } from './header/Header'
// @ts-ignore
import MultiTab, { MultiTabProps } from '@/components/MultiTab/MultiTab.vue'

import SmartProvider, { smartProviderProps } from '@/components/config/SmartProvider'

import './ProLayout.less'

const MediaQueryEnum = {
  'screen-xs': {
    maxWidth: 575
  },
  'screen-sm': {
    minWidth: 576,
    maxWidth: 767
  },
  'screen-md': {
    minWidth: 768,
    maxWidth: 991
  },
  'screen-lg': {
    minWidth: 992,
    maxWidth: 1199
  },
  'screen-xl': {
    minWidth: 1200,
    maxWidth: 1599
  },
  'screen-xxl': {
    minWidth: 1600
  }
}

const ProLayoutProps = Object.assign({
  mediaQuery: {
    type: Object as PropType<object>,
    default: () => {}
  },
  hasMultiTab: Boolean as PropType<boolean>,
  openMenuList: Array as PropType<Array<any>>,
  handleMediaQuery: Function as PropType<Function>
}, SiderMenuProps, HeaderViewProps, MultiTabProps, smartProviderProps)

/**
 * 渲染头部
 * @param props
 */
const renderHeader = (props: any) => {
  if (props.headerRender === false) {
    return false
  }
  return createVNode(HeaderView, props)
}

const getPaddingLeft = function getPaddingLeft(hasLeftPadding: any) {
  // eslint-disable-next-line prefer-rest-params
  const collapsed = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : undefined
  // eslint-disable-next-line prefer-rest-params
  const siderWidth = arguments.length > 2 ? arguments[2] : undefined

  if (hasLeftPadding) {
    return collapsed ? 80 : siderWidth
  }

  return 0
}

const defaultI18nRender = function defaultI18nRender(key: string) {
  return key
}

const renderMultiTab = (props: any) => {
  const { hasMultiTab, openMenuList } = props
  if (hasMultiTab === false) {
    return ''
  }
  const multiProps: any = {}
  Object.keys(MultiTabProps).forEach(propKey => {
    multiProps[propKey] = props[propKey]
  })
  multiProps.dataList = openMenuList
  return <MultiTab {...multiProps}/>
}

export default defineComponent({
  name: 'ProLayout',
  components: {
    WrapContent,
    SmartProvider,
    MultiTab
  },
  props: ProLayoutProps,
  setup (props) {
    const propRefs: any = toRefs(props)
    const { layout, fixSiderbar, isMobile, collapsed, siderWidth, mediaQuery, fixedHeader } = propRefs
    const isTopMenu = computed(() => layout.value === 'topmenu')

    const computedSmartProviderProps = computed(() => {
      const result: any = {}
      Object.keys(smartProviderProps).forEach(key => {
        const refValue = propRefs[key]
        if (refValue && refValue.value) {
          result[key] = refValue.value
        }
      })
      return result
    })

    const computedLayoutStyle = computed(() => {
      return {
        // @ts-ignore
        paddingLeft: isTopMenu.value ? undefined : ''.concat(getPaddingLeft((fixSiderbar.value && !isTopMenu.value && !isMobile.value), collapsed.value, siderWidth.value), 'px'),
        minHeight: '100vh'
      }
    })
    /**
     * 外层layout样式
     */
    const computedOutLayoutClass = computed(() => {
      let fullHeight = false
      if (isTopMenu.value) {
        if (fixedHeader.value === true) {
          fullHeight = true
        }
      } else {
        fullHeight = fixSiderbar.value && fixedHeader.value
      }
      return Object.assign({
        'ant-pro-basicLayout': true,
        'ant-pro-topmenu': isTopMenu.value,
        'full-height': fullHeight
      }, mediaQuery)
    })
    return {
      isTopMenu,
      computedLayoutStyle,
      computedOutLayoutClass,
      computedSmartProviderProps
    }
  },
  render () {
    console.log('----------------- render ProLayout ---------------------')
    const { layout, computedLayoutStyle, computedOutLayoutClass, isTopMenu, handleMediaQuery, i18nRender, contentWidth } = this
    const menuHeaderRender = getComponentFromProp(this, 'menuHeaderRender')
    const breadcrumbRender = getComponentFromProp(this, 'breadcrumbRender')
    const props = Object.assign({}, this.$props, {
      menuHeaderRender,
      hasSiderMenu: !isTopMenu,
      footerRender: getComponentFromProp(this, 'footerRender'),
      rightContentRender: getComponentFromProp(this, 'rightContentRender'),
      collapsedButtonRender: getComponentFromProp(this, 'collapsedButtonRender'),
      breadcrumbRender: breadcrumbRender,
      headerContentRender: getComponentFromProp(this, 'headerContentRender'),
      menuRender: getComponentFromProp(this, 'menuRender')
    })
    const doI18nRender = i18nRender === void 0 ? defaultI18nRender : i18nRender
    return (
      <a-config-provider
        contentWidth={contentWidth}
        breadcrumbRender={breadcrumbRender}
        i18nRender={doI18nRender}>
        <smart-provider {...this.computedSmartProviderProps}>
          { // @ts-ignore
            <VueContainerQuery onContainerChange={handleMediaQuery} query={MediaQueryEnum}>
              <a-layout class={computedOutLayoutClass}>
                {/* 渲染侧边栏 */}
                <SiderMenuWrapper {...props} />
                <a-layout class={layout} style={computedLayoutStyle}>
                  {renderHeader(Object.assign({}, props, {
                    mode: 'horizontal'
                  }))}
                  {
                    renderMultiTab(Object.assign({}, this.$props, {
                      i18nRender: doI18nRender
                    }))
                  }
                  <WrapContent v-slots={this.$slots} contentWidth={props.contentWidth}
                               class="ant-pro-basicLayout-content full-height" />
                </a-layout>
              </a-layout>
            </VueContainerQuery>
          }
        </smart-provider>
      </a-config-provider>
    )
  }
})
