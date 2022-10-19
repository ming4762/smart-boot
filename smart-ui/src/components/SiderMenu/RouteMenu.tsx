import { defineComponent, ref, createVNode, computed } from 'vue'
import type { PropType } from 'vue'
import * as icons from '@ant-design/icons-vue'

import { useRoute } from 'vue-router'

export const RouteMenuProps = {
  mode: {
    type: String as PropType<string>,
    default: 'inline'
  },
  theme: {
    type: String as PropType<string>,
    default: 'dark'
  },
  menus: {
    type: Array as PropType<Array<any>>,
    default: () => []
  },
  collapsed: {
    type: Boolean as PropType<boolean>
  },
  i18nRender: {
    type: [Function, Boolean] as PropType<Function | boolean>,
    default: false
  },
  lang: {
    type: String as PropType<string>,
    default: 'zh-CN'
  },
  // 菜单点击事件
  menuClick: {
    type: Function as PropType<Function>
  }
}

/**
 * 渲染菜单项
 * @param menu 菜单信息
 * @param lang
 */
const renderMenu = (menu: any, lang: string) => {
  if (menu && !menu.hidden) {
    return menu.children && menu.children.length > 0
      ? renderSubMenu(menu, lang)
      : renderMenuItem(menu, lang)
  }
  return null
}

/**
 * 渲染submenu
 * @param menu
 * @param lang
 */
const renderSubMenu = (menu: any, lang: string) => {
  const slots = {
    title: () => (
      <span>
        {
          // 渲染图标
          renderMenuIcon(menu.meta.icon)
        }
        {renderMenuTitle(menu.meta, lang)}
      </span>
    )
  }
  return (
    <a-sub-menu key={menu.path} v-slots={slots}>
      {
        menu.children.map((child: any) => renderMenu(child, lang))
      }
    </a-sub-menu>
  )
}

/**
 * 渲染菜单图标
 * @param icon
 */
const renderMenuIcon = (icon: object | string | null | undefined) => {
  if (icon === undefined || icon === 'none' || icon === null) {
    return null
  }
  // @ts-ignore
  return createVNode(icons[icon])
}

const getI18nTitle = (menuMeta: any, lang: string) => {
  const { title, locales } = menuMeta
  if (locales && locales[lang] && locales[lang].trim() !== '') {
    return locales[lang].trim()
  }
  return title
}

/**
 * 渲染菜单标题
 */
const renderMenuTitle = (menuMeta: any, lang: string) => {
  return <span>{getI18nTitle(menuMeta, lang)}</span>
}

/**
 * 渲染菜单项
 * @param menu
 * @param lang
 */
const renderMenuItem = (menu: any, lang: string) => {
  return (
    <a-menu-item key={menu.path}>
      {renderMenuIcon(menu.meta.icon)}
      {renderMenuTitle(menu.meta, lang)}
    </a-menu-item>
  )
}

export default defineComponent({
  name: 'RouteMenu',
  props: RouteMenuProps,
  emits: ['change'],
  setup(props) {
    const route = useRoute()
    const openKeys = ref<Array<string>>([])
    const selectedKeys = computed(() => {
      return [route.fullPath]
    })
    const handleMenuSelect = (menu: any) => {
      if (props.menuClick) {
        props.menuClick(menu.key)
      }
    }
    /**
     * 实现一次只打开一个菜单
     * @param openKeyList 打开的菜单列表
     */
    const handleOpenChange = (openKeyList: Array<string>) => {
      const latestOpenKey = openKeyList.find((key) => openKeys.value.indexOf(key) === -1)
      openKeys.value = latestOpenKey ? [latestOpenKey] : []
    }
    return {
      openKeys,
      selectedKeys,
      handleMenuSelect,
      handleOpenChange
    }
  },
  render() {
    const { mode, theme, openKeys, selectedKeys, menus, handleMenuSelect, lang, handleOpenChange } = this
    const dynamicProps = {
      mode,
      theme,
      openKeys,
      selectedKeys,
      onSelect: handleMenuSelect,
      onOpenChange: handleOpenChange
    }
    const menuItemVnodes = menus.map((item) => {
      if (item.hidden) {
        return null
      }
      return renderMenu(item, lang)
    })
    return <a-menu {...dynamicProps}>
      {
        menuItemVnodes
      }
    </a-menu>
  }
})
