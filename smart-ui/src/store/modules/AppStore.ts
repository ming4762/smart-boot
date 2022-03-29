import { StoreOptions } from 'vuex'

import router from '@/router'

import StoreUtil from '@/common/utils/StoreUtil'

import defaultSettings from '@/config/defaultSetting'
import { CONTENT_WIDTH_TYPE } from '@/modules/system/store/mutation-types'
import { STORE_APP_MUTATION } from '@/common/constants/CommonConstants'
import defaultSetting from '@/config/defaultSetting'

const STORE_KEYS = {
  ACTIVE_MENU: 'smart_app_active_menu',
  OPEN_MENU_LIST: 'smart_app_open_menu',
  APP_USER_MENU_LIST: 'smart_app_user_menu',
  APP_LAYOUT: 'smart_app_layout',
  APP_CONTENT_WIDTH: 'smart_app_content_width',
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
  contentWidth: STORE_KEYS.APP_CONTENT_WIDTH,
  theme: STORE_KEYS.APP_THEME,
  primaryColor: STORE_KEYS.APP_PRIMARY_COLOR,
  fixedHeader: STORE_KEYS.APP_FIXED_HEADER,
  fixSiderbar: STORE_KEYS.APP_FIXED_SIDERBAR,
  buttonSize: STORE_KEYS.APP_BUTTON_SIZE,
  tableSize: STORE_KEYS.APP_TABLE_SIZE
}

const defaultLayout = StoreUtil.getStore(STORE_KEYS.APP_LAYOUT) || defaultSettings.layout

const LayoutStore: StoreOptions<any> = {
  state: {
    // 国际化信息
    i18n: {
      lang: StoreUtil.getStore(STORE_KEYS.APP_LANGUAGE) || defaultSetting.lang
    },
    menu: {
      // 打开的菜单
      openMenuList: StoreUtil.getStore(STORE_KEYS.OPEN_MENU_LIST) || [],
      // 用户菜单信息
      userMenuList: StoreUtil.getStore(STORE_KEYS.APP_USER_MENU_LIST) || []
    },
    /**
     * 系统设置
     */
    setting: {
      // 布局类型
      layout: StoreUtil.getStore(STORE_KEYS.APP_LAYOUT) || defaultSettings.layout,
      contentWidth: StoreUtil.getStore(STORE_KEYS.APP_CONTENT_WIDTH) || defaultLayout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : defaultSettings.contentWidth,
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
      hasMultiTab: StoreUtil.getStore(STORE_KEYS.APP_HAS_MULTI_TAB) || defaultSettings.hasMultiTab
    },
    settingDrawerVisible: false,
    // 全局加载状态
    globalLoading: false,
    // 侧边栏关闭状态
    collapsed: StoreUtil.getStore(STORE_KEYS.APP_SIDEBAR_STATUS) || false,
    // 是否是移动模式
    isMobile: false
  },
  mutations: {
    /**
     * 开启关闭全局加载状态
     * @param state
     * @param loading
     */
    [STORE_APP_MUTATION.GLOBAL_LOADING]: (state, loading) => {
      state.globalLoading = loading
    },
    /**
     * 修改系统设置
     * @param state
     * @param key 配置的key
     * @param value 配置value
     */
    [STORE_APP_MUTATION.CHANGE_SETTING]: (state, { key, value }) => {
      if (key && typeof state.setting[key] !== 'undefined') {
        state.setting[key] = value
        StoreUtil.setStore(KEY_STORE_KEY[key], value)
        if (key === 'layout') {
          if (value === 'sidemenu') {
            state.setting.contentWidth = CONTENT_WIDTH_TYPE.Fluid
          } else {
            state.setting.fixSiderbar = false
            state.setting.contentWidth = CONTENT_WIDTH_TYPE.Fixed
            StoreUtil.setStore(STORE_KEYS.APP_FIXED_SIDERBAR, false)
          }
        }
      }
    },
    /**
     * 设置用户菜单信息
     * @param state
     * @param userMenuList
     */
    [STORE_APP_MUTATION.APP_SET_USER_MENU]: (state, userMenuList) => {
      state.menu.userMenuList = userMenuList
      StoreUtil.setStore(STORE_KEYS.APP_USER_MENU_LIST, userMenuList, StoreUtil.SESSION_TYPE)
    },
    /**
     * 添加打开的菜单
     * @param state
     * @param menu
     */
    [STORE_APP_MUTATION.APP_ADD_OPEN_MENU]: (state, menu) => {
      state.menu.openMenuList.push(menu)
      StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, state.menu.openMenuList, StoreUtil.SESSION_TYPE)
    },
    /**
     * 移除打开的菜单
     * @param state
     * @param menuKey
     */
    [STORE_APP_MUTATION.APP_REMOVE_OPEN_MENU]: (state, menuKey: string) => {
      for (let i=0; i<state.menu.openMenuList.length; i++) {
        const menu = state.menu.openMenuList[i]
        if (menu.path === menuKey) {
          state.menu.openMenuList.splice(i, 1)
          break
        }
      }
      StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, state.menu.openMenuList, StoreUtil.SESSION_TYPE)
    },
    /**
     * 登出操作
     * @param state
     */
    [STORE_APP_MUTATION.APP_LOGOUT]: (state) => {
      state.menu = {
        openMenuList: [],
        userMenuList: []
      }
      StoreUtil.clearSession()
    },
    /**
     * 设置语言
     * @param state
     * @param lang 语言
     */
    [STORE_APP_MUTATION.APP_LANGUAGE]: (state, lang) => {
      state.i18n.lang = lang
      StoreUtil.setStore(STORE_KEYS.APP_LANGUAGE, lang)
    },
    /**
     * 打开关闭侧边栏
     * @param state
     */
    [STORE_APP_MUTATION.APP_COLLAPSED_SIDEBAR]: (state) => {
      state.collapsed = !state.collapsed
      StoreUtil.setStore(STORE_KEYS.APP_SIDEBAR_STATUS, state.collapsed)
    },
    /**
     * 显示关闭SettingDrawer
     * @param state
     * @param show
     */
    showHideSettingDrawer: (state, show: boolean) => {
      state.settingDrawerVisible = show
    }
  },
  actions: {
    addMenu ({ commit, state }, menuKey: any): Promise<any> {
      return new Promise((resolve) => {
        // 获取菜单信息
        let menu: any = null
        for (const item of state.menu.userMenuList) {
          if (item.path === menuKey || item.id === menuKey) {
            menu = item
            break
          }
        }
        if (menu === null) {
          resolve(null)
          return false
        }
        router.push(menu.path)
        // 判断是否超出最大打开数
        // if (state.openMenuList.length >= 15) {
        //   reject('more page')
        // }
        if (state.setting.hasMultiTab === true) {
          // 判断菜单是否已经打开
          const hasMenu = state.menu.openMenuList.some((item: any) => {
            return item.id === menu.id
          })
          if (!hasMenu) {
            commit(STORE_APP_MUTATION.APP_ADD_OPEN_MENU, menu)
          }
        }
        resolve(null)
      })
    },
    /**
     * 设置用户菜单
     * @param commit
     * @param userMenuList
     */
    setUserMenu({commit}, userMenuList): Promise<null> {
      return new Promise((resolve) => {
        commit(STORE_APP_MUTATION.APP_SET_USER_MENU, userMenuList)
        return resolve(null)
      })
    },
    /**
     * 登出操作
     * @param commit
     */
    logout({ commit }): Promise<void> {
      return new Promise(resolve => {
        commit(STORE_APP_MUTATION.APP_LOGOUT)
        resolve()
      })
    },
    /**
     * 移除菜单
     * @param commit
     * @param state
     * @param menuKey
     */
    removeMenu({ commit, state }, menuKey: string): Promise<void> {
      return new Promise(resolve => {
        // 移除菜单
        commit(STORE_APP_MUTATION.APP_REMOVE_OPEN_MENU, menuKey)
        // 判断是否是当前菜单
        if (router.currentRoute.value.fullPath === menuKey) {
          const activeMenu = state.menu.openMenuList.slice(-1)[0]
          router.push(activeMenu.path)
        }
        return resolve()
      })
    },
    /**
     * 设置语言信息
     * @param commit
     * @param lang
     */
    setLang ({ commit }, lang): Promise<void> {
      return new Promise((resolve) => {
        commit(STORE_APP_MUTATION.APP_LANGUAGE, lang)
        resolve()
      })
    }
  },
  getters: {
    // 全局加载状态
    globalLoading: state => state.globalLoading,
    appSetting: state => state.setting,
    appCollapsed: state => state.collapsed,
    userMenuList: state => state.menu.userMenuList,
    openMenuList: state => state.menu.openMenuList,
    // 语言
    lang: state => state.i18n.lang,
    settingDrawerVisible: state => state.settingDrawerVisible
  }
}

export default LayoutStore
