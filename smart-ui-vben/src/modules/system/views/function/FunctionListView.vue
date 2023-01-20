<template>
  <div class="full-height">
    <SmartTable @register="registerTable" :size="getTableSize">
      <template #table-icon="{ row }">
        <Icon v-if="row.icon" :icon="getIcon(row.icon)" />
        <div v-else></div>
      </template>
      <template #table-functionType="{ row }">
        <a-tag :color="getTagData(row.functionType).color">
          {{ getTagData(row.functionType).text }}
        </a-tag>
      </template>
      <template #table-operation="{ row }">
        <TableAction :actions="getTableActions(row)" />
      </template>
      <template #addEditForm-functionType="{ model }">
        <a-radio-group v-model:value="model.functionType">
          <a-radio
            v-for="(type, key) in functionTypes"
            :key="key"
            :disabled="type.disabled"
            :value="type.value">
            {{ type.label }}
          </a-radio>
        </a-radio-group>
      </template>
    </SmartTable>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue'

import { ActionItem, SmartTable, useSmartTable } from '/@/components/SmartTable'
import { Icon } from '/@/components/Icon'
import { tableColumns, getAddEditForm } from './FunctionListView.config'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { listTree, deleteApi, getByIdApi, saveApi } from './FunctionListView.api'
import { useI18n } from '/@/hooks/web/useI18n'
import StringUtils from '/@/utils/StringUtils'
import { TableAction } from '/@/components/SmartTable'

const { t } = useI18n()
const { getTableSize } = useSizeSetting()

const [registerTable, { showAddModal, editByRow }] = useSmartTable({
  id: 'FunctionListView',
  columns: tableColumns,
  resizableConfig: {},
  border: true,
  align: 'left',
  treeConfig: {},
  highlightHoverColumn: true,
  height: 'auto',
  columnConfig: {
    resizable: true,
  },
  sortConfig: {
    remote: true,
    defaultSort: {
      field: 'seq',
      order: 'asc',
    },
  },
  addEditConfig: {
    modalConfig: {
      width: 860,
    },
    formConfig: {
      baseColProps: {
        span: 12,
      },
      labelCol: { span: 7 },
      wrapperCol: { span: 16 },
      schemas: getAddEditForm(t),
    },
  },
  proxyConfig: {
    ajax: {
      query: (params) => listTree(params.ajaxParameter),
      delete: (params) => deleteApi(params),
      save: saveApi,
      getById: getByIdApi,
    },
  },
  toolbarConfig: {
    refresh: true,
    buttons: [
      {
        code: 'ModalAdd',
        props: {
          onClick: () => {
            setTypeDisabled(['function'])
            showAddModal({
              parentId: '0',
              parentName: '根目录',
            })
          },
        },
      },
      {
        code: 'delete',
      },
    ],
  },
})
const getTagData = (functionType: string) => {
  switch (functionType) {
    case 'CATALOG': {
      return {
        color: '#f50',
        text: t('system.views.function.common.catalogue'),
      }
    }
    case 'MENU': {
      return {
        color: '#2db7f5',
        text: t('system.views.function.common.menu'),
      }
    }
    case 'FUNCTION': {
      return {
        color: '#108ee9',
        text: t('system.views.function.common.function'),
      }
    }
    default: {
      return {}
    }
  }
}

const getIcon = (compatibleIcon: string) => {
  if (compatibleIcon && compatibleIcon.indexOf(':') === -1) {
    compatibleIcon = StringUtils.humpToLine(compatibleIcon)
    compatibleIcon = 'ant-design:' + compatibleIcon
  }
  return compatibleIcon
}

const getTableActions = (row: Recordable): ActionItem[] => {
  return [
    {
      label: t('common.button.add'),
      icon: 'ant-design:plus-outlined',
      onClick: () => {
        const data: Recordable = {
          parentId: row.functionId,
          parentName: row.functionName,
        }
        const functionType = row.functionType
        switch (functionType) {
          case 'CATALOG': {
            setTypeDisabled(['function'])
            break
          }
          case 'MENU': {
            setTypeDisabled(['catalogue', 'menu'])
            data.functionType = 'FUNCTION'
            break
          }
          case 'FUNCTION': {
            setTypeDisabled(['catalogue', 'menu', 'function'])
            break
          }
        }
        showAddModal(data)
      },
    },
    {
      label: t('common.button.edit'),
      icon: 'ant-design:edit-outlined',
      onClick: () => {
        setTypeDisabled(['catalogue', 'menu', 'function'])
        editByRow(row)
      },
    },
  ]
}

const defaultFunctionTypes = (t: Function) => {
  return {
    catalogue: {
      value: 'CATALOG',
      label: t('system.views.function.common.catalogue'),
      disabled: false,
    },
    menu: {
      value: 'MENU',
      label: t('system.views.function.common.menu'),
      disabled: false,
    },
    function: {
      value: 'FUNCTION',
      label: t('system.views.function.common.function'),
      disabled: false,
    },
  }
}
/**
 * 设置不可用类型
 * @param keys
 */
const setTypeDisabled = (keys: Array<string>) => {
  Object.keys(functionTypes).forEach((key) => {
    functionTypes[key].disabled = keys.includes(key)
  })
}
const functionTypes: any = reactive(defaultFunctionTypes(t))
</script>

<style scoped></style>
