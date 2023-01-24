import type { SmartTableProps } from '/@/components/SmartTable'

import { defineComponent, toRefs, unref } from 'vue'
import { propTypes } from '/@/utils/propTypes'

import { BasicModal, useModalInner } from '/@/components/Modal'
import { SmartTable } from '/@/components/SmartTable'

import { useSmartTableSelect } from '../../hooks/useSmartTableSelect'

export default defineComponent({
  name: 'SmartTableSelectModal',
  components: {
    BasicModal,
  },
  props: {
    tableProps: {
      type: Object as PropType<SmartTableProps>,
      required: true,
    },
    selectTableProps: {
      type: Object as PropType<Partial<SmartTableProps>>,
    },
    // 是否多选
    multiple: propTypes.bool.def(true),
    // 是否显示选中
    showSelect: propTypes.bool.def(false),
    // label字段
    labelField: propTypes.string.isRequired,
    // value字段
    valueField: propTypes.string.isRequired,
    selectValues: propTypes.array.def([]),
  },
  emits: ['register', 'select-data'],
  setup(props, { emit }) {
    const { tableProps, selectTableProps, valueField, selectValues } = toRefs(props)
    const {
      registerTable,
      handleCheckboxChange,
      registerSelectTable,
      selectRowsRef,
      setSelectData,
      getTableCheckboxConfig,
      handleCheckboxAll,
    } = useSmartTableSelect(
      tableProps,
      selectTableProps,
      props.showSelect,
      valueField,
      selectValues,
    )
    const [registerModal] = useModalInner((data) => {
      console.log(data)
    })

    const handleOk = () => {
      const selectOptions: LabelValueOptions = unref(selectRowsRef).map((item) => {
        return {
          label: item[props.labelField],
          value: item[props.valueField],
        }
      })
      emit('select-data', selectOptions)
    }

    return {
      registerModal,
      registerTable,
      setSelectData,
      handleCheckboxChange,
      registerSelectTable,
      selectRowsRef,
      handleOk,
      getTableCheckboxConfig,
      handleCheckboxAll,
    }
  },
  render() {
    const { $attrs, registerModal, $slots, setSelectData, handleOk } = this
    return (
      <BasicModal {...$attrs} onRegister={registerModal} onOk={handleOk}>
        {$slots.table ? $slots.table({ setSelectData }) : renderTable(this)}
      </BasicModal>
    )
  },
})

const renderTable = (instance) => {
  const {
    $attrs,
    showSelect,
    registerTable,
    handleCheckboxChange,
    registerSelectTable,
    selectRowsRef,
    getTableCheckboxConfig,
    handleCheckboxAll,
  } = instance
  return (
    <a-row>
      <a-col span={showSelect ? 12 : 24}>
        <SmartTable
          onRegister={registerTable}
          checkboxConfig={getTableCheckboxConfig}
          onCheckboxChange={handleCheckboxChange}
          onCheckboxAll={handleCheckboxAll}
          {...$attrs}
        />
      </a-col>
      {showSelect ? (
        <a-col span={12}>
          <SmartTable data={selectRowsRef} onRegister={registerSelectTable} />
        </a-col>
      ) : (
        ''
      )}
    </a-row>
  )
}
