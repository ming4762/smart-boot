import { defineComponent, watch, createVNode } from 'vue'
import type { PropType } from 'vue'
import { storeToRefs } from 'pinia'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'

import { Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import { useSystemLoginStore } from '@/modules/system/store'
import { LOGIN_PATH } from '@/modules/system/constants/SystemConstants'

export default defineComponent({
  name: 'LoginMessageModal',
  props: {
    close: {
      type: Boolean as PropType<boolean>,
      default: false
    }
  },
  setup(props) {
    const systemLoginStore = useSystemLoginStore()
    const { noLoginModalVisible } = storeToRefs(systemLoginStore)
    const { t } = useI18n()
    const router = useRouter()

    watch(noLoginModalVisible, (value) => {
      if (value) {
        Modal.confirm({
          title: 'Confirm',
          icon: createVNode(ExclamationCircleOutlined),
          content: t('system.login.noLogin'),
          onCancel() {
            systemLoginStore.hideWarningNoLogin()
          },
          onOk() {
            // 清除登录信息
            systemLoginStore.clearLoginMessage()
            if (props.close === true) {
              window.close()
              return false
            }
            // 跳转登录页
            const route = router.currentRoute
            router.push({
              path: LOGIN_PATH,
              query: {
                redirect: route.value.path
              }
            })
            systemLoginStore.hideWarningNoLogin()
          }
        })
      }
    })
  },
  render() {
    return ''
  }
})
