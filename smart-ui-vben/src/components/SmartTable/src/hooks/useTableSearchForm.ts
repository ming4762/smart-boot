import type { ComputedRef, Slot, Slots } from 'vue'
import { computed, unref } from 'vue'

import type { FormProps } from '/@/components/Form'

import type { FetchParams, SmartTableProps } from '../types/SmartTableType'

export const useTableSearchForm = (
  propsRef: ComputedRef<SmartTableProps>,
  slots: Slots,
  fetch: (opt?: FetchParams | undefined) => Promise<void>,
) => {
  /**
   * searchForm props计算属性
   */
  const getSearchFormProps = computed((): Partial<FormProps> => {
    const { searchFormConfig } = unref(propsRef)

    return {
      ...searchFormConfig,
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

  return {
    getSearchFormProps,
    handleSearchInfoChange,
    getSearchFormSlot,
    getSearchFormColumnSlot,
  }
}
