<template>
  <BasicModal
    @register="registerModal"
    :title="$t('layout.header.changePassword')"
    @ok="handleChangePassword">
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>

<script lang="ts" setup>
import { useModalInner, BasicModal } from '/@/components/Modal'
import { useForm, BasicForm } from '/@/components/Form'
import { useI18n } from '/@/hooks/web/useI18n'
import { useUserStore } from '/@/store/modules/user'
import { successMessage } from '/@/common/utils/SystemNotice'

const { t } = useI18n()

const [registerModal, { changeOkLoading, closeModal }] = useModalInner()

const handleChangePassword = async () => {
  try {
    changeOkLoading(true)
    const model = await validate()
    const { changePassword } = useUserStore()
    await changePassword(model)
    successMessage({
      msg: t('app.changePassword.successMessage'),
    })
    closeModal()
  } finally {
    changeOkLoading(false)
  }
}

const [registerForm, { validate }] = useForm({
  showActionButtonGroup: false,
  colon: true,
  baseColProps: {
    span: 24,
  },
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 17,
  },
  schemas: [
    {
      label: t('app.changePassword.oldPassword'),
      field: 'oldPassword',
      component: 'InputPassword',
      required: true,
    },
    {
      label: t('app.changePassword.newPassword'),
      field: 'newPassword',
      component: 'StrengthMeter',
      required: true,
    },
    {
      label: t('app.changePassword.newPasswordConfirm'),
      field: 'newPasswordConfirm',
      component: 'InputPassword',
      dynamicRules: ({ model }) => {
        return [
          {
            required: true,
            validator: (rule, value) => {
              const { newPassword } = model
              if (!newPassword) {
                return Promise.resolve()
              }
              if (newPassword !== value) {
                return Promise.reject('密码不一致')
              }
              return Promise.resolve()
            },
          },
        ]
      },
    },
  ],
})
</script>

<style scoped></style>
