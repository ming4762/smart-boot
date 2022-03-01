<template>
  <div>
    <a-menu
      theme="dark"
      mode="inline"
      :selected-keys="computedOpenMenu"
      @click="handlerClickMenu">
      <a-menu-item-group key="group1">
        <template #title>
          <span>导航</span>
        </template>
        <template v-for="item in data">
          <a-sub-menu v-if="item.children.length > 0" :key="item.id + ''" :disabled="noHasActuator(item.data.actuator)">
            <template #title>
              <component :is="icons[item.data.icon]" v-if="item.data.icon !== ''"></component>
              {{ item.text }}
            </template>
            <a-menu-item v-for="item2 in item.children" :key="item2.data.path" :disabled="noHasActuator(item2.data.actuator)">
              <component :is="icons[item2.data.icon]" v-if="item2.data.icon !== ''"></component>
              {{ item2.text }}
            </a-menu-item>
          </a-sub-menu>
          <a-menu-item v-else :key="item.id + ''" :disabled="noHasActuator(item.data.actuator)">
            <component :is="icons[item.data.icon]" v-if="item.data.icon !== ''"></component>
            {{ item.text }}
          </a-menu-item>
        </template>
      </a-menu-item-group>
    </a-menu>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import * as icons from '@ant-design/icons-vue'

/**
 * 侧边栏菜单
 */
export default defineComponent({
  name: 'SideMenu',
  props: {
    data: {
      type: Array,
      default: () => []
    },
    pathHandler: {
      type: Function,
      default: path => path
    },
    hasActuator: {
      type: Function,
      default: () => true
    }
  },
  setup (props) {
    const router = useRouter()
    const route = useRoute()
    const handlerClickMenu = ({ key }) => {
      router.push(props.pathHandler(key))
    }

    const noHasActuator = (actuator: string | undefined) => {
      if (actuator === undefined) {
        return false
      }
      return !props.hasActuator(actuator)
    }

    const computedOpenMenu = computed(() => {
      return [route.path]
    })
    return {
      handlerClickMenu,
      icons,
      computedOpenMenu,
      noHasActuator
    }
  }
})
</script>

<style scoped>

</style>
