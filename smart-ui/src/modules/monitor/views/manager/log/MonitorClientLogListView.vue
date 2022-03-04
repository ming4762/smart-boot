<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      show-overflow="tooltip"
      :show-header="false"
      :column-config="columnConfig"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button v-permission="'monitor:client:log'" type="primary" :size="buttonSizeConfig" @click="handleDownload">
              <download-outlined />
              {{ $t('common.button.download') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('monitor.views.client.log.title.applicationCode')">
            <a-select v-model:value="searchModel.applicationCode" style="width: 210px" show-search :size="formSizeConfig">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option v-for="applicationName in applicationNameList" :key="'applicationName' + applicationName" :value="applicationName">{{ applicationName }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.log.title.clientId')">
            <a-input v-model:value="searchModel.clientId" :size="formSizeConfig" style="width: 150px;" :placeholder="$t('monitor.views.client.log.search.clientId')" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.log.title.level')">
            <a-select v-model:value="searchModel.level" style="width: 90px" :size="formSizeConfig" :placeholder="$t('monitor.views.client.log.search.level')">
              <a-select-option v-for="level in logLevels" :key="level" :value="level === 'ALL' ? '' : level">{{ level }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.log.title.timestamp')">
            <a-range-picker v-model:value="searchModel.timestamp" :size="formSizeConfig" show-time style="width: 320px" />
          </a-form-item>

          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              @click="handleReset">
              {{ $t('common.button.reset') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #table-logText="{ row }">
        <div style="white-space: pre">{{ row.logText }}</div>
      </template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive } from 'vue'

import { DownloadOutlined } from '@ant-design/icons-vue'

import { useVxeTable } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData, handleDownload } from './MonitorClientLogHook'
import { useUserApplicationName } from '@/modules/monitor/components/hooks/ApplicationHooks'

const levels = ['ALL', 'ERROR', 'WARN', 'INFO', 'DEBUG', 'TRACE']

export default defineComponent({
  name: 'MonitorClientLogListView',
  components: {
    DownloadOutlined
  },
  setup () {
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, loadData, searchModel } = useVxeTable(handleLoadData, {
      paging: true,
      defaultParameter: {
        level: '',
        applicationCode: ''
      }
    })

    const { applicationNameList } = useUserApplicationName()

    onMounted(loadData)
    return {
      gridRef,
      applicationNameList,
      ...SizeConfigHooks(),
      tableProps,
      pageProps,
      handleReset,
      loadData,
      searchModel,
      logLevels: reactive(levels),
      handleDownload: () => handleDownload(searchModel.value)
    }
  },
  data () {
    return {
      columnConfig: {
      },
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools'
        }
      },
      columns: [
        {
          type: 'seq',
          width: 60
        },
        {
          field: 'logText',
          title: '{monitor.views.client.log.title.logText}',
          slots: {
            default: 'table-logText'
          }
        }
      ]
    }
  }
})
</script>
