<template>
  <a-dropdown placement="bottomRight">
    <span class="select-lang-trigger" :class="prefixCls">
      <GlobalOutlined :title="$t('system.lang.navBar')" />
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
import { GlobalOutlined } from '@ant-design/icons-vue'

import LangVueSupport from '@/i18n/LangVueSupport'

export default defineComponent({
  name: 'SelectLang',
  components: {
    GlobalOutlined
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
