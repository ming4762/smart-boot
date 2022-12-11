<template>
  <a-modal
    :title="$t('generator.views.codeCreateForm.title.choseAddendum')"
    width="920px"
    :visible="visible"
    :size="tableSizeConfig"
    v-bind="$attrs"
    @ok="handleOk">
    <Grid
      ref="tableRef"
      :data="data"
      :loading="dataLoading"
      :checkbox-config="{}"
      :toolbar-config="{ slots: { buttons: 'toolbarConfigButton' } }"
      :columns="computedColumns">
      <template #toolbarConfigButton>
        <a-form layout="inline">
          <a-form-item :label="$t('generator.views.code.table.configName')">
            <a-input v-model:value="searchModel.configName" :size="formSizeConfig" />
          </a-form-item>
          <a-form-item :label="$t('generator.views.code.table.tableName')">
            <a-input v-model:value="searchModel.tableName" :size="formSizeConfig" />
          </a-form-item>
          <a-form-item>
            <a-button :size="buttonSizeConfig" type="primary">
              {{ $t('common.button.search') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #table-relatedColumn="{ row }">
        <a-input v-model:value="row.relatedColumn" :size="formSizeConfig" />
      </template>
    </Grid>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, ref, getCurrentInstance, watch, toRefs, computed } from 'vue'
import type { PropType } from 'vue'
import { useI18n } from 'vue-i18n'

import { Form, FormItem, Input, message, Modal } from 'ant-design-vue'
import { Grid } from 'vxe-table'

import ApiService from '/@/common/utils/ApiService'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'

const columns = [
  {
    title: '{generator.views.code.table.connectionName}',
    field: 'connectionName',
    width: 120,
  },
  {
    title: '{generator.views.code.table.configName}',
    field: 'configName',
    width: 120,
  },
  {
    title: '{generator.views.code.table.tableName}',
    field: 'tableName',
    width: 120,
  },
  {
    title: '{generator.views.addendumTable.title.relatedColumn}',
    field: 'relatedColumn',
    width: 120,
    slots: {
      default: 'table-relatedColumn',
    },
  },
  {
    title: '{generator.views.code.table.remarks}',
    field: 'remarks',
    minWidth: 120,
  },
  {
    title: '{common.table.remark}',
    field: 'remark',
    minWidth: 200,
  },
]

/**
 * 附表选择组件
 */
export default defineComponent({
  name: 'PageAddendumTableChose',
  components: {
    Grid,
    AForm: Form,
    AFormItem: FormItem,
    AInput: Input,
    AModal: Modal,
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    multiple: {
      type: Boolean,
      default: true,
    },
    selectTableList: {
      type: Array as PropType<Array<any>>,
      default: () => [],
    },
  },
  setup(props) {
    const { visible, multiple, selectTableList } = toRefs(props)
    const { t } = useI18n()
    const { ctx } = getCurrentInstance() as any
    const data = ref<Array<any>>([])
    const dataLoading = ref(false)
    const sizeConfigHoops = useSizeSetting()
    const tableRef = ref()
    const searchModel = {
      configName: '',
      tableName: '',
    }
    const loadData = async () => {
      dataLoading.value = true
      try {
        data.value = await ApiService.postAjax('db/code/main/list', {
          parameter: {
            'type@=': '30',
          },
        })
      } finally {
        dataLoading.value = false
      }
      // 设置选中
      if (selectTableList.value.length > 0) {
        const selectTableMap: Map<number, any> = new Map<number, any>()
        selectTableList.value.forEach((item) => {
          selectTableMap.set(item.addendumId, item)
        })
        // 选中的行
        const selectRowsList: Array<any> = []
        data.value.forEach((item) => {
          if (selectTableMap.has(item.id)) {
            selectRowsList.push(item)
            item.relatedColumn = selectTableMap.get(item.id).relatedColumn
          }
        })
        if (selectRowsList.length > 0) {
          if (!multiple.value) {
            tableRef.value.setRadioRow(selectRowsList[0])
          } else {
            tableRef.value.setCheckboxRow(selectRowsList, true)
          }
        }
      }
    }
    watch(visible, () => {
      if (visible.value) {
        loadData()
      }
    })
    // 表格列计算属性
    const computedColumns = computed(() => {
      const firstColumn = {
        type: 'checkbox',
        width: 60,
      }
      if (!multiple.value) {
        firstColumn.type = 'radio'
      }
      return [firstColumn, ...columns]
    })
    /**
     * OK状态
     */
    const handleOk = () => {
      const data: any[] = []
      // 获取选中的行
      if (multiple.value) {
        data.push(...tableRef.value.getCheckboxRecords())
      } else {
        const radioRecord = tableRef.value.getRadioRecord()
        if (radioRecord) {
          data.push(radioRecord)
        }
      }
      // 验证数据
      const errorDataList = data.filter(
        (item) =>
          item.relatedColumn === undefined ||
          item.relatedColumn == null ||
          item.relatedColumn.trim() === '',
      )
      if (errorDataList.length > 0) {
        errorDataList.forEach((item) => {
          message.error(
            t('generator.views.addendumTable.validate.relatedColumnWithConfig', item.configName),
          )
        })
        return false
      }
      const dealData = data.map((item) => {
        return {
          addendumId: item.id,
          relatedColumn: item.relatedColumn,
          configName: item.configName,
        }
      })
      ctx.$emit('chose', dealData)
      ctx.$emit('update:visible', false)
    }
    return {
      data,
      handleOk,
      searchModel,
      computedColumns,
      dataLoading,
      tableRef,
      ...sizeConfigHoops,
    }
  },
  data() {
    return {
      rules: {
        relatedColumn: [
          {
            required: true,
            message: this.$t('generator.views.addendumTable.validate.relatedColumn'),
            trigger: 'blur',
          },
        ],
      },
    }
  },
})
</script>

<style scoped></style>
