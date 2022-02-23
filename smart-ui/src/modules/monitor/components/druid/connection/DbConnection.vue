<template>
  <div class="db-monitor">
    <a-table
      size="small"
      :columns="columns"
      bordered
      :pagination="false"
      :show-header="false"
      :row-class-name="(record, index) => (index % 2 === 1 ? 'table-striped' : null)"
      :data-source="data">
      <template #key="{ text }">
        <span class="table-font bold">{{ text }}</span>
      </template>
      <template #value="{ text }">
        <span class="table-font">{{ text }}</span>
      </template>
      <template #desc="{ text }">
        <span class="table-font">{{ text }}</span>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, onMounted, toRefs, ref } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'

import dayjs from 'dayjs'

const dbKeyDescMap: {[index: string]: string} = {
  UserName: '指定建立连接时使用的用户名',
  URL: 'JDBC连接字符串',
  DbType: '数据库类型',
  DriverClassName: 'JDBC驱动的类名',
  FilterClassNames: 'filter的类名',
  TestOnBorrow: '是否在获得连接后检测其可用性',
  TestWhileIdle: '是否在连接空闲一段时间后检测其可用性',
  TestOnReturn: '是否在连接放回连接池后检测其可用性',
  InitialSize: '连接池建立时创建的初始化连接数',
  MinIdle: '连接池中最小的活跃连接数',
  MaxActive: '连接池中最大的活跃连接数',
  QueryTimeout: '查询超时时间',
  TransactionQueryTimeout: '事务查询超时时间',
  LoginTimeout: '',
  ValidConnectionCheckerClassName: '',
  ExceptionSorterClassName: '',
  DefaultAutoCommit: '',
  DefaultReadOnly: '',
  DefaultTransactionIsolation: '',
  MinEvictableIdleTimeMillis: '',
  MaxEvictableIdleTimeMillis: '',
  KeepAlive: '',
  FailFast: '',
  PoolPreparedStatements: '',
  MaxPoolPreparedStatementPerConnectionSize: '',
  MaxWait: '',
  MaxWaitThreadCount: '',
  LogDifferentThread: '',
  UseUnfairLock: '',
  InitGlobalVariants: '',
  InitVariants: '',
  NotEmptyWaitCount: '获取连接时累计等待多少次',
  NotEmptyWaitMillis: '获取连接时累计等待多长时间',
  WaitThreadCount: '当前等待获取连接的线程数',
  StartTransactionCount: '事务开始的个数',
  TransactionHistogram: '事务运行时间分布，分布区间为[0-1 ms, 1-10 ms, 10-100 ms, 100-1 s, 1-10 s, 10-100 s, >100 s]',
  PoolingCount: '当前连接池中的数目',
  PoolingPeak: '连接池中数目的峰值',
  PoolingPeakTime: '连接池数目峰值出现的时间',
  ActiveCount: '当前连接池中活跃连接数',
  ActivePeak: '连接池中活跃连接数峰值',
  ActivePeakTime: '活跃连接池峰值出现的时间',
  LogicConnectCount: '产生的逻辑连接建立总数',
  LogicCloseCount: '产生的逻辑连接关闭总数',
  LogicConnectErrorCount: '产生的逻辑连接出错总数',
  DiscardCount: '校验连接失败丢弃连接次数',
  PhysicalConnectCount: '产生的物理连接建立总数',
  PhysicalCloseCount: '产生的物理关闭总数',
  PhysicalConnectErrorCount: '产生的物理连接失败总数',
  ExecuteCount: '',
  ExecuteQueryCount: '',
  ExecuteUpdateCount: '',
  ExecuteBatchCount: '',
  ErrorCount: '',
  CommitCount: '',
  RollbackCount: '',
  PSCacheAccessCount: 'PSCache访问总数',
  PSCacheHitCount: 'PSCache命中次数',
  PSCacheMissCount: 'PSCache不命中次数',
  ConnectionHoldTimeHistogram: '连接持有时间分布，分布区间为[0-1 ms, 1-10 ms, 10-100 ms, 100ms-1s, 1-10 s, 10-100 s, 100-1000 s, >1000 s]',
  ClobOpenCount: 'Clob打开数',
  BlobOpenCount: 'Blob打开数',
  KeepAliveCheckCount: '\tKeepAlive检测次数'
}

const formatterHandler: any = {
  FilterClassNames: (value: Array<string>) => {
    return value.join(',')
  },
  PoolingPeakTime: (value: number) => {
    return dayjs(value).format()
  },
  ActivePeakTime: (value: number) => {
    return dayjs(value).format()
  }
}

/**
 * 数据库连接信息
 */
export default defineComponent({
  name: 'DbConnection',
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    },
    dbName: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup (props) {
    const { clientId, dbName } = toRefs(props)
    const data = ref([] as Array<any>)
    onMounted(async () => {
      const result = await loadActuator(clientId.value, `druidDatasource/${dbName.value}`)
      data.value = Object.keys(dbKeyDescMap).map(key => {
        let value = result[key]
        if (value === undefined) {
          console.error(key)
        }
        if (formatterHandler[key]) {
          value = formatterHandler[key](value)
        }
        return {
          key: key,
          value: value,
          desc: dbKeyDescMap[key]
        }
      })
    })
    return {
      data
    }
  },
  data () {
    return {
      columns: [
        {
          dataIndex: 'key',
          width: 286,
          slots: {
            customRender: 'key'
          }
        },
        {
          dataIndex: 'value',
          width: 759,
          slots: {
            customRender: 'value'
          }
        },
        {
          dataIndex: 'desc',
          slots: {
            customRender: 'desc'
          }
        }
      ]
    }
  }
})
</script>

<style lang="less" scoped>
</style>
