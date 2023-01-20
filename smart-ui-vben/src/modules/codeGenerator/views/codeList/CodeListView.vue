<template>
  <div class="full-height">
    <SmartTable @register="registerTable">
      <template #table-operation="{ row }">
        <TableAction :actions="getTableAction(row)" />
      </template>
      <template #addEditForm-connectionId="{ model }">
        <DatabaseSelect v-model:value="model.connectionId" />
      </template>
      <template #addEditForm-RelateTable="{ model }">
        <a-tag
          v-for="(table, index) in model.addendumTableList"
          :key="index"
          style="display: inline-block"
          @close="() => handleRemoveRelateTable(model.addendumTableList, index)"
          closable>
          {{ table.configName }}
        </a-tag>
        <PlusOutlined
          :style="{ cursor: 'pointer' }"
          @click="() => openPageAddendumTableChoseModal(true, {})" />
        <PageAddendumTableChoseModal
          :select-table-list="model.addendumTableList"
          :setAddEditFieldsValue="setAddEditFieldsValue"
          @register="registerPageAddendumTableChoseModal" />
      </template>
      <template #addEditForm-syncTable>
        <a-button type="primary" :size="getTableSize" @click="handleSyncTableData">
          {{ $t('generator.views.code.button.syncTableData') }}
        </a-button>
      </template>
      <template #addEditForm-tabs="{ model }">
        <CodeTableSettingTab
          :data="computedTableData"
          :loading="dbDataLoading"
          :connection-id="model.connectionId"
          :table-name="model.tableName" />
      </template>
    </SmartTable>
  </div>
</template>

<script lang="ts" setup>
import { PlusOutlined } from '@ant-design/icons-vue'

import { SmartTable, useSmartTable } from '/@/components/SmartTable'

import { tableColumns, searchFormColumns } from './CodeListView.config'
import { listApi, deleteApi } from './CodeListView.api'

import { useI18n } from '/@/hooks/web/useI18n'
import { TableAction, ActionItem } from '/@/components/SmartTable'
import DatabaseSelect from './components/DatabaseSelect.vue'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { useModal } from '/@/components/Modal'
import PageAddendumTableChoseModal from './components/PageAddendumTableChoseModal.vue'
import CodeTableSettingTab from './components/CodeTableSettingTab.vue'
import { useLoadDbData } from './hooks/useLoadDbData'
import { useRouter } from 'vue-router'
import { buildUUID } from '/@/utils/uuid'

const { t } = useI18n()
const router = useRouter()
const { getTableSize } = useSizeSetting()

const toDesign = (configId?: number) => {
  router.push({
    path: '/code/codeDesign',
    query: {
      pageKey: buildUUID(),
      configId,
    },
  })
}

const getTableAction = (row): ActionItem[] => {
  return [
    {
      label: '编辑',
      onClick: () => toDesign(row.id),
    },
    {
      label: '生成',
    },
  ]
}

const [registerPageAddendumTableChoseModal, { openModal: openPageAddendumTableChoseModal }] =
  useModal()

const handleRemoveRelateTable = (dataList: any[], index: number) => {
  dataList.splice(index, 1)
}

const [registerTable, { setAddEditFieldsValue, validateAddEdit }] = useSmartTable({
  searchFormConfig: {
    layout: 'inline',
    schemas: searchFormColumns(t),
    actionColOptions: {
      span: 6,
    },
    baseColProps: {
      span: 6,
    },
    baseRowStyle: {
      width: '100%',
    },
    labelAlign: 'left',
  },
  height: 'auto',
  columns: tableColumns,
  useSearchForm: true,
  pagerConfig: true,
  searchWithSymbol: true,
  sortConfig: {
    remote: true,
  },
  proxyConfig: {
    ajax: {
      query: (params) => listApi(params.ajaxParameter),
      delete: ({ body }) => deleteApi(body.removeRecords),
    },
  },
  toolbarConfig: {
    refresh: true,
    buttons: [
      {
        code: 'ModalAdd',
        props: {
          onClick: () => toDesign(),
        },
      },
      {
        code: 'delete',
      },
    ],
  },
})

const { handleSyncTableData, computedTableData, dbDataLoading } = useLoadDbData({ validateAddEdit })
</script>

<style lang="less" scoped>
.code-container {
  ::v-deep(.ant-modal-content) {
    height: 100%;
  }
  ::v-deep(.ant-modal) {
    max-width: 100%;
  }
  ::v-deep(.ant-form-item) {
    margin-bottom: 0;
  }
  ::v-deep(.ant-list-item) {
    padding: 8px 0;
  }
  ::v-deep(.ant-modal-body) {
    padding: 10px;
  }
  ::v-deep(.ant-tabs-top-content) {
    height: calc(100% - 65px);
  }
  ::v-deep(.ant-spin-nested-loading) {
    height: 100%;
  }
  ::v-deep(.ant-spin-container) {
    height: 100%;
    overflow: auto;
  }
}
</style>
