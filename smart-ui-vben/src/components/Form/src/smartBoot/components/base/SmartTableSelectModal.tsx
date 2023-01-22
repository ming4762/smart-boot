import type { SmartTableProps } from '/@/components/SmartTable'

import { defineComponent } from 'vue'
import { propTypes } from '/@/utils/propTypes'

import { BasicModal, useModalInner } from '/@/components/Modal'
import { SmartTable, useSmartTable } from '/@/components/SmartTable'

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
    // 是否多选
    multiple: propTypes.bool.def(true),
  },
  emits: ['register'],
  setup(props, { slots }) {
    const [registerModal] = useModalInner((data) => {
      console.log(data)
    })
    let registerTable: any = null
    if (!slots.table) {
      const smartTable = useSmartTable(props.tableProps)
      registerTable = smartTable[0]
    }
    const setSelectData = (data) => {
      console.log(data)
    }
    return {
      registerModal,
      registerTable,
      setSelectData,
    }
  },
  render() {
    const { $attrs, registerModal, $slots, registerTable, setSelectData } = this
    return (
      <BasicModal {...$attrs} onRegister={registerModal}>
        {$slots.table ? (
          $slots.table({ setSelectData })
        ) : (
          <SmartTable onRegister={registerTable} {...$attrs} />
        )}
      </BasicModal>
    )
  },
})
