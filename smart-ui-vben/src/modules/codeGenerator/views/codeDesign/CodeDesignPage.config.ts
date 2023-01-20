import type { FormSchema } from '/@/components/Form'

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

const yesNoList = [
  {
    label: 'Yes',
    value: true,
  },
  {
    label: 'No',
    value: false,
  },
]

export const formSchemas = (t: Function): FormSchema[] => {
  return [
    {
      label: t('generator.views.code.table.connectionName'),
      field: 'connectionId',
      slot: 'addEditForm-connectionId',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.code.table.tableName'),
      field: 'tableName',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.code.table.configName'),
      field: 'configName',
      component: 'Input',
      required: true,
    },
    {
      label: t('generator.views.code.table.type'),
      field: 'type',
      component: 'Select',
      defaultValue: 10,
      componentProps: {
        options: tableTypeList.map((item) => ({ label: t(item.label), value: item.value })),
      },
    },
    // ------------ 第二行 ---------------------
    {
      label: t('generator.views.code.title.showCheckBox'),
      field: 'showCheckbox',
      component: 'RadioGroup',
      defaultValue: true,
      componentProps: {
        options: yesNoList,
      },
    },
    {
      label: t('generator.views.code.title.isPage'),
      field: 'page',
      component: 'RadioGroup',
      defaultValue: true,
      componentProps: {
        options: yesNoList,
      },
    },
    {
      label: t('generator.views.code.title.invented'),
      field: 'invented',
      component: 'RadioGroup',
      defaultValue: false,
      componentProps: {
        options: yesNoList,
      },
    },
    {
      label: t('generator.views.code.title.columnSort'),
      field: 'columnSort',
      component: 'RadioGroup',
      defaultValue: false,
      componentProps: {
        options: yesNoList,
      },
    },
    // ------------ 第三行 ---------------------
    {
      label: t('generator.views.code.title.leftButton'),
      field: 'leftButtonList',
      component: 'Select',
      defaultValue: ['SEARCH', 'RESET'],
      componentProps: {
        mode: 'multiple',
        options: [...commonButtonList, ...rowButtonList].map((item) => ({
          label: item.value,
          value: item.key,
        })),
      },
    },
    {
      label: t('generator.views.code.title.rightButton'),
      field: 'rightButtonList',
      component: 'Select',
      defaultValue: ['ADD', 'EDIT', 'DELETE'],
      componentProps: {
        mode: 'multiple',
        options: [...commonButtonList, ...rowButtonList].map((item) => ({
          label: item.value,
          value: item.key,
        })),
      },
    },
    {
      label: t('generator.views.code.title.rowButtonType.title'),
      field: 'rowButtonType',
      component: 'Select',
      defaultValue: 'NONE',
      componentProps: {
        options: rowButtonTypeList(t),
      },
    },
    {
      label: t('generator.views.code.title.rowButtonList'),
      field: 'rowButtonList',
      component: 'Select',
      componentProps: {
        mode: 'multiple',
        options: rowButtonList.map((item) => ({
          label: item.value,
          value: item.key,
        })),
      },
    },
    // ------------ 第四行 ---------------------
    {
      label: t('generator.views.code.title.formColNum'),
      field: 'formColNum',
      component: 'Select',
      defaultValue: 1,
      componentProps: {
        options: columnNumList(t),
      },
    },
    {
      label: t('generator.views.code.title.searchColNum'),
      field: 'searchColNum',
      component: 'Select',
      componentProps: {
        options: columnNumList(t),
      },
    },
    {
      label: t('common.table.remark'),
      field: 'remark',
      component: 'Input',
    },
    {
      label: t('generator.views.code.title.i18nPrefix'),
      field: 'i18nPrefix',
      component: 'Input',
      required: true,
    },
    // ------------ 第五行 ---------------------
    {
      label: t('generator.views.code.title.relateTable'),
      field: 'addendumTableList',
      component: 'Input',
      defaultValue: [],
      slot: 'addEditForm-RelateTable',
    },
    {
      label: '',
      field: 'id',
      component: 'Input',
      slot: 'addEditForm-syncTable',
    },
  ]
}

/**
 * 左侧按钮列表
 */
const commonButtonList = [
  {
    key: 'SEARCH',
    value: '搜索',
  },
  {
    key: 'RESET',
    value: '重置',
  },
  {
    key: 'ADD',
    value: '添加',
  },
  {
    key: 'EXCEL_IMPORT',
    value: 'excel导入',
  },
  {
    key: 'DOWNLOAD',
    value: '导出',
  },
  {
    key: 'DOWNLOAD_ALL',
    value: '全部导出',
  },
  {
    key: 'COLUMN_SETTING',
    value: '列设置',
  },
  {
    key: 'SET_VALID',
    value: '设置有效',
  },
  {
    key: 'SET_INVALID',
    value: '设置无效',
  },
]

/**
 * 行按钮
 */
const rowButtonList = [
  {
    key: 'EDIT',
    value: '修改',
  },
  {
    key: 'DELETE',
    value: '删除',
  },
  {
    key: 'save',
    value: '保存',
  },
]

const columnNumList = (t: Function) => {
  return [
    {
      value: 1,
      label: t('generator.views.code.title.colNum.one'),
    },
    {
      value: 2,
      label: t('generator.views.code.title.colNum.two'),
    },
    {
      value: 3,
      label: t('generator.views.code.title.colNum.three'),
    },
    {
      value: 4,
      label: t('generator.views.code.title.colNum.four'),
    },
  ]
}

const rowButtonTypeList = (t: Function) => [
  {
    label: t('generator.views.code.title.rowButtonType.none'),
    value: 'NONE',
  },
  {
    label: t('generator.views.code.title.rowButtonType.single'),
    value: 'SINGLE',
  },
  {
    label: t('generator.views.code.title.rowButtonType.more'),
    value: 'MORE',
  },
  {
    label: t('generator.views.code.title.rowButtonType.text'),
    value: 'TEXT',
  },
]
