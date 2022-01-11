<template>
  <a-dropdown v-if="currentUser && currentUser.fullName" placement="bottomRight">
    <span class="ant-pro-account-avatar">
      <a-avatar size="small" src="https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png" class="antd-pro-global-header-index-avatar" />
      <span>{{ currentUser.fullName }}</span>
    </span>
    <template #overlay>
      <a-menu class="ant-pro-drop-down menu" :selected-keys="[]" @click="handleAction">
        <a-menu-item v-if="menu" key="center">
          用户
        </a-menu-item>
        <a-menu-item key="logout">
          <LogoutOutlined />
          {{ $t('system.main.account.logout') }}
        </a-menu-item>
      </a-menu>
    </template>
  </a-dropdown>
</template>

<script lang="ts">
import { defineComponent, PropType, createVNode } from 'vue'

import { Modal } from 'ant-design-vue'
import { LogoutOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'

export default defineComponent({
  name: 'AvatarDropdown',
  components: {
    LogoutOutlined
  },
  props: {
    currentUser: {
      type: Object as PropType<object>,
      default: () => null
    },
    menu: {
      type: Boolean as PropType<boolean>,
      default: null
    }
  },
  methods: {
    handleAction ({ key }: any) {
      // 执行登出操作
      switch (key) {
        case 'logout': {
          this.handleLogout()
          break
        }
      }
    },
    handleLogout () {
      const { $store, $router} = this
      Modal.confirm({
        title: this.$t('app.common.notice.logout'),
        icon: createVNode(ExclamationCircleOutlined),
        onOk () {
          ApiService.postAjax('auth/logout')
            .finally(() => {
              $store.dispatch('app/logout')
              $router.push('/user/login')
            })
        }
      })
    }
  }
})
</script>

<style lang="less" scoped>
.ant-pro-drop-down {
  ::v-deep(.anticon) {
    margin-right: 8px;
  }
  ::v-deep(.ant-dropdown-menu-item) {
    min-width: 160px;
  }
}
</style>
