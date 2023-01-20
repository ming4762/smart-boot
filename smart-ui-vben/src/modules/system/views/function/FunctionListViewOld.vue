<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      :columns="columns"
      resizable
      :loading="dataLoading"
      :data="functionList"
      :tree-config="treeConfig"
      align="left"
      border
      highlight-hover-row
      :size="tableSizeConfig"
      :toolbar-config="toolbarConfig"
      height="auto">
      <template #toolbar_buttons></template>
      <template #toolbar_tools>
        <a-button
          v-permission="permissions.add"
          :size="buttonSizeConfig"
          type="primary"
          @click="() => handleShowAdd(null)">
          <plus-outlined />
          {{ $t('common.button.add') }}
        </a-button>
        <a-button
          v-permission="permissions.delete"
          :size="buttonSizeConfig"
          style="margin-left: 5px"
          type="primary"
          danger
          @click="handleDelete">
          <delete-outlined />
          {{ $t('common.button.delete') }}
        </a-button>
      </template>
      <template #table-operation="{ row }">
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <a-menu-item
                key="add"
                :disabled="!hasPermission(permissions.add) || row.functionType === '30'">
                <plus-outlined />
                {{ $t('common.button.add') }}
              </a-menu-item>
              <a-menu-item key="edit" :disabled="!hasPermission(permissions.update)">
                <edit-outlined />
                {{ $t('common.button.edit') }}
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
      <template #table-icon="{ row }">
        <component :is="row.icon" v-if="row.icon" />
        <div v-else></div>
      </template>
      <template #table-functionType="{ row }">
        <a-tag :color="getTagData(row.functionType).color">
          {{ getTagData(row.functionType).text }}
        </a-tag>
      </template>
    </vxe-grid>
    <a-modal
      v-model:visible="addEditModalVisible"
      width="860px"
      :confirm-loading="saveLoading"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      @ok="handleSave">
      <a-spin :spinning="getLoading">
        <a-form
          :label-col="{ span: 7 }"
          :wrapper-col="{ span: 16 }"
          :rules="formRules"
          style="padding: 10px"
          ref="formRef"
          :model="saveModel">
          <a-row>
            <a-col :span="12">
              <a-form-item label="上级">
                <a-input v-model:value="showDataModel.parentName" disabled />
                <a-input v-show="false" v-model:value="saveModel.parentId" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                name="functionName"
                :label="$t('system.views.function.table.functionName')">
                <a-input
                  v-model:value="saveModel.functionName"
                  :placeholder="$t('system.views.function.validate.functionName')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                name="functionType"
                :label="$t('system.views.function.table.functionType')">
                <a-radio-group v-model:value="saveModel.functionType">
                  <a-radio
                    v-for="(type, key) in functionTypes"
                    :key="key"
                    :disabled="type.disabled"
                    :value="type.value">
                    {{ type.label }}
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item name="i18nCode" :label="$t('system.views.function.table.i18nCode')">
                <a-input
                  v-model:value="saveModel.i18nCode"
                  :placeholder="$t('system.views.function.validate.i18nCode')" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item name="icon" :label="$t('system.views.function.table.icon')">
                <a-input
                  v-model:value="saveModel.icon"
                  :placeholder="$t('system.views.function.validate.icon')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                name="component"
                :rules="[
                  {
                    required: saveModel.functionType === 'MENU',
                    trigger: 'blur',
                    message: $t('system.views.function.validate.component'),
                  },
                ]"
                :label="$t('system.views.function.table.component')">
                <a-input
                  v-model:value="saveModel.component"
                  :placeholder="$t('system.views.function.validate.component')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                name="componentName"
                :rules="[
                  {
                    required: saveModel.functionType === 'MENU',
                    trigger: 'blur',
                    message: $t('system.views.function.validate.componentName'),
                  },
                ]"
                :label="$t('system.views.function.table.componentName')">
                <a-input
                  v-model:value="saveModel.componentName"
                  :placeholder="$t('system.views.function.validate.componentName')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item name="url" label="url">
                <a-input
                  v-model:value="saveModel.url"
                  :placeholder="$t('system.views.function.validate.url')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item name="redirect" label="redirect">
                <a-input
                  v-model:value="saveModel.redirect"
                  :placeholder="$t('system.views.function.validate.redirect')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item
                name="httpMethod"
                :label="$t('system.views.function.table.httpMethod')"
                :rules="[
                  {
                    required: saveModel.functionType === 'FUNCTION',
                    message: $t('system.views.function.validate.httpMethod'),
                    trigger: 'blur',
                  },
                ]"
                :placeholder="$t('system.views.function.validate.httpMethod')">
                <a-select
                  v-model:value="saveModel.httpMethod"
                  :placeholder="$t('system.views.function.validate.httpMethod')">
                  <a-select-option value="GET">GET</a-select-option>
                  <a-select-option value="POST">POST</a-select-option>
                  <a-select-option value="PUT">PUT</a-select-option>
                  <a-select-option value="DELETE">DELETE</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                name="permission"
                :rules="[
                  {
                    required: saveModel.functionType === 'FUNCTION',
                    message: $t('system.views.function.validate.permission'),
                    trigger: 'blur',
                  },
                ]"
                :label="$t('system.views.function.table.permission')">
                <a-input
                  v-model:value="saveModel.permission"
                  :placeholder="$t('system.views.function.validate.permission')" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item name="menuIs" :label="$t('system.views.function.table.menuIs')">
                <a-switch v-model:checked="saveModel.menuIs" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                name="internalOrExternal"
                :label="$t('system.views.function.table.internalOrExternal')">
                <a-switch v-model:checked="saveModel.internalOrExternal" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item name="dataRule" :label="$t('system.views.function.table.dataRule')">
                <a-switch v-model:checked="saveModel.dataRule" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item name="seq" :label="$t('common.table.seq')">
                <a-input-number v-model:value="saveModel.seq" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item v-show="!isAdd" :label="$t('common.table.createUser')">
                <a-input disabled :value="showDataModel.createUser" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item v-show="!isAdd" :label="$t('common.table.createTime')">
                <a-input disabled :value="showDataModel.createTime" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item v-show="!isAdd" :label="$t('common.table.updateUser')">
                <a-input disabled :value="showDataModel.createUser" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item v-show="!isAdd" :label="$t('common.table.updateTime')">
                <a-input disabled :value="showDataModel.updateTime" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { DownOutlined, EditOutlined, PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'

import { vueLoadFunctionList, vueAddEdit, vueDelete } from './FunctionListSupport'
import { SystemPermissions } from '../../constants/SystemConstants'
import dayjs from 'dayjs'
import { hasPermission } from '/@/common/auth/AuthUtils'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'

/**
 * 功能管理界面
 */
export default defineComponent({
  name: 'FunctionListViewOld',
  components: {
    DownOutlined,
    EditOutlined,
    PlusOutlined,
    DeleteOutlined,
  },
  setup() {
    const gridRef = ref()
    const { t } = useI18n()
    const loadFunctionListVue = vueLoadFunctionList()
    const addEditVue = vueAddEdit(loadFunctionListVue.loadData, t)
    const deleteVue = vueDelete(loadFunctionListVue.loadData, gridRef, t)
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
    /**
     * 操作列点击
     * @param row
     * @param action
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        case 'add': {
          addEditVue.handleShowAdd(row)
          break
        }
        case 'edit': {
          addEditVue.handleShowEdit(row)
          break
        }
      }
    }
    return {
      ...useSizeSetting(),
      hasPermission,
      ...loadFunctionListVue,
      ...addEditVue,
      getTagData,
      handleActions,
      ...deleteVue,
      gridRef,
    }
  },
  data() {
    return {
      permissions: SystemPermissions.function,
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools',
        },
      },
      treeConfig: {},
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left',
        },
        {
          title: '{system.views.function.table.functionName}',
          field: 'functionName',
          width: 220,
          fixed: 'left',
          treeNode: true,
        },
        {
          title: '{system.views.function.table.functionType}',
          field: 'functionType',
          width: 110,
          align: 'center',
          headerAlign: 'left',
          slots: {
            default: 'table-functionType',
          },
        },
        {
          title: '{system.views.function.table.icon}',
          field: 'icon',
          width: 80,
          align: 'center',
          headerAlign: 'left',
          slots: {
            default: 'table-icon',
          },
        },
        {
          title: 'URL',
          field: 'url',
          minWidth: 200,
        },
        {
          title: '{system.views.function.table.permission}',
          field: 'permission',
          width: 160,
        },
        {
          title: '{system.views.function.table.httpMethod}',
          field: 'httpMethod',
          width: 120,
        },
        {
          title: '{common.table.seq}',
          field: 'seq',
          width: 100,
          sortable: true,
        },
        {
          title: '{common.table.createTime}',
          field: 'createTime',
          width: 165,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          sortable: true,
        },
        {
          title: '{common.table.createUser}',
          field: 'createUserId',
          width: 120,
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName
            }
            return ''
          },
        },
        {
          title: '{common.table.updateTime}',
          field: 'updateTime',
          width: 165,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          sortable: true,
        },
        {
          title: '{common.table.updateUser}',
          field: 'updateUserId',
          width: 120,
          formatter: ({ row }: any) => {
            if (row.updateUser) {
              return row.updateUser.fullName
            }
            return ''
          },
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
      ],
    }
  },
})
</script>

<style lang="less" scoped></style>
