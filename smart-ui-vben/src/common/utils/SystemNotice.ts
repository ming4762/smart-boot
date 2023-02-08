import type { SuccessOptions } from '/@/hooks/web/useMessage'
import { useMessage } from '/@/hooks/web/useMessage'

export const errorMessage = (e: any) => {
  const { errorMessage } = useMessage()
  errorMessage(e)
}

export const successMessage = (options: SuccessOptions) => {
  const { successMessage } = useMessage()
  successMessage(options)
}
