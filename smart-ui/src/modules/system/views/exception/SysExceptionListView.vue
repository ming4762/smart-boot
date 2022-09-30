<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      show-overflow="tooltip"
      border
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
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('system.views.exception.title.exceptionMessage')">
            <a-input v-model:value="searchModel.exceptionMessage" style="width: 130px" :size="formSizeConfig" :placeholder="$t('system.views.exception.search.exceptionMessage')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.exception.title.requestIp')">
            <a-input v-model:value="searchModel.requestIp" style="width: 130px" :size="formSizeConfig" :placeholder="$t('system.views.exception.search.requestIp')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.exception.title.serverIp')">
            <a-input v-model:value="searchModel.serverIp" style="width: 130px" :size="formSizeConfig" :placeholder="$t('system.views.exception.search.serverIp')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.exception.title.userFeedback')">
            <a-select v-model:value="searchModel.userFeedback" style="width: 90px" :size="formSizeConfig">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option value="true">{{ $t('common.form.yes') }}</a-select-option>
              <a-select-option value="false">{{ $t('common.form.no') }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('system.views.exception.title.resolved')">
            <a-select v-model:value="searchModel.resolved" style="width: 90px" :size="formSizeConfig">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option value="true">{{ $t('common.form.yes') }}</a-select-option>
              <a-select-option value="false">{{ $t('common.form.no') }}</a-select-option>
            </a-select>
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
      <template #table-operation="{ row }">
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <a-menu-item key="showStackTrace" :disabled="!hasPermission('sys:exception:query')">{{ $t('system.views.exception.title.showStackTrace') }}</a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
    </vxe-grid>

    <a-modal
      v-model:visible="stackTraceModalVisible"
      width="800px"
      title="堆栈信息">
      <a-spin :spinning="stackTraceLoading">
        <div style="overflow: auto">
          <div style="white-space: pre">
            {{ stackTrace }}
          </div>
        </div>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
// import { useI18n } from 'vue-i18n'

import { DownOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

import { useVxeTable } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'
import { handleLoadData, useShowStackTrace } from './SysExceptionHook'
import { tableBooleanColumn } from '@/components/common/TableCommon'
import { hasPermission } from '@/common/auth/AuthUtils'

export default defineComponent({
  name: 'SysExceptionListView',
  components: {
    DownOutlined
  },
  setup () {
    // const { t } = useI18n()
    const gridRef = ref()

    const showStackTrackHook = useShowStackTrace()
    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, loadData, searchModel } = useVxeTable(handleLoadData, {
      paging: true,
      defaultParameter: {
        userFeedback: '',
        resolved: ''
      }
    })
    /**
     * 按钮操作
     * @param row 行数据
     * @param action 操作
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        case 'showStackTrace': {
          // 查看堆栈信息
          showStackTrackHook.handleShowStackTrace(row.id)
          break
        }
      }
    }
    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...showStackTrackHook,
      tableProps,
      pageProps,
      handleReset,
      handleActions,
      loadData,
      searchModel,
      hasPermission
    }
  },
  data () {
    return {
      columnConfig: {
      },
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
          field: 'id',
          visible: false,
          title: '{system.views.exception.title.id}',
          width: 120
        },
        {
          field: 'exceptionMessage',
          fixed: 'left',
          title: '{system.views.exception.title.exceptionMessage}',
          width: 200
        },
        {
          field: 'requestIp',
          title: '{system.views.exception.title.requestIp}',
          width: 120
        },
        {
          field: 'serverIp',
          title: '{system.views.exception.title.serverIp}',
          width: 120
        },
        {
          field: 'requestPath',
          title: '{system.views.exception.title.requestPath}',
          width: 200
        },
        {
          field: 'operateUserId',
          title: '{system.views.exception.title.operateUserId}',
          width: 120
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
          width: 150
        },
        {
          ...tableBooleanColumn(this.$t, '{system.views.exception.title.userFeedback}', 'userFeedback').createColumn(),
          width: 120,
          sortable: true
        },
        {
          field: 'feedbackMessage',
          title: '{system.views.exception.title.feedbackMessage}',
          width: 160
        },
        {
          field: 'feedbackTime',
          title: '{system.views.exception.title.feedbackTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 150
        },
        {
          ...tableBooleanColumn(this.$t, '{system.views.exception.title.resolved}', 'resolved').createColumn(),
          width: 120,
          sortable: true
        },
        {
          field: 'resolvedMessage',
          title: '{system.views.exception.title.resolvedMessage}',
          width: 160
        },
        {
          field: 'resolvedUserId',
          title: '{system.views.exception.title.resolvedUserId}',
          width: 120
        },
        {
          field: 'resolvedTime',
          title: '{system.views.exception.title.resolvedTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 150
        },
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
      ]
    }
  }
})
</script>
