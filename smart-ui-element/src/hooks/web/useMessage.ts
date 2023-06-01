import type { Result } from '/#/axios'

import { markRaw } from 'vue'
import { Warning } from '@element-plus/icons-vue'

import { ElMessage, ElMessageBox } from 'element-plus'
import { isString } from '@/utils/is'

export interface MessageOptions {
  message: string
}

export interface ModalOptions {
  title: string
  content?: string
  onOk: () => any
}

export interface SuccessOptions extends MessageOptions {
  icon?: string
}

function createErrorModal(options: { title: string; message: string }) {
  return ElMessageBox.confirm(options.message, options.title, {
    type: 'error',
    icon: markRaw(Warning),
  })
}

/**
 * 500错误弹窗
 * @param e
 */
const createError500Modal = (e: Result) => {
  const { exceptionNo } = e
  // const systemExceptionStore = useSystemExceptionStore()
  // systemExceptionStore.handleShowExceptionModal(exceptionNo!)
}

const createConfirm = (options: ModalOptions) => {
  ElMessageBox.confirm(options.content || '', options.title).then(options.onOk)
}

const errorMessage = (e: Result | string | Error) => {
  if (isString(e)) {
    ElMessage.error({
      message: e,
    })
    return
  }
  console.error(e)
  const code = (e as any).code
  switch (code) {
    case 500: {
      createError500Modal(e as Result)
      break
    }
    default:
      ElMessage.error({
        message: e.message,
      })
  }
}

const successMessage = (options: SuccessOptions | string) => {
  if (isString(options)) {
    return ElMessage.success({
      message: options,
    })
  }
  ElMessage.success({
    message: options.message,
  })
}

const warnMessage = (options: MessageOptions | string) => {
  if (isString(options)) {
    return ElMessage.warning({
      message: options,
    })
  }
  return ElMessage.warning({
    message: options.message,
  })
}

export function useMessage() {
  return {
    createMessage: ElMessage,
    createErrorModal,
    createConfirm,
    errorMessage,
    successMessage,
    warnMessage,
  }
}
