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
  | 'between'

export interface SmartSearchFormSchema extends FormSchema {
  searchSymbol?: SearchSymbol
  customSymbol?: ({
    schema,
    value,
    model,
  }: {
    schema: SmartSearchFormSchema
    value: any
    model: Recordable
  }) => Recordable
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
