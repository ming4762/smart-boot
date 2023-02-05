import type { ComputedRef, Slot, Slots } from 'vue'
import type { SmartSearchFormProps } from '/@/components/SmartTable'
import type { FormProps } from '/@/components/Form'
import type { FetchParams, SmartTableProps } from '../types/SmartTableType'
import type { SmartSearchFormParameter } from '../types/SmartSearchFormType'

import { computed, unref } from 'vue'
import { useForm } from '/@/components/Form'
import { isBoolean } from '/@/utils/is'

export const useTableSearchForm = (
  propsRef: ComputedRef<SmartTableProps>,
  slots: Slots,
  fetch: (opt?: FetchParams | undefined) => Promise<void>,
  getLoading: ComputedRef<boolean | undefined>,
) => {
  const [registerSearchForm, searchFormAction] = useForm()

  /**
   * searchForm props计算属性
   */
  const getSearchFormProps = computed((): Partial<FormProps> => {
    const { searchFormConfig } = unref(propsRef)
    const { submitButtonOptions } = searchFormConfig || {}
    return {
      ...searchFormConfig,
      submitButtonOptions: { loading: unref(getLoading), ...submitButtonOptions },
    }
  })

  /**
   * 搜索条件触发
   */
  const handleSearchInfoChange = () => {
    fetch({
      page: {
        currentPage: 1,
      },
    })
  }

  function replaceFormSlotKey(key: string) {
    if (!key) return ''
    return key?.replace?.(/searchForm\-/, '') ?? ''
  }

  /**
   * form slot key计算属性
   */
  const getSearchFormSlot: ComputedRef<Slots> = computed(() => {
    const result: { [name: string]: Slot | undefined } = {}
    Object.keys(slots)
      .map((item) => (item.startsWith('searchForm-') ? item : null))
      .filter((item) => !!item)
      .forEach((item) => {
        const formKey = replaceFormSlotKey(item as string)
        result[formKey] = slots[item as string]
      })
    return result
  })

  const getSearchFormColumnSlot = computed<Slots>(() => {
    const { searchFormConfig } = unref(propsRef)
    const slotNames =
      (searchFormConfig?.schemas
        ?.map((item) => {
          const { slot } = item
          return slot
        })
        .filter((item) => !!item) as string[]) || []
    const result: { [name: string]: Slot | undefined } = {}
    slotNames.forEach((item) => {
      result[item] = slots[item]
    })
    return result
  })

  /**
   * 获取搜索扁担参数
   */
  const getSearchFormParameter = (): SmartSearchFormParameter => {
    const searchForm = searchFormAction.getFieldsValue()
    const searchWithSymbol = unref(propsRef).searchFormConfig?.searchWithSymbol
    const result: SmartSearchFormParameter = {
      searchWithSymbol: isBoolean(searchWithSymbol) && searchWithSymbol,
    }
    if (result.searchWithSymbol) {
      // 处理搜索符号
      const { symbolForm, noSymbolForm } = dealSearchSymbol(searchForm)
      result.searchSymbolForm = symbolForm
      result.noSymbolForm = noSymbolForm
    }
    result.searchForm = searchForm
    return result
  }

  /**
   * 获取搜索符号
   */
  const getSearchFormSymbolRef = computed<Recordable | boolean>(() => {
    const { searchFormConfig, useSearchForm } = unref(propsRef)
    const searchWithSymbol = searchFormConfig?.searchWithSymbol
    if (!useSearchForm || !searchWithSymbol) {
      return false
    }
    const { schemas } = searchFormConfig as Partial<SmartSearchFormProps>
    const result: Recordable = {}
    schemas?.forEach(({ field, searchSymbol }) => {
      if (searchSymbol) {
        result[field] = searchSymbol
      }
    })
    return result
  })

  const dealSearchSymbol = (info: Recordable) => {
    const symbolForm: Recordable = {}
    const noSymbolForm: Recordable = {}
    const getSearchFormSymbol = unref(getSearchFormSymbolRef)
    if (isBoolean(getSearchFormSymbol)) {
      return info
    }
    Object.keys(info).forEach((key) => {
      const value = info[key]
      const symbol = getSearchFormSymbol[key]
      if (symbol) {
        symbolForm[`${key}@${symbol}`] = value
      } else {
        noSymbolForm[key] = value
      }
    })
    return {
      symbolForm,
      noSymbolForm,
    }
  }

  return {
    getSearchFormProps,
    handleSearchInfoChange,
    getSearchFormSlot,
    getSearchFormColumnSlot,
    registerSearchForm,
    searchFormAction: {
      ...searchFormAction,
      getSearchFormParameter,
    },
  }
}
