import type { SmartColumn, SmartSearchFormSchema } from '/@/components/SmartTable'
import type { FormSchema } from '/@/components/Form'

/**
 * 表格列表
 */
export const getTableColumns = (): SmartColumn[] => {
  return [
    {
      type: 'checkbox',
      width: 60,
      align: 'center',
      fixed: 'left',
    },
    {
      field: 'seq',
      sortable: true,
      title: '{common.table.seq}',
      width: 100,
    },
    {
      field: 'accessKey',
      title: '{system.views.auth.acccessSecret.title.accessKey}',
      width: 120,
    },
    {
      field: 'secretKey',
      title: '{system.views.auth.acccessSecret.title.secretKey}',
      width: 120,
    },
    {
      field: 'expireDate',
      title: '{system.views.auth.acccessSecret.title.expireDate}',
      width: 165,
    },
    {
      field: 'accessIp',
      title: '{system.views.auth.acccessSecret.title.accessIp}',
      width: 120,
    },
    {
      field: 'remark',
      title: '{common.table.remark}',
      width: 200,
    },
    {
      field: 'useYn',
      component: 'booleanTag',
      title: '{common.table.useYn}',
      width: 120,
    },
    {
      field: 'createBy',
      title: '{common.table.createUser}',
      width: 120,
    },
    {
      field: 'createTime',
      title: '{common.table.createTime}',
      width: 165,
    },
    {
      field: 'updateBy',
      title: '{common.table.updateUser}',
      width: 120,
    },
    {
      field: 'updateTime',
      title: '{common.table.updateTime}',
      width: 165,
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

/**
 * 添加修改表单
 */
export const getFormSchemas = (t: Function): FormSchema[] => {
  return [
    {
      field: 'id',
      show: false,
      label: '',
      component: 'Input',
      componentProps: {},
    },
    {
      field: 'accessKey',
      label: t('system.views.auth.acccessSecret.title.accessKey'),
      component: 'Input',
      componentProps: {},
      dynamicDisabled: true,
    },
    {
      field: 'secretKey',
      label: t('system.views.auth.acccessSecret.title.secretKey'),
      component: 'Input',
      componentProps: {},
      dynamicDisabled: true,
    },
    {
      field: 'expireDate',
      label: t('system.views.auth.acccessSecret.title.expireDate'),
      component: 'DatePicker',
      componentProps: {
        showTime: true,
        style: { width: '100%' },
      },
    },
    {
      field: 'accessIp',
      label: t('system.views.auth.acccessSecret.title.accessIp'),
      component: 'InputTextArea',
      componentProps: {
        placeholder: t('system.views.auth.acccessSecret.validate.accessIp'),
      },
    },
    {
      field: 'remark',
      label: t('common.table.remark'),
      component: 'InputTextArea',
      componentProps: {},
    },
    {
      field: 'useYn',
      label: t('common.table.useYn'),
      component: 'Switch',
      componentProps: {},
      defaultValue: true,
    },
    {
      field: 'seq',
      label: t('common.table.seq'),
      component: 'InputNumber',
      componentProps: {},
      required: true,
    },
  ]
}

export const getSearchFormSchemas = (t: Function): SmartSearchFormSchema[] => {
  return [
    {
      field: 'accessKey',
      label: t('system.views.auth.acccessSecret.title.accessKey'),
      component: 'Input',
      searchSymbol: 'like',
    },
    {
      field: 'secretKey',
      label: t('system.views.auth.acccessSecret.title.secretKey'),
      component: 'Input',
      searchSymbol: 'like',
    },
    {
      field: 'useYn',
      label: t('common.table.useYn'),
      component: 'Select',
      searchSymbol: '=',
      defaultValue: 1,
      componentProps: {
        style: {
          width: '120px',
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
