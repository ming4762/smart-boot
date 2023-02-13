import type { SmartTableAjaxQueryParams } from '/@/components/SmartTable'
import type { VxeGridInstance, VxeGridPropTypes } from 'vxe-table'
import type { ComputedRef, Ref } from 'vue'
import type { FetchParams, SmartTableProps, SmartTableProxyConfig } from '/@/components/SmartTable'

import { computed, createVNode, unref } from 'vue'

import { message, Modal } from 'ant-design-vue'

import { isArray } from '/@/utils/is'
import { omit, merge } from 'lodash-es'
import { error } from '/@/utils/log'
import { useI18n } from '/@/hooks/web/useI18n'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import type { SmartSearchFormParameter } from '/@/components/SmartTable'

interface ActionType {
  commitVxeProxy: (code: string, ...args) => void
  getSearchFormParameter: () => SmartSearchFormParameter
  setLoading: (loading: boolean) => void
  getCheckboxRecords: (isFull: boolean) => Array<any>
}

export const useTableAjax = (
  propsRef: ComputedRef<SmartTableProps>,
  vxeGridRef: Ref<VxeGridInstance>,
  emit,
  { commitVxeProxy, getSearchFormParameter, setLoading, getCheckboxRecords }: ActionType,
) => {
  const { t } = useI18n()

  const getProxyConfigRef = computed<SmartTableProxyConfig | undefined>(() => {
    const { proxyConfig, useSearchForm, sortConfig } = unref(propsRef)
    if (!proxyConfig) {
      return
    }
    const ajax = proxyConfig.ajax || {}
    let queryAjax: any = {}
    if (ajax.query) {
      queryAjax = {
        query: async (params, args) => {
          const { form, filters, page, sorts, sort, $grid } =
            params as VxeGridPropTypes.ProxyAjaxQueryParams
          let fetchParams: FetchParams = {}
          if (args && args.length > 0) {
            fetchParams = args[0]
          }
          const searchParameter: SmartTableAjaxQueryParams = merge(
            {
              $grid,
              form,
              filters,
              page,
              sorts,
              sort,
            },
            fetchParams,
          )
          let ajaxParameter: Recordable = {
            ...form,
            ...page,
          }
          if (sorts.length > 0) {
            const sortNameList: string[] = []
            const sortOrderList: string[] = []
            for (const item of sorts) {
              sortNameList.push(item.field)
              sortOrderList.push(item.order)
            }
            ajaxParameter.sortName = sortNameList.join(',')
            ajaxParameter.sortOrder = sortOrderList.join(',')
          }
          const { searchForm, searchSymbolForm, noSymbolForm, searchWithSymbol } =
            getSearchFormParameter()
          if (useSearchForm) {
            if (searchWithSymbol) {
              // 处理搜索符号
              searchParameter.searchFormSymbol = searchSymbolForm
              Object.assign(
                ajaxParameter,
                {
                  parameter: searchSymbolForm,
                },
                noSymbolForm,
              )
            } else {
              Object.assign(ajaxParameter, searchForm)
            }
            searchParameter.searchForm = searchForm
          }
          ajaxParameter = omit(ajaxParameter, ['total'])
          ajaxParameter = merge(ajaxParameter, searchParameter.searchInfo)
          // 添加额外的查询条件
          searchParameter.ajaxParameter = ajaxParameter
          let result = await ajax.query!(searchParameter)
          if (proxyConfig.afterLoad) {
            result = proxyConfig.afterLoad(result)
          }
          let tableData
          if (isArray(result)) {
            tableData = result
          } else {
            tableData = result[proxyConfig.props?.result || 'rows']
          }
          emit('after-load', tableData)
          return result
        },
      }
    }
    const sort = sortConfig?.remote === true
    return {
      // autoLoad: false,
      sort,
      props: {
        result: 'rows',
        total: 'total',
      },
      ...proxyConfig,
      ajax: {
        ...ajax,
        ...queryAjax,
      },
    }
  })

  /**
   * 重载数据函数
   * @param opt
   */
  const reload = async (opt?: FetchParams) => {
    try {
      setLoading(true)
      await commitVxeProxy('reload', opt)
      emit('proxy-query', { status: true, isReload: true, isInited: false })
    } finally {
      setLoading(false)
    }
  }

  const query = async (opt?: FetchParams) => {
    try {
      setLoading(true)
      await commitVxeProxy('query', opt)
      emit('proxy-query', { status: true, isReload: false, isInited: false })
    } finally {
      setLoading(false)
    }
  }

  /**
   * 通过checkbox删除
   */
  const deleteByCheckbox = async () => {
    const rows = getCheckboxRecords(false)
    if (!rows.length) {
      message.warn(t('common.notice.deleteChoose'))
      return false
    }
    return await doDelete(rows)
  }

  const deleteByRow = (row: any | any[]) => {
    if (isArray(row)) {
      return doDelete(row)
    }
    return doDelete([row])
  }

  const doDelete = async (rows: any[]): Promise<boolean | undefined> => {
    const proxyConfig = unref(propsRef)?.proxyConfig
    const deleteMethod = proxyConfig?.ajax?.delete
    if (!deleteMethod) {
      error('proxyConfig.ajax.delete未配置，无法删除')
      return false
    }
    if (rows.length === 0) {
      return false
    }
    Modal.confirm({
      title: t('common.button.confirm'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('common.notice.deleteConfirm'),
      onOk: async () => {
        const result = await deleteMethod({
          $grid: unref(vxeGridRef),
          body: {
            removeRecords: rows,
          },
        })
        message.success(t('common.message.deleteSuccess'))
        const afterDelete = proxyConfig?.afterDelete || query
        afterDelete && afterDelete(result)
        return Promise.resolve(true)
      },
    })
  }

  return {
    reload,
    query,
    getProxyConfigRef,
    deleteByRow,
    deleteByCheckbox,
  }
}
