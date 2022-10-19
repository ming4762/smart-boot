import { message } from 'ant-design-vue'

import { useSystemExceptionStore, useSystemLoginStore } from '@/modules/system/store'

/**
 * 错误信息处理
 * @param e
 */
export const errorMessage = (e: any) => {
  console.error(e)
  const code = e.code
  switch (code) {
    case 500:
      error500Handler(e)
      break
    case 401:
      error401Handler()
      break
    default:
      message.error(e.message)
  }
}

export const successMessage = (msg: string) => {
  message.success(msg)
}

/**
 * 500 错误提示
 * @param e
 */
const error500Handler = (e: any) => {
  const { exceptionNo } = e
  const systemExceptionStore = useSystemExceptionStore()
  systemExceptionStore.handleShowExceptionModal(exceptionNo)
}

/**
 * 401未登录错误处理
 */
const error401Handler = () => {
  const store = useSystemLoginStore()
  store.waringNoLogin()
}
