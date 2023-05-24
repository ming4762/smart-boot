<script setup lang="ts">
import { computed, reactive, ref, unref, watch } from 'vue'
import { Form } from '@/components/Form'
import { useI18n } from '@/hooks/web/useI18n'
import { ElButton, ElCheckbox, ElLink } from 'element-plus'
import { useForm } from '@/hooks/web/useForm'
import { loginApi, listUserMenuApi } from '@/api/login'
import { useCache } from '@/hooks/web/useCache'
import { useAppStore } from '@/store/modules/app'
import { usePermissionStore } from '@/store/modules/permission'
import { useRouter } from 'vue-router'
import type { RouteLocationNormalizedLoaded, RouteRecordRaw } from 'vue-router'
import { UserType } from '@/api/login/types'
import { useValidator } from '@/hooks/web/useValidator'
import { FormSchema } from '@/types/form'
import { buildUUID } from '@/utils/uuid'
import { ApiServiceEnum, defHttp } from '@/config/axios'
import { createPassword } from '@/utils/auth'
import { useUserStore } from '@/store/modules/user'

const { required } = useValidator()

const emit = defineEmits(['to-register'])

const appStore = useAppStore()
const userStore = useUserStore()

const permissionStore = usePermissionStore()

const { currentRoute, addRoute, push } = useRouter()

const { wsCache } = useCache()

const { t } = useI18n()
const captchaKey = ref(buildUUID())

const rules = {
  username: [required()],
  password: [required()],
}

const schema = reactive<FormSchema[]>([
  {
    field: 'title',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'username',
    label: t('login.username'),
    value: 'admin',
    component: 'Input',
    colProps: {
      span: 24,
    },
    componentProps: {
      placeholder: t('login.usernamePlaceholder'),
    },
  },
  {
    field: 'password',
    label: t('login.password'),
    value: 'admin',
    component: 'InputPassword',
    colProps: {
      span: 24,
    },
    componentProps: {
      style: {
        width: '100%',
      },
      placeholder: t('login.passwordPlaceholder'),
    },
  },
  {
    field: 'captcha',
    component: 'InputPassword',
    colProps: {
      span: 24,
    },
    componentProps: {
      style: {
        width: '100%',
      },
    },
  },
  {
    field: 'tool',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'login',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'other',
    component: 'Divider',
    label: t('login.otherLogin'),
    componentProps: {
      contentPosition: 'center',
    },
  },
  {
    field: 'otherIcon',
    colProps: {
      span: 24,
    },
  },
])

const iconSize = 30

const remember = ref(false)

const { register, elFormRef, methods } = useForm()

const loading = ref(false)

const iconColor = '#999'

const redirect = ref<string>('')

watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true,
  },
)

// 登录
const signIn = async () => {
  const formRef = unref(elFormRef)
  await formRef?.validate(async (isValid) => {
    if (isValid) {
      loading.value = true
      const { getFormData } = methods
      const formData: any = await getFormData<UserType>()

      try {
        const res = await loginApi({
          username: formData.username,
          password: createPassword(formData.username, formData.password),
          codeKey: unref(captchaKey),
          code: formData.captcha,
        })

        if (res) {
          const { token, permissions, roles, user } = res
          // 设置用户信息
          userStore.setUserInfo(user)
          // 设置token
          userStore.setToken(token)
          // 设置角色和权限
          userStore.setRoleList(roles)
          userStore.setPermissionList(permissions)
          // 是否使用动态路由
          if (appStore.getDynamicRouter) {
            getRole()
          } else {
            await permissionStore.generateRoutes(false).catch(() => {})
            permissionStore.getAddRouters.forEach((route) => {
              addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
            })
            permissionStore.setIsAddRouters(true)
            push({ path: redirect.value || permissionStore.addRouters[0].path })
          }
        }
      } catch (e) {
        handleChangeCaptcha()
      } finally {
        loading.value = false
      }
    }
  })
}

// 获取角色信息
const getRole = async () => {
  const { getFormData } = methods
  const formData = await getFormData<UserType>()
  const params = {
    roleName: formData.username,
  }
  const routers = await listUserMenuApi()
  if (routers) {
    const { wsCache } = useCache()
    wsCache.set('roleRouters', routers)

    permissionStore.generateRoutes(true, routers).catch(() => {})

    permissionStore.getAddRouters.forEach((route) => {
      addRoute(route as RouteRecordRaw) // 动态添加可访问路由表
    })
    permissionStore.setIsAddRouters(true)
    push({ path: redirect.value || permissionStore.addRouters[0].path })
  }
}

// 去注册页面
const toRegister = () => {
  emit('to-register')
}

const computedCaptchaUrl = computed(() => {
  return `${defHttp.getApiUrlByService(
    ApiServiceEnum.SMART_AUTH,
  )}/auth/createCaptcha?codeKey=${unref(captchaKey)}`
})
const handleChangeCaptcha = () => {
  captchaKey.value = buildUUID()
}
</script>

<template>
  <Form
    :schema="schema"
    :rules="rules"
    label-position="top"
    hide-required-asterisk
    size="large"
    class="dark:(border-1 border-[var(--el-border-color)] border-solid)"
    @register="register">
    <template #title>
      <h2 class="text-2xl font-bold text-center w-[100%]">{{ t('login.login') }}</h2>
    </template>

    <template #tool>
      <div class="flex justify-between items-center w-[100%]">
        <ElCheckbox v-model="remember" :label="t('login.remember')" size="small" />
        <ElLink type="primary" :underline="false">{{ t('login.forgetPassword') }}</ElLink>
      </div>
    </template>

    <template #captcha="{ model }">
      <el-row :gutter="16" style="width: 100%">
        <el-col :span="16">
          <el-input
            size="large"
            :placeholder="t('system.login.login-captcha')"
            v-model="model.captcha" />
        </el-col>
        <el-col :span="8">
          <el-tooltip :content="t('system.login.captchaRefreshTooltip')">
            <img style="height: 40px" :src="computedCaptchaUrl" @click="handleChangeCaptcha" />
          </el-tooltip>
        </el-col>
      </el-row>
    </template>

    <template #login>
      <div class="w-[100%]">
        <ElButton :loading="loading" type="primary" class="w-[100%]" @click="signIn">
          {{ t('login.login') }}
        </ElButton>
      </div>
      <div class="w-[100%] mt-15px">
        <ElButton class="w-[100%]" @click="toRegister">
          {{ t('login.register') }}
        </ElButton>
      </div>
    </template>

    <template #otherIcon>
      <div class="flex justify-between w-[100%]">
        <Icon
          icon="ant-design:github-filled"
          :size="iconSize"
          class="cursor-pointer anticon"
          :color="iconColor" />
        <Icon
          icon="ant-design:wechat-filled"
          :size="iconSize"
          class="cursor-pointer anticon"
          :color="iconColor" />
        <Icon
          icon="ant-design:alipay-circle-filled"
          :size="iconSize"
          :color="iconColor"
          class="cursor-pointer anticon" />
        <Icon
          icon="ant-design:weibo-circle-filled"
          :size="iconSize"
          :color="iconColor"
          class="cursor-pointer anticon" />
      </div>
    </template>
  </Form>
</template>

<style lang="less" scoped>
:deep(.anticon) {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}
</style>
