<template>
  <ConfigProvider :locale="getAntdLocale">
    <AppProvider>
      <RouterView />
    </AppProvider>
    <ExceptionModal />
  </ConfigProvider>
</template>

<script lang="ts" setup>
import { ConfigProvider } from 'ant-design-vue'
import { AppProvider } from '/@/components/Application'
import { useTitle } from '/@/hooks/web/useTitle'
import { useLocale } from '/@/locales/useLocale'
import { ExceptionModal } from '/@/components/Exception'

import 'dayjs/locale/zh-cn'
// support Multi-language
const { getAntdLocale } = useLocale()

// Listening to page changes and dynamically changing site titles
useTitle()
</script>
