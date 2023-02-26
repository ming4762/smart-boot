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

import {
  ActionItem,
  SmartTable,
  SmartVxeTableAction,
  useSmartTable,
} from '/@/components/SmartTable'

import { getTableColumns, getFormSchemas, getSearchFormSchemas } from './SysTenantListView.config'
import { listApi, deleteApi, getByIdApi, batchSaveUpdateApi } from './SysTenantListView.api'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

const getActions = (row: Recordable): ActionItem[] => {
  return [
    {
      label: t('common.button.edit'),
      onClick: () => editByRowModal(row),
    },
  ]
}

const [registerTable, { editByRowModal }] = useSmartTable({
  columns: getTableColumns(),
  height: 'auto',
  pagerConfig: true,
  useSearchForm: true,
  border: true,
  highlightHoverRow: true,
  stripe: true,
  rowConfig: {
    keyField: 'id',
  },
  columnConfig: {
    resizable: true,
  },
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
      width: 800,
    },
    formConfig: {
      colon: true,
      schemas: getFormSchemas(t),
      baseColProps: { span: 12 },
      labelCol: { span: 6 },
      wrapperCol: { span: 17 },
    },
  },
  proxyConfig: {
    ajax: {
      query: (params) => listApi(params.ajaxParameter),
      save: ({ body: { insertRecords, updateRecords } }) =>
        batchSaveUpdateApi([...insertRecords, ...updateRecords]),
      delete: ({ body: { removeRecords } }) => deleteApi(removeRecords),
      getById: (params) => getByIdApi(params.id),
    },
  },
  toolbarConfig: {
    zoom: true,
    refresh: true,
    custom: true,
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
