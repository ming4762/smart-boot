import type { RouteRecordRaw } from 'vue-router'
import UserLayout from '../components/layouts/UserLayout.vue'
import BasicLayout from '../components/layouts/BasicLayout.vue'

const topRouters: Array<RouteRecordRaw> = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "LoginView" */ '../views/login/LoginView.vue'),
        meta: {
          title: '{system.pageTitle.login}'
        }
      }
    ]
  },
  {
    path: '/',
    component: BasicLayout,
    redirect: '/main',
    children: [
      {
        path: 'main',
        component: () => import('@/modules/app/views/main/Main.vue'),
        meta: {
          title: '{system.pageTitle.main}'
        }
      },
      {
        path: 'sys/userList',
        component: () => import('@/modules/system/views/user/UserListView.vue'),
        meta: {
          permission: 'menu:system:userList'
        }
      },
      {
        name: 'system_roleList',
        path: 'sys/roleList',

        component: () => import('@/modules/system/views/role/RoleManagerView.vue'),
        meta: {
          permission: 'menu:system:roleManager'
        }
      },
      {
        path: 'sys/i18n',
        component: () => import('@/modules/system/views/i18n/I18nMainView.vue'),
        meta: {
          permission: 'menu:system:i18n'
        }
      },
      {
        path: 'sys/functionList',
        component: () => import('@/modules/system/views/function/FunctionListView.vue'),
        meta: {
          permission: 'menu:system:functionManager'
        }
      },
      {
        name: 'system_userGroupList',
        path: 'sys/userGroupList',
        component: () => import('@/modules/system/views/userGroup/UserGroupListView.vue'),
        meta: {
          permission: 'menu:system:userGroupManager'
        }
      },
      // auth相关
      // 在线用户
      {
        name: 'auth_onlineUser',
        path: 'monitor/onlineUserList',
        component: () => import('@/modules/system/views/auth/onlineUser/OnlineUserListView.vue'),
        meta: {
          permission: 'menu:system:onlineUser'
        }
      },
      {
        name: 'system_log',
        path: 'sys/logList',
        component: () => import('@/modules/system/views/log/LogListView.vue'),
        meta: {
          permission: 'menu:system:log'
        }
      },
      {
        name: 'data_dict',
        path: 'sys/dataDict',
        component: () => import('@/modules/system/views/dict/dataDict/DataDictListView.vue'),
        meta: {
          permission: 'menu:system:DataDict'
        }
      },
      {
        name: 'system_exception',
        path: 'sys/exception',
        component: () => import('@/modules/system/views/exception/SysExceptionListView.vue'),
        meta: {
          permission: 'menu:system:exception'
        }
      },
      {
        name: 'system_file',
        path: 'sys/file',
        component: () => import('@/modules/system/views/file/SysFileListView.vue'),
        meta: {
          permission: 'menu:system:file'
        }
      },
      {
        name: 'system_dept',
        path: 'sys/dept',
        component: () => import('@/modules/system/views/dept/SysDeptTreeList.vue'),
        meta: {
          permission: 'menu:system:dept'
        }
      }
    ]
  }
]

export { topRouters }
