<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
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
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('monitor.views.client.httpTrace.title.applicationCode')">
            <a-select v-model:value="searchModel.applicationCode" style="width: 210px" show-search :size="formSizeConfig">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option v-for="applicationName in applicationNameList" :key="'applicationName' + applicationName" :value="applicationName">{{ applicationName }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.httpTrace.title.clientId')">
            <a-select v-model:value="searchModel.clientId" :size="formSizeConfig" style="width: 140px;">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option v-for="clientId in clientIdList" :key="clientId" :value="clientId">{{ clientId }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.httpTrace.title.httpMethod')">
            <a-select v-model:value="searchModel.httpMethod" :size="formSizeConfig" style="width: 120px">
              <a-select-option v-for="httpMethod in httpMethods" :key="'' + httpMethod" :value="httpMethod">{{ httpMethod }} </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.httpTrace.title.responseStatus')">
            <a-input v-model:value="searchModel.responseStatus" :size="formSizeConfig" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.httpTrace.title.timestamp')">
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
      <template #table-responseStatus="{ row }">

      </template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref, watch} from 'vue'
import { useI18n } from 'vue-i18n'

import { useVxeTable, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'
import { useUserApplicationName } from '@/modules/monitor/components/hooks/ApplicationHooks'

import { handleLoadData, handleDelete } from './MonitorClientHttpTraceHook'
import dayjs from 'dayjs'
import ApiService from '@/common/utils/ApiService'
import {errorMessage} from '@/components/notice/SystemNotice'

const httpMethods = ['GET', 'HEAD', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS', 'TRACE']

const useClientId = (applicationCode: any) => {
  const clientIdList = ref<Array<string>>([])
  const loadClientId = async () => {
    try {
      clientIdList.value = await ApiService.postAjax('monitor/client/httpTrace/listClientId', {
        applicationName: applicationCode.value
      })
    } catch (e) {
      errorMessage(e)
    }
  }
  onMounted(loadClientId)
  watch(applicationCode, loadClientId)
  return {
    clientIdList
  }
}

export default defineComponent({
  name: 'MonitorClientHttpTraceListView',
  components: {

  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, loadData, searchModel } = useVxeTable(handleLoadData, {
      paging: true,
      defaultParameter: {
        applicationCode: ''
      }
    })

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'id', listHandler: loadData })

    const { applicationNameList } = useUserApplicationName()

    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...deleteHook,
      tableProps,
      pageProps,
      handleReset,
      loadData,
      searchModel,
      applicationNameList,
      ...useClientId(computed(() => searchModel.value.applicationCode))
    }
  },
  data () {
    return {
      httpMethods: httpMethods,
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons'
        }
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
        {
          field: 'applicationCode',
          title: '{monitor.views.client.httpTrace.title.applicationCode}',
          width: 200
        },
        {
          field: 'clientId',
          title: '{monitor.views.client.httpTrace.title.clientId}',
          width: 180
        },
        {
          field: 'httpMethod',
          title: '{monitor.views.client.httpTrace.title.httpMethod}',
          width: 120
        },
        {
          field: 'timeTaken',
          title: '{monitor.views.client.httpTrace.title.timeTaken}',
          width: 150,
          sortable: true
        },
        {
          field: 'url',
          title: '{monitor.views.client.httpTrace.title.url}',
          minWidth: 180
        },
        {
          field: 'responseStatus',
          sortable: true,
          title: '{monitor.views.client.httpTrace.title.responseStatus}',
          width: 120,
          slots: {
            default: 'table-responseStatus'
          }
        },
        {
          field: 'timestamp',
          sortable: true,
          title: '{monitor.views.client.httpTrace.title.timestamp}',
          width: 180,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          }
        }
      ]
    }
  }
})
</script>
