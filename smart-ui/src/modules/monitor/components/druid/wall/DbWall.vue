<template>
  <div class="db-wall">
    <h3>防御统计</h3>
    <DbCommonTable :data="computedWallStatData" />
    <br />
    <h3>表访问统计</h3>
    <a-table v-bind="tableStatOptions"></a-table>
    <br />
    <h3>函数调用统计</h3>
    <a-table v-bind="functionStatOption"></a-table>
    <br />
    <h3>SQL防御统计 - 白名单</h3>
    <a-table v-bind="whiteStatOption"></a-table>
    <br />
    <h3>SQL防御统计 - 黑名单</h3>
    <a-table v-bind="blackStatOption"></a-table>
    <br />
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, toRefs, ref, onMounted, watch, computed } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import TimeTaskUtil from '@/common/utils/TimeTaskUtil'

import { MONITOR_DETAIL_LOOP_GROUP } from '@/modules/monitor/constants/MonitorConstants'

import DbCommonTable from '@/modules/monitor/components/druid/common/DbCommonTable.vue'

import { createTableOptions } from './DbWallTable'

const wallStatKey: {[index: string]: string} = {
  CheckCount: 'checkCount',
  HardCheckCount: 'hardCheckCount',
  ViolationCount: 'violationCount',
  BlackListHitCount: 'blackListHitCount',
  BlackListSize: 'blackListSize',
  WhiteListHitCount: 'whiteListHitCount',
  WhiteListSize: 'whiteListSize',
  SyntaxErrrorCount: 'syntaxErrorCount'
}

const formatData = (keys: {[index: string]: string}, data: any) => {
  return Object.keys(keys).map(key => {
    let value = data[keys[key]]
    if (value === 0) {
      value = ''
    }
    return {
      key: key,
      value: value
    }
  })
}

export default defineComponent({
  name: 'DbWall',
  components: {
    DbCommonTable
  },
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

    const data = ref<Array<any>>([])
    const doLoadData = async () => {
      data.value = await loadActuator(clientId.value, `druidWall/${dbName.value}`)
    }
    TimeTaskUtil.addLoop(MONITOR_DETAIL_LOOP_GROUP, `druid_wall_${clientId.value}_${dbName.value}`, doLoadData)
    onMounted(doLoadData)
    watch([clientId, dbName], doLoadData)

    // table stat数据
    const computedWallStatData = computed(() => formatData(wallStatKey, data.value))
    const { tableStatOptions, functionStatOption, whiteStatOption, blackStatOption } = createTableOptions(data)
    return {
      data,
      computedWallStatData,
      tableStatOptions,
      functionStatOption,
      whiteStatOption,
      blackStatOption
    }
  }
})
</script>

<style lang="less" scoped>
h3 {
  font-size: 24px;
  line-height: 40px;
}
h5 {
  font-size: 14px;
  line-height: 20px;
}
h2 {
  font-size: 30px;
  line-height: 40px;
}
.db-wall ::v-deep(.table-striped) {
  background: rgb(250, 250, 250);
}
</style>
