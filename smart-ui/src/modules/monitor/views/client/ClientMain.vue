<template>
  <a-layout class="full-height client-main">
    <a-layout-header class="top-header">
      <div class="full-height header-left">
        <h3>{{ computedApplicationName }}</h3>
      </div>
      <div class="full-height header-right">
        <div>
          <a-form layout="inline">
            <a-form-item label="刷新时间：">
              <a-select v-model:value="refreshTimeModel" style="width: 120px">
                <a-select-option v-for="item in refreshTimes" :key="item.key" :value="item.key">{{ item.value }}</a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item>
              <a-button v-if="actuators.shutdown" type="primary" danger @click="handleShutdown">Shutdown</a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </a-layout-header>
    <a-layout class="full-height">
      <a-layout-sider style="overflow: auto"></a-layout-sider>
      <a-layout>
        <div class="page-container full-height">
          <div class="view full-height">
            <router-view v-slot="{ Component }">
              <transition name="page-transition">
                <component :is="Component" />
              </transition>
            </router-view>
          </div>
        </div>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, PropType } from 'vue'

import { useShutdown, useLoadApplication, useRefreshClient, useLoadActuator } from './ClientMainHook'

/**
 * 客户端管理主页
 */
export default defineComponent({
  name: 'ClientMain',
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {

    const shutDownHook = useShutdown(props.clientId)
    /**
     * 加载应用信息
     */
    const loadApplicationHook = useLoadApplication(props.clientId)
    /**
     * 客户端刷新hook
     */
    const refreshClientHook = useRefreshClient()
    /**
     * 加载端点
     */
    const { actuators, loadActuators } = useLoadActuator(props.clientId)
    loadActuators()
    return {
      actuators,
      ...shutDownHook,
      ...loadApplicationHook,
      ...refreshClientHook
    }
  }
})
</script>

<style lang="less" scoped>
.client-main {
  .ant-layout-header {
    padding: 0 15px !important;
  }
  .top-header {
    background: rgb(50, 109, 230);
    .header-right {
      display: flex;
      align-items: center;
      float: right;
      ::v-deep(label) {
        color: white !important;
      }
    }
    .header-left {
      display: flex;
      align-items: center;
      float: left;
      ::v-deep(h3) {
        color: white !important;
      }
    }
  }
}
</style>