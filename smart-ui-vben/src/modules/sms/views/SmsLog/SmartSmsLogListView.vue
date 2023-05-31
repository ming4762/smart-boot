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

import { getTableColumns, getSearchFormSchemas } from './SmartSmsLogListView.config'
import { listApi, getByIdApi } from './SmartSmsLogListView.api'

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
  border: true,
  sortConfig: {
    remote: true,
  },
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
  proxyConfig: {
    ajax: {
      query: (params) => listApi(params.ajaxParameter),
      getById: (params) => getByIdApi(params.id),
    },
  },
  toolbarConfig: {
    zoom: true,
    refresh: true,
    custom: true,
    buttons: [],
  },
})
</script>
