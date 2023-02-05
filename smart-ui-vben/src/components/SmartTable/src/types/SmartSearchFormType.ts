import type { FormActionType, FormProps, FormSchema } from '/@/components/Form'

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

  searchWithSymbol?: boolean
}

export interface SmartSearchFormParameter {
  searchSymbolForm?: Recordable
  noSymbolForm?: Recordable
  searchForm?: Recordable
  searchWithSymbol: boolean
}

export interface SmartSearchFormActionType extends FormActionType {
  getSearchFormParameter: () => SmartSearchFormParameter
}
