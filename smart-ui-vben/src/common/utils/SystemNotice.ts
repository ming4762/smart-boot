import type {
  SuccessOptions,
  ModalOptionsEx,
  MessageOptions,
  ModalOptionsPartial,
} from '/@/hooks/web/useMessage'
import { useMessage } from '/@/hooks/web/useMessage'

export const errorMessage = (e: any) => {
  const { errorMessage } = useMessage()
  errorMessage(e)
}

export const successMessage = (options: SuccessOptions) => {
  const { successMessage } = useMessage()
  successMessage(options)
}

export const createConfirm = (options: ModalOptionsEx) => {
  const { createConfirm } = useMessage()
  return createConfirm(options)
}

export const warnMessage = (options: MessageOptions) => {
  const { warnMessage } = useMessage()
  return warnMessage(options)
}

export const createWaringModal = (options: ModalOptionsPartial) => {
  const { createWarningModal } = useMessage()
  createWarningModal(options)
}
