import { defineStore } from 'pinia'

import { useAppSettingStore } from '@/store/modules/AppStore2'
import router from '@/router'

import StoreUtil from '@/common/utils/StoreUtil'
import { publish } from '@/common/utils/PublishUtils'
import TreeUtils from '@/common/utils/TreeUtils'

import { EVENT_SYMBOLS } from '@/common/constants/CommonConstants'
import ApiService from '@/common/utils/ApiService'

// const STORE_KEYS = {
//   OPEN_MENU_LIST: 'smart_app_open_menu',
//   APP_USER_MENU_LIST: 'smart_app_user_menu'
// }

/**
 * 系统菜单store
 */
export const useSystemMenuStore = defineStore('systemMenu', {
  state: () => {
    return {
      // 打开的菜单
      openMenuList: [] as Array<any>,
      // 用户菜单信息
      userMenuList: [] as Array<any>
    }
  },
  getters: {
    userTreeMenu: (state) => {
      const userMenuList = state.userMenuList
      if (userMenuList === null || userMenuList === undefined) {
        return []
      }
      return (
        TreeUtils.convertList2Tree(
          JSON.parse(JSON.stringify(userMenuList)),
          ['id', 'parentId'],
          '0'
        ) || []
      )
    }
  },
  actions: {
    /**
     * 设置用户菜单列表
     * @param userMenuList
     */
    setUserMenu(userMenuList: Array<any>) {
      this.userMenuList = userMenuList
    },
    /**
     *
     * @param menuKey
     */
    addMenu(menuKey: string) {
      return new Promise<void>((resolve) => {
        // 获取菜单信息
        let menu: any = null
        for (const item of this.userMenuList) {
          if (item.path === menuKey || item.id === menuKey) {
            menu = item
            break
          }
        }
        if (menu === null) {
          resolve()
          return false
        }
        // TODO: 待完善 判断是否超出最大打开数
        const appSettingStore = useAppSettingStore()
        if (appSettingStore.hasMultiTab === true) {
          // 判断菜单是否已经打开
          const hasMenu = this.openMenuList.some((item: any) => {
            return item.id === menu.id
          })
          if (!hasMenu) {
            this.openMenuList.push(menu)
            // StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
            if (menu.name !== 'main') {
              // 发布添加菜单事件
              publish(EVENT_SYMBOLS.SYSTEM_ADD_MENU, menu)
            }
          }
        }
      })
    },
    /**
     * 移除菜单
     * @param menuKey
     */
    removeMenu(menuKey: string) {
      return new Promise<void>((resolve) => {
        for (let i = 0; i < this.openMenuList.length; i++) {
          const menu = this.openMenuList[i]
          if (menu.path === menuKey) {
            this.openMenuList.splice(i, 1)
            break
          }
        }
        // StoreUtil.setStore(STORE_KEYS.OPEN_MENU_LIST, this.openMenuList, StoreUtil.SESSION_TYPE)
        // 判断是否是当前菜单
        if (router.currentRoute.value.fullPath === menuKey) {
          const activeMenu = this.openMenuList.slice(-1)[0]
          router.push(activeMenu.path)
        }
        return resolve()
      })
    }
  }
})

/**
 * 系统登录登出store
 */
export const useSystemLoginStore = defineStore('systemLoginStore', {
  state: () => {
    return {
      // 未登录modal显示状态
      noLoginModalVisible: false
    }
  },
  actions: {
    /**
     * 警告未登录
     */
    waringNoLogin() {
      this.noLoginModalVisible = true
    },
    hideWarningNoLogin() {
      this.noLoginModalVisible = false
    },
    /**
     * 登出操作
     */
    logout() {
      return ApiService.postAjax('auth/logout').finally(() => {
        this.clearLoginMessage()
      })
    },
    /**
     * 清除登录信息
     */
    clearLoginMessage() {
      const systemMenuStore = useSystemMenuStore()
      systemMenuStore.$patch((state) => {
        state.openMenuList = []
        state.userMenuList = []
      })
      StoreUtil.clearSession()
    }
  }
})

/**
 * 系统异常store
 */
export const useSystemExceptionStore = defineStore('systemExceptionStore', {
  state: () => {
    return {
      modalVisible: false,
      noList: [] as Array<number>
    }
  },
  actions: {
    /**
     * 显示异常弹窗
     * @param exceptionNo
     */
    handleShowExceptionModal(exceptionNo: number) {
      if (this.modalVisible === false) {
        this.noList = []
      }
      this.modalVisible = true
      this.noList.push(exceptionNo)
    },
    /**
     * 隐藏弹窗
     */
    handleHideExceptionModal() {
      this.noList = []
      this.modalVisible = false
    }
  }
})
