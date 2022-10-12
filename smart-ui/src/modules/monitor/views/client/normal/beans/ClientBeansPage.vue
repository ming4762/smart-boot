<template>
  <div class="full-height" style="padding: 10px">
    <div class="full-height beans-container">
      <div class="search-container">
        <div class="search-value">
          <a-input v-model:value="searchValue" />
        </div>
        <div class="search-button">
          <a-button type="primary" @click="handleSearch">搜索</a-button>
        </div>
      </div>
      <div class="table-container">
        <vxe-grid
          ref="tableRef"
          highlight-hover-row
          height="auto"
          highlight-current-row
          :expand-config="expandConfig"
          :show-header="false"
          show-overflow="tooltip"
          :loading="dataLoading"
          :data="filterDataList"
          :columns="columns"
          @cell-click="handleCellClick">
          <template #table-application="{ row }">
            <div style="margin: 8px 12px">
              <span style="color: black">{{ row.name }}</span>
              <br />
              <small style="color: #b5b5b5">{{ row.data.type }}</small>
            </div>
          </template>
          <template #table-expand="{ row }">
            <div style="padding: 0 10px">
              <KeyValueTable :columns="keyValueColumns" :data="convertDetail(row)" />
            </div>
          </template>
          <template #table-type="{ row }">
            <div>{{ row.data.scope }}</div>
          </template>
        </vxe-grid>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, toRefs } from 'vue'

import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

import KeyValueTable from '@/modules/monitor/components/common/KeyValueTable.vue'

const beansMapping: any = {
  aliases: '别名',
  resource: '资源',
  dependencies: '依赖关系'
}

export default defineComponent({
  name: 'ClientBeansPage',
  components: {
    KeyValueTable
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup(props) {
    const tableRef = ref()
    const { clientId } = toRefs(props)
    const searchValue = ref('')
    let dataList: Array<any> = []
    const filterDataList = ref<Array<any>>([])
    const currentRow = ref<any>({})
    /**
     * 执行搜索
     */
    const handleSearch = () => {
      if (searchValue.value.trim() === '') {
        filterDataList.value = dataList
      } else {
        filterDataList.value = dataList.filter(
          (item) => item.name.indexOf(searchValue.value.trim()) >= 0
        )
      }
    }

    const handleCellClick = ({ row }: any) => {
      currentRow.value = row
      tableRef.value.toggleRowExpand(row)
    }
    const dataLoading = ref(false)
    /**
     * 加载数据函数
     */
    const handleLoadData = async () => {
      try {
        dataLoading.value = true
        const data = await loadActuator(clientId.value, 'beans')
        const beans = data.contexts.application.beans
        dataList = Object.keys(beans)
          .map((key) => {
            return {
              name: key,
              data: beans[key]
            }
          })
          .sort((a, b) => a.name.localeCompare(b.name))
        filterDataList.value = dataList
      } catch (e) {
        errorMessage(e)
      } finally {
        dataLoading.value = false
      }
    }
    onMounted(handleLoadData)

    return {
      searchValue,
      handleSearch,
      dataLoading,
      filterDataList,
      handleCellClick,
      tableRef
    }
  },
  data() {
    return {
      expandConfig: {
        visibleMethod() {
          return false
        }
      },
      keyValueColumns: [
        {
          dataIndex: 'key',
          width: '100px'
        },
        {
          dataIndex: 'value'
        }
      ],
      columns: [
        {
          field: 'application',
          type: 'expand',
          slots: {
            default: 'table-application',
            content: 'table-expand'
          },
          minWidth: '500px'
        },
        {
          field: 'type',
          slots: {
            default: 'table-type'
          },
          width: '100px'
        }
      ]
    }
  },
  methods: {
    convertDetail(row: any) {
      const data = row.data
      const dataList: Array<any> = []
      Object.keys(data).forEach((key) => {
        const value = data[key]
        if (value !== null && beansMapping[key]) {
          if (key === 'aliases') {
            if (value.length > 0) {
              dataList.push({
                key: beansMapping[key],
                value: value
              })
            }
          } else {
            dataList.push({
              key: beansMapping[key],
              value: value
            })
          }
        }
      })
      return dataList
    }
  }
})
</script>

<style lang="less" scoped>
.beans-container {
  .search-container {
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
    height: calc(100% - 40px);
    ::v-deep(.vxe-cell) {
      margin: 8px 0;
      cursor: pointer;
    }
  }
}
small {
  font-size: 0.875em;
}
</style>
