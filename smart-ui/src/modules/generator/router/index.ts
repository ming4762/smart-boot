import { RouteRecordRaw } from 'vue-router'
import BasicLayout from '@/modules/system/components/layouts/BasicLayout.vue'

const generatorRouters: Array<RouteRecordRaw> = [
  {
    path: '/',
    component: BasicLayout,
    children: [
      {
        path: 'code/databaseList',
        component: () => import('@/modules/generator/views/database/DatabaseListView.vue')
      },
      // 模板管理
      {
        path: 'code/templateList',
        component: () => import('@/modules/generator/views/template/CodeTemplateList.vue')
      },
      {
        path: 'code/codeList',
        component: () => import('@/modules/generator/views/codeList/CodeListView.vue')
      }
    ]
  }
]

export default generatorRouters
