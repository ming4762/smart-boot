<template>
  <div class="full-height" style="padding: 10px 0 10px 10px">
    <Grid
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
      @current-change="handleCurrentChange"
      @filter-change="handleFilterChange">
      <template #pager>
        <Pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevPage', 'Number', 'NextPage', 'Total']" />
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              v-permission="'sys:dict:save'"
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="() => handleAddEdit(true, null)">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              v-permission="'sys:dict:edit'"
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="handleEditByCheckbox">
              {{ $t('common.button.edit') }}
            </a-button>
            <a-button
              v-permission="'sys:dict:delete'"
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
          <a-input
            v-model:value="column.filters[0].data"
            @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])" />
        </div>
      </template>
      <template #dictName-filter="{ column, $panel }">
        <div style="padding: 5px">
          <a-input
            v-model:value="column.filters[0].data"
            @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])" />
        </div>
      </template>
      <template #useYn-filter="{ column, $panel }">
        <div style="padding: 5px">
          <a-select
            v-model:value="column.filters[0].data"
            style="width: 100px"
            @change="$panel.changeOption($event, !!column.filters[0].data, column.filters[0])">
            <a-select-option value="">ALL</a-select-option>
            <a-select-option :value="true">YES</a-select-option>
            <a-select-option :value="false">NO</a-select-option>
          </a-select>
        </div>
      </template>
    </Grid>
    <a-modal v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :label-col="{ span: 5 }"
          :rules="rules"
          :wrapper-col="{ span: 18 }"
          v-bind="formProps">
          <a-row>
            <a-col :span="24">
              <a-form-item :label="$t('system.views.dictGroup.title.dictCode')" name="dictCode">
                <a-input
                  v-model:value="formProps.model.dictCode"
                  :placeholder="$t('system.views.dictGroup.validate.dictCode')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item :label="$t('system.views.dictGroup.title.dictName')" name="dictName">
                <a-input
                  v-model:value="formProps.model.dictName"
                  :placeholder="$t('system.views.dictGroup.validate.dictName')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item :label="$t('common.table.seq')" name="seq">
                <a-input-number
                  v-model:value="formProps.model.seq"
                  style="width: 100%"
                  :placeholder="$t('common.formValidate.seq')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item :label="$t('common.table.useYn')" name="useYn">
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

import { Grid, Pager } from 'vxe-table'
import {
  Modal,
  Spin,
  Form,
  FormItem,
  Row,
  Col,
  Select,
  SelectOption,
  Switch,
  InputNumber,
} from 'ant-design-vue'

import { useVxeTable, useAddEdit, useVxeDelete } from '/@/hooks/page/CrudHooks'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'

import { handleGetById, handleSaveUpdate, handleDelete } from './DataDictGroupHook'
import ApiService from '/@/common/utils/ApiService'
import { tableUseYn } from '/@/components/common/TableCommon'

export default defineComponent({
  name: 'SysDictListView',
  components: {
    AModal: Modal,
    ASpin: Spin,
    AForm: Form,
    AFormItem: FormItem,
    ARow: Row,
    ACol: Col,
    ASelect: Select,
    ASelectOption: SelectOption,
    Grid,
    Pager,
    ASwitch: Switch,
    AInputNumber: InputNumber,
  },
  emits: ['code-change'],
  // @ts-ignore
  setup(props, { emit }) {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 加载数据
     * @param params 参数
     * @param searchParameter 查询参数
     */
    const handleLoadData = async (params: any, searchParameter: any) => {
      const parameter: any = {}
      Object.keys(searchParameter).forEach((key) => {
        let symbol = 'like'
        if (key === 'useYn') {
          symbol = '='
        }
        parameter[`${key}@${symbol}`] = searchParameter[key]
      })
      try {
        const result = await ApiService.postAjax('sys/dict/list', {
          ...params,
          parameter,
        })
        emit('code-change', null)
        return result
      } catch (e) {
        throw e
      }
    }

    /**
     * 查询数据hook
     */
    const { tableProps, loadData, handleReset, pageProps, searchModel } = useVxeTable(
      handleLoadData,
      {
        paging: true,
      },
    )

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'dictCode',
      defaultModel: {
        useYn: true,
        seq: 1,
      },
    })
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, {
      idField: 'dictCode',
      listHandler: loadData,
    })

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

    /**
     * 当前行发生变化事件
     */
    const handleCurrentChange = ({ row }: any) => {
      emit('code-change', row.dictCode)
    }
    onMounted(loadData)
    return {
      gridRef,
      ...useSizeSetting(),
      ...addEditHook,
      ...deleteHook,
      tableProps,
      loadData,
      handleReset,
      handleActions,
      pageProps,
      searchModel,
      handleFilterChange,
      handleCurrentChange,
    }
  },
  data() {
    return {
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
        },
      },
      filterConfig: {
        remote: true,
      },
      rules: {
        dictCode: [
          {
            required: true,
            message: this.$t('system.views.dictGroup.validate.dictCode'),
            trigger: ['blur'],
          },
        ],
        dictName: [
          {
            required: true,
            message: this.$t('system.views.dictGroup.validate.dictName'),
            trigger: ['blur'],
          },
        ],
        seq: [
          {
            required: true,
            message: this.$t('common.formValidate.seq'),
            trigger: ['blur'],
          },
        ],
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left',
        },
        {
          title: '{system.views.dictGroup.title.dictCode}',
          field: 'dictCode',
          width: 180,
          filters: [{ data: '' }],
          slots: {
            filter: 'dictCode-filter',
          },
          fixed: 'left',
        },
        {
          title: '{system.views.dictGroup.title.dictName}',
          field: 'dictName',
          minWidth: 180,
          filters: [{ data: '' }],
          slots: {
            filter: 'dictName-filter',
          },
        },
        {
          title: '{common.table.seq}',
          field: 'seq',
          sortable: true,
          width: 120,
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true,
          width: 120,
          filterMultiple: false,
          filters: [
            { label: 'YES', data: true },
            { label: 'NO', data: false },
          ],
        },
      ],
    }
  },
})
</script>
