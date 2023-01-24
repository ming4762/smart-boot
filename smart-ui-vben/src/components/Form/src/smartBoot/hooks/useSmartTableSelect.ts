import type { SmartTableProps } from '/@/components/SmartTable'
import type { Ref } from 'vue'

import { computed, ref, unref, watch } from 'vue'
import { remove } from 'lodash-es'

import { useSmartTable } from '/@/components/SmartTable'

export const useSmartTableSelect = (
  tablePropsRef: Ref<SmartTableProps>,
  selectTablePropsRef: Ref<SmartTableProps | undefined>,
  showSelect: boolean,
  valueFieldRef: Ref<string>,
  selectValuesRef: Ref<Array<any>>,
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

  const getTableCheckboxConfig = computed(() => {
    return {
      highlight: true,
      checkRowKeys: unref(selectValuesRef),
    }
  })

  watch(selectValuesRef, () => {
    handleSetSelectRows()
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

  const setSelectData = (dataList: any[]) => {
    selectRowsRef.value = dataList
  }

  const handleCheckboxChange = ({ checked, row }) => {
    const selectRows = unref(selectRowsRef)
    const valueField = unref(valueFieldRef)
    if (checked) {
      selectRows.push(row)
    } else {
      remove(selectRows, (item) => {
        return item[valueField] === row[valueField]
      })
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
    const selectRows = unref(selectRowsRef)
    const valueField = unref(valueFieldRef)
    if (checked) {
      selectRows.push(...currentDataList)
    } else {
      remove(selectRows, (item) => {
        return currentDataList.some((current) => current[valueField] === item[valueField])
      })
    }
  }

  return {
    registerTable,
    handleCheckboxChange,
    registerSelectTable,
    selectRowsRef,
    setSelectData,
    getTableCheckboxConfig,
    handleCheckboxAll,
  }
}
