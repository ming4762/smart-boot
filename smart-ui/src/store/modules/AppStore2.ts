import { defineStore } from 'pinia'

import defaultSettings from '@/config/defaultSetting'
import StoreUtil from '@/common/utils/StoreUtil'

const STORE_KEYS = {
  APP_LAYOUT: 'smart_app_layout',
  APP_CONTENT_WIDTH_TYPE: 'smart_app_content_width',
  APP_THEME: 'smart_app_theme',
  APP_PRIMARY_COLOR: 'smart_app_primary_color',
  APP_FIXED_HEADER: 'smart_app_fixed_header',
  APP_FIXED_SIDERBAR: 'smart_app_fixed_sidebar',
  APP_COLOR_WEAK: 'smart_app_color_weak',
  APP_BUTTON_SIZE: 'smart_app_button_size',
  APP_TABLE_SIZE: 'smart_app_table_size',
  APP_FORM_SIZE: 'smart_app_form_size',
  APP_HAS_MULTI_TAB: 'smart_app_hasMultiTab',

  APP_LANGUAGE: 'smart_app_lang',

  // 侧边栏开启状态
  APP_SIDEBAR_STATUS: 'smart_app_sidebar_status'
}

const KEY_STORE_KEY: {[index: string]: string} = {
  layout: STORE_KEYS.APP_LAYOUT,
  contentWidthType: STORE_KEYS.APP_CONTENT_WIDTH_TYPE,
  theme: STORE_KEYS.APP_THEME,
  primaryColor: STORE_KEYS.APP_PRIMARY_COLOR,
  fixedHeader: STORE_KEYS.APP_FIXED_HEADER,
  fixSiderbar: STORE_KEYS.APP_FIXED_SIDERBAR,
  buttonSize: STORE_KEYS.APP_BUTTON_SIZE,
  tableSize: STORE_KEYS.APP_TABLE_SIZE,
  formSize: STORE_KEYS.APP_FORM_SIZE
}

const CONTENT_WIDTH_TYPE = {
  Fluid: 'Fluid',
  Fixed: 'Fixed'
}

const defaultLayout = StoreUtil.getStore(STORE_KEYS.APP_LAYOUT) || defaultSettings.layout

/**
 * APP setting store
 */
export const useAppSettingStore = defineStore('appSetting', {
  state: () => {
    return {
      // 布局类型
      layout: StoreUtil.getStore(STORE_KEYS.APP_LAYOUT) || defaultSettings.layout,
      contentWidthType: StoreUtil.getStore(STORE_KEYS.APP_CONTENT_WIDTH_TYPE) || defaultLayout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : defaultSettings.contentWidth,
      // 主题
      theme: StoreUtil.getStore(STORE_KEYS.APP_THEME) || defaultSettings.navTheme,
      // 主色调
      primaryColor: StoreUtil.getStore(STORE_KEYS.APP_PRIMARY_COLOR) || defaultSettings.primaryColor,
      // 锁定头部
      fixedHeader: StoreUtil.getStore(STORE_KEYS.APP_FIXED_HEADER) || defaultSettings.fixedHeader,
      // 锁定侧边栏
      fixSiderbar: StoreUtil.getStore(STORE_KEYS.APP_FIXED_SIDERBAR) || defaultSettings.fixSiderbar,
      // 按钮尺寸
      buttonSize: StoreUtil.getStore(STORE_KEYS.APP_BUTTON_SIZE) || defaultSettings.buttonSize,
      // table尺寸
      tableSize: StoreUtil.getStore(STORE_KEYS.APP_TABLE_SIZE) || defaultSettings.tableSize,
      // 表单尺寸
      formSize: StoreUtil.getStore(STORE_KEYS.APP_FORM_SIZE) || defaultSettings.formSize,
      hasMultiTab: StoreUtil.getStore(STORE_KEYS.APP_HAS_MULTI_TAB) || defaultSettings.hasMultiTab,

      // 系统设置显示状态
      settingDrawerVisible: false
    }
  },
  getters: {
    appSetting: state => state
  },
  actions: {
    changeSetting (key: string, value: any) {
      // @ts-ignore
      if (key && typeof this[key] !== 'undefined') {
        // @ts-ignore
        this[key] = value
        StoreUtil.setStore(KEY_STORE_KEY[key], value)
        if (key === 'layout') {
          if (value === 'sidemenu') {
            this.contentWidthType = CONTENT_WIDTH_TYPE.Fluid
          } else {
            this.fixSiderbar = false
            this.contentWidthType = CONTENT_WIDTH_TYPE.Fixed
            StoreUtil.setStore(STORE_KEYS.APP_FIXED_SIDERBAR, false)
          }
        }
      }
    },
    /**
     * 显示关闭SettingDrawer
     * @param show
     */
    showHideSettingDrawer (show: boolean) {
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
      lang: StoreUtil.getStore(STORE_KEYS.APP_LANGUAGE) as string | undefined || defaultSettings.lang
    }
  },
  actions: {
    setLang (lang: string) {
      return new Promise<void>(resolve => {
        this.lang = lang
        StoreUtil.setStore(STORE_KEYS.APP_LANGUAGE, lang)
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
      collapsed: StoreUtil.getStore(STORE_KEYS.APP_SIDEBAR_STATUS) as boolean || false,
      // 是否是移动模式
      isMobile: false
    }
  },
  actions: {
    /**
     * 开启关闭全局加载状态
     * @param loading
     */
    setGlobalLoading (loading: boolean) {
      this.globalLoading = loading
    },
    /**
     * 打开关闭侧边栏
     */
    openCloseSidebar () {
      this.collapsed = !this.collapsed
      StoreUtil.setStore(STORE_KEYS.APP_SIDEBAR_STATUS, this.collapsed)
    }
  }
})
