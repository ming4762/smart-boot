<template>
  <pro-layout
    :collapsed="collapsed"
    :media-query="query"
    :is-mobile="isMobile"
    :open-menu-list="computedOpenMenuList"
    :tab-remove="handleTabRemove"
    :tab-click="handleTabClick"
    :handle-collapse="handleCollapse"
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

    <template #headerContentRender>
      <div>
        <a-tooltip title="刷新页面">
          <ReloadOutlined style="font-size: 18px;cursor: pointer;" @click="() => { console.log('=============') }" />
        </a-tooltip>
      </div>
    </template>

    <template #rightContentRender>
      <RightContent :is-mobile="isMobile" :theme="setting.theme" :top-menu="setting.layout === 'topmenu'" />
    </template>

    <SettingDrawer v-if="isDev" :i18n-render="i18nRender" :settings="setting" @change="handleSettingChange">
      <div style="margin: 12px 0;">
        This is SettingDrawer custom footer content.
      </div>
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
  </pro-layout>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, Ref, ref, watch } from 'vue'
import { Store, useStore } from 'vuex'
import { useI18n } from 'vue-i18n'
import { RouteLocationNormalized, Router, useRoute, useRouter } from 'vue-router'

import { ReloadOutlined } from '@ant-design/icons-vue'

import defaultSettings from '@/config/defaultSetting'
import { TOGGLE_MOBILE_TYPE } from '@/modules/system/store/mutation-types'

import { STORE_APP_MUTATION } from '@/common/constants/CommonConstants'

import ProLayout from '@/components/layouts/ProLayout'
import RightContent from './header/RightContent.vue'
import SettingDrawer from './SettingDrawer/SettingDrawer'

import './BasicLayout.less'
import TreeUtils from '@/common/utils/TreeUtils'

const MobileVueSupport = (collapsed: Ref<boolean>) => {
  const store = useStore()
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
      collapsed.value = false
    }
  }
  watch(isMobile, () => {
    store.commit(TOGGLE_MOBILE_TYPE, isMobile.value)
  })
  return {
    isMobile,
    query,
    handleMediaQuery
  }
}

const collapsedVueSupport = () => {
  const store = useStore()
  const collapsed = computed(() => store.getters['app/appCollapsed'])
  const handleCollapse = () => {
    store.commit(`app/${STORE_APP_MUTATION.APP_COLLAPSED_SIDEBAR}`)
  }
  return {
    collapsed,
    handleCollapse
  }
}

const settingVueSupport = (store: Store<any>) => {
  const setting = computed(() => store.getters['app/appSetting'])
  const handleSettingChange = ({ type, value }: any) => {
    store.commit(`app/${STORE_APP_MUTATION.CHANGE_SETTING}`, {
      key: type,
      value
    })
  }
  return {
    setting,
    handleSettingChange
  }
}


/**
 * 加载用户菜单信息
 */
const UserMenuVueSupport = (store : Store<any>) => {

  const userMenu = computed(() => {
    const userMenuList = store.getters['app/userMenuList']
    if (userMenuList === null || userMenuList === undefined) {
      return []
    }
    return  TreeUtils.convertList2Tree(JSON.parse(JSON.stringify(userMenuList)), ['id', 'parentId'], '0') || []
  })
  return {
    userMenu
  }
}

/**
 * tab相关
 */
const tabsVueSupport = (store: Store<any>, route: RouteLocationNormalized, router: Router) => {
  // 打开的菜单信息
  const computedOpenMenuList = computed(() => {
    return store.getters['app/openMenuList']
  })
  watch(route, () => {
    store.dispatch('app/addMenu', route.fullPath)
  })
  // 页面初始加载，如果路径不是主页 则添加页面
  onMounted(() => {
    store.dispatch('app/addMenu', '/main')
    if (route.fullPath !== '/main') {
      store.dispatch('app/addMenu', route.fullPath)
    }
  })
  /**
   * 移除菜单
   */
  const handleTabRemove = (menu: any) => {
    store.dispatch('app/removeMenu', menu.path)
  }
  const handleTabClick = (menu: any) => {
    router.push(menu.path)
    // store.dispatch('app/addMenu', menu.id)
  }
  return {
    computedOpenMenuList,
    handleTabRemove,
    handleTabClick
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
    SettingDrawer
  },
  setup () {
    const store = useStore()
    const route = useRoute()
    const router = useRouter()

    const collapsedVue = collapsedVueSupport()
    const mobileVue = MobileVueSupport(collapsedVue.collapsed)
    const settingVue = settingVueSupport(store)
    const i18nRender = useI18n().t
    const userMenuVue = UserMenuVueSupport(store)

    // TODO:不显示tab，以下代码无意义，如何优化？
    const tabsVue = tabsVueSupport(store, route, router)

    return {
      ...mobileVue,
      ...collapsedVue,
      ...settingVue,
      ...userMenuVue,
      title: defaultSettings.title,
      isDev: import.meta.env.DEV,
      i18nRender,
      ...tabsVue
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
