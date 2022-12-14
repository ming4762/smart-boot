import { useMessage } from '/@/hooks/web/useMessage'

export const errorMessage = (e: any) => {
  const { errorMessage } = useMessage()
  errorMessage(e)
}

export const successMessage = (options: any) => {
  const { successMessage } = useMessage()
  successMessage(options)
}
