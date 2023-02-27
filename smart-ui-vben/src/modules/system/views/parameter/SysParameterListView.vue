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

import {
  getTableColumns,
  getFormSchemas,
  getSearchFormSchemas,
  Permissions,
} from './SysParameterListView.config'
import { listApi, deleteApi, getByIdApi, batchSaveUpdateApi } from './SysParameterListView.api'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

const getActions = (row): ActionItem[] => {
  return [
    {
      label: t('common.button.edit'),
      auth: Permissions.update,
      onClick: () => editByRowModal(row),
    },
    {
      label: t('common.button.delete'),
      auth: Permissions.delete,
      danger: true,
      onClick: () => deleteByRow(row),
    },
  ]
}

const [registerTable, { editByRowModal, deleteByRow }] = useSmartTable({
  columns: getTableColumns(),
  height: 'auto',
  pagerConfig: true,
  border: true,
  highlightHoverRow: true,
  stripe: true,
  useSearchForm: true,
  columnConfig: {
    resizable: true,
  },
  rowConfig: {
    keyField: 'id',
  },
  sortConfig: {
    remote: true,
  },
  showOverflow: 'tooltip',
  searchFormConfig: {
    schemas: getSearchFormSchemas(t),
    searchWithSymbol: true,
    colon: true,
    layout: 'inline',
    actionColOptions: {
      span: undefined,
    },
  },
  addEditConfig: {
    formConfig: {
      schemas: getFormSchemas(t),
      baseColProps: { span: 24 },
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
  exportConfig: {},
  toolbarConfig: {
    zoom: true,
    refresh: true,
    custom: true,
    export: true,
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
