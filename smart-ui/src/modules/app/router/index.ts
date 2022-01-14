import { RouteRecordRaw } from 'vue-router'
import BasicLayout from '@/modules/system/components/layouts/BasicLayout.vue'

/**
 * APP 路由
 */
const appRouters: Array<RouteRecordRaw> = [
  {
    path: '/error/404',
    component: () => import('@/modules/app/views/exception/404.vue'),
    meta: {
      title: '404'
    }
  },
  {
    path: '/error/403',
    component: () => import('@/modules/app/views/exception/403.vue'),
    meta: {
      title: '403'
    }
  },
  {
    path: '/',
    component: BasicLayout,
    children: [
      {
        path: '',
        component: () => import('@/modules/app/views/main/Main.vue'),
        meta: {
          title: '{system.pageTitle.main}'
        }
      }
    ]
  }
]

export default appRouters
