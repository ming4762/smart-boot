import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'

// APP 路由
import appRouters from '@/modules/app/router'
import { topRouters } from '@/modules/system/router'
import generatorRouters from '@/modules/generator/router'

const routes: Array<RouteRecordRaw> = [
  ...topRouters,
  ...generatorRouters,
  ...appRouters
]


const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router

export {
  routes
}
