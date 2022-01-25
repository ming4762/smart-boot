<template>
  <div class="login-main">
    <div class="form-container">
      <a-form
        ref="formRef"
        :model="loginModel"
        :rules="rules"
        :wrapper-col="{
          span: 24
        }"
        class="user-layout-login">
        <a-tabs v-model:activeKey="customActiveKey" animated centered>
          <a-tab-pane key="username" :tab="$t('system.login.tab-login-username')">
            <a-form-item name="username">
              <a-input
                v-model:value="loginModel.username"
                type="text"
                :placeholder="$t('system.login.login-username-validate')"
                size="large"
                @pressEnter="handleLogin">
                <template #prefix>
                  <user-outlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item name="password">
              <a-input-password
                v-model:value="loginModel.password"
                :placeholder="$t('system.login.login-password-validate')"
                size="large"
                @pressEnter="handleLogin">
                <template #prefix>
                  <LockOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item>
              <a-row :gutter="16">
                <a-col :span="16">
                  <a-form-item name="captcha">
                    <a-input v-model:value="loginModel.captcha" size="large" type="text" :placeholder="$t('system.login.login-captcha')" @pressEnter="handleLogin">
                      <template #prefix>
                        <MailOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                      </template>
                    </a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="8">
                  <a-tooltip>
                    <template #title>{{ $t('system.login.captchaRefreshTooltip') }}</template>
                    <img style="height: 40px" :src="computedCaptchaUrl" @click="handleChangeCaptcha" />
                  </a-tooltip>
                </a-col>
              </a-row>
            </a-form-item>
          </a-tab-pane>
          <a-tab-pane key="phone" :tab="$t('system.login.tab-login-phone')">
            <a-form-item name="phone">
              <a-input
                v-model:value="loginModel.phone"
                type="text"
                :placeholder="$t('system.login.login-phone-validate')"
                size="large">
                <template #prefix>
                  <MobileOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                </template>
              </a-input>
            </a-form-item>
            <a-row :gutter="16">
              <a-col class="gutter-row" :span="16">
                <a-form-item name="phoneCode">
                  <a-input v-model:value="loginModel.phoneCode" size="large" type="text" :placeholder="$t('system.login.login-phone-code-validate')">
                    <template #prefix>
                      <MailOutlined :style="{ color: 'rgba(0,0,0,.25)' }" />
                    </template>
                  </a-input>
                </a-form-item>
              </a-col>
              <a-col class="gutter-row" :span="8">
                <a-button class="getCaptcha" :disabled="state.smsButtonActive" size="large" @click="handleSendSms">{{ computedSmsButtonText }}</a-button>
              </a-col>
            </a-row>
          </a-tab-pane>
        </a-tabs>
        <a-form-item>
          <a-button size="large" :loading="loginLoading" block type="primary" @click="handleLogin">{{ $t('system.login.login-button') }}</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { RouteRecord, useRouter } from 'vue-router'
import { useStore } from 'vuex'

import { message } from 'ant-design-vue'
import { UserOutlined, LockOutlined, MobileOutlined, MailOutlined }  from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'
import { saveUser, saveUserPermission, saveUserRole, saveToken, getToken, createPassword } from '@/common/auth/AuthUtils'
import defaultSettings from '@/config/defaultSetting'
import { generateUUID } from '@/common/utils/KeyGenerator'

/**
 * 设置路由元数据
 */
const setRouteMeta = (routes: Array<RouteRecord>, menuList: Array<any>) => {
  const pathMetaMap = new Map()
  menuList.forEach(menu => {
    if (menu.path) {
      pathMetaMap.set(menu.path, menu.meta)
    }
  })
  routes.forEach(route => {
    const path = route.path
    const menuMeta = pathMetaMap.get(path)
    if (menuMeta) {
      route.meta = {
        ...menuMeta,
        title: route.meta.title
      }
    }
  })
}

const useCaptcha = () => {
  const captchaKey = ref<string>(generateUUID())
  const computedCaptchaUrl = computed(() => {
    return `${ApiService.getApiUrl()}auth/createCaptcha?codeKey=${captchaKey.value}`
  })
  const handleChangeCaptcha = () => {
    captchaKey.value = generateUUID()
  }
  return {
    captchaKey,
    computedCaptchaUrl,
    handleChangeCaptcha
  }
}

export default defineComponent({
  name: 'LoginView',
  components: {
    UserOutlined,
    LockOutlined,
    MobileOutlined,
    MailOutlined
  },
  setup() {
    const formRef = ref()
    const store = useStore()
    const i18n = useI18n()
    const router = useRouter()
    const loginLoading = ref(false)
    const customActiveKey = ref('username')
    const captchaHook = useCaptcha()

    // 执行登出操作，防止直接跳转到登录页面未执行登录
    if (getToken()) {
      ApiService.postAjax('auth/logout')
        .finally(() => {
          store.dispatch('app/logout')
        })
    }
    const loginModel = reactive({
      username: '',
      password: '',
      phone: '',
      phoneCode: '',
      captcha: ''
    })
    const state = reactive({
      time: 60,
      smsButtonActive: false
    })
    /**
     * 发送验证按钮text
     */
    const computedSmsButtonText = computed(() => {
      if (!state.smsButtonActive) {
        return i18n.t('system.login.login-phone-getCaptcha')
      }
      return `${state.time}s`
    })
    /**
     * 发送验证码
     */
    const handleSendSms = () => {
      // TODO：待完善
      state.smsButtonActive = true
      const interval = setInterval(() => {
        if (state.time === 0) {
          state.time = 60
          state.smsButtonActive = false
          window.clearInterval(interval)
        } else {
          state.time = state.time - 1
        }
      }, 1000)
    }
    /**
     * 执行登录操作
     * @param e
     */
    const handleLogin = async (e: Event) => {
      e.preventDefault()
      const fields = customActiveKey.value === 'username' ? ['username', 'password', 'captcha'] : ['phone', 'phoneCode']
      try {
        await formRef.value.validate(fields)
      } catch (e) {
        // 验证不通过
        console.error(e)
        return false
      }
      // 验证通过，执行登录操作
      let requestParameter
      if (customActiveKey.value === 'username') {
        requestParameter = {
          username: loginModel.username,
          password: createPassword(loginModel.username, loginModel.password),
          codeKey: captchaHook.captchaKey.value,
          code: loginModel.captcha
        }
      } else {
        requestParameter = {
          phone: loginModel.phone,
          phoneCode: loginModel.phoneCode
        }
      }
      loginLoading.value = true

      try {
        const { user, token, roles, permissions } = await ApiService.postForm('auth/login', requestParameter)
        saveUser(user)
        saveUserRole(roles)
        saveUserPermission(permissions)
        saveToken(token)
        const languageList = defaultSettings.languageList.map(item => item.key)
        // 加载菜单信息
        const loadMenus = await ApiService.postAjax('sys/user/listUserMenu', languageList) || []
        const formatUserMenu = loadMenus.map((item: any) => {
          const { functionName, functionType, icon, url, functionId, parentId, locales } = item
          return {
            id: functionId.toString(),
            parentId: parentId.toString(),
            name: functionId.toString(),
            path: url,
            meta: {
              title: functionName,
              type: functionType,
              icon,
              locales
            }
          }
        })
        const allUserMenu = [
          {
            id: 'main',
            name: 'main',
            path: '/main',
            meta: {
              title: '{system.pageTitle.main}',
              locales: {
                'zh-CN': '主页',
                'en-US': 'Main'
              }
            }
          }
        ].concat(formatUserMenu)
        store.dispatch('app/setUserMenu', allUserMenu)
        // 设置route meta
        setRouteMeta(router.getRoutes(), allUserMenu)
        router.push('/')
      } catch (error: any) {
        error.message && message.error(error.message)
        captchaHook.handleChangeCaptcha()
      } finally {
        loginLoading.value = false
      }
    }
    return {
      ...captchaHook,
      formRef,
      customActiveKey,
      state,
      computedSmsButtonText,
      handleSendSms,
      handleLogin,
      loginModel,
      loginLoading
    }
  },
  data () {
    return {
      rules: {
        username: [
          { required: true, message: this.$t('system.login.login-username-validate'), trigger: 'blur' }
        ],
        password: [
          { required: true, message: this.$t('system.login.login-password-validate'), trigger: 'blur' }
        ],
        phone: [
          { required: true, message: this.$t('system.login.login-phone-validate'), trigger: 'blur' }
        ],
        phoneCode: [
          { required: true, message: this.$t('system.login.login-phone-code-validate'), trigger: 'blur' }
        ],
        captcha: [
          { required: true, message: this.$t('system.login.login-captcha'), trigger: 'blur' }
        ]
      }
    }
  }
})
</script>

<style lang='less' scoped>
.login-main {
  padding-top: 160px;
  .form-container {
    width: 400px;
    margin: 0 auto;
    min-width: 260px;

    .getCaptcha {
      display: block;
      width: 100%;
      height: 40px;
    }
  }
}

</style>
