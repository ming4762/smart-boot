<template>
  <div class="full-height logger-config-table" style="padding: 10px">
    <div class="full-height">
      <div class="search-container">
        <a-input v-model:value="searchModel" placeholder="search">
          <template #prefix>
            <FilterOutlined />
          </template>
          <template #addonAfter>
            {{ computedSearchAfter }}
          </template>
        </a-input>
        <div style="margin-top: 5px">
          <a-checkbox v-model:checked="isConfig">已配置</a-checkbox>
        </div>
      </div>
      <div class="table-container">
        <vxe-grid
          id="LoggerConfigPage"
          stripe
          :show-header="false"
          :loading="loading"
          highlight-hover-row
          show-overflow="tooltip"
          empty-text="没有更多数据了！"
          :data="computedData"
          :columns="columns"
          border
          height="auto">
          <template #logger-config="{ row }">
            <div>
              <a-tag
                v-for="level in computedLevels"
                :key="level"
                class="cursor-pointer"
                :class="row.effectiveLevel === level ? 'ant-tag-checkable-checked' : ''"
                @click="() => handleSetLevel(row.key, level, row.effectiveLevel)">
                {{ level }}
              </a-tag>
            </div>
          </template>
          <template #table-reset="{ row }">
            <a-button
              type="primary"
              :disabled="row.configuredLevel === null"
              @click="() => handleSetLevel(row.key, null, row.effectiveLevel)">
              重置
            </a-button>
          </template>
        </vxe-grid>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, onMounted, computed, ref } from 'vue'
import type { PropType } from 'vue'

import { FilterOutlined } from '@ant-design/icons-vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 日志配置页面
 */
export default defineComponent({
  name: 'LoggerConfigPage',
  components: {
    FilterOutlined
  },
  props: {
    clientId: {
      type: String as PropType<string>,
      required: true
    }
  },
  setup(props) {
    const isConfig = ref(false)
    const { clientId } = toRefs(props)
    const data = ref<any>({})
    const loading = ref(false)
    const doLoadData = async () => {
      loading.value = true
      setTimeout(async () => {
        try {
          data.value = await loadActuator(clientId.value, 'loggers')
        } finally {
          loading.value = false
        }
      }, 1000)
    }
    onMounted(doLoadData)
    // 搜索form
    const searchModel = ref('')
    // 表格数据计算属性
    const computedData = computed(() => {
      const loggers = data.value.loggers
      if (!loggers) {
        return []
      }
      const result: Array<any> = []
      Object.keys(loggers).forEach((key) => {
        const loggerData = loggers[key]
        const searchValue = searchModel.value
        if (searchValue === '' || key.indexOf(searchValue) !== -1) {
          if (isConfig.value) {
            if (loggerData.configuredLevel !== null) {
              result.push({
                key: key,
                configuredLevel: loggerData.configuredLevel,
                effectiveLevel: loggerData.effectiveLevel
              })
            }
          } else {
            result.push({
              key: key,
              configuredLevel: loggerData.configuredLevel,
              effectiveLevel: loggerData.effectiveLevel
            })
          }
        }
      })

      return result
    })
    // 级别计算属性
    const computedLevels = computed(() => {
      return data.value.levels || []
    })

    const computedSearchAfter = computed(() => {
      const loggers = data.value.loggers
      let length = 0
      if (loggers) {
        length = Object.keys(loggers).length
      }
      return `${computedData.value.length}/${length}`
    })
    // 设置级别
    const handleSetLevel = (key: string, level: string, effectiveLevel: string) => {
      if (level !== effectiveLevel) {
        try {
          loadActuator(clientId.value, `loggers/${key}`, null, 'POST', {
            configuredLevel: level
          })
          doLoadData()
        } catch (e) {
          errorMessage(e)
        }
      }
    }
    return {
      computedData,
      searchModel,
      computedSearchAfter,
      computedLevels,
      handleSetLevel,
      loading,
      isConfig
    }
  },
  data() {
    return {
      columns: [
        {
          field: 'key'
        },
        {
          field: 'effectiveLevel',
          width: 360,
          slots: {
            default: 'logger-config'
          }
        },
        {
          field: 'reset',
          width: '90px',
          align: 'center',
          slots: {
            default: 'table-reset'
          }
        }
      ],
      tagClass: {
        OFF: ''
      }
    }
  }
})
</script>

<style lang="less" scoped>
.logger-config-table {
  .search-container {
    background: white;
    padding: 5px;
    width: 100%;
    display: inline-block;
    .search-value {
      width: calc(100% - 70px);
      float: left;
    }
    .search-button {
      float: right;
    }
  }
  .table-container {
    margin-top: 5px;
    height: calc(100% - 70px);
    ::v-deep(.vxe-cell) {
      margin: 8px 0;
      cursor: pointer;
    }
  }
}
</style>
