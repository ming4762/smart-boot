import { ref, watch, Ref } from 'vue'

/**
 * 空间列表
 */
export const controlList = [
  {
    key: 'INPUT',
    value: 'generator.views.code.title.controlList.input'
  },
  {
    key: 'TEXTAREA',
    value: 'generator.views.code.title.controlList.textarea'
  },
  {
    key: 'NUMBER',
    value: 'generator.views.code.title.controlList.number'
  },
  {
    key: 'PASSWORD',
    value: 'generator.views.code.title.controlList.password'
  },
  {
    key: 'SELECT',
    value: 'generator.views.code.title.controlList.select'
  },
  {
    key: 'TRANSFER',
    value: 'generator.views.code.title.controlList.transfer'
  },
  {
    key: 'SELECT_TABLE',
    value: 'generator.views.code.title.controlList.selectTable'
  },
  {
    key: 'RADIO',
    value: 'generator.views.code.title.controlList.radio'
  },
  {
    key: 'CHECKBOX',
    value: 'generator.views.code.title.controlList.checkbox'
  },
  {
    key: 'SWITCH_TYPE',
    value: 'generator.views.code.title.controlList.switch_type'
  },
  {
    key: 'DATE',
    value: 'generator.views.code.title.controlList.date'
  },
  {
    key: 'TIME',
    value: 'generator.views.code.title.controlList.time'
  },
  {
    key: 'DATETIME',
    value: 'generator.views.code.title.controlList.datetime'
  },
  {
    key: 'FILE',
    value: 'generator.views.code.title.controlList.file'
  }
]

/**
 * rule列表
 */
export const ruleList = [
  {
    value: 'notEmpty',
    label: 'generator.views.code.title.ruleList.notEmpty'
  },
  {
    value: 'PHONE',
    label: 'generator.views.code.title.ruleList.PHONE'
  },
  {
    value: 'EMAIL',
    label: 'generator.views.code.title.ruleList.EMAIL'
  },
  {
    value: 'NUMBER',
    label: 'generator.views.code.title.ruleList.NUMBER'
  },
  {
    value: 'REGEXP',
    label: 'generator.views.code.title.ruleList.REGEXP'
  }
]

/**
 * 查询标识列表
 */
export const searchSymbolList = ['=', 'like', '>', '>=', '<', '<=', 'in', 'notIn', 'notLike', 'likeLeft', 'likeRight']

/**
 * table header checkbox
 * @param tableData
 * @param field
 * @param defaultValue
 */
export const vueTableHeaderCheckboxSupport = (tableData: Ref, field: string, defaultValue = true) => {
  const checked = ref(defaultValue)
  watch(checked, () => {
    tableData.value.forEach((item: any) => {
      item[field] = checked.value
    })
  })
  return {
    checked
  }
}

/**
 * 下拉表格支持
 */
export const vueChoseSelectTableSupport = (currentRow: Ref) => {
  const choseAddendumModalVisible = ref(false)
  /**
   * 显示列选择
   * @param row
   */
  const handleShowChoseSelectTable = (row: any) => {
    currentRow.value = row
    choseAddendumModalVisible.value = true
  }
  /**
   * 选择表格后
   * @param tableList
   */
  const handleChoseTable = (tableList: Array<any>) => {
    currentRow.value.selectTableList = tableList
  }
  return {
    choseAddendumModalVisible,
    handleShowChoseSelectTable,
    handleChoseTable
  }
}

/**
 * 设置验证规则
 */
export const vueSetRulesSupport = (currentRow: Ref) => {
  // 设置验证规则modal状态
  const setRuleModalVisible = ref(false)
  /**
   * 显示这是规则页面
   * @param row 当前行
   */
  const handleShowSetRules = (row: any) => {
    setRuleModalVisible.value = true
    currentRow.value = row
  }
  /**
   * 点击OK
   * @param dataList
   */
  const handleSave = (dataList: Array<any>) => {
    currentRow.value.ruleList = dataList
  }
  return {
    handleShowSetRules,
    setRuleModalVisible,
    handleSave
  }
}

export default {}
