import type { SmartSearchFormProps, SmartTableAjaxQueryParams } from '/@/components/SmartTable'
import type { VxeGridInstance, VxeGridPropTypes } from 'vxe-table'
import type { ComputedRef, Ref } from 'vue'
import type { FetchParams, SmartTableProps, SmartTableProxyConfig } from '/@/components/SmartTable'

import { computed, createVNode, onMounted, unref } from 'vue'

import { message, Modal } from 'ant-design-vue'

import { isArray, isBoolean } from '/@/utils/is'
import { omit, merge } from 'lodash-es'
import { error } from '/@/utils/log'
import { useI18n } from '/@/hooks/web/useI18n'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

interface ActionType {
  commitVxeProxy: (code: string, ...args) => void
  getSearchFormModel: () => Recordable
  setLoading: (loading: boolean) => void
  getCheckboxRecords: (isFull: boolean) => Array<any>
}

export const useTableAjax = (
  propsRef: ComputedRef<SmartTableProps>,
  vxeGridRef: Ref<VxeGridInstance>,
  emit,
  { commitVxeProxy, getSearchFormModel, setLoading, getCheckboxRecords }: ActionType,
) => {
  const { t } = useI18n()
  // 是否自动加载，取消vxe-table自动加载，解决触发事件问题（事件无法在数据加载完成之后执行）
  onMounted(() => {
    if (unref(propsRef).proxyConfig?.autoLoad !== false) {
      reload()
    }
  })
  /**
   * 获取搜索符号
   */
  const getSearchFormSymbolRef = computed<Recordable | boolean>(() => {
    const { searchFormConfig, useSearchForm } = unref(propsRef)
    const searchWithSymbol = searchFormConfig?.searchWithSymbol
    if (!useSearchForm || !searchWithSymbol) {
      return false
    }
    const { schemas } = searchFormConfig as Partial<SmartSearchFormProps>
    const result: Recordable = {}
    schemas?.forEach(({ field, searchSymbol }) => {
      result[field] = searchSymbol || '='
    })
    return result
  })

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
          if (useSearchForm) {
            // 处理参数
            const searchForm = getSearchFormModel()
            const searchWithSymbol = unref(propsRef).searchFormConfig?.searchWithSymbol
            if (isBoolean(searchWithSymbol) && searchWithSymbol) {
              // 处理搜索符号
              const symbolForm = dealSearchSymbol(searchForm)
              searchParameter.searchFormSymbol = symbolForm
              Object.assign(ajaxParameter, {
                parameter: symbolForm,
              })
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
          return result
        },
      }
    }
    const sort = sortConfig?.remote === true
    return {
      autoLoad: false,
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

  const dealSearchSymbol = (info: Recordable) => {
    const result: Recordable = {}
    const getSearchFormSymbol = unref(getSearchFormSymbolRef)
    if (isBoolean(getSearchFormSymbol)) {
      return info
    }
    Object.keys(info).forEach((key) => {
      const value = info[key]
      const symbol = getSearchFormSymbol[key]
      if (symbol) {
        result[`${key}@${symbol}`] = value
      } else {
        result[key] = value
      }
    })
    return result
  }

  /**
   * 重载数据函数
   * @param opt
   */
  const reload = async (opt?: FetchParams) => {
    await commitVxeProxy('reload', opt)
    emit('after-load')
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
        try {
          setLoading(true)
          const result = await deleteMethod({
            $grid: unref(vxeGridRef),
            body: {
              removeRecords: rows,
            },
          })
          message.success(t('common.message.deleteSuccess'))
          const afterDelete = proxyConfig?.afterDelete || reload
          afterDelete && afterDelete(result)
          return Promise.resolve(true)
        } finally {
          setLoading(false)
        }
      },
    })
  }

  return {
    reload,
    getProxyConfigRef,
    deleteByRow,
    deleteByCheckbox,
  }
}
