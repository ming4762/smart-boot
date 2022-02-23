import { reactive, computed } from 'vue'

const histogramFormatter = (value?: Array<string>) => {
  if (!value) {
    return ''
  }
  return value.join(', ')
}

// 表访问统计列
const tableStatColumns = [
  {
    title: '序号',
    dataIndex: '',
    width: 80,
    fixed: 'left',
    customRender: ({ index }: any) => index + 1
  },
  {
    title: '表名',
    dataIndex: 'name',
    width: 200,
    fixed: 'left'
  },
  {
    title: 'Select数',
    dataIndex: 'selectCount',
    width: 80
  },
  {
    title: 'SelectInto数',
    dataIndex: 'selectIntoCount',
    width: 100
  },
  {
    title: 'Insert数',
    dataIndex: 'insertCount',
    width: 100
  },
  {
    title: 'Update数',
    dataIndex: 'updateCount',
    width: 100
  },
  {
    title: 'Delete数',
    dataIndex: 'deleteCount',
    width: 100
  },
  {
    title: 'Truncate数',
    dataIndex: 'TtruncateCount',
    width: 100
  },
  {
    title: 'Create数',
    dataIndex: 'createCount',
    width: 100
  },
  {
    title: 'Alter数',
    dataIndex: 'alterCount',
    width: 100
  },
  {
    title: 'Drop数',
    dataIndex: 'dropCount',
    width: 100
  },
  {
    title: 'Replace数',
    dataIndex: 'replaceCount',
    width: 100
  },
  {
    title: '删除数据行数',
    dataIndex: 'deleteDataCount',
    width: 110
  },
  {
    title: '删除行分布',
    dataIndex: '',
    width: 100
  },
  {
    title: '更新数据行数',
    dataIndex: 'updateDataCount',
    width: 110
  },
  {
    title: '更新行分布',
    dataIndex: '',
    width: 100
  },
  {
    title: '读取行数',
    dataIndex: 'fetchRowCount',
    width: 100
  },
  {
    title: '读取行分布',
    dataIndex: 'fetchRowCountHistogram',
    width: 160,
    customRender: ({ text }: any) => histogramFormatter(text)
  }
]

/**
 * 函数调用统计
 */
const functionStatColumns = [
  {
    title: 'Function Name',
    dataIndex: 'functionName'
  },
  {
    title: 'Invoke Count',
    dataIndex: 'invokeCount'
  }
]

/**
 * 白名单防御统计
 */
const whiteListColumns = [
  {
    title: '序号',
    dataIndex: 'seq',
    width: 120,
    customRender: ({ index }: any) => index + 1
  },
  {
    title: 'SQL',
    dataIndex: 'sql',
    width: 400
  },
  {
    title: '样本',
    dataIndex: 'sample',
    width: 120
  },
  {
    title: '执行数',
    dataIndex: 'executeCount',
    width: 120
  },
  {
    title: '执行出错数',
    dataIndex: 'executeErrorCount',
    width: 120
  },
  {
    title: '读取行数',
    dataIndex: 'fetchRowCount',
    width: 120
  },
  {
    title: '更新行数',
    dataIndex: 'updateCount',
    width: 120
  }
]

/**
 * 黑名单防御统计
 */
const blackListColumns = [
  {
    title: '序号',
    dataIndex: 'seq',
    width: 120,
    customRender: ({ index }: any) => index + 1
  },
  {
    title: 'SQL',
    dataIndex: 'sql',
    width: 400
  },
  {
    title: '样本',
    dataIndex: 'sample',
    width: 120
  },
  {
    title: 'violationMessage',
    dataIndex: 'violationMessage',
    width: 130
  },
  {
    title: '执行数',
    dataIndex: 'executeCount',
    width: 120
  },
  {
    title: '读取行数',
    dataIndex: 'fetchRowCount',
    width: 120
  },
  {
    title: '更新行数',
    dataIndex: 'updateCount',
    width: 120
  }
]

/**
 * 添加通用参数
 * @param result
 */
const addCommonOption = (result: any) => {
  const commonOption = {
    size: 'small',
    bordered: true,
    pagination: false,
    rowClassName: (record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)
  }
  return Object.assign(result, commonOption)
}

/**
 * 创建表格stat数据
 * @param data
 */
export const createTableOptions = (data: any) => {
  const tableStat = {
    columns: tableStatColumns,
    rowKey: 'name',
    scroll: {
      x: 1400
    },
    dataSource: computed(() => data.value.tables)
  }
  const functionStat = {
    columns: functionStatColumns,
    rowKey: 'functionName',
    dataSource: computed(() => data.value.functions)
  }
  const whiteStat = {
    columns: whiteListColumns,
    rowKey: 'sql',
    dataSource: computed(() => data.value.whiteList)
  }
  const blackStat = {
    columns: blackListColumns,
    rowKey: 'sql',
    dataSource: computed(() => data.value.blackList)
  }
  return {
    tableStatOptions: reactive(addCommonOption(tableStat)),
    functionStatOption: reactive(addCommonOption(functionStat)),
    whiteStatOption: reactive(addCommonOption(whiteStat)),
    blackStatOption: reactive(addCommonOption(blackStat))
  }
}
