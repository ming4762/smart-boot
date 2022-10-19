import { createRouter, createWebHashHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// APP 路由
import appRouters from '@/modules/app/router'
import { topRouters } from '@/modules/system/router'
import generatorRouters from '@/modules/generator/router'
// 监控模块路由
import monitorRouters from '@/modules/monitor/router'

const routes: Array<RouteRecordRaw> = [
  ...topRouters,
  ...generatorRouters,
  ...appRouters,
  ...monitorRouters
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router

export {
  routes
}
