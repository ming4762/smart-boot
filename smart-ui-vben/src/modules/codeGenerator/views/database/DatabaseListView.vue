<!--
数据列表页面
@author zhongming4762
-->
<template>
  <div class="full-height">
    <SmartTable @register="registerTable">
      <template #table-operation="{ row }">
        <TableAction :actions="getTableAction(row)" :drop-down-actions="getDropDownAction(row)" />
      </template>
    </SmartTable>
    <TemplateSelectedModal template-type="template_db_dict" @register="registerModal" />
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from '/@/hooks/web/useI18n'

import { ActionItem, TableAction } from '/@/components/SmartTable'
import { SmartTable, useSmartTable } from '/@/components/SmartTable'
import TemplateSelectedModal from './components/TemplateSelectedModal.vue'

import { addEditForm, searchForm, tableColumns } from './DatabaseListView.data'
import { listApi, deleteApi, getByIdApi, saveUpdateApi } from './DatabaseListView.api'
import { handleTestConnected } from './DatabaseListHooks'
import { useModal } from '/@/components/Modal'

const { t } = useI18n()

const [registerModal, { openModal }] = useModal()

const getTableAction = (row): ActionItem[] => {
  return [
    {
      label: '编辑',
      onClick: () => editByRow(row),
    },
  ]
}

const getDropDownAction = (row): ActionItem[] => {
  return [
    {
      label: t('generator.views.database.button.testConnected'),
      onClick: () => handleTestConnected(row, t, setLoading),
    },
    {
      label: t('generator.views.database.button.createDic'),
      onClick: () => openModal(true, row),
      auth: 'db:connection:createDic',
    },
  ]
}

const [registerTable, { editByRow, setLoading }] = useSmartTable({
  searchFormConfig: {
    schemas: searchForm(t),
    layout: 'inline',
    actionColOptions: {
      span: 6,
    },
  },
  addEditConfig: {
    modalConfig: {
      width: 600,
    },
    formConfig: {
      schemas: addEditForm(t),
      baseColProps: {
        span: 24,
      },
    },
  },
  searchWithSymbol: true,
  columns: tableColumns,
  useSearchForm: true,
  pagerConfig: true,
  sortConfig: {
    remote: true,
  },
  proxyConfig: {
    ajax: {
      query: (parameter) => {
        return listApi(parameter.ajaxParameter)
      },
      delete: async ({ body }) => {
        await deleteApi(body.removeRecords)
      },
      getById: async (row) => {
        return getByIdApi(row.id)
      },
      save: async ({ body }) => {
        const { insertRecords, updateRecords } = body
        const data = [...insertRecords, ...updateRecords][0]
        return saveUpdateApi(data)
      },
    },
  },
  rowConfig: {
    keyField: 'id',
  },
  toolbarConfig: {
    refresh: true,
    buttons: [
      {
        // name: t('common.button.add'),
        code: 'ModalAdd',
      },
      {
        code: 'ModalEdit',
      },
      {
        code: 'delete',
      },
    ],
  },
})
</script>

<style scoped></style>
