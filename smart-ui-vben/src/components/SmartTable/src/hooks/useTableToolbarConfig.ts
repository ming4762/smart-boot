import type { ComputedRef } from 'vue'
import type { ButtonProps } from '/@/components/Button'

import type { SmartTableProps, SmartTableToolbarConfig } from '../types/SmartTableType'
import type { SmartTableButton, SmartTableToolbarTool } from '../types/SmartTableButton'
import type { SizeType } from 'ant-design-vue/es/config-provider'
import type { VxeToolbarPropTypes } from 'vxe-table'
import type { FetchParams } from '../types/SmartTableType'

import { computed, ref, unref } from 'vue'

import { merge } from 'lodash-es'
import { isBoolean, isPromise } from '/@/utils/is'

import {
  VxeTableToolButtonAntRenderer,
  VxeTableToolAntRenderer,
} from '../renderer/VxeTableButtonRenderer'
import { SmartTableCode } from '../const'

const tableButtonSizeMap: { [key: string]: SizeType } = {
  medium: 'middle',
  small: 'small',
  mini: 'small',
}

interface TableAction {
  deleteByCheckbox: () => Promise<boolean | undefined>
  showAddModal: () => void
  editByCheckbox: () => Promise<boolean> | boolean
  query: (opt?: FetchParams) => Promise<any>
  getSearchFormVisible: ComputedRef<boolean>
}

export const useTableToolbarConfig = (
  tableProps: ComputedRef<SmartTableProps>,
  t: Function,
  { deleteByCheckbox, showAddModal, editByCheckbox, query, getSearchFormVisible }: TableAction,
) => {
  // const configRef = ref<SmartTableToolbarConfig>({})

  const getToolbarConfigInfo = computed<SmartTableToolbarConfig | undefined>(() => {
    const { toolbarConfig, size: tableSize } = unref(tableProps)
    if (!toolbarConfig) {
      return undefined
    }
    const buttons = dealButtons(toolbarConfig.buttons, tableSize)
    let refresh = toolbarConfig.refresh
    if (refresh) {
      refresh = getDefaultRefresh()
    }
    return {
      ...toolbarConfig,
      buttons,
      refresh,
      tools: getTools(toolbarConfig),
    }
  })

  const getTools = (toolbarConfig: SmartTableToolbarConfig) => {
    const { tools, showSearch } = toolbarConfig
    if (!tools && !showSearch) {
      return undefined
    }
    const result: SmartTableToolbarTool[] = [...(tools || [])]
    if (showSearch && unref(tableProps).useSearchForm) {
      if (isBoolean(showSearch)) {
        const props = computed(() => {
          return {
            circle: true,
            icon: 'vxe-icon-search',
            title: unref(getSearchFormVisible)
              ? t('component.table.hideSearch')
              : t('component.table.showSearch'),
          }
        })
        result.push({
          code: SmartTableCode.showSearch,
          toolRender: {
            name: VxeTableToolAntRenderer,
          },
          props,
        })
      } else {
        result.push(showSearch)
      }
    }
    return result
  }

  const getDefaultRefresh = (): VxeToolbarPropTypes.RefreshConfig => {
    return {
      queryMethod: (params) => {
        return query(params)
      },
    }
  }

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
      // props添加响应性
      const loading = ref(false)
      const props = computed<ButtonProps>(() => {
        const buttonProps = unref(item.props) as ButtonProps | undefined
        const result: ButtonProps = {
          ...buttonProps,
        }
        // 点击事件加载状态添加操作
        if (item.clickLoading && buttonProps?.loading === undefined) {
          result.loading = unref(loading)
          const defaultClickHandler = buttonProps?.onClick
          if (defaultClickHandler) {
            result.onClick = async () => {
              try {
                loading.value = true
                const handlerResult = defaultClickHandler()
                if (isPromise(handlerResult)) {
                  await handlerResult
                }
              } finally {
                loading.value = false
              }
            }
          }
        }
        return result
      })
      // 如果是ant 按钮使用VxeTableToolButtonRenderer进行渲染
      if (item.isAnt) {
        return {
          size: tableButtonSizeMap[tableSize],
          buttonRender: {
            name: VxeTableToolButtonAntRenderer,
          },
          ...item,
          props,
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
      name: VxeTableToolButtonAntRenderer,
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
      name: VxeTableToolButtonAntRenderer,
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
      name: VxeTableToolButtonAntRenderer,
    },
  }
}
