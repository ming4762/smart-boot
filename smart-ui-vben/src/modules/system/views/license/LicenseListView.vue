<!--
license管理页面
-->
<template>
  <div class="full-height page-container">
    <LayoutSeparate first-size="240px" :show-line="false" class="full-height">
      <template #first>
        <div class="full-height system-container">
          <SystemSimpleList
            @current-change="handleSelectSystemChange"
            :row-config="{ isHover: true, isCurrent: true }"
            height="auto" />
        </div>
      </template>
      <template #second>
        <SmartTable class="license-view" @register="registerTable" :size="getTableSize">
          <template #table-operation="{ row }">
            <SmartVxeTableAction
              :drop-down-actions="getTableDropDownActions(row)"
              :actions="getTableActions(row)" />
          </template>
        </SmartTable>
      </template>
    </LayoutSeparate>
  </div>
</template>

<script lang="ts" setup>
import type { ActionItem } from '/@/components/SmartTable'

import { ref, unref } from 'vue'
import { LayoutSeparate } from '/@/components/LayoutSeparate'
import SystemSimpleList from '/@/modules/system/components/system/SystemSimpleList.vue'
import { useSmartTable, SmartTable, SmartVxeTableAction } from '/@/components/SmartTable'
import { useI18n } from '/@/hooks/web/useI18n'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'

import {
  getAddEditFormSchemas,
  getSearchFormSchemas,
  getTableColumns,
  Permissions,
} from './LicenseListView.config'
import { listApi, deleteApi, saveUpdateBatchApi, getByIdApi } from './LicenseListView.api'
import { buildUUID } from '/@/utils/uuid'
import dayjs from 'dayjs'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

/**
 * 系统变更时触发：更新数据
 */
const currentSystemRef = ref<Recordable>({})
const handleSelectSystemChange = (system) => {
  currentSystemRef.value = system
  query()
}

const getTableActions = (row): ActionItem[] => {
  return [
    {
      label: t('common.button.edit'),
      auth: Permissions.update,
      onClick: () => editByRowModal(row),
    },
  ]
}

const getTableDropDownActions = (row): ActionItem[] => {
  return [
    {
      label: t('smart.license.button.generator'),
      preIcon: 'ant-design:check-outlined',
      auth: Permissions.generator,
      onClick: () => {
        console.log(row)
      },
    },
    {
      label: t('common.button.download'),
      preIcon: 'ant-design:download-outlined',
      auth: Permissions.download,
      disabled: row.status === 'GENERATOR',
      onClick: () => {
        console.log(row)
      },
    },
  ]
}

const [registerTable, { query, editByRowModal, showAddModal }] = useSmartTable({
  columns: getTableColumns(),
  border: true,
  height: 'auto',
  stripe: true,
  highlightHoverRow: true,
  columnConfig: {
    resizable: true,
  },
  pagerConfig: true,
  sortConfig: {
    remote: true,
    defaultSort: {
      field: 'seq',
      order: 'asc',
    },
  },
  useSearchForm: true,
  searchFormConfig: {
    colon: true,
    searchWithSymbol: true,
    baseColProps: {
      span: 8,
    },
    labelCol: {
      span: 8,
    },
    // layout: 'inline',
    actionColOptions: {
      span: 8,
    },
    schemas: getSearchFormSchemas(t),
  },
  proxyConfig: {
    ajax: {
      query: (params) => {
        const parameter = {
          ...params.ajaxParameter,
          systemId: unref(currentSystemRef)?.id,
        }
        return listApi(parameter)
      },
      save: ({ body: { insertRecords, updateRecords } }) => {
        const dataList = [...insertRecords, ...updateRecords]
        dataList.forEach((item) => {
          const times = item.times
          Object.assign(item, {
            effectiveTime: times[0],
            expirationTime: times[1],
          })
          delete item.times
        })
        return saveUpdateBatchApi(dataList)
      },
      delete: ({ body: { removeRecords } }) => deleteApi(removeRecords),
      getById: async (params) => {
        const result = await getByIdApi(params)
        const system = result.system
        return {
          ...result,
          times: [dayjs(result.effectiveTime), dayjs(result.expirationTime)],
          systemName: `${system.name}(${system.code})`,
        }
      },
    },
  },
  printConfig: {},
  exportConfig: {},
  toolbarConfig: {
    refresh: true,
    custom: true,
    zoom: true,
    print: true,
    export: true,
    buttons: [
      {
        code: 'ModalAdd',
        auth: Permissions.save,
        props: {
          onClick: () => {
            const currentSystem = unref(currentSystemRef)
            showAddModal({
              licenseCode: buildUUID(),
              systemId: currentSystem.id,
              systemName: `${currentSystem.name}(${currentSystem.code})`,
            })
          },
        },
      },
      {
        code: 'delete',
        auth: Permissions.delete,
      },
    ],
  },
  addEditConfig: {
    modalConfig: {
      width: '1000px',
      defaultFullscreen: true,
    },
    formConfig: {
      labelCol: {
        span: 6,
      },
      wrapperCol: {
        span: 17,
      },
      schemas: getAddEditFormSchemas(t),
      baseColProps: { span: 12 },
    },
  },
})
</script>

<style lang="less" scoped>
.license-view {
  :deep(.smart-table-container) {
    height: calc(100% - 122px) !important;
  }
}

.system-container {
  background: white;
  margin-right: 5px;
}
</style>
