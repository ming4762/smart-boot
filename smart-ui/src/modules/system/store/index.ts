import { defineStore } from 'pinia'

import { useAppSettingStore } from '@/modules/app/store'
import router from '@/router'

import StoreUtil from '@/common/utils/StoreUtil'
import { publish } from '@/common/utils/PublishUtils'
import TreeUtils from '@/common/utils/TreeUtils'

import { EVENT_SYMBOLS } from '@/common/constants/CommonConstants'
import ApiService from '@/common/utils/ApiService'

export const mainMenuMeta = {
  type: 'MENU',
  title: '{system.pageTitle.main}',
  locales: {
    'zh-CN': '主页',
    'en-US': 'Main'
  },
  menuId: '0',
  tab: true,
  path: '/main'
}

/**
 * 系统菜单store
 */
export const useSystemMenuStore = defineStore('systemMenu', {
  state: () => {
    return {
      // 打开的菜单
      openMenuList: [mainMenuMeta] as Array<any>,
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
     * 添加菜单
     * @param menuMeta
     */
    async addMenu(menuMeta: any) {
      // 判断menu是否已经打开
      const hasOpen = this.openMenuList.some((item: any) => {
        return item.menuId === menuMeta.menuId
      })
      if (!hasOpen) {
        this.openMenuList.push(menuMeta)
        if (menuMeta.menuId !== '0') {
          // 发布添加菜单事件，main菜单（id=0）不执行此操作
          publish(EVENT_SYMBOLS.SYSTEM_ADD_MENU, menuMeta)
        }
      }
    },
    /**
     *
     * @param menuKey
     */
    addMenu2(menuKey: string) {
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
    async removeMenu(menuMeta: any) {
      for (let i = 0; i < this.openMenuList.length; i++) {
        const menu = this.openMenuList[i]
        if (menu.menuId === menuMeta.menuId) {
          this.openMenuList.splice(i, 1)
          break
        }
      }
      // 判断是否是当前菜单
      if (router.currentRoute.value.meta.id === menuMeta.id) {
        const activeMenu = this.openMenuList.slice(-1)[0]
        // todo:待完善，菜单ID未设置
        router.push(activeMenu.path)
      }
      return true
    },
    /**
     * 移除菜单
     * @param menuKey
     */
    removeMenu2(menuKey: string) {
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
      return ApiService.postAjax('auth/logout')
        .catch((e) => {
          // do nothing
          console.log(e)
        })
        .finally(() => {
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
