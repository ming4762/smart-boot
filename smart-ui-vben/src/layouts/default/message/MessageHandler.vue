<template>
  <div></div>
</template>
<script setup lang="ts">
import { useRegisterWebsocketMessageHandler } from '/@/hooks/web/useSmartWebSocket'

useRegisterWebsocketMessageHandler('test', {
  message: (e) => {
    console.log(e)
  },
})
</script>

<style scoped lang="less"></style>
