<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :column-config="{ resizable: true }"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      show-overflow="tooltip"
      height="auto"
      stripe
      highlight-hover-row>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="() => handleAddEdit(true, null)">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="handleEditByCheckbox">
              {{ $t('common.button.edit') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              danger
              style="margin-left: 5px"
              @click="handleDeleteByCheckbox">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('monitor.views.application.title.name')">
            <a-input v-model:value="searchModel.name" :size="formSizeConfig" :placeholder="$t('monitor.views.application.validate.name')" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.application.title.applicationCode')">
            <a-input v-model:value="searchModel.applicationCode" :size="formSizeConfig" :placeholder="$t('monitor.views.application.validate.applicationCode')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.useYn')">
            <a-switch v-model:checked="searchModel.useYn" />
          </a-form-item>
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              @click="handleReset">
              {{ $t('common.button.reset') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #table-operation="{ row }">
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <a-menu-item key="EDIT">
                {{ $t('common.button.edit') }}
              </a-menu-item>
              <a-menu-item key="DELETE">
                {{ $t('common.button.delete') }}
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
    </vxe-grid>
    <a-modal
      width="900px"
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{span: 8}"
          :wrapper-col="{span: 15}"
          v-bind="formProps">
          <a-row>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.name')"
                name="name">
                <a-input
                  v-model:value="formProps.model.name"
                  :placeholder="$t('monitor.views.application.validate.name')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.applicationCode')"
                name="applicationCode">
                <a-input
                  v-model:value="formProps.model.applicationCode"
                  :placeholder="$t('monitor.views.application.validate.applicationCode')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('common.table.remark')"
                name="remark">
                <a-input
                  v-model:value="formProps.model.remark"
                  :placeholder="$t('common.formValidate.remark')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('common.table.seq')"
                name="seq">
                <a-input-number
                  v-model:value="formProps.model.seq"
                  style="width: 100%"
                  :placeholder="$t('common.formValidate.seq')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.statusInterval')"
                name="statusInterval">
                <a-input-number
                  v-model:value="formProps.model.statusInterval"
                  style="width: 100%"
                  :placeholder="$t('monitor.views.application.validate.statusInterval')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.offlineInterval')"
                name="offlineInterval">
                <a-input-number
                  v-model:value="formProps.model.offlineInterval"
                  style="width: 100%"
                  :placeholder="$t('monitor.views.application.validate.offlineInterval')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('common.table.useYn')"
                name="useYn">
                <a-switch v-model:checked="formProps.model.useYn" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.token')"
                name="token">
                <a-input
                  v-model:value="formProps.model.token"
                  :placeholder="$t('monitor.views.application.validate.token')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('monitor.views.application.title.serializeEventCode')"
                name="serializeEventCode">
                <a-textarea
                  v-model:value="formProps.model.serializeEventCode"
                  :rows="3"
                  :placeholder="$t('monitor.views.application.validate.serializeEventCode')" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { DownOutlined } from '@ant-design/icons-vue'

import dayjs from 'dayjs'

import { useVxeTable, useAddEdit, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData, handleGetById, handleSaveUpdate, handleDelete } from './MonitorApplicationHook'

import { tableUseYn } from '@/components/common/TableCommon'

export default defineComponent({
  name: 'MonitorApplicationListView',
  components: {
    DownOutlined
  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, searchModel, loadData } = useVxeTable(handleLoadData, {
      paging: true,
      defaultParameter: {
        useYn: true
      },
      defaultSorter: {
        sortName: 'seq',
        sortOrder: 'asc'
      }
    })

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'id',
      defaultModel: {
        useYn: true,
        seq: 1
      }
    })

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'id', listHandler: loadData })

    /**
     * 按钮操作
     * @param row 行数据
     * @param action 操作
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        case 'EDIT': {
          addEditHook.handleAddEdit(false, row.id)
          break
        }
        case 'DELETE': {
          deleteHook.handleDeleteByRow(row)
          break
        }
      }
    }
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...addEditHook,
      searchModel,
      ...deleteHook,
      tableProps,
      pageProps,
      handleReset,
      handleActions,
      loadData
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
          buttons: 'toolbar_buttons'
        }
      },
      rules: {
        name: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('monitor.views.application.rules.name_NOT_EMPTY')
          }
        ],
        applicationCode: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('monitor.views.application.rules.applicationCode_NOT_EMPTY')
          }
        ],
        seq: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('common.formValidate.seq')
          }
        ]
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
        {
          field: 'name',
          fixed: 'left',
          title: '{monitor.views.application.title.name}',
          width: 180
        },
        {
          field: 'applicationCode',
          fixed: 'left',
          title: '{monitor.views.application.title.applicationCode}',
          width: 180
        },
        {
          field: 'remark',
          title: '{common.table.remark}',
          minWidth: 180
        },
        {
          field: 'seq',
          sortable: true,
          title: '{common.table.seq}',
          width: 120
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true
        },
        {
          field: 'statusInterval',
          title: '{monitor.views.application.title.statusInterval}',
          width: 150
        },
        {
          field: 'offlineInterval',
          title: '{monitor.views.application.title.offlineInterval}',
          width: 150
        },
        {
          field: 'createUserId',
          title: '{common.table.createUser}',
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName
            }
            return ''
          },
          width: 120
        },
        {
          field: 'createTime',
          title: '{common.table.createTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 160
        },
        {
          field: 'updateUserId',
          title: '{common.table.updateUser}',
          formatter: ({ row }: any) => {
            if (row.updateUser) {
              return row.updateUser.fullName
            }
            return ''
          },
          width: 120
        },
        {
          field: 'updateTime',
          title: '{common.table.updateTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 160
        },
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
      ]
    }
  }
})
</script>
