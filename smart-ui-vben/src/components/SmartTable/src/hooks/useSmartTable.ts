import { onUnmounted, ref, unref, watch } from 'vue'
import type { WatchStopHandle } from 'vue'

import type { FetchParams, SmartTableProps, TableActionType } from '../types/SmartTableType'
import type { FormActionType } from '/@/components/Form'
import { isProdMode } from '/@/utils/env'
import type { DynamicProps } from '/#/utils'
import { getDynamicProps } from '/@/utils'
import { error } from '/@/utils/log'
import { VxeGridPropTypes } from 'vxe-table'
import {NamePath} from 'ant-design-vue/lib/form/interface';

type Props = Partial<DynamicProps<SmartTableProps>>
type UseTableMethod = TableActionType & {
  getSearchForm: () => FormActionType
}

export const useSmartTable = (
  tableProps?: Props,
): [(instance: TableActionType, formInstance: UseTableMethod) => void, UseTableMethod] => {
  // 表格是否加载
  const loadedRef = ref<Nullable<boolean>>(false)
  const tableRef = ref<Nullable<TableActionType>>(null)
  const searchFormRef = ref<Nullable<UseTableMethod>>(null)

  let stopWatch: WatchStopHandle

  const register = (instance: TableActionType, searchFormInstance: UseTableMethod) => {
    isProdMode() &&
      onUnmounted(() => {
        tableRef.value = null
        searchFormRef.value = null
      })
    if (unref(loadedRef) && isProdMode() && instance === unref(tableRef)) return

    tableRef.value = instance
    searchFormRef.value = searchFormInstance
    loadedRef.value = true
    // 设置函数传递的props
    tableProps && instance.setProps(getDynamicProps(tableProps))

    /**
     * 监控table props变化
     */
    stopWatch?.()
    stopWatch = watch(
      () => tableProps,
      () => {
        tableProps && instance.setProps(getDynamicProps(tableProps))
      },
      {
        // todo:是否重复执行
        immediate: true,
        deep: true,
      },
    )
  }

  const getTableInstance = (): TableActionType => {
    const table = unref(tableRef)
    if (!table) {
      error(
        'The table instance has not been obtained yet, please make sure the table is presented when performing the table operation!',
      )
    }
    return table as TableActionType
  }

  const methods: UseTableMethod = {
    setProps: (props: Partial<SmartTableProps>) => {
      getTableInstance().setProps(props)
    },
    getSearchForm: () => {
      return unref(searchFormRef) as unknown as FormActionType
    },
    reload: async (opt?: FetchParams) => {
      return await getTableInstance().reload(opt)
    },
    setLoading: (loading: boolean) => {
      getTableInstance().setLoading(loading)
    },
    setPagination: (info: Partial<VxeGridPropTypes.PagerConfig>) => {
      getTableInstance().setPagination(info)
    },
    setShowPagination: async (show: boolean) => {
      await getTableInstance().setShowPagination(show)
    },
    getPagination: () => getTableInstance().getPagination(),
    getShowPagination: () => {
      return getTableInstance().getShowPagination()
    },
    commitVxeProxy: (code, ...args) => {
      return getTableInstance().commitVxeProxy(code, args)
    },
    deleteByCheckbox: () => {
      getTableInstance().deleteByCheckbox()
    },
    getCheckboxRecords: (isFull: boolean) => {
      return getTableInstance().getCheckboxRecords(isFull)
    },
    getRadioRecord: (isFull: boolean) => {
      return getTableInstance().getRadioRecord(isFull)
    },
    openAddEditModal: (props?: boolean, data?: any, openOnSet?: boolean) => {
      return getTableInstance().openAddEditModal(props, data, openOnSet)
    },
    showAddModal: (formData?: Recordable) => {
      getTableInstance().showAddModal(formData)
    },
    editByCheckbox: () => {
      return getTableInstance().editByCheckbox()
    },
    getAddEditFieldsValue: () => {
      return getTableInstance().getAddEditFieldsValue()
    },
    resetAddEditFields: () => {
      return getTableInstance().resetAddEditFields()
    },
    setAddEditFieldsValue: (data: any) => {
      return getTableInstance().setAddEditFieldsValue(data)
    },
    editByRow: (data, formData) => {
      return getTableInstance().editByRow(data, formData)
    },
    deleteByRow: (data) => {
      return getTableInstance().deleteByRow(data)
    },
    setRadioRow: (row) => {
      return getTableInstance().setRadioRow(row)
    },
    setCheckboxRow(rows: any, checked: boolean): Promise<any> {
      return getTableInstance().setCheckboxRow(rows, checked)
    },
    validateAddEdit: (nameList?: NamePath[]) => {
      return getTableInstance().validateAddEdit(nameList)
    },
    validateAddEditFields: (nameList?: NamePath[]) => {
      return getTableInstance().validateAddEdit(nameList)
    },
  }
  return [register, methods]
}
