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
          <a-form-item :label="$t('monitor.views.client.slowSql.title.applicationCode')">
            <a-input v-model:value="searchModel.applicationCode" style="width: 120px" :size="formSizeConfig" :placeholder="$t('monitor.views.client.slowSql.validate.applicationCode')" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.slowSql.title.clientId')">
            <a-input v-model:value="searchModel.clientId" :size="formSizeConfig" style="width: 100px" :placeholder="$t('monitor.views.client.slowSql.validate.clientId')" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.slowSql.title.datasourceName')">
            <a-input v-model:value="searchModel.datasourceName" style="width: 120px" :size="formSizeConfig" :placeholder="$t('monitor.views.client.slowSql.validate.datasourceName')" />
          </a-form-item>
          <a-form-item :label="$t('monitor.views.client.slowSql.title.timestamp')">
            <a-range-picker v-model:value="searchModel.timestamp" style="width: 320px" :size="formSizeConfig" show-time :placeholder="['Start Time', 'End Time']" />
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
      <template #table-clientId="{ row }">
        <a :href="row.clientUrl" target="_blank">{{ row.clientId }}</a>
      </template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'

import { useVxeTable } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData } from './MonitorClientSlowSqlHook'
import dayjs from 'dayjs'

export default defineComponent({
  name: 'MonitorClientSlowSqlListView',
  components: {

  },
  setup () {
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, loadData, searchModel } = useVxeTable(handleLoadData, {
      paging: true
    })
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      tableProps,
      pageProps,
      handleReset,
      loadData,
      searchModel
    }
  },
  data () {
    return {
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
          title: '{monitor.views.client.slowSql.title.applicationCode}',
          fixed: 'left',
          width: 160
        },
        {
          field: 'clientId',
          title: '{monitor.views.client.slowSql.title.clientId}',
          width: 120,
          slots: {
            default: 'table-clientId'
          }
        },
        {
          field: 'datasourceName',
          title: '{monitor.views.client.slowSql.title.datasourceName}',
          width: 160
        },
        {
          field: 'sqlText',
          title: '{monitor.views.client.slowSql.title.sqlText}',
          minWidth: 300
        },
        {
          field: 'parameter',
          title: '{monitor.views.client.slowSql.title.parameter}',
          width: 120
        },
        {
          field: 'useMillis',
          sortable: true,
          title: '{monitor.views.client.slowSql.title.useMillis}',
          width: 130
        },
        {
          field: 'timestamp',
          sortable: true,
          title: '{monitor.views.client.slowSql.title.timestamp}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 170
        }
      ]
    }
  }
})
</script>
