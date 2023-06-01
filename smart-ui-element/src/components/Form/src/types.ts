import { FormSchema, FormSetPropsType } from '@/types/form'

export interface PlaceholderMoel {
  placeholder?: string
  startPlaceholder?: string
  endPlaceholder?: string
  rangeSeparator?: string
}

export type FormProps = {
  schema?: FormSchema[]
  isCol?: boolean
  model?: Recordable
  autoSetPlaceholder?: boolean
  isCustom?: boolean
  labelWidth?: string | number
} & Recordable

export interface FormActionType {
  setProps: (props: Recordable) => void
  setValues: (data: Recordable) => void
  getFormData: <T = Recordable | undefined>() => Promise<T>
  setSchema: (schemaProps: FormSetPropsType[]) => void
  addSchema: (formSchema: FormSchema, index?: number) => void
  delSchema: (field: string) => void
}
