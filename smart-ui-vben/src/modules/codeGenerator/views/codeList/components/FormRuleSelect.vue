<template>
  <a-modal :visible="visible" width="1000px" v-bind="$attrs" title="配置验证规则" @ok="handleOk">
    <Grid
      ref="gridRef"
      border
      :edit-rules="editValidRules"
      size="small"
      :data="rules"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      :edit-config="editConfig">
      <template #table-buttons>
        <a-button type="primary" @click="insertRow">添加一行</a-button>
      </template>
      <template #table-options="{ row }">
        <a-button @click="() => handleDeleteRow(row)">删除</a-button>
      </template>
    </Grid>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, ref, getCurrentInstance } from 'vue'
import type { PropType } from 'vue'
import type { VxeTableInstance, VxeTableDefines } from 'vxe-table'
import { message } from 'ant-design-vue'
import { Grid } from 'vxe-table'

import { ruleList } from './PageSettingSupport'

/**
 * 校验数据
 */
const validateData = (dataList: Array<any>) => {
  // 验证类型是否重复
  const hasType: Array<string> = []
  for (const { ruleType, pattern } of dataList) {
    if (hasType.includes(ruleType)) {
      return '校验类型不可重复：' + ruleType
    }
    hasType.push(ruleType)
    if (ruleType === 'regexp' && (pattern == null || pattern.trim() === '')) {
      return '正则类型必须设置正则表达式'
    }
  }
  return null
}

export default defineComponent({
  name: 'FormRuleSelect',
  components: {
    Grid,
  },
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    rules: {
      type: Array as PropType<Array<any>>,
      default: () => [],
    },
  },
  setup() {
    const gridRef = ref({} as VxeTableInstance)
    const { ctx } = getCurrentInstance() as any
    const data = ref([])
    /**
     * 插入行
     */
    const insertRow = async () => {
      const $grid = gridRef.value
      const record = {}
      const { row: newRow } = await $grid.insertAt(record, -1)
      await $grid.setActiveCell(newRow, 'ruleType')
    }
    /**
     * 删除行
     */
    const handleDeleteRow = (row: any) => {
      const $grid = gridRef.value
      $grid.remove(row)
    }
    /**
     * 确定事件
     */
    const handleOk = async () => {
      const $grid = gridRef.value
      // 校验表格
      const errMap: VxeTableDefines.ValidatorErrorMapParams = await $grid
        .fullValidate()
        .catch((errMap) => errMap)
      if (errMap) {
        message.error('校验不通过!')
        return false
      }
      const { tableData } = $grid.getTableData()
      // 关闭弹窗
      const errorMessage = validateData(tableData)
      if (errorMessage != null) {
        message.error(errorMessage)
        return false
      }
      ctx.$emit('update:visible', false)
      ctx.$emit('save', tableData)
    }
    return {
      insertRow,
      gridRef,
      handleOk,
      data,
      handleDeleteRow,
    }
  },
  data() {
    return {
      editConfig: {
        trigger: 'click',
        mode: 'row',
      },
      toolbarConfig: {
        slots: {
          tools: 'table-buttons',
        },
      },
      columns: [
        {
          title: '校验类型',
          field: 'ruleType',
          width: 140,
          editRender: {
            name: '$select',
            options: ruleList,
          },
        },
        {
          title: '触发时机',
          field: 'ruleTrigger',
          width: 150,
          editRender: {
            name: '$select',
            options: [
              {
                value: 'BLUR',
                label: 'blur',
              },
              {
                value: 'CHANGE',
                label: 'change',
              },
            ],
            props: {
              multiple: true,
            },
          },
        },
        {
          title: '长度',
          field: 'len',
          width: 120,
          editRender: {
            name: '$input',
            props: { type: 'number' },
          },
        },
        {
          title: '最大程度',
          field: 'max',
          width: 120,
          editRender: {
            name: '$input',
            props: { type: 'number' },
          },
        },
        {
          title: '最小长度',
          field: 'min',
          width: 120,
          editRender: {
            name: '$input',
            props: { type: 'number' },
          },
        },
        {
          title: '校验文案',
          field: 'message',
          minWidth: 200,
          editRender: {
            name: '$input',
          },
        },
        {
          title: '正则表达式',
          field: 'pattern',
          width: 180,
          editRender: {
            name: '$input',
          },
        },
        {
          title: '操作',
          field: 'options',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-options',
          },
        },
      ],
      editValidRules: {
        message: [{ required: true, message: '请填写校验文案' }],
        ruleTrigger: [{ required: true, message: '请选择触发时机' }],
        ruleType: [{ required: true, message: '校验类型必须选择' }],
      },
    }
  },
})
</script>

<style scoped></style>
