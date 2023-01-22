import type { SmartTableProps } from '/@/components/SmartTable'

import { defineComponent } from 'vue'

import { propTypes } from '/@/utils/propTypes'
import { useSmartTableSelect } from '../../hooks/useSmartTableSelect'
import { useModal } from '/@/components/Modal'

import SmartTableSelectModal from './SmartTableSelectModal'

import './SmartTableSelect.less'

export default defineComponent({
  name: 'SmartTableSelect',
  components: {
    SmartTableSelectModal,
  },
  props: {
    // 是否支持多选
    multiple: propTypes.bool.def(true),
    value: propTypes.oneOfType([propTypes.string, propTypes.array]),
    // label字段
    labelField: propTypes.string.isRequired,
    // value字段
    valueField: propTypes.string.isRequired,
    tableProps: {
      type: Object as PropType<SmartTableProps>,
      required: true,
    },
    disabled: propTypes.bool.def(false),
  },
  setup() {
    const { selectValues } = useSmartTableSelect()
    const [registerModal, { openModal }] = useModal()
    return {
      selectValues,
      registerModal,
      openModal,
    }
  },
  render() {
    const {
      selectValues,
      $attrs,
      multiple,
      tableProps,
      $slots,
      disabled,
      $t,
      openModal,
      registerModal,
    } = this
    const modalSlots: any = {
      table: $slots.table,
    }
    return (
      <div class="smart-table-select">
        <a-row type="flex" gutter={8}>
          <a-col class="select">
            <a-select
              {...$attrs}
              style={{ width: '100%' }}
              open={false}
              value={selectValues.value}
              model={multiple ? 'multiple' : 'combobox'}></a-select>
          </a-col>
          <a-col class="button">
            <a-button disabled={disabled} type="primary" onClick={() => openModal(true)}>
              {$t('common.button.choose')}
            </a-button>
          </a-col>
        </a-row>
        <SmartTableSelectModal
          {...$attrs}
          onRegister={registerModal}
          multiple={multiple}
          tableProps={tableProps}>
          {modalSlots}
        </SmartTableSelectModal>
      </div>
    )
  },
})
