import type { SmartColumn } from '/@/components/SmartTable'
import type { FormSchema } from '/@/components/Form'
import type { SmartSearchFormSchema } from '/@/components/SmartTable'

export const tableColumns: SmartColumn[] = [
  {
    type: 'checkbox',
    width: 60,
    align: 'center',
    fixed: 'left',
  },
  {
    title: '{system.views.function.table.functionName}',
    field: 'functionName',
    width: 220,
    fixed: 'left',
    treeNode: true,
  },
  {
    title: '{system.views.function.table.functionType}',
    field: 'functionType',
    width: 110,
    align: 'center',
    headerAlign: 'left',
    slots: {
      default: 'table-functionType',
    },
  },
  {
    title: '{system.views.function.table.icon}',
    field: 'icon',
    width: 80,
    align: 'center',
    headerAlign: 'left',
    slots: {
      default: 'table-icon',
    },
  },
  {
    title: 'URL',
    field: 'url',
    minWidth: 200,
  },
  {
    title: '{system.views.function.table.permission}',
    field: 'permission',
    width: 160,
  },
  {
    title: '{system.views.function.table.httpMethod}',
    field: 'httpMethod',
    width: 120,
  },
  {
    title: '{common.table.seq}',
    field: 'seq',
    width: 100,
    sortable: true,
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
    width: 200,
    fixed: 'right',
    slots: {
      default: 'table-operation',
    },
  },
]

export const getAddEditForm = (t: Function): FormSchema[] => {
  return [
    {
      field: 'functionId',
      label: '',
      component: 'Input',
      show: false,
    },
    {
      field: 'parentName',
      label: '上级',
      component: 'Input',
      componentProps: {
        disabled: true,
      },
    },
    {
      field: 'parentId',
      label: '',
      component: 'Input',
      show: false,
    },
    {
      field: 'functionName',
      label: t('system.views.function.table.functionName'),
      component: 'Input',
      required: true,
    },
    {
      field: 'functionType',
      label: t('system.views.function.table.functionType'),
      component: 'Input',
      slot: 'addEditForm-functionType',
      required: true,
    },
    {
      field: 'i18nCode',
      label: t('system.views.function.table.i18nCode'),
      component: 'Input',
    },
    {
      field: 'icon',
      label: t('system.views.function.table.icon'),
      component: 'IconPicker',
    },
    {
      field: 'seq',
      required: true,
      label: t('common.table.seq'),
      component: 'InputNumber',
      defaultValue: 1,
    },
    {
      field: 'componentName',
      label: t('system.views.function.table.componentName'),
      component: 'Input',
      dynamicRules: ({ model }) => {
        return [
          {
            required: model.functionType === 'MENU',
            trigger: 'blur',
            message: t('system.views.function.validate.componentName'),
          },
        ]
      },
      show: ({ model }) => {
        return model.functionType !== 'FUNCTION'
      },
    },
    {
      field: 'component',
      label: t('system.views.function.table.component'),
      component: 'Input',
      dynamicRules: ({ model }) => {
        return [
          {
            required: model.functionType === 'MENU',
            trigger: 'blur',
            message: t('system.views.function.validate.component'),
          },
        ]
      },
      show: ({ model }) => {
        return model.functionType !== 'FUNCTION'
      },
    },
    {
      field: 'url',
      label: 'URL',
      component: 'Input',
      dynamicRules: ({ model }) => {
        return [
          {
            required: model.functionType === 'MENU',
            trigger: 'blur',
            message: t('system.views.function.validate.url'),
          },
        ]
      },
      required: true,
    },
    {
      field: 'redirect',
      label: 'Redirect',
      component: 'Input',
      show: ({ model }) => {
        return model.functionType !== 'FUNCTION'
      },
    },
    {
      field: 'httpMethod',
      label: t('system.views.function.table.httpMethod'),
      component: 'Select',
      componentProps: {
        options: ['GET', 'POST', 'PUT', 'DELETE'].map((item) => ({ label: item, value: item })),
      },
      show: ({ model }) => {
        return model.functionType === 'FUNCTION'
      },
    },
    {
      field: 'permission',
      label: t('system.views.function.table.permission'),
      component: 'Input',
      dynamicRules: ({ model }) => {
        return [
          {
            required: model.functionType === 'FUNCTION',
            message: t('system.views.function.validate.permission'),
            trigger: 'blur',
          },
        ]
      },
      show: ({ model }) => {
        return model.functionType === 'FUNCTION'
      },
    },
    {
      field: 'isMenu',
      label: t('system.views.function.table.menuIs'),
      component: 'Switch',
      defaultValue: true,
      show: ({ model }) => {
        return model.functionType !== 'FUNCTION'
      },
    },
    {
      field: 'internalOrExternal',
      label: t('system.views.function.table.internalOrExternal'),
      component: 'Switch',
      defaultValue: false,
      show: ({ model }) => {
        return model.functionType === 'MENU'
      },
    },
    {
      field: 'dataRule',
      label: t('system.views.function.table.dataRule'),
      component: 'Switch',
      defaultValue: false,
      show: ({ model }) => {
        return model.functionType === 'FUNCTION'
      },
    },
    {
      field: 'cached',
      label: t('system.views.function.title.cached'),
      component: 'Switch',
      defaultValue: true,
      show: ({ model }) => {
        return model.functionType === 'MENU'
      },
    },
  ]
}

export const getSearchSchemas = (t: Function): SmartSearchFormSchema[] => {
  return [
    {
      label: t('system.views.userGroup.search.useYnTitle'),
      field: 'useYn',
      component: 'Select',
      defaultValue: 1,
      searchSymbol: '=',
      componentProps: {
        style: {
          width: '100px',
        },
        options: [
          {
            label: t('common.form.use'),
            value: 1,
          },
          {
            label: t('common.form.noUse'),
            value: 0,
          },
        ],
      },
    },
  ]
}
