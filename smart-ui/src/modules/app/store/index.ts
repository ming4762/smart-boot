import { defineStore } from 'pinia'

import defaultSettings from '@/config/defaultSetting'

const CONTENT_WIDTH_TYPE = {
  Fluid: 'Fluid',
  Fixed: 'Fixed'
}

/**
 * APP setting store
 */
export const useAppSettingStore = defineStore('appSetting', {
  state: () => {
    return {
      // 布局类型
      layout: defaultSettings.layout,
      contentWidthType: defaultSettings.contentWidth,
      // 主题
      theme: defaultSettings.navTheme,
      // 主色调
      primaryColor: defaultSettings.primaryColor,
      // 锁定头部
      fixedHeader: defaultSettings.fixedHeader,
      // 锁定侧边栏
      fixSiderbar: defaultSettings.fixSiderbar,
      // 按钮尺寸
      buttonSize: defaultSettings.buttonSize,
      // table尺寸
      tableSize: defaultSettings.tableSize,
      // 表单尺寸
      formSize: defaultSettings.formSize,
      hasMultiTab: defaultSettings.hasMultiTab,
      noPermissionMode: defaultSettings.noPermissionMode,

      // 系统设置显示状态
      settingDrawerVisible: false
    }
  },
  getters: {
    appSetting: (state) => state
  },
  actions: {
    changeSetting(key: string, value: any) {
      // @ts-ignore
      if (key && typeof this[key] !== 'undefined') {
        // @ts-ignore
        this[key] = value
        // StoreUtil.setStore(KEY_STORE_KEY[key], value)
        if (key === 'layout') {
          if (value === 'sidemenu') {
            this.contentWidthType = CONTENT_WIDTH_TYPE.Fluid
          } else {
            this.fixSiderbar = false
            this.contentWidthType = CONTENT_WIDTH_TYPE.Fixed
          }
        }
      }
    },
    /**
     * 显示关闭SettingDrawer
     * @param show
     */
    showHideSettingDrawer(show: boolean) {
      this.settingDrawerVisible = show
    }
  }
})

/**
 * APP 国际化store
 */
export const useAppI18nStore = defineStore('appI18n', {
  state: () => {
    return {
      lang: defaultSettings.lang
    }
  },
  actions: {
    setLang(lang: string) {
      return new Promise<void>((resolve) => {
        this.lang = lang
        // StoreUtil.setStore(STORE_KEYS.APP_LANGUAGE, lang)
        resolve()
      })
    }
  }
})

/**
 * APP state store
 */
export const useAppStateStore = defineStore('appState', {
  state: () => {
    return {
      // 全局加载状态
      globalLoading: false,
      collapsed: false,
      // 是否是移动模式
      isMobile: false
    }
  },
  actions: {
    /**
     * 开启关闭全局加载状态
     * @param loading
     */
    setGlobalLoading(loading: boolean) {
      this.globalLoading = loading
    },
    /**
     * 打开关闭侧边栏
     */
    openCloseSidebar() {
      this.collapsed = !this.collapsed
      // StoreUtil.setStore(STORE_KEYS.APP_SIDEBAR_STATUS, this.collapsed)
    }
  }
})
