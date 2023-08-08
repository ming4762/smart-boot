import type { SmartColumn } from '/@/components/SmartTable'

export interface ColumnOption {
  title: string
  field: string
}

const operateTypeOptions = [
  {
    operateType: 'CREATE',
    color: '#87d068',
  },
  {
    operateType: 'UPDATE',
    color: '#2db7f5',
  },
  {
    operateType: 'DELETE',
    color: '#f50',
  },
]

export const getTableColumns = (t: Function, columnOptions: ColumnOption[]): SmartColumn[] => {
  return [
    {
      field: 'seq',
      title: t('common.table.seq'),
      align: 'center',
      width: 60,
    },
    {
      title: '变更时间',
      field: 'createTime',
      width: 170,
    },
    {
      title: '变更人',
      field: 'createBy',
      width: 160,
    },
    {
      title: '变更类型',
      field: 'operateType',
      width: 100,
      slots: {
        default: ({ row }) => {
          const options = operateTypeOptions.filter((item) => item.operateType === row.operateType)
          if (options.length === 0) {
            return ''
          }
          return <a-tag color={options[0].color}>{row.operateType}</a-tag>
        },
      },
    },
    {
      title: '变更字段',
      field: 'changeField',
      width: 160,
      formatter: ({ row }) => {
        const changeField = row.changeField
        if (columnOptions.length === 0) {
          return changeField
        }
        const options = columnOptions.filter((item) => item.field === changeField)
        if (options.length === 0) {
          return changeField
        }
        return options[0].title
      },
    },
    {
      title: '变更前',
      field: 'beforeValue',
      minWidth: 160,
    },
    {
      title: '变更后',
      field: 'afterValue',
      minWidth: 160,
    },
  ]
}
