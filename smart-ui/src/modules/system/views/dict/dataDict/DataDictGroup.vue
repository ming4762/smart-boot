<template>
  <div class="full-height" style="padding: 10px 0 10px 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :filter-config="filterConfig"
      :columns="columns"
      :toolbar-config="toolbarConfig"
      highlight-current-row
      height="auto"
      stripe
      highlight-hover-row
      @filter-change="handleFilterChange">
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevPage', 'Number', 'NextPage', 'Total']" />
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
      <template #dictCode-filter="{ column, $panel }">
        <div style="padding: 5px">
          <a-input v-model:value="column.filters[0].data" @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])" />
        </div>
      </template>
      <template #dictName-filter="{ column, $panel }">
        <div style="padding: 5px">
          <a-input v-model:value="column.filters[0].data" @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])" />
        </div>
      </template>
      <template #useYn-filter="{ column, $panel }">
        <div style="padding: 5px">
          <a-select v-model:value="column.filters[0].data" style="width: 100px" @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])">
            <a-select-option value="">ALL</a-select-option>
            <a-select-option :value="true">YES</a-select-option>
            <a-select-option :value="false">NO</a-select-option>
          </a-select>
        </div>
      </template>
    </vxe-grid>
    <a-modal
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :label-col="{span: 5}"
          :rules="rules"
          :wrapper-col="{span: 18}"
          v-bind="formProps">
          <a-row>
            <a-col :span="24">
              <a-form-item label="字典编码" name="dictCode">
                <a-input v-model:value="formProps.model.dictCode" placeholder="请输入字典编码" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="字典名称" name="dictName">
                <a-input v-model:value="formProps.model.dictName" placeholder="请输入字典名称" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="序号" name="seq">
                <a-input-number v-model:value="formProps.model.seq" style="width: 100%" placeholder="请输入序号" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="启用状态" name="useYn">
                <a-switch v-model:checked="formProps.model.useYn" />
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

import { useVxeTable, useAddEdit, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData, handleGetById, handleSaveUpdate, handleDelete } from './DataDictGroupHook'

export default defineComponent({
  name: 'SysDictListView',
  components: {
  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, loadData, handleReset, pageProps, searchModel } = useVxeTable(handleLoadData, {
      paging: true
    })

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'dictCode'
    })
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'dictCode', listHandler: loadData })

    /**
     * 按钮操作
     * @param row 行数据
     * @param action 操作
     */
    const handleActions = (row: any, action: String) => {
      switch (action) {
        case 'EDIT': {
          addEditHook.handleAddEdit(false, row.dictCode)
          break
        }
        case 'DELETE': {
          deleteHook.handleDeleteByRow(row)
          break
        }
      }
    }
    /**
     * 过滤变化时触发
     * @param a
     */
    const handleFilterChange = ({ property, datas }: any) => {
      searchModel.value[property] = datas[0]
      loadData()
    }
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...addEditHook,
      ...deleteHook,
      tableProps,
      loadData,
      handleReset,
      handleActions,
      pageProps,
      searchModel,
      handleFilterChange
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools'
        }
      },
      filterConfig: {
        remote: true
      },
      rules: {
        dictCode: [
          {
            required: true,
            message: '请输入字典编码',
            trigger: [
              'BLUR'
            ]
          }
        ],
        dictName: [
          {
            required: true,
            message: '请输入字典名称',
            trigger: [
              'BLUR'
            ]
          }
        ],
        seq: [
          {
            required: true,
            message: '请输入序号',
            trigger: [
              'BLUR'
            ]
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
          title: '字典编码',
          field: 'dictCode',
          width: 180,
          filters: [{data: ''}],
          slots: {
            filter: 'dictCode-filter'
          },
          fixed: 'left'
        },
        {
          title: '字典名称',
          field: 'dictName',
          minWidth: 180,
          filters: [{data: ''}],
          slots: {
            filter: 'dictName-filter'
          }
        },
        {
          title: '序号',
          field: 'seq',
          sortable: true,
          width: 120
        },
        {
          title: '启用状态',
          field: 'useYn',
          width: 100,
          filterMultiple: false,
          filters: [{label: 'YES', data: true}, {label: 'NO', data: false}]
        }
      ]
    }
  }
})
</script>
