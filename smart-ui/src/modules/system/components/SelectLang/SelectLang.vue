<template>
  <a-dropdown placement="bottomRight">
    <span class="select-lang-trigger" :class="prefixCls">
      <Icon :title="$t('system.lang.navBar')">
        <template #component>
          <svg
            x="1666318227493"
            class="icon"
            viewBox="0 0 1024 1024"
            version="1.1"
            xmlns="http://www.w3.org/2000/svg"
            p-id="1778"
            width="16"
            height="16">
            <path
              d="M547.797333 638.208l-104.405333-103.168 1.237333-1.28a720.170667 720.170667 0 0 0 152.490667-268.373333h120.448V183.082667h-287.744V100.906667H347.605333v82.218666H59.818667V265.386667h459.178666a648.234667 648.234667 0 0 1-130.304 219.946666 643.242667 643.242667 0 0 1-94.976-137.728H211.541333a722.048 722.048 0 0 0 122.453334 187.434667l-209.194667 206.378667 58.368 58.368 205.525333-205.525334 127.872 127.829334 31.232-83.84m231.424-208.426667h-82.218666l-184.96 493.312h82.218666l46.037334-123.306667h195.242666l46.464 123.306667h82.218667l-185.002667-493.312m-107.690666 287.744l66.56-178.005333 66.602666 178.005333z"
              fill="#2c2c2c"
              p-id="1779"></path>
          </svg>
        </template>
      </Icon>
    </span>
    <template #overlay>
      <a-menu
        :selected-keys="[currentLang]"
        class="menu ant-pro-header-menu"
        @click="handleLangChange">
        <a-menu-item v-for="item in languageList" :key="item.key">
          <span role="img" :aria-label="item.name">{{ item.icon }}</span>
          {{ item.name }}
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import type { PropType } from 'vue'
import Icon from '@ant-design/icons-vue'

import LangVueSupport from '@/i18n/LangVueSupport'

export default defineComponent({
  name: 'SelectLang',
  components: {
    Icon
  },
  props: {
    prefixCls: {
      type: String as PropType<string>,
      default: 'ant-pro-drop-down'
    }
  },
  setup() {
    const vueLangSupport = LangVueSupport()
    const handleLangChange = ({ key }: any) => {
      vueLangSupport.setLang(key)
    }
    return {
      ...vueLangSupport,
      handleLangChange
    }
  }
})
</script>

<style scoped></style>
