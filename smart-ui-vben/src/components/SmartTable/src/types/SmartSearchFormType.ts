import type { FormProps, FormSchema } from '/@/components/Form'

export type SearchSymbol =
  | '='
  | 'like'
  | '>'
  | '>='
  | '<>'
  | '<'
  | '<='
  | 'in'
  | 'notLike'
  | 'likeLeft'
  | 'likeRight'
  | 'notIn'
  | 'groupBy'

export interface SmartSearchFormSchema extends FormSchema {
  searchSymbol?: SearchSymbol
}

export interface SmartSearchFormProps extends FormProps {
  schemas?: SmartSearchFormSchema[]
}
