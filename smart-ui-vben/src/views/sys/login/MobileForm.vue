<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill" />
      </FormItem>
      <FormItem name="smsCode" class="enter-x">
        <CountdownInput
          ref="countInputRef"
          size="large"
          class="fix-auto-fill"
          @click="handleSendCode"
          :startHandler="handleDoSendCode"
          v-model:value="formData.smsCode"
          :placeholder="t('sys.login.smsCode')" />
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
    <SmartDragVerifyModal
      @register="registerCaptchaModal"
      type="SLIDER"
      @success="handleCaptchaValidateSuccess" />
  </template>
</template>
<script lang="ts" setup>
import { reactive, ref, computed, unref } from 'vue'
import { Form, Input, Button } from 'ant-design-vue'
import { CountdownInput } from '/@/components/CountDown'
import LoginFormTitle from './LoginFormTitle.vue'
import { useI18n } from '/@/hooks/web/useI18n'
import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin'
import { smsSendCodeApi } from '/@/api/sys/user'
import { errorMessage, successMessage } from '/@/common/utils/SystemNotice'
import { useUserStore } from '/@/store/modules/user'
import { SmartDragVerifyModal } from '/@/components/SmartDragVerify'
import { useModal } from '/@/components/Modal'

const FormItem = Form.Item
const { t } = useI18n()
const { handleBackLogin, getLoginState } = useLoginState()
const { getFormRules } = useFormRules()
const userStore = useUserStore()

const countInputRef = ref()
const formRef = ref()
const loading = ref(false)
const captchaTokenRef = ref('')

const formData = reactive({
  mobile: '',
  smsCode: '',
})

const { validForm } = useFormValid(formRef)

const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE)

const [registerCaptchaModal, { openModal, closeModal }] = useModal()
const handleSendCode = async () => {
  if (!formData.mobile || formData.mobile.trim() === '') {
    errorMessage(t('sys.login.trueMobile'))
    return false
  }
  openModal()
}

const handleCaptchaValidateSuccess = (token: string) => {
  closeModal()
  captchaTokenRef.value = token
  countInputRef.value?.start()
}

const handleDoSendCode = async () => {
  await smsSendCodeApi(formData.mobile, unref(captchaTokenRef))
  successMessage({
    msg: t('sys.login.smsCodeSuccess'),
  })
  return true
}

async function handleLogin() {
  const data = await validForm()
  if (!data) return

  // 执行登录
  try {
    loading.value = true
    await userStore.mobileLogin({
      phone: data.mobile,
      code: data.smsCode,
    })
  } finally {
    loading.value = false
  }
}
</script>
