import type { Result } from '@/types/axios'

import { markRaw } from 'vue'
import { Warning } from '@element-plus/icons-vue'

import { ElMessage, ElMessageBox } from 'element-plus'

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

export function useMessage() {
  return {
    createMessage: ElMessage,
    createErrorModal,
  }
}
