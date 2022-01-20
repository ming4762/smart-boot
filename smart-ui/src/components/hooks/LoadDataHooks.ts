import { ref, reactive, computed } from 'vue'

type Params = { page?: number; limit?: number, sortName?: string, sortOrder?: string }

type Data = { total: number, rows: Array<any> } | Array<any>

/**
 * 参数类型
 */
type Parameter = {
  defaultPageSize?: number;
  paging: boolean,
  defaultSorter?: { sortName: string, sortOrder: string },
  // 搜索参数
  defaultParameter?: {[index: string]: any}
}

/**
 * VXE TABLE加载数据hook
 * @param service 加载数据服务
 * @param parameter hook 参数
 */
export const useVxeTable = (service: (params: Params, searchParameter: any) => Promise<Data>, parameter: Parameter = { paging: true } ) => {
  // 数据加载状态
  const loading = ref(false)
  // 表格数据
  const data = ref<Array<any>>([])
  // 分页数据
  const tablePage: any = parameter.paging ? reactive({
    total: 0,
    currentPage: 1,
    pageSize: parameter.defaultPageSize || 500
  }) : {}
  const searchModel = ref<any>(parameter.defaultParameter || {})
  // 排序数据
  const sortData: any = reactive(parameter.defaultSorter || {})
  const sortConfig: any = {
    remote: true
  }
  if (parameter.defaultSorter) {
    sortConfig.defaultSort = {
      field: parameter.defaultSorter.sortName,
      order: parameter.defaultSorter.sortOrder
    }
  }

  /**
   * 加载数据函数
   */
  const loadData = async () => {
    const allParameter: any = {
      ...sortData
    }
    if (parameter.paging) {
      // 添加分页数据
      Object.assign(allParameter, {
        limit: tablePage.pageSize,
        page: tablePage.currentPage
      })
    }
    loading.value = true
    try {
      const result = await service(allParameter, searchModel.value)
      if (parameter.paging) {
        const { total, rows } = result as any
        tablePage.total = total
        data.value = rows
      } else {
        data.value = result as Array<any>
      }
    } catch (e) {
      // do nothing
    } finally {
      loading.value = false
    }
  }
  /**
   * 重置擦欧总
   */
  const handleReset = () => {
    searchModel.value = parameter.defaultParameter || {}
    loadData()
  }

  /**
   * 排序变化时触发
   * @param property
   * @param order
   */
  const sortChange = ({ property, order }: any) => {
    sortData.sortName = property
    sortData.sortOrder = order
    loadData()
  }
  /**
   * 分页改变时触发
   * @param currentPage 当前页面
   * @param pageSize 页面大小
   */
  const handlePageChange = ({ currentPage, pageSize }: any) => {
    if (parameter.paging) {
      tablePage.currentPage = currentPage
      tablePage.pageSize = pageSize
      loadData()
    }
  }

  return {
    tableProps: computed(() => {
      return {
        loading: loading.value,
        data: data.value,
        onSortChange: sortChange,
        sortConfig: reactive(sortConfig)
      }
    }),
    loadData,
    handleReset,
    searchModel,
    pageProps: computed(() => {
      return {
        currentPage: tablePage.currentPage,
        pageSize: tablePage.pageSize,
        total: tablePage.total,
        onPageChange: handlePageChange
      }
    })
  }
}
