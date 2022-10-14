<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :column-config="columnConfig"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              @click="() => handleAddEdit(true, null)"
              style="margin-left: 5px">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              danger
              @click="handleDeleteByCheckbox"
              style="margin-left: 5px">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('system.views.dept.title.deptCode')">
            <a-input v-model:value="searchModel.deptCode" :size="formSizeConfig" :placeholder="$t('system.views.dept.validate.deptCode')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.dept.title.deptName')">
            <a-input v-model:value="searchModel.deptName" :size="formSizeConfig" :placeholder="$t('system.views.dept.validate.deptName')" />
          </a-form-item>
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData"
>
              {{ $t('common.button.search') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              @click="handleReset"
              >
              {{ $t('common.button.reset') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
    </vxe-grid>
    <a-modal
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{span: 6}"
          :wrapper-col="{span: 17}"
          v-bind="formProps">
          <a-row>
            <a-input v-model:value="formProps.model.deptId" style="display: none" />
            <a-input v-model:value="formProps.model.parentId" style="display: none" />
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.deptCode')"
                name="deptCode">
                <a-input
                  v-model:value="formProps.model.deptCode"
                  :placeholder="$t('system.views.dept.validate.deptCode')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.deptType')"
                name="deptType">
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.deptName')"
                name="deptName">
                <a-input
                  v-model:value="formProps.model.deptName"
                  :placeholder="$t('system.views.dept.validate.deptName')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.email')"
                name="email">
                <a-input
                  v-model:value="formProps.model.email"
                  :placeholder="$t('system.views.dept.validate.email')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.director')"
                name="director">
                <a-input
                  v-model:value="formProps.model.director"
                  :placeholder="$t('system.views.dept.validate.director')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.phone')"
                name="phone">
                <a-input
                  v-model:value="formProps.model.phone"
                  :placeholder="$t('system.views.dept.validate.phone')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.useYn')"
                name="useYn">
                <a-switch v-model:checked="formProps.model.useYn" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.deleteYn')"
                name="deleteYn">
                <a-switch v-model:checked="formProps.model.deleteYn" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.createUserId')"
                name="createUserId">
                <a-input
                  v-model:value="formProps.model.createUserId"
                  :placeholder="$t('system.views.dept.validate.createUserId')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.createTime')"
                name="createTime">
                <a-input
                  v-model:value="formProps.model.createTime"
                  :placeholder="$t('system.views.dept.validate.createTime')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.updateUserId')"
                name="updateUserId">
                <a-input
                  v-model:value="formProps.model.updateUserId"
                  :placeholder="$t('system.views.dept.validate.updateUserId')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dept.title.updateTime')"
                name="updateTime">
                <a-input
                  v-model:value="formProps.model.updateTime"
                  :placeholder="$t('system.views.dept.validate.updateTime')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.seq')"
                name="seq">
                <a-input-number
                  v-model:value="formProps.model.seq"
                  style="width: 100%"
                  :placeholder="$t('common.formValidate.seq')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.remark')"
                name="remark">
                <a-input
                  v-model:value="formProps.model.remark"
                  :placeholder="$t('common.formValidate.remark')" />
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

import {  } from '@ant-design/icons-vue'

import { useVxeTable, useAddEdit, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData, handleGetById, handleSaveUpdate, handleDelete } from './SysDeptHook'

export default defineComponent({
  name: 'SysDeptListView',
  components: {
    
  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, searchModel, loadData } = useVxeTable(handleLoadData, {
      paging: false
    })

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'deptId'
    })

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'deptId', listHandler: loadData })

    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...addEditHook,
      searchModel,
      ...deleteHook,
      tableProps,
      handleReset,
      loadData
    }
  },
  data () {
    return {
      columnConfig: {
      },
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
          buttons: 'toolbar_buttons'
        }
      },
      rules: {
        deptCode: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('system.views.dept.rules.deptCode_NOT_EMPTY')
          }
        ],
        deptType: [
          {
            required: true,
            trigger: [
              'change'
            ],
            message: this.$t('system.views.dept.rules.deptType_NOT_EMPTY')
          }
        ],
        deptName: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('system.views.dept.rules.deptName_NOT_EMPTY')
          }
        ],
      },
      columns: [
        {
          field: 'deptId',
          hidden: true,
          title: '{system.views.dept.title.deptId}',
          width: 120
        },
        {
          field: 'parentId',
          title: '{system.views.dept.title.parentId}',
          width: 120
        },
        {
          field: 'deptCode',
          title: '{system.views.dept.title.deptCode}',
          width: 120
        },
        {
          field: 'deptType',
          title: '{system.views.dept.title.deptType}',
          width: 120
        },
        {
          field: 'deptName',
          title: '{system.views.dept.title.deptName}',
          width: 120
        },
        {
          field: 'email',
          title: '{system.views.dept.title.email}',
          width: 120
        },
        {
          field: 'director',
          title: '{system.views.dept.title.director}',
          width: 120
        },
        {
          field: 'useYn',
          title: '{common.table.useYn}',
          width: 120
        },
        {
          field: 'deleteYn',
          title: '{common.table.deleteYn}',
          width: 120
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
          width: 120
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
          width: 120
        },
        {
          field: 'seq',
          title: '{common.table.seq}',
          width: 120
        },
        {
          field: 'phone',
          title: '{system.views.dept.title.phone}',
          width: 120
        },
        {
          field: 'remark',
          title: '{common.table.remark}',
          width: 120
        },
      ]
    }
  }
})
</script>
