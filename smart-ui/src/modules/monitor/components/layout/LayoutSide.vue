<template>
  <div class="side-menu-container">
    <SideMenu v-bind="$attrs" />
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import SideMenu from './SideMenu.vue'

/**
 * 侧边栏
 */
export default defineComponent({
  name: 'LayoutSide',
  components: {
    SideMenu
  }
})

</script>

<style lang="less" scoped>
  .side-menu-container{
    padding-top: 10px;
  }
</style>
