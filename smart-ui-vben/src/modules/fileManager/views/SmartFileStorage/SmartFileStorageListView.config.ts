import type { SmartColumn, SmartSearchFormSchema } from '/@/components/SmartTable'
import type { FormSchema } from '/@/components/Form'

import { tableUseYn } from '/@/components/common/TableCommon'

/**
 * 表格列表
 */
export const getTableColumns = (t: Function): SmartColumn[] => {
  return [
    {
      type: 'checkbox',
      width: 60,
      align: 'center',
      fixed: 'left',
    },
    {
      field: 'id',
      visible: false,
      title: '{smart.file.storage.title.id}',
      width: 120,
    },
    {
      field: 'storageCode',
      title: '{smart.file.storage.title.storageCode}',
      width: 160,
      fixed: 'left',
    },
    {
      field: 'storageName',
      title: '{smart.file.storage.title.storageName}',
      width: 160,
      fixed: 'left',
    },
    {
      field: 'storageType',
      sortable: true,
      title: '{smart.file.storage.title.storageType}',
      width: 120,
    },
    {
      field: 'seq',
      sortable: true,
      title: '{common.table.seq}',
      width: 120,
    },
    {
      field: 'remark',
      title: '{common.table.remark}',
      width: 120,
    },
    {
      field: 'defaultStorage',
      title: '{smart.file.storage.title.defaultStorage}',
      width: 140,
    },
    // {
    //   field: 'storageConfig',
    //   title: '{smart.file.storage.title.storageConfig}',
    //   width: 120,
    // },
    {
      field: 'createTime',
      title: '{common.table.createTime}',
      width: 160,
    },
    {
      field: 'createBy',
      title: '{common.table.createUser}',
      width: 120,
    },
    {
      field: 'updateTime',
      title: '{common.table.updateTime}',
      width: 160,
    },
    {
      field: 'updateBy',
      title: '{common.table.updateUser}',
      width: 120,
    },
    {
      ...tableUseYn(t).createColumn(),
      width: 120,
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
      label: t('smart.file.storage.title.id'),
      component: 'Input',
      componentProps: {},
    },
    {
      field: 'storageCode',
      label: t('smart.file.storage.title.storageCode'),
      component: 'Input',
      componentProps: {},
      required: true,
    },
    {
      field: 'storageName',
      label: t('smart.file.storage.title.storageName'),
      component: 'Input',
      componentProps: {},
      required: true,
    },
    // {
    //   field: 'defaultStorage',
    //   label: t('smart.file.storage.title.defaultStorage'),
    //   component: 'Switch',
    //   componentProps: {},
    //   colProps: {
    //     span: 12,
    //   },
    // },
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
      defaultValue: 1,
    },
    {
      field: 'remark',
      label: t('common.table.remark'),
      component: 'Input',
      componentProps: {},
    },
    {
      field: 'storageType',
      label: t('smart.file.storage.title.storageType'),
      component: 'SmartApiSelectDict',
      componentProps: {
        dictCode: 'FILE_STORAGE_TYPE',
      },
      required: true,
    },
    // --------------自定义配置信息
    // 磁盘存储配置
    {
      field: 'storageConfig.DISK.basePath',
      component: 'Input',
      label: t('smart.file.storage.title.basePath'),
      show: ({ model }) => {
        return model.storageType === 'DISK'
      },
      required: ({ model }) => model.storageType === 'DISK',
    },
    // ---------- minio配置
    {
      field: 'storageConfig.MINIO.endpoint',
      component: 'Input',
      label: t('smart.file.storage.title.endpoint'),
      show: ({ model }) => {
        return model.storageType === 'MINIO'
      },
      required: ({ model }) => model.storageType === 'MINIO',
    },
    {
      field: 'storageConfig.MINIO.accessKey',
      component: 'Input',
      label: t('smart.file.storage.title.accessKey'),
      show: ({ model }) => {
        return model.storageType === 'MINIO'
      },
      required: ({ model }) => model.storageType === 'MINIO',
    },
    {
      field: 'storageConfig.MINIO.secretKey',
      component: 'Input',
      label: t('smart.file.storage.title.secretKey'),
      show: ({ model }) => {
        return model.storageType === 'MINIO'
      },
      required: ({ model }) => model.storageType === 'MINIO',
    },
    {
      field: 'storageConfig.MINIO.bucketName',
      component: 'Input',
      label: t('smart.file.storage.title.bucketName'),
      show: ({ model }) => {
        return model.storageType === 'MINIO'
      },
      required: ({ model }) => model.storageType === 'MINIO',
    },
    // ------------- sftp
    {
      field: 'storageConfig.SFTP.host',
      component: 'Input',
      label: t('smart.file.storage.title.host'),
      show: ({ model }) => {
        return model.storageType === 'SFTP'
      },
      required: ({ model }) => model.storageType === 'SFTP',
    },
    {
      field: 'storageConfig.SFTP.port',
      component: 'InputNumber',
      label: t('smart.file.storage.title.port'),
      show: ({ model }) => {
        return model.storageType === 'SFTP'
      },
      required: ({ model }) => model.storageType === 'SFTP',
    },
    {
      field: 'storageConfig.SFTP.basePath',
      component: 'Input',
      label: t('smart.file.storage.title.basePath'),
      show: ({ model }) => {
        return model.storageType === 'SFTP'
      },
      required: ({ model }) => model.storageType === 'SFTP',
    },
    {
      field: 'storageConfig.SFTP.username',
      component: 'Input',
      label: t('smart.file.storage.title.username'),
      show: ({ model }) => {
        return model.storageType === 'SFTP'
      },
      required: ({ model }) => model.storageType === 'SFTP',
    },
    {
      field: 'storageConfig.SFTP.password',
      component: 'Input',
      label: t('smart.file.storage.title.password'),
      show: ({ model }) => {
        return model.storageType === 'SFTP'
      },
      required: ({ model }) => model.storageType === 'SFTP',
    },
    // {
    //   field: 'storageConfig',
    //   label: t('smart.file.storage.title.storageConfig'),
    //   component: 'Input',
    //   componentProps: {},
    // },
  ]
}

export const getSearchFormSchemas = (t: Function): SmartSearchFormSchema[] => {
  return [
    {
      field: 'storageCode',
      label: t('smart.file.storage.title.storageCode'),
      component: 'Input',
      searchSymbol: 'like',
    },
    {
      field: 'storageName',
      label: t('smart.file.storage.title.storageName'),
      component: 'Input',
      searchSymbol: 'like',
    },
    {
      field: 'storageType',
      label: t('smart.file.storage.title.storageType'),
      component: 'SmartApiSelectDict',
      componentProps: {
        dictCode: 'FILE_STORAGE_TYPE',
        style: { width: '140px' },
      },
      searchSymbol: '=',
    },
  ]
}
