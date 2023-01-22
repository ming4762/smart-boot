import type { ComputedRef } from 'vue'

import type { SmartTableProps, SmartTableToolbarConfig } from '../types/SmartTableType'
import type { SmartTableButton } from '../types/SmartTableButton'
import type { SizeType } from 'ant-design-vue/es/config-provider'

import { computed, unref } from 'vue'

import { merge } from 'lodash-es'
import { useI18n } from '/@/hooks/web/useI18n'

const tableButtonSizeMap: { [key: string]: SizeType } = {
  medium: 'middle',
  small: 'small',
  mini: 'small',
}

interface TableAction {
  deleteByCheckbox: () => Promise<boolean | undefined>
  showAddModal: () => void
  editByCheckbox: () => Promise<boolean> | boolean
}

export const useTableToolbarConfig = (
  tableProps: ComputedRef<SmartTableProps>,
  { deleteByCheckbox, showAddModal, editByCheckbox }: TableAction,
) => {
  const { t } = useI18n()

  // const configRef = ref<SmartTableToolbarConfig>({})

  const getToolbarConfigInfo = computed<SmartTableToolbarConfig | undefined>(() => {
    const { toolbarConfig, size: tableSize } = unref(tableProps)
    if (!toolbarConfig) {
      return undefined
    }
    const buttons = dealButtons(toolbarConfig.buttons, tableSize)
    return {
      ...toolbarConfig,
      buttons,
    }
  })

  const dealButtons = (
    buttons: SmartTableButton[] | undefined,
    tableSize,
  ): SmartTableButton[] | undefined => {
    if (!buttons) {
      return undefined
    }
    return buttons.map((item) => {
      if (item.code === 'ModalAdd') {
        // 添加按钮处理
        const defaultConfig = getDefaultAddButtonConfig(t)
        return merge(
          { size: tableButtonSizeMap[tableSize] },
          defaultConfig,
          {
            props: {
              onClick: () => {
                showAddModal()
              },
            },
          },
          item,
        ) as SmartTableButton
      } else if (item.code == 'ModalEdit') {
        return merge(
          { size: tableButtonSizeMap[tableSize] },
          getDefaultEditButtonConfig(t),
          {
            props: {
              onClick: () => {
                editByCheckbox()
              },
            },
          },
          item,
        ) as SmartTableButton
      } else if (item.code === 'delete') {
        return merge(
          { size: tableButtonSizeMap[tableSize] },
          getDefaultDeleteButtonConfig(t),
          {
            props: {
              onClick: () => {
                deleteByCheckbox && deleteByCheckbox()
              },
            },
          },
          item,
        ) as SmartTableButton
      }
      if (item.isAnt) {
        item.buttonRender = {
          name: 'VxeTableToolButtonRenderer',
        }
      }
      return item
    })
  }

  return {
    getToolbarConfigInfo,
  }
}

const getDefaultAddButtonConfig = (t: Function): SmartTableButton => {
  return {
    name: t('common.button.add'),
    code: 'ModalAdd',
    props: {
      preIcon: 'ant-design:plus-outlined',
      type: 'primary',
    },
    buttonRender: {
      name: 'VxeTableToolButtonRenderer',
    },
  }
}

const getDefaultEditButtonConfig = (t: Function): SmartTableButton => {
  return {
    name: t('common.button.edit'),
    code: 'ModalEdit',
    props: {
      color: 'warning',
      preIcon: 'ant-design:edit-outlined',
      type: 'default',
    },
    buttonRender: {
      name: 'VxeTableToolButtonRenderer',
    },
  }
}

const getDefaultDeleteButtonConfig = (t: Function): SmartTableButton => {
  return {
    name: t('common.button.delete'),
    code: 'delete',
    props: {
      danger: true,
      preIcon: 'ant-design:edit-outlined',
      type: 'primary',
    },
    buttonRender: {
      name: 'VxeTableToolButtonRenderer',
    },
  }
}
