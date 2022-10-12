<template>
  <pro-layout
    :collapsed="collapsed"
    :media-query="query"
    :is-mobile="isMobile"
    :open-menu-list="openMenuList"
    :tab-remove="handleTabRemove"
    :lang="computedLang"
    :tab-click="handleTabClick"
    :handle-collapse="handleCollapse"
    :menu-click="handleMenuClick"
    :handle-media-query="handleMediaQuery"
    :i18n-render="i18nRender"
    v-bind="setting"
    :menus="userMenu">
    <!--  logo标题插槽  -->
    <template #menuHeaderRender>
      <div>
        <img src="../../assets/logo.svg" alt="" />
        <h1>{{ title }}</h1>
      </div>
    </template>

    <template #headerContentRender></template>

    <template #rightContentRender>
      <RightContent
        :is-mobile="isMobile"
        :theme="setting.theme"
        :top-menu="setting.layout === 'topmenu'" />
    </template>

    <SettingDrawer
      v-if="isDev"
      :visible="settingDrawerVisible"
      :i18n-render="i18nRender"
      :settings="setting"
      @change="handleSettingChange">
      <div style="margin: 12px 0">This is SettingDrawer custom footer content.</div>
    </SettingDrawer>
    <router-view v-slot="{ Component, route }">
      <transition name="fade" mode="out-in">
        <!--    TODO: keep-alive 热更新报错，使用该方式临时处理    -->
        <keep-alive v-if="!isDev">
          <component :is="Component" :key="route.name" />
        </keep-alive>
        <component :is="Component" v-else :key="route.name" />
      </transition>
    </router-view>

    <ExceptionModal />
  </pro-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from 'vue'
import { errorMessage } from '@/components/notice/SystemNotice'
import { storeToRefs } from 'pinia'
import { useI18n } from 'vue-i18n'
import { useRoute, useRouter } from 'vue-router'
import type { RouteLocationNormalized, Router } from 'vue-router'

import { ReloadOutlined } from '@ant-design/icons-vue'
import { notification } from 'ant-design-vue'

import { useAppSettingStore, useAppStateStore, useAppI18nStore } from '@/store/modules/AppStore2'
import { useSystemMenuStore, useSystemExceptionStore } from '@/modules/system/store'

import defaultSettings from '@/config/defaultSetting'

import ProLayout from '@/components/layouts/ProLayout'
import RightContent from './header/RightContent.vue'
import SettingDrawer from './SettingDrawer/SettingDrawer'
import ExceptionModal from '../excption/ExceptionModal.vue'

import './BasicLayout.less'

import ApiService from '@/common/utils/ApiService'

const collapsedVueSupport = (appStateStore: any) => {
  const handleCollapse = () => {
    appStateStore.openCloseSidebar()
  }
  const { collapsed } = storeToRefs(appStateStore)
  const isMobile = ref(false)
  const query = ref({})
  const handleMediaQuery = (val: any) => {
    query.value = val
    if (isMobile.value && !val['screen-xs']) {
      isMobile.value = false
      return
    }
    if (!isMobile.value && val['screen-xs']) {
      isMobile.value = true
      appStateStore.collapsed = false
    }
  }
  return {
    collapsed: collapsed,
    handleCollapse,
    isMobile,
    query,
    handleMediaQuery
  }
}

/**
 * APP setting
 */
const settingVueSupport = () => {
  const appSettingStore = useAppSettingStore()

  const setting = appSettingStore.appSetting
  const handleSettingChange = ({ type, value }: any) => {
    appSettingStore.changeSetting(type, value)
  }
  return {
    setting,
    handleSettingChange
  }
}
/**
 * 加载用户菜单信息
 */
const UserMenuVueSupport = (route: RouteLocationNormalized, router: Router) => {
  const systemMenuStore = useSystemMenuStore()

  watch(route, () => {
    systemMenuStore.addMenu(route.fullPath)
  })

  onMounted(() => {
    systemMenuStore.addMenu('/main')
    if (route.fullPath !== '/main') {
      systemMenuStore.addMenu(route.fullPath)
    }
  })
  /**
   * 移除菜单
   */
  const handleTabRemove = (menu: any) => {
    systemMenuStore.removeMenu(menu.path)
  }

  const handleTabClick = (menu: any) => {
    router.push(menu.path)
  }

  return {
    userMenu: systemMenuStore.userTreeMenu,
    openMenuList: systemMenuStore.openMenuList,
    handleTabRemove,
    handleTabClick
  }
}

/**
 * 异常信息谭
 */
const useSystemException = () => {
  const systemExceptionStore = useSystemExceptionStore()
  const { modalVisible } = storeToRefs(systemExceptionStore)

  return {
    exceptionModalVisible: modalVisible
  }
}

/**
 * 基础布局
 */
export default defineComponent({
  name: 'BasicLayout',
  components: {
    ProLayout,
    ReloadOutlined,
    RightContent,
    SettingDrawer,
    ExceptionModal
  },
  setup() {
    const route = useRoute()
    const router = useRouter()

    const appStateStore = useAppStateStore()
    const appSettingStore = useAppSettingStore()
    const { settingDrawerVisible } = storeToRefs(appSettingStore)

    const collapsedVue = collapsedVueSupport(appStateStore)
    const settingVue = settingVueSupport()
    const i18nRender = useI18n().t
    const userMenuVue = UserMenuVueSupport(route, router)
    const systemExceptionHook = useSystemException()

    const appI18nStore = useAppI18nStore()
    const { lang } = storeToRefs(appI18nStore)

    onMounted(async () => {
      try {
        const result = await ApiService.postAjax('auth/isInitialPassword')
        if (result === true) {
          notification.warning({
            message: i18nRender('system.main.account.initPassword.noticeTitle'),
            description: i18nRender('system.main.account.initPassword.noticeDesc')
          })
        }
      } catch (e) {
        errorMessage(e)
      }
    })
    // 监控添加菜单事件
    const handleMenuClick = (menuId: string) => {
      router.push(menuId)
    }

    return {
      ...collapsedVue,
      ...settingVue,
      ...userMenuVue,
      ...systemExceptionHook,
      title: defaultSettings.title,
      isDev: import.meta.env.DEV,
      i18nRender,
      computedLang: lang,
      handleMenuClick,
      settingDrawerVisible
    }
  }
})
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
