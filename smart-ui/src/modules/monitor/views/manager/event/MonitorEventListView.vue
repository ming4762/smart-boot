<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      show-overflow
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
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px">
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item>
            <a-input v-model:value="searchModel.applicationCode" :size="formSizeConfig" :placeholder="$t('monitor.views.event.validate.applicationCode')" />
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="searchModel.clientId" :size="formSizeConfig" :placeholder="$t('monitor.views.event.validate.clientId')" />
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="searchModel.eventCode" :size="formSizeConfig" :placeholder="$t('monitor.views.event.validate.eventCode')" />
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
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'

import dayjs from 'dayjs'

import { useVxeTable } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData } from './MonitorEventHook'

export default defineComponent({
  name: 'MonitorEventListView',
  components: {

  },
  setup () {
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, searchModel, loadData } = useVxeTable(handleLoadData, {
      paging: true
    })


    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      searchModel,
      tableProps,
      pageProps,
      handleReset,
      loadData
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
          buttons: 'toolbar_buttons'
        }
      },
      rules: {
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
          fixed: 'left',
          sortable: true,
          title: '{monitor.views.event.title.applicationCode}',
          width: 200
        },
        {
          field: 'clientId',
          title: '{monitor.views.event.title.clientId}',
          width: 160
        },
        {
          field: 'eventCode',
          sortable: true,
          title: '{monitor.views.event.title.eventCode}',
          width: 160
        },
        {
          field: 'timestamp',
          title: '{monitor.views.event.title.timestamp}',
          width: 170,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          }
        },
        {
          field: 'eventMessage',
          title: '{monitor.views.event.title.eventMessage}',
          minWidth: 200
        },
        {
          field: 'createTime',
          sortable: true,
          title: '{common.table.createTime}',
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
