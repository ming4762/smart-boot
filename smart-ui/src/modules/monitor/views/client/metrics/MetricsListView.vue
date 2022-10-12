<template>
  <div class="full-height" style="padding: 20px; overflow: auto">
    <div>
      <a-select
        v-model:value="currentMeter"
        show-search
        size="large"
        :options="meterList"
        style="width: 240px" />
    </div>
    <!--  tag选择  -->
    <div class="tag-container">
      <div v-if="tagList.length > 0">
        <a-row v-for="tag in tagList" :key="tag.tag" class="tag-row">
          <a-col :span="5" class="tag-title">
            <span>{{ tag.tag }}</span>
          </a-col>
          <a-col :span="19">
            <a-select
              class="tag-select"
              :value="tagModel[tag.tag]"
              @change="(value) => handleTagChange(tag.tag, value)">
              <a-select-option v-for="item in tag.values" :key="item" :value="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-col>
        </a-row>
      </div>
      <div v-else style="margin-bottom: 12px">
        <span>没有可用标签</span>
      </div>

      <a-row>
        <a-col :span="24">
          <a-button type="primary" @click="handleAddMeter">添加指标</a-button>
        </a-col>
      </a-row>
    </div>
    <div class="metrics-container">
      <table
        v-for="(meter, i) in selectMeterList"
        :key="meter.name"
        class="full-width"
        :style="{ marginBottom: i === selectMeterList.length - 1 ? '' : '20px' }">
        <thead>
          <tr>
            <th class="metrics-label">{{ meter.name }}</th>
            <th
              v-for="statistic in meterMeasurementsMap[meter.name]"
              :key="meter.name + statistic"
              class="metrics-statistic">
              {{ statistic }}
              <a-select
                style="width: 100px; margin-left: 10px; font-weight: normal; font-size: 12px"
                :value="meter.types[statistic]"
                placeholder="-"
                @change="(value) => handleChangeType(meter.name, statistic, value)">
                <a-select-option value="">-</a-select-option>
                <a-select-option value="int">整型</a-select-option>
                <a-select-option value="float">浮点型</a-select-option>
                <a-select-option value="time">持续时间</a-select-option>
                <a-select-option value="ms">毫秒</a-select-option>
                <a-select-option value="byte">字节</a-select-option>
              </a-select>
            </th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="tag in meter.tags" :key="meter.name + tag">
            <td class="metrics-label">
              <span>{{ createTagTitle(tag) }}</span>
            </td>
            <td
              v-for="statistic in meterMeasurementsMap[meter.name]"
              :key="meter.name + statistic"
              class="metrics-statistic">
              {{ getMeterStatisticData(meter, tag, statistic) }}
            </td>
            <td class="metrics-actions">
              <div>
                <DeleteFilled @click="() => handleRemoveMeter(meter.name, tag)" />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

import { DeleteFilled } from '@ant-design/icons-vue'

import { useLoadMeterList, useLoadMeterTagList, useShowMeterData } from './MetricsListHook'
import UnitUtils from '@/common/utils/UnitUtils'
import TimeUtils from '@/common/utils/TimeUtils'

/**
 * 指标列表页面
 */
export default defineComponent({
  name: 'MetricsListView',
  components: {
    DeleteFilled
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup(props) {
    const { meterList, currentMeter } = useLoadMeterList(props.clientId)

    const { tagList, meterMeasurementsMap } = useLoadMeterTagList(props.clientId, currentMeter)

    const {
      tagModel,
      handleTagChange,
      handleAddMeter,
      selectMeterList,
      loadMeterData,
      handleRemoveMeter,
      meterDataMap,
      handleChangeType
    } = useShowMeterData(props.clientId, currentMeter)

    const getMeterStatisticData = (meter: any, tag: any, statistic: string) => {
      const key = `${meter.name}/${JSON.stringify(tag)}`
      if (!meterDataMap.has(key)) {
        return ''
      }
      const data = meterDataMap.get(key)
      const statisticData = data.filter((item: any) => item.statistic === statistic)
      if (statisticData.length <= 0) {
        return ''
      }
      // 格式化数据
      const dataValue = statisticData[0].value
      const statisticType = meter.types[statistic]
      if (!statisticType || statisticType === '') {
        return dataValue
      }
      let formatValue = dataValue
      switch (statisticType) {
        case 'int': {
          formatValue = parseInt(dataValue.toString())
          break
        }
        case 'float': {
          formatValue = parseFloat(dataValue.toString())
          break
        }
        case 'time': {
          formatValue = TimeUtils.convertDuration(formatValue * 1000)
          break
        }
        case 'ms': {
          formatValue = formatValue * 1000
          break
        }
        case 'byte': {
          formatValue = UnitUtils.convertByte(formatValue)
          break
        }
      }
      return formatValue
    }

    return {
      meterList,
      currentMeter,
      tagList,
      meterMeasurementsMap,
      tagModel,
      handleTagChange,
      handleAddMeter,
      selectMeterList,
      loadMeterData,
      handleRemoveMeter,
      meterDataMap,
      getMeterStatisticData,
      handleChangeType
    }
  },
  methods: {
    createTagTitle(tag: any) {
      const keys = Object.keys(tag)
      if (keys.length === 0) {
        return '(没有标签)'
      }
      return keys.map((item) => `${item}:${tag[item]}`).join('\n')
    }
  }
})
</script>

<style lang="less" scoped>
.tag-container {
  margin-top: 10px;
  background: white;
  padding: 20px;
  .tag-row {
    margin-bottom: 12px;
    .tag-title {
      text-align: right;
      padding-right: 20px;
      span {
        font-weight: 700;
        font-size: 1rem;
      }
    }
  }
  .tag-select {
    min-width: 600px;
  }
}
.metrics-container {
  margin-top: 10px;
  padding: 20px;
  background: white;
  table {
    thead {
      th {
        border-width: 0 0 2px;
        font-size: 16px;
      }
    }
    th,
    td {
      border-bottom: 1px solid #dbdbdb;
      padding: 0.5em 0.75em;
    }
  }
  .metrics-label {
    width: 300px;
    white-space: pre-wrap;
  }
  .metrics-actions {
    cursor: pointer;
    width: 1px;
  }
  .metrics-statistic {
  }
}
</style>
