import type { SmartTableProps } from '/@/components/SmartTable'
import type { ComputedRef, Ref } from 'vue'

import { computed, ref, unref, watch } from 'vue'
import { remove } from 'lodash-es'

import { useSmartTable } from '/@/components/SmartTable'

export const useSmartTableSelect = (
  tablePropsRef: Ref<SmartTableProps>,
  selectTablePropsRef: Ref<SmartTableProps | undefined>,
  showSelect: boolean,
  valueFieldRef: Ref<string>,
  selectValuesRef: Ref<Array<any>>,
  hasTableSlot: ComputedRef<boolean>,
) => {
  const getTableProps = computed<SmartTableProps>(() => {
    const tableProps = unref(tablePropsRef)
    return {
      ...tableProps,
      rowConfig: {
        keyField: unref(valueFieldRef),
      },
    }
  })
  /**
   * 是否有搜索表单
   */
  const getHasSearchForm = computed(() => {
    return unref(tablePropsRef).useSearchForm
  })

  const getTableCheckboxConfig = computed(() => {
    return {
      highlight: true,
      checkRowKeys: unref(selectValuesRef),
    }
  })

  watch(selectValuesRef, () => {
    if (!unref(hasTableSlot)) {
      handleSetSelectRows()
    }
  })

  const handleSetSelectRows = async () => {
    const selectRows = getSelectRows()
    selectRowsRef.value = selectRows
    await getTableInstance().setAllCheckboxRow(false)
    await setCheckboxRow(selectRows, true)
  }

  /**
   * 获取选中的数据
   */
  const getSelectRows = () => {
    const selectValues = unref(selectValuesRef)
    if (!selectValues || selectValues.length === 0) {
      return []
    }
    const tableData = getData()
    if (!tableData || tableData.length === 0) {
      return []
    }
    const valueField = unref(valueFieldRef)
    return tableData.filter((item) => {
      return selectValues.includes(item[valueField])
    })
  }

  const [registerTable, { setCheckboxRow, getData, getTableInstance }] = useSmartTable(
    unref(getTableProps),
  )
  const [registerSelectTable, { setPagination }] = useSmartTable(unref(selectTablePropsRef) || {})

  const selectRowsRef = ref<any[]>([])

  /**
   * 设置选中的数据
   * @param dataList
   */
  const setSelectData = (dataList: any[]) => {
    selectRowsRef.value = dataList
  }

  /**
   * 添加选中的数据
   * @param dataList
   */
  const addSelectData = (dataList: any[]) => {
    const selectRows = unref(selectRowsRef)
    selectRows.push(...dataList)
  }

  /**
   * 移除数据
   * @param dataList
   */
  const removeSelectData = (dataList: any[]) => {
    const selectRows = unref(selectRowsRef)
    const valueField = unref(valueFieldRef)
    remove(selectRows, (item) => {
      return dataList.some((current) => current[valueField] === item[valueField])
    })
  }
  /**
   * 获取选中的数据
   */
  const getSelectData = () => unref(selectRowsRef)

  const handleCheckboxChange = ({ checked, row }) => {
    const selectRows = unref(selectRowsRef)
    if (checked) {
      addSelectData([row])
    } else {
      removeSelectData([row])
    }
    if (showSelect) {
      setPagination({
        total: selectRows.length,
      })
    }
  }

  const handleCheckboxAll = ({ checked }) => {
    const currentDataList = getData()
    if (!currentDataList || currentDataList.length === 0) {
      return
    }
    if (checked) {
      addSelectData([currentDataList])
    } else {
      removeSelectData(currentDataList)
    }
  }

  return {
    registerTable,
    handleCheckboxChange,
    registerSelectTable,
    selectRowsRef,
    setSelectData,
    addSelectData,
    getSelectData,
    removeSelectData,
    getTableCheckboxConfig,
    handleCheckboxAll,
    getData,
    getHasSearchForm,
  }
}
