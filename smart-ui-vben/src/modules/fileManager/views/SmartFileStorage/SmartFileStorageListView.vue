<template>
  <div class="full-height page-container">
    <SmartTable @register="registerTable" :size="getTableSize">
      <template #table-operation="{ row }">
        <SmartVxeTableAction :actions="getActions(row)" />
      </template>
    </SmartTable>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from '/@/hooks/web/useI18n'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { replace } from 'lodash-es'

import {
  ActionItem,
  SmartTable,
  SmartVxeTableAction,
  useSmartTable,
} from '/@/components/SmartTable'

import {
  getTableColumns,
  getFormSchemas,
  getSearchFormSchemas,
} from './SmartFileStorageListView.config'
import { listApi, deleteApi, getByIdApi, batchSaveUpdateApi } from './SmartFileStorageListView.api'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

const storageConfigPrefix = 'storageConfig'

const getActions = (row: Recordable): ActionItem[] => {
  return [
    {
      label: t('common.button.edit'),
      onClick: () => editByRowModal(row),
    },
    {
      label: t('common.button.delete'),
      onClick: () => deleteByRow(row),
      danger: true,
    },
  ]
}

const [registerTable, { editByRowModal, deleteByRow }] = useSmartTable({
  columns: getTableColumns(t),
  height: 'auto',
  pagerConfig: true,
  useSearchForm: true,
  searchFormConfig: {
    schemas: getSearchFormSchemas(t),
    searchWithSymbol: true,
    colon: true,
    layout: 'inline',
    actionColOptions: {
      span: undefined,
    },
    labelCol: {
      span: 8,
    },
  },
  addEditConfig: {
    modalConfig: {
      // width: '800px',
      height: 800,
    },
    formConfig: {
      colon: true,
      schemas: getFormSchemas(t),
      baseColProps: { span: 24 },
      labelCol: { style: { width: '120px' } },
      wrapperCol: { span: 17 },
    },
  },
  proxyConfig: {
    ajax: {
      query: (params) => listApi(params.ajaxParameter),
      save: ({ body: { insertRecords, updateRecords } }) => {
        const saveDatList = [...insertRecords, ...updateRecords]
        const formatDataList = saveDatList.map((item) => {
          const result: any = {}
          const storageConfig: Recordable = {}
          const configKeyPrefix = storageConfigPrefix + '.' + item.storageType + '.'
          Object.keys(item).forEach((key) => {
            if (!key.startsWith(storageConfigPrefix)) {
              result[key] = item[key]
            } else if (key.startsWith(configKeyPrefix)) {
              storageConfig[replace(key, configKeyPrefix, '')] = item[key]
            }
          })
          result.storageConfig = JSON.stringify(storageConfig)
          return result
        })
        return batchSaveUpdateApi(formatDataList)
      },
      delete: ({ body: { removeRecords } }) => deleteApi(removeRecords),
      getById: async (params) => {
        const result = await getByIdApi(params)
        if (result.storageConfig) {
          const storageConfig = JSON.parse(result.storageConfig)
          const storageType = result.storageType
          const formatData: Recordable = {}
          Object.keys(storageConfig).forEach((item) => {
            formatData[`${storageConfigPrefix}.${storageType}.${item}`] = storageConfig[item]
          })
          return {
            ...result,
            ...formatData,
          }
        }
        return result
      },
    },
  },
  toolbarConfig: {
    custom: true,
    zoom: true,
    refresh: true,
    buttons: [
      {
        code: 'ModalAdd',
      },
      {
        code: 'delete',
      },
    ],
  },
})
</script>
