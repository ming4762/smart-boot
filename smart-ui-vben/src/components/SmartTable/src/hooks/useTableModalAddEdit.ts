import type { ComputedRef, Ref } from 'vue'
import type { SmartAddEditModalCallbackData, SmartTableProps } from '/@/components/SmartTable'
import type { FormProps } from '/@/components/Form'
import type { ModalProps } from '/@/components/Modal'

import { computed, ref, unref } from 'vue'
import { useI18n } from '/@/hooks/web/useI18n'
import { message } from 'ant-design-vue'

interface TableAction {
  getCheckboxRecords: (isFull: boolean) => Array<any>
  openAddEditModal: <T = any>(props?: boolean, data?: T, openOnSet?: boolean) => void
  reload: (parameter?) => Promise<void>
}

export const useTableModalAddEditConfig = (
  tableProps: ComputedRef<SmartTableProps>,
  { getCheckboxRecords, openAddEditModal, reload }: TableAction,
) => {
  const { t } = useI18n()
  const isAddRef = ref(true)

  /**
   * 是否有添加修改弹窗
   */
  const getHasAddEdit = computed<boolean>(() => {
    return unref(tableProps).addEditConfig !== undefined
  })

  /**
   * 添加修改弹窗props
   */
  const getAddEditFormProps = computed<FormProps>(() => {
    const formConfig = unref(tableProps).addEditConfig?.formConfig || {}
    return {
      ...getDefaultFormConfig(),
      ...formConfig,
    }
  })

  /**
   * 添加修改弹窗props
   */
  const getAddEditModalProps = computed<Partial<ModalProps>>(() => {
    const tablePropsData = unref(tableProps)
    const { modalConfig, beforeSave, afterSave } = tablePropsData.addEditConfig!
    const saveFunction = tablePropsData.proxyConfig?.ajax?.save
    return {
      ...getDefaultModalConfig(isAddRef, t),
      ...(modalConfig || {}),
      beforeSave,
      saveFunction,
      afterSave:
        afterSave ||
        (() => {
          reload()
          return true
        }),
    }
  })

  const showAddModal = () => {
    if (!unref(getHasAddEdit)) {
      throw new Error('addEditConfig未定义')
    }
    isAddRef.value = true
    openAddEditModal(true, getCallbackData(null, true))
  }

  const editByRow = (row) => {
    return doEdit(row)
  }

  const editByCheckbox = () => {
    const selectRows = getCheckboxRecords(false)
    if (selectRows.length !== 1) {
      message.warn(t('common.notice.choseOne'))
      return false
    }
    const editRow = selectRows[0]
    return doEdit(editRow)
  }

  const doEdit = async (row) => {
    if (!unref(getHasAddEdit)) {
      throw new Error('addEditConfig未定义')
    }
    isAddRef.value = false
    openAddEditModal(true, getCallbackData(row, false))
    return true
  }

  const getCallbackData = (
    selectData: Recordable | null,
    isAdd: boolean,
  ): SmartAddEditModalCallbackData => {
    const getByIdFunction = unref(tableProps)?.proxyConfig?.ajax?.getById
    if (!getByIdFunction) {
      throw new Error('proxyConfig.ajax.getById未设置')
    }
    return {
      getFunction: getByIdFunction,
      isAdd,
      selectData,
      validateFunction: unref(tableProps)?.addEditConfig?.afterLoadData,
    }
  }

  return {
    showAddModal,
    editByCheckbox,
    getHasAddEdit,
    getAddEditFormProps,
    getAddEditModalProps,
    editByRow,
  }
}

const getDefaultFormConfig = (): Partial<FormProps> => {
  return {
    showActionButtonGroup: false,
    labelCol: {
      span: 5,
    },
    wrapperCol: {
      span: 19,
    },
    baseColProps: {
      span: 24,
      xxl: 12,
    },
  }
}

const getDefaultModalConfig = (isAddRef: Ref<boolean>, t: Function): Partial<ModalProps> => {
  const title = unref(isAddRef) ? t('common.title.add') : t('common.title.edit')
  return {
    title,
  }
}
