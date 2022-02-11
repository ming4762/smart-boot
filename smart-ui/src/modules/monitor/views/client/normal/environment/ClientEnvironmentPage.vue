<template>
  <div class="full-height" style="width: 100%; overflow: auto">
    <div class="full-height env-container">
      <div class="search-container">
        <div class="search-value">
          <a-input v-model:value="searchValue" />
        </div>
        <div class="search-button">
          <a-button type="primary" @click="handleSearch">搜索</a-button>
        </div>
      </div>
      <a-spin :spinning="loading">
        <div v-if="searchWithMatch">
          <ClientCard
            title="Search Result">
            <KeyValueTable :columns="columns" :data="searchData" />
          </ClientCard>
        </div>
        <div v-else>
          <template v-for="property in envData.propertySources" :key="property.name">
            <ClientCard
              :title="property.name">
              <KeyValueTable :columns="columns" :data="createTableData(property.properties)" />
            </ClientCard>
            <br />
          </template>
        </div>
      </a-spin>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, reactive, ref, onMounted } from 'vue'

import ClientCard from '@/modules/monitor/components/common/ClientCard.vue'
import KeyValueTable from '@/modules/monitor/components/common/KeyValueTable.vue'
import { loadActuator } from '@/modules/monitor/utils/ClientApiUtils'
import { errorMessage } from '@/components/notice/SystemNotice'

const createTableData = (properties: any) => {
  if (!properties) {
    return []
  }
  return Object.keys(properties).map(key => {
    return {
      key: key,
      value: properties[key].value
    }
  })
}

const columns = [
  {
    dataIndex: 'key',
    width: '237px',
    // slots: {
    //   customRender: 'key'
    // }
  },
  {
    dataIndex: 'value',
    width: '600px'
  }
]

/**
 * client Environment页面
 */
export default defineComponent({
  name: 'ClientEnvironmentPage',
  components: {
    ClientCard,
    KeyValueTable
  },
  props: {
    // 客户端ID
    clientId: {
      required: true,
      type: String
    }
  },
  setup (props) {
    const { clientId } = toRefs(props)
    const envData = ref([])
    const loading = ref(false)
    const searchValue = ref('')
    const searchWithMatch = ref(false)
    const searchData = ref([] as Array<any>)
    const doLoadData = async () => {
      loading.value = true
      searchWithMatch.value = searchValue.value.trim() !== ''
      try {
        const result = await loadActuator(clientId.value, `env/${searchValue.value.trim()}`)
        envData.value = result
        if (searchWithMatch.value && Object.keys(result.property).length > 0) {
          searchData.value = [
            {
              key: result.property.source,
              value: result.property.value
            }
          ]
        } else {
          searchData.value = []
        }
      } catch (e) {
        errorMessage(e)
      } finally {
        loading.value = false
      }
    }
    const handleSearch = () => {
      doLoadData()
    }
    onMounted(doLoadData)
    return {
      envData,
      createTableData,
      columns: reactive(columns),
      searchValue,
      handleSearch,
      searchWithMatch,
      searchData,
      loading
    }
  }
})
</script>

<style lang="less" scoped>
.env-container {
  width: 937px;
  margin: 0 auto;
  padding: 10px 0;
}
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
</style>
