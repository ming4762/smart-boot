<template>
  <div>
    <a-dropdown v-if="currentUser && currentUser.fullName" placement="bottomRight">
      <span class="ant-pro-account-avatar">
        <a-avatar
          size="small"
          :src="currentUser.avatar || '/src/modules/system/assets/default_avator.png'"
          class="antd-pro-global-header-index-avatar" />
        <span>{{ currentUser.fullName }}</span>
      </span>
      <template #overlay>
        <a-menu class="ant-pro-drop-down menu" :selected-keys="[]" @click="handleAction">
          <a-menu-item v-if="menu" key="userAccount">
            <UserOutlined />
            {{ $t('system.main.account.userAccount.title') }}
          </a-menu-item>
          <a-menu-item key="changePassword">
            <LockOutlined />
            {{ $t('system.main.account.changePassword.title') }}
          </a-menu-item>
          <a-menu-item key="logout">
            <LogoutOutlined />
            {{ $t('system.main.account.logout') }}
          </a-menu-item>
          <a-menu-item v-if="isDev" key="settingDrawer">
            <setting-outlined />
            {{ $t('system.setting.title') }}
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
    <!--  修改密码  -->
    <a-modal
      :visible="changePasswordModalVisible"
      width="650px"
      :get-container="getContainer"
      :title="$t('system.main.account.changePassword.title')"
      @cancel="changePasswordModalVisible = false"
      @ok="handleChangePassword">
      <a-form
        ref="changePasswordFormRef"
        :label-col="{ span: 7 }"
        :wrapper-col="{ span: 16 }"
        :rules="changePasswordRules"
        :model="changePasswordModel">
        <a-form-item
          name="oldPassword"
          :label="$t('system.main.account.changePassword.oldPassword')">
          <a-input-password
            v-model:value="changePasswordModel.oldPassword"
            :placeholder="$t('system.main.account.changePassword.validateOldPassword')" />
        </a-form-item>
        <a-form-item
          name="newPassword"
          :label="$t('system.main.account.changePassword.newPassword')">
          <a-input-password
            v-model:value="changePasswordModel.newPassword"
            :placeholder="$t('system.main.account.changePassword.validateNewPassword')" />
        </a-form-item>
        <a-form-item
          name="newPasswordConfirm"
          :label="$t('system.main.account.changePassword.newPasswordConfirm')">
          <a-input-password
            v-model:value="changePasswordModel.newPasswordConfirm"
            :placeholder="$t('system.main.account.changePassword.validateNewPasswordConfirm')" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, createVNode, ref, reactive } from 'vue'
import type { PropType } from 'vue'
import { useI18n } from 'vue-i18n'

import { Modal, message } from 'ant-design-vue'
import {
  LogoutOutlined,
  ExclamationCircleOutlined,
  LockOutlined,
  UserOutlined,
  SettingOutlined
} from '@ant-design/icons-vue'

import { useAppSettingStore } from '@/store/modules/AppStore2'
import { useSystemLoginStore } from '@/modules/system/store'

import ApiService from '@/common/utils/ApiService'
import { createPassword, getCurrentUser } from '@/common/auth/AuthUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 验证新旧密码是否一致
 */
const validateNewConfirm = (newPassword: any, newConfirmPassword: any, t: Function) => {
  return new Promise<void>((resolve, reject) => {
    if (newConfirmPassword && newPassword !== newConfirmPassword) {
      reject(t('system.main.account.changePassword.validateNewPasswordSame'))
    } else {
      resolve()
    }
  })
}

/**
 * 验证新旧密码是否一致
 */
const validateOldNewPasswordSame = (oldPassword: any, newPassword: any, t: Function) => {
  return new Promise<void>((resolve, reject) => {
    if (oldPassword === newPassword) {
      reject(t('system.main.account.changePassword.validateOldNewPasswordSame'))
    } else {
      resolve()
    }
  })
}

const changePasswordHoop = (t: Function) => {
  const changePasswordFormRef = ref()
  // modal显示状态
  const changePasswordModalVisible = ref(false)
  const getContainer = () => document.body
  const changePasswordModel = ref<any>({})
  const changePasswordRules = reactive({
    oldPassword: [
      {
        required: true,
        message: t('system.main.account.changePassword.validateOldPassword'),
        trigger: 'blur',
        whitespace: true
      },
      // 验证新旧密码是否一致
      {
        required: true,
        trigger: 'blur',
        whitespace: true,
        validator: (rule: any, value: any) =>
          validateOldNewPasswordSame(value, changePasswordModel.value.newPassword, t)
      }
    ],
    newPassword: [
      {
        required: true,
        message: t('system.main.account.changePassword.validateNewPassword'),
        trigger: 'blur',
        whitespace: true
      },
      // 验证2次输入密码是否一致
      {
        required: true,
        trigger: 'blur',
        whitespace: true,
        validator: (rule: any, value: any) =>
          validateNewConfirm(value, changePasswordModel.value.newPasswordConfirm, t)
      },
      // 验证新旧密码是否一致
      {
        required: true,
        trigger: 'blur',
        whitespace: true,
        validator: (rule: any, value: any) =>
          validateOldNewPasswordSame(changePasswordModel.value.oldPassword, value, t)
      }
    ],
    newPasswordConfirm: [
      {
        required: true,
        message: t('system.main.account.changePassword.validateNewPasswordConfirm'),
        trigger: 'blur',
        whitespace: true
      },
      {
        required: true,
        message: t('system.main.account.changePassword.validateNewPasswordSame'),
        trigger: 'blur',
        whitespace: true,
        validator: (rule: any, value: any) =>
          validateNewConfirm(changePasswordModel.value.newPassword, value, t)
      }
    ]
  })
  /**
   * 显示弹窗
   */
  const handleShowPasswordModal = () => {
    changePasswordModel.value = {}
    changePasswordModalVisible.value = true
  }
  /**
   * 执行修改密码
   */
  const handleChangePassword = async () => {
    try {
      // 表单验证
      await changePasswordFormRef.value.validate()
    } catch (e) {
      return false
    }
    Modal.confirm({
      title: t('common.notice.confirm'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('system.main.account.changePassword.changeConfirm'),
      onOk: async () => {
        try {
          const { oldPassword, newPassword, newPasswordConfirm } = changePasswordModel.value
          const { username } = getCurrentUser()
          await ApiService.postAjax('/auth/changePassword', {
            oldPassword: createPassword(username, oldPassword),
            newPassword: createPassword(username, newPassword),
            newPasswordConfirm: createPassword(username, newPasswordConfirm)
          })
          changePasswordModalVisible.value = false
          message.success(t('system.main.account.changePassword.editSuccessMessage'))
        } catch (e) {
          errorMessage(e)
        }
      }
    })
  }

  return {
    changePasswordModalVisible,
    handleShowPasswordModal,
    handleChangePassword,
    changePasswordModel,
    changePasswordFormRef,
    changePasswordRules,
    getContainer
  }
}

export default defineComponent({
  name: 'AvatarDropdown',
  components: {
    LogoutOutlined,
    LockOutlined,
    UserOutlined,
    SettingOutlined
  },
  props: {
    currentUser: {
      type: Object as PropType<any>,
      default: () => null
    },
    menu: {
      type: Boolean as PropType<boolean>,
      default: null
    }
  },
  setup() {
    const { t } = useI18n()
    const appSettingStore = useAppSettingStore()
    const systemLoginStore = useSystemLoginStore()
    return {
      ...changePasswordHoop(t),
      isDev: import.meta.env.DEV,
      appSettingStore,
      systemLoginStore
    }
  },
  methods: {
    handleAction({ key }: any) {
      // 执行登出操作
      switch (key) {
        case 'logout': {
          this.handleLogout()
          break
        }
        case 'changePassword': {
          this.handleShowPasswordModal()
          break
        }
        case 'userAccount': {
          // TODO: 开发中
          message.warn('开发中')
          break
        }
        case 'settingDrawer': {
          this.appSettingStore.showHideSettingDrawer(true)
          break
        }
      }
    },
    handleLogout() {
      const { $router, systemLoginStore } = this
      Modal.confirm({
        title: this.$t('app.common.notice.logout'),
        icon: createVNode(ExclamationCircleOutlined),
        onOk() {
          return ApiService.postAjax('auth/logout').finally(() => {
            systemLoginStore.logout()
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
