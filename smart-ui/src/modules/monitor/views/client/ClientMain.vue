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
                <a-select-option v-for="item in refreshTimes" :key="item.key" :value="item.key">
                  {{ item.value }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item>
              <a-button
                v-if="actuators.shutdown"
                v-permission="'monitor:client:shutdown'"
                type="primary"
                danger
                @click="handleShutdown">
                Shutdown
              </a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
    </a-layout-header>
    <a-layout class="full-height">
      <a-layout-sider style="overflow: auto">
        <LayoutSide :data="menuData" :path-handler="pathHandler" :has-actuator="hasActuator" />
      </a-layout-sider>
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
import { defineComponent, reactive } from 'vue'
import type { PropType } from 'vue'

import { useShutdown, useLoadApplication, useRefreshClient } from './ClientMainHook'

import { useActuator } from '../../components/hooks/ClientActuatorHooks'

import LayoutSide from '@/modules/monitor/components/layout/LayoutSide.vue'

interface Data {
  icon: string
  path?: string
  actuator?: string
}

interface Menu {
  id: string
  data: Data
  text: string
  children: Array<Menu>
}

const menuList: Array<Menu> = [
  {
    id: '1',
    data: {
      icon: 'BarsOutlined'
    },
    text: 'Normal',
    children: [
      {
        id: '1-1',
        data: {
          icon: 'RadarChartOutlined',
          path: '/monitor/client/detail'
        },
        text: 'Details',
        children: []
      },
      {
        id: '1-2',
        data: {
          icon: 'ApiOutlined',
          path: '/monitor/client/environment',
          actuator: 'env'
        },
        text: 'Environment',
        children: []
      },
      {
        id: '1-3',
        data: {
          icon: 'ApartmentOutlined',
          path: '/monitor/client/beans',
          actuator: 'beans'
        },
        text: 'Beans',
        children: []
      },
      {
        id: '1-4',
        data: {
          icon: 'UnorderedListOutlined',
          path: '/monitor/client/metrics',
          actuator: 'metrics'
        },
        text: 'Metrics',
        children: []
      }
    ]
  },
  {
    id: '2',
    data: {
      icon: 'DatabaseOutlined',
      actuator: 'druidDatasource'
    },
    text: 'Druid',
    children: [
      {
        id: '2-1',
        data: {
          icon: 'ApiOutlined',
          path: '/monitor/client/druid/dbConnection',
          actuator: 'druidDatasource'
        },
        text: 'Connection',
        children: []
      },
      {
        id: '2-2',
        data: {
          icon: 'FileTextOutlined',
          path: '/monitor/client/druid/dbSql',
          actuator: 'druidSql'
        },
        text: 'sql',
        children: []
      },
      {
        id: '2-3',
        data: {
          icon: 'WalletOutlined',
          path: '/monitor/client/druid/dbWall',
          actuator: 'druidWall-datasourceName'
        },
        text: 'Wall',
        children: []
      }
    ]
  },
  {
    id: '3',
    data: {
      icon: 'ProfileOutlined'
    },
    text: 'Loggers',
    children: [
      {
        id: '3-1',
        data: {
          icon: 'SettingOutlined',
          path: '/monitor/client/loggerConfig',
          actuator: 'loggers'
        },
        text: 'LoggerConfig',
        children: []
      }
    ]
  },
  {
    id: '4',
    data: {
      icon: 'IeOutlined'
    },
    text: 'Web',
    children: [
      {
        id: '4-1',
        data: {
          icon: 'ForkOutlined',
          path: '/monitor/client/httpMapping',
          actuator: 'mappings'
        },
        text: 'Http Mapping',
        children: []
      }
    ]
  },
  {
    id: '5',
    data: {
      icon: 'DatabaseOutlined',
      actuator: 'redisInfo'
    },
    text: 'Redis',
    children: [
      {
        id: '5-1',
        data: {
          icon: 'InfoCircleOutlined',
          path: '/monitor/client/redis/info',
          actuator: 'redisInfo'
        },
        text: 'Info',
        children: []
      }
    ]
  }
]

/**
 * 客户端管理主页
 */
export default defineComponent({
  name: 'ClientMain',
  components: {
    LayoutSide
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup(props) {
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
    const { actuators, hasActuator } = useActuator(props.clientId)
    const pathHandler = (path: string) => {
      return {
        path: path,
        query: {
          clientId: props.clientId
        }
      }
    }
    return {
      actuators,
      ...shutDownHook,
      ...loadApplicationHook,
      ...refreshClientHook,
      menuData: reactive(menuList),
      pathHandler,
      hasActuator
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

.page-container {
  overflow: hidden;
}

.page-transition-enter {
  opacity: 0;
}
.page-transition-leave-active {
  opacity: 0;
}
.page-transition-enter .page-transition-container,
.page-transition-leave-active .page-transition-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
