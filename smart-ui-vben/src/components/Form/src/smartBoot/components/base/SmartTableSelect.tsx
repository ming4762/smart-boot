import type { SmartTableProps } from '/@/components/SmartTable'

import { defineComponent, ref } from 'vue'

import { propTypes } from '/@/utils/propTypes'
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
  emits: ['update:value', 'change'],
  setup(props, { emit }) {
    const [registerModal, { openModal }] = useModal()
    const optionsRef = ref<Array<any>>([])
    const handleSelectData = (options: any[], selectRows: any[]) => {
      optionsRef.value = options
      emit(
        'update:value',
        options.map((item) => item.value),
      )
      emit('change', selectRows)
    }
    const handleDeselect = (value) => {
      emit(
        'update:value',
        (props.value as any[]).filter((item) => item !== value),
      )
    }
    return {
      registerModal,
      openModal,
      handleSelectData,
      optionsRef,
      handleDeselect,
    }
  },
  render() {
    const {
      $attrs,
      multiple,
      tableProps,
      $slots,
      disabled,
      $t,
      openModal,
      registerModal,
      labelField,
      valueField,
      handleSelectData,
      optionsRef,
      value,
      handleDeselect,
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
              options={optionsRef}
              open={false}
              value={value}
              onDeselect={handleDeselect}
              mode={multiple ? 'multiple' : 'combobox'}></a-select>
          </a-col>
          <a-col class="button">
            <a-button
              disabled={disabled}
              type="primary"
              onClick={() => openModal(true, value || {})}>
              {$t('common.button.choose')}
            </a-button>
          </a-col>
        </a-row>
        <SmartTableSelectModal
          {...$attrs}
          onRegister={registerModal}
          labelField={labelField}
          onSelectData={handleSelectData}
          valueField={valueField}
          // @ts-ignore
          selectValues={value}
          multiple={multiple}
          tableProps={tableProps}>
          {modalSlots}
        </SmartTableSelectModal>
      </div>
    )
  },
})
