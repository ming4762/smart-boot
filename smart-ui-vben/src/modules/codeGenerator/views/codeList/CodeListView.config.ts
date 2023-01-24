import type { SmartColumn, SmartSearchFormSchema } from '/@/components/SmartTable'

const tableTypeList = [
  {
    label: 'generator.views.code.title.tableType.single',
    value: '10',
  },
  {
    label: 'generator.views.code.title.tableType.main',
    value: '20',
  },
  {
    label: 'generator.views.code.title.tableType.addendum',
    value: '30',
  },
]

export const tableColumns = (t: Function): SmartColumn[] => {
  return [
    {
      type: 'checkbox',
      width: 60,
      fixed: 'left',
    },
    {
      title: '{generator.views.code.table.connectionName}',
      field: 'connectionName',
      width: 160,
      fixed: 'left',
    },
    {
      title: '{generator.views.code.table.configName}',
      field: 'configName',
      width: 120,
      fixed: 'left',
    },
    {
      title: '{generator.views.code.table.tableName}',
      field: 'tableName',
      width: 120,
      fixed: 'left',
    },
    {
      title: '{generator.views.code.table.type}',
      field: 'type',
      width: 120,
      formatter: ({ cellValue }: any) => {
        if (cellValue && tableTypeList[cellValue]) {
          return t(tableTypeList[cellValue])
        }
        return ''
      },
    },
    {
      title: '{generator.views.code.table.remarks}',
      field: 'remarks',
      minWidth: 120,
    },
    {
      title: '{common.table.remark}',
      field: 'remark',
      minWidth: 200,
    },
    {
      title: '{common.table.createTime}',
      field: 'createTime',
      width: 165,
      sortable: true,
    },
    {
      title: '{common.table.createUser}',
      field: 'createUserId',
      width: 120,
      formatter: ({ row }: any) => {
        if (row.createUser) {
          return row.createUser.fullName
        }
        return ''
      },
    },
    {
      title: '{common.table.updateTime}',
      field: 'updateTime',
      width: 165,
      sortable: true,
    },
    {
      title: '{common.table.updateUser}',
      field: 'updateUserId',
      width: 120,
      formatter: ({ row }: any) => {
        if (row.updateUser) {
          return row.updateUser.fullName
        }
        return ''
      },
    },
    {
      title: '{common.table.operation}',
      field: 'operation',
      width: 120,
      fixed: 'right',
      slots: {
        default: 'table-operation',
      },
    },
  ]
}

export const searchFormColumns = (t: Function): SmartSearchFormSchema[] => {
  return [
    {
      field: 'tableName',
      label: '',
      component: 'Input',
      componentProps: {
        placeholder: t('generator.views.code.table.tableName'),
      },
    },
    {
      field: 'type',
      label: '',
      component: 'Select',
      componentProps: {
        placeholder: t('generator.views.code.table.type'),
        options: tableTypeList.map((item) => {
          return {
            ...item,
            label: t(item.label),
          }
        }),
      },
    },
  ]
}
