import type { RouteRecordRaw } from 'vue-router'
import BasicLayout from '@/modules/system/components/layouts/BasicLayout.vue'

const monitorRouters: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BasicLayout,
    children: [
      {
        path: 'monitor/manager/application',
        component: () =>
          import('@/modules/monitor/views/manager/application/MonitorApplicationListView.vue'),
        meta: {
          permission: 'menu:monitor:application'
        }
      },
      // 客户端管理页面
      {
        path: 'monitor/manager/client',
        component: () => import('@/modules/monitor/views/manager/client/ClientManagerPage.vue'),
        meta: {
          permission: 'menu:monitor:client'
        }
      },
      // 应用事件
      {
        path: 'monitor/manager/event',
        component: () => import('@/modules/monitor/views/manager/event/MonitorEventListView.vue'),
        meta: {
          permission: 'menu:monitor:event'
        }
      },
      {
        path: 'monitor/manager/slowSql',
        component: () =>
          import('@/modules/monitor/views/manager/slowSql/MonitorClientSlowSqlListView.vue'),
        meta: {
          permission: 'menu:monitor:slowSql'
        }
      },
      {
        path: 'monitor/manager/log',
        component: () => import('@/modules/monitor/views/manager/log/MonitorClientLogListView.vue'),
        meta: {
          permission: 'menu:monitor:log'
        }
      },
      {
        path: 'monitor/manager/httpTrace',
        component: () =>
          import('@/modules/monitor/views/manager/httpTrace/MonitorClientHttpTraceListView.vue'),
        meta: {
          permission: 'menu:monitor:httpTrace'
        }
      }
    ]
  },
  {
    path: '/monitor/client',
    name: 'Client',
    redirect: '/monitor/client/detail',
    component: () => import('@/modules/monitor/views/client/ClientMain.vue'),
    props: (route) => ({ clientId: route.query.clientId }),
    children: [
      {
        path: 'detail',
        component: () => import('@/modules/monitor/views/client/detail/ClientDetailPage.vue'),
        props: (route) => ({ clientId: route.query.clientId })
      },
      {
        path: 'environment',
        props: (route) => ({ clientId: route.query.clientId }),
        component: () =>
          import('@/modules/monitor/views/client/normal/environment/ClientEnvironmentPage.vue')
      },
      {
        path: 'beans',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/normal/beans/ClientBeansPage.vue')
      },
      {
        path: 'loggerConfig',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/logger/LoggerConfigPage.vue')
      },
      {
        path: 'httpMapping',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/http/HttpMappingView.vue')
      },
      {
        path: 'metrics',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/metrics/MetricsListView.vue')
      },
      // druid
      {
        path: 'druid/dbConnection',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/druid/connection/DbConnectionListView.vue')
      },
      {
        path: 'druid/dbSql',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/druid/sql/DbSqlListView.vue')
      },
      {
        path: 'druid/dbWall',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/druid/wall/DbWallListView.vue')
      },
      {
        path: 'redis/info',
        props: route => ({ clientId: route.query.clientId }),
        component: () => import('@/modules/monitor/views/client/redis/RedisMonitorView.vue')
      }
    ]
  }
]

export default monitorRouters
