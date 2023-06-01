import type { ComputedRef, VNode } from 'vue'
import { computed, unref } from 'vue'
import type { SmartColumn, SmartTableProps } from '@/components/SmartTable'
import type { VxeColumnPropTypes } from 'vxe-table'
import { isBoolean, isFunction } from '@/utils/is'

const getComponentProps = (
  params: VxeColumnPropTypes.DefaultSlotParams,
  column: SmartColumn,
): Recordable => {
  const componentProps = column.componentProps
  let props
  if (isFunction(componentProps)) {
    props = componentProps(params)
  } else {
    props = componentProps
  }
  return props
}

const componentMap: {
  [index: string]: (
    column: SmartColumn,
    t: Function,
  ) => (params: VxeColumnPropTypes.DefaultSlotParams) => VNode | string
} = {
  switch: (column) => {
    return (params: VxeColumnPropTypes.DefaultSlotParams) => {
      const props = {
        checked: params.row[params.column.field],
        ...getComponentProps(params, column),
      }
      return <a-switch {...props} />
    }
  },
  tag: (column) => {
    return (params: VxeColumnPropTypes.DefaultSlotParams) => {
      const props = getComponentProps(params, column)
      const defaultValue = props?.default || params.row[params.column.field]
      return <a-tag {...props}>{defaultValue}</a-tag>
    }
  },
  button: (column) => {
    return (params: VxeColumnPropTypes.DefaultSlotParams) => {
      const props: Recordable = {
        size: params.$table.props.size,
        ...getComponentProps(params, column),
      }
      const defaultValue = props?.default || params.row[params.column.field]
      return <a-button {...props}>{defaultValue}</a-button>
    }
  },
  booleanTag: (column, t) => {
    return (params) => {
      const componentProps = getComponentProps(params, column)
      const defaultValue = componentProps?.default || params.row[params.column.field]
      if (!isBoolean(defaultValue)) {
        return ''
      }
      const props: Recordable = {
        ...componentProps,
        color: defaultValue ? '#108ee9' : '#f50',
      }
      return <a-tag {...props}>{defaultValue ? t('common.form.yes') : t('common.form.no')}</a-tag>
    }
  },
}

export const useSmartTableColumn = (tableProps: ComputedRef<SmartTableProps>, t: Function) => {
  const getTableColumns = computed<Array<SmartColumn>>((): SmartColumn[] => {
    const propsColumns = unref(tableProps).columns || []
    return propsColumns.map((column) => {
      const { component, slots } = column
      if (!component || slots) {
        return column
      }
      const defaultSlot = componentMap[component]
      if (!defaultSlot) {
        return column
      }
      return {
        ...column,
        slots: {
          default: defaultSlot(column, t),
        },
      }
    })
  })

  return {
    getTableColumns,
  }
}
