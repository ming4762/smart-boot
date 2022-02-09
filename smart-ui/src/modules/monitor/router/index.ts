import { RouteRecordRaw } from 'vue-router'
import BasicLayout from '@/modules/system/components/layouts/BasicLayout.vue'

const monitorRouters: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BasicLayout,
    children: [
      {
        path: 'monitor/manager/application',
        component: () => import('@/modules/monitor/views/manager/application/MonitorApplicationListView.vue')
      }
    ]
  }
]

export default monitorRouters
