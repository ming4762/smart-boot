<template>
  <BasicModal
    @register="registerModal"
    @ok="handleSave"
    width="1200px"
    :mask-closable="false"
    :ok-text="$t('common.button.save')"
    :title="$t('system.views.user.account.title')">
    <a-descriptions :title="$t('system.views.user.account.title')" bordered>
      <a-descriptions-item :label="$t('system.views.user.table.username')">
        {{ userData.username }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.table.fullName')">
        {{ userData.fullName }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.table.userType')">
        {{ userData.userType }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.user.table.mobile')">
        {{ userData.mobile }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.table.email')" :span="2">
        {{ userData.email }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.user.account.createTime')">
        {{ accountData.createTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.account.accountStatus')">
        {{ accountData.accountStatus }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.account.initialPasswordYn')">
        <ATag v-if="accountData.initialPasswordYn" color="#f50">
          {{ $t('common.form.yes') }}
        </ATag>
        <ATag v-else color="#108ee9">{{ $t('common.form.no') }}</ATag>
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.user.account.loginFailTime')">
        {{ accountData.loginFailTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.account.lockTime')">
        {{ accountData.lockTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.user.account.passwordModifyTime')">
        {{ accountData.passwordModifyTime }}
      </a-descriptions-item>
    </a-descriptions>
    <a-divider />
    <section class="account-setting">
      <div class="title">{{ $t('system.views.user.account.accountSet') }}</div>
      <BasicForm @register="registerForm" />
    </section>
  </BasicModal>
</template>

<script lang="ts" setup>
import { computed, ref, unref } from 'vue'

import { BasicModal, useModalInner } from '/@/components/Modal'
import { useForm, BasicForm } from '/@/components/Form'
import { hasPermission } from '/@/common/auth/AuthUtils'
import { useI18n } from '/@/hooks/web/useI18n'
import { message } from 'ant-design-vue'

import { saveAccountSettingApi, getByIdApi } from '../UserListView.api'

const { t } = useI18n()

const userData = ref({})
const accountData = ref({})

/**
 * 是否有编辑权限
 */
const computedHasEditPermission = computed(() => hasPermission('sys:account:update'))

const [registerModal, { changeOkLoading, closeModal, changeLoading }] = useModalInner(
  async (user) => {
    userData.value = {}
    accountData.value = {}
    changeLoading(true)
    try {
      const result = await getByIdApi(user.userId)
      if (result) {
        userData.value = result
        if (result.userAccount) {
          accountData.value = result.userAccount
          setFieldsValue({
            ...result.userAccount,
          })
        } else {
          message.error(t('system.views.user.message.noAccount'))
        }
      }
    } finally {
      changeLoading(false)
    }
  },
)

const handleSave = async () => {
  const data = await validate()
  try {
    changeOkLoading(true)
    await saveAccountSettingApi(data)
    closeModal()
  } finally {
    changeOkLoading(false)
  }
}

const [registerForm, { setFieldsValue, validate }] = useForm({
  showActionButtonGroup: false,
  colon: true,
  baseColProps: {
    span: 12,
  },
  rowProps: {
    gutter: 18,
  },
  schemas: [
    {
      label: '',
      field: 'userId',
      component: 'Input',
      show: false,
    },
    {
      label: t('system.views.user.account.maxConnections'),
      field: 'maxConnections',
      component: 'InputNumber',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.maxDaysSinceLogin'),
      field: 'maxDaysSinceLogin',
      component: 'InputNumber',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.passwordLifeDays'),
      field: 'passwordLifeDays',
      component: 'InputNumber',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.maxConnectionsPolicy'),
      field: 'maxConnectionsPolicy',
      component: 'Select',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        options: [
          {
            label: t('system.views.user.account.loginNotAllow'),
            value: 'LOGIN_NOT_ALLOW',
          },
          {
            label: t('system.views.user.account.firstUserLogout'),
            value: 'FIRST_USER_LOGOUT',
          },
        ],
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.loginFailTimeLimit'),
      field: 'loginFailTimeLimit',
      component: 'InputNumber',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.passwordErrorUnlockSecond'),
      field: 'passwordErrorUnlockSecond',
      component: 'InputNumber',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        style: {
          width: '100%',
        },
      },
    },
    {
      label: t('system.views.user.account.ipWhiteList'),
      field: 'ipWhiteList',
      component: 'InputTextArea',
      componentProps: {
        disabled: !unref(computedHasEditPermission),
        rows: 4,
      },
    },
  ],
})
</script>

<style lang="less" scoped>
.account-setting {
  .title {
    font-size: 16px;
    font-weight: 700;
    margin-bottom: 5px;
  }
}
</style>
