<template>
  <div class="full-height">
    <SmartTable @register="registerTable" :size="getTableSize">
      <template #table-operation="{ row }">
        <TableAction :actions="getTableAction(row)" />
      </template>
    </SmartTable>
    <CodeCreateModal @register="registerCodeCreateModal" />
  </div>
</template>

<script lang="ts" setup>
import { SmartTable, useSmartTable } from '/@/components/SmartTable'

import { tableColumns, searchFormColumns } from './CodeListView.config'
import { listApi, deleteApi } from './CodeListView.api'
import { useRouter } from 'vue-router'
import { buildUUID } from '/@/utils/uuid'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { useModal } from '/@/components/Modal'
import { useI18n } from '/@/hooks/web/useI18n'

import { TableAction, ActionItem } from '/@/components/SmartTable'
import CodeCreateModal from './components/CodeCreateModal.vue'

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
      onClick: () => openCodeCreateModal(true, row),
    },
  ]
}

// 生成代码弹窗
const [registerCodeCreateModal, { openModal: openCodeCreateModal }] = useModal()

const [registerTable] = useSmartTable({
  searchFormConfig: {
    searchWithSymbol: true,
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
  columns: tableColumns(t),
  useSearchForm: true,
  pagerConfig: true,
  sortConfig: {
    remote: true,
  },
  columnConfig: {
    resizable: true,
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
