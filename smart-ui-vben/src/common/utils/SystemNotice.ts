import { useMessage } from '/@/hooks/web/useMessage'

export const errorMessage = (e: any) => {
  const { errorMessage } = useMessage()
  errorMessage(e)
}
