<template>
  <div class="full-height mapping-container" style="padding: 20px; overflow: auto">
    <a-spin :spinning="dataLoading" class="full-height">
      <div class="title">DispatcherServlet</div>
      <div style="width: 100%">
        <a-list :data-source="computedDispatcherServletsDataList">
          <template #renderItem="{ item }">
            <a-list-item>
              <div>
                <div class="dispatcher-left">
                  <div v-for="predicate in getPredicateList(item)" :key="'predicate-' + predicate">
                    <code>{{ predicate }}</code>
                  </div>
                </div>
                <div class="dispatcher-right">
                  <a-list :data-source="getDetailData(item)">
                    <template #renderItem="detailData">
                      <a-list-item>
                        <div style="overflow: hidden; width: 100%">
                          <div style="width: 100px; display: inline-block; font-weight: bold">{{ detailData.item.label }}</div>
                          <div style="display: inline-block; width: calc(100% - 100px); word-wrap: break-word">
                            <span>{{ detailData.item.value }}</span>
                          </div>
                        </div>
                      </a-list-item>
                    </template>
                  </a-list>
                </div>
              </div>
            </a-list-item>
          </template>
        </a-list>
      </div>

      <div class="title" style="margin-top: 5px">Servlets</div>
      <div class="servlets-container">
        <a-table
          :columns="servletsColumns"
          :pagination="false"
          :data-source="computedServletsData">
          <template #bodyCell="{ column, text }">
            <template v-if="column.dataIndex === 'mappings'">
              <code>{{ text.join(',') }}</code>
            </template>
          </template>
        </a-table>
      </div>

      <div class="title" style="margin-top: 15px">Filters</div>
      <div class="filter-container">
        <a-table
          :data-source="computedFilterData"
          :pagination="false"
          :columns="filterColumns">
          <template #bodyCell="{ column, text }">
            <template v-if="column.dataIndex === 'urlPatternMappings'">
              <code>{{ text.join(',') }}</code>
            </template>
          </template>
        </a-table>
      </div>
    </a-spin>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * http mapping画面
 */
export default defineComponent({
  name: 'HttpMappingView',
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup (props) {
    const dataLoading = ref(false)
    const actuatorData = ref<any>({})
    /**
     * DispatcherServlets 数据计算属性
     */
    const computedDispatcherServletsDataList = computed(() => {
      if (actuatorData.value.contexts) {
        return actuatorData.value.contexts.application.mappings.dispatcherServlets.dispatcherServlet
      }
      return  []
    })
    /**
     * servlets 表格数据
     */
    const computedServletsData = computed(() => {
      if (actuatorData.value.contexts) {
        return actuatorData.value.contexts.application.mappings.servlets
      }
      return  []
    })

    const computedFilterData = computed(() => {
      if (actuatorData.value.contexts) {
        return actuatorData.value.contexts.application.mappings.servletFilters
      }
      return  []
    })
    /**
     * 加载数据函数
     */
    const loadData = async () => {
      dataLoading.value = true
      try {
        actuatorData.value = await loadActuator(props.clientId, 'mappings')
      } catch (e) {
        errorMessage(e)
      } finally {
        dataLoading.value = false
      }
    }
    onMounted(loadData)
    return {
      dataLoading,
      loadData,
      computedDispatcherServletsDataList,
      computedServletsData,
      computedFilterData
    }
  },
  data () {
    return {
      servletsColumns: [
        {
          title: 'Url Pattern',
          dataIndex: 'mappings',
          width: '20%'
        },
        {
          title: 'Servlet Name',
          dataIndex: 'name',
          width: '30%'
        },
        {
          title: 'Class',
          dataIndex: 'className',
          width: '50%'
        }
      ],
      filterColumns: [
        {
          title: 'Url Pattern',
          dataIndex: 'urlPatternMappings',
          width: '15%'
        },
        {
          title: 'Servlet Name',
          dataIndex: 'servletNameMappings',
          width: '15%'
        },
        {
          title: 'Filter Name',
          dataIndex: 'name',
          width: '20%'
        },
        {
          title: 'Class',
          dataIndex: 'className',
          width: '50%'
        }
      ]
    }
  },
  methods: {
    getPredicateList (item: any) {
      if (item.details == null) {
        return [item.predicate]
      }
      return item.details.requestMappingConditions.patterns
    },
    /**
     * 获取详细数据
     * @param item 每一项的数据
     */
    getDetailData (item: any) {
      const data: Array<any> = []
      const { details, handler } = item
      if (details && details.requestMappingConditions) {
        if (details.requestMappingConditions.methods && details.requestMappingConditions.methods.length > 0) {
          data.push({
            label: 'Methods',
            value: details.requestMappingConditions.methods.join(',')
          })
        }
        if (details.requestMappingConditions.produces && details.requestMappingConditions.produces.length > 0) {
          data.push({
            label: 'Produces',
            value: details.requestMappingConditions.produces.map((produce: any) => produce.mediaType).join(',')
          })
        }
      }
      if (handler) {
        data.push({
          label: 'Handler',
          value: handler
        })
      }
      return data
    }
  }
})
</script>

<style lang="less" scoped>
.mapping-container {
  background: white;
  ::v-deep(.ant-spin-nested-loading) {
    height: 100%;
  }
  code {
    color: #da1039;
    background: #f5f5f5;
    font-size: 0.875em;
    font-weight: 400;
    padding: .25em .5em .25em;
  }
  .dispatcher-left {
    width: 370px;
    display: inline-block;
  }
  .dispatcher-right {
    width: calc(100% - 370px);
    display: inline-block;
  }
  .title {
    font-weight: bold;
    font-size: larger;
    border-bottom: 2px solid #dbdbdb;
    padding: 0 0 5px 10px;
  }
  .servlets-container {
    width: 100%;
    padding-top: 5px;
  }
  .filter-container {
    padding-top: 5px;
  }
}
</style>
