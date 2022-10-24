<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      height="auto"
      :loading="tableProps.loading"
      :data="computedData"
      :size="tableSizeConfig"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      :column-config="{ resizable: true }"
      border
      highlight-hover-row
      stripe>
      <template #table-status="{ row }">
        <CheckCircleTwoTone
          v-if="row.status === 'UP'"
          :style="{ 'font-size': '18px' }"
          two-tone-color="#52c41a" />
        <a-tooltip v-else>
          <WarningTwoTone :style="{ 'font-size': '18px' }" two-tone-color="#eb2f96" />
          <template #title>
            <span>{{ row.status + ': ' + row.errorMessage }}</span>
          </template>
        </a-tooltip>
      </template>
      <template #table-clientUrl="{ row }">
        <a target="_blank" :href="row.application.clientUrl">{{ row.application.clientUrl }}</a>
      </template>
      <template #table-option="{ row }">
        <a-button
          :disabled="row.status !== 'UP'"
          type="primary"
          :size="tableButtonSizeConfig"
          @click="() => handleShowDetail(row)">
          详情
        </a-button>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="({ key }) => handleMenuClick(key, row)">
              <a-menu-item
                key="heapdump"
                :disabled="!hasPermission(permissions.heapdump) || row.status !== 'UP'">
                <download-outlined />
                下载内存转储
              </a-menu-item>
              <a-menu-item
                key="threaddump"
                :disabled="!hasPermission(permissions.threaddump) || row.status !== 'UP'">
                <download-outlined />
                下载线程转储
              </a-menu-item>
            </a-menu>
          </template>
          <a-button :size="tableButtonSizeConfig" style="margin-left: 5px">
            Actions
            <DownOutlined />
          </a-button>
        </a-dropdown>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item label="应用名称">
            <a-input v-model:value="searchModel.applicationName" :size="formSizeConfig" />
          </a-form-item>
          <a-form-item label="Status">
            <a-form-item>
              <a-select
                v-model:value="searchModel.status"
                :size="formSizeConfig"
                style="width: 120px"
                placeholder="请选择">
                <a-select-option value="ALL">ALL</a-select-option>
                <a-select-option value="UP">UP</a-select-option>
                <a-select-option value="ERROR">ERROR</a-select-option>
                <a-select-option value="DOWN">DOWN</a-select-option>
              </a-select>
            </a-form-item>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item label="刷新时间：">
            <a-select v-model:value="refreshTimeModel" style="width: 120px">
              <a-select-option v-for="item in refreshTimes" :key="item.key" :value="item.key">
                {{ item.value }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-form>
      </template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from 'vue'
import { useRouter } from 'vue-router'

import {
  WarningTwoTone,
  CheckCircleTwoTone,
  DownOutlined,
  DownloadOutlined
} from '@ant-design/icons-vue'

import SizeConfigHooks from '@/components/config/SizeConfigHooks'
import dayjs from 'dayjs'
import TimeUtils from '@/common/utils/TimeUtils'
import { useVxeTable } from '@/components/hooks/CrudHooks'
import AuthMixins from '@/modules/system/mixins/AuthMixins'
import { handleLoadData, useRefreshData, handlerDownloadThreaddump } from './ClientManagerHook'
import { downActuator } from '@/modules/monitor/utils/ClientApiUtils'
import fileDownload from 'js-file-download'

const permissions = {
  heapdump: 'monitor:client:heapdump',
  threaddump: 'monitor:client:threaddump'
}

/**
 * 客户端管理页面
 */
export default defineComponent({
  name: 'ClientManagerPage',
  components: {
    WarningTwoTone,
    CheckCircleTwoTone,
    DownOutlined,
    DownloadOutlined
  },
  mixins: [AuthMixins],
  setup() {
    const router = useRouter()

    /**
     * 加载数据hook
     */
    const { loadData, tableProps, searchModel, handleReset } = useVxeTable(handleLoadData, {
      paging: false
    })
    onMounted(loadData)

    const computedData = computed(() => {
      const dataList = tableProps.value.data
      if (dataList.length == 0) {
        return []
      }
      const searchModelValue = searchModel.value
      return dataList.filter((item: any) => {
        if (
          searchModelValue.applicationName &&
          searchModelValue.applicationName.trim() !== '' &&
          item.application.applicationName.indexOf(searchModelValue.applicationName) < 0
        ) {
          return false
        }
        return !(
          searchModelValue.status &&
          searchModelValue.status.trim() !== '' &&
          searchModelValue.status !== 'ALL' &&
          item.status !== searchModelValue.status
        )
      })
    })

    // 刷新数据
    const refreshDataHook = useRefreshData(loadData)
    /**
     * 查询应用详情
     * @param row
     */
    const handleShowDetail = (row: any) => {
      const url = router.resolve({
        path: '/monitor/client',
        query: {
          clientId: row.id.value
        }
      })
      window.open(url.href, '_blank')
    }
    /**
     * 点击action
     * @param key
     * @param row
     */
    const handleMenuClick = (key: string, { id, application }: any) => {
      if (key === 'heapdump') {
        downActuator(id.value, key).then((result) => {
          fileDownload(result, 'heapdump')
        })
      } else if (key === 'threaddump') {
        // 下载内存转储
        handlerDownloadThreaddump(id.value, application.applicationName)
      }
    }
    return {
      ...SizeConfigHooks(),
      ...refreshDataHook,
      handleShowDetail,
      handleMenuClick,
      loadData,
      tableProps,
      searchModel,
      handleReset,
      computedData
    }
  },
  data() {
    return {
      permissions,
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools'
        }
      },
      columns: [
        {
          title: '注册ID',
          field: 'id.value',
          width: 150,
          fixed: 'left'
        },
        {
          title: '客户端名称',
          field: 'application.applicationName',
          width: 200,
          fixed: 'left'
        },
        {
          title: 'Status',
          field: 'status',
          width: 100,
          fixed: 'left',
          slots: {
            default: 'table-status'
          }
        },
        {
          title: '已启动',
          field: 'application1',
          width: 150,
          formatter: ({ row }: any) => {
            return TimeUtils.convertDuration(new Date().getTime() - row.application.startupTime)
          }
        },
        {
          title: '启动时间',
          field: 'application.startupTime',
          width: 190,
          formatter: ({ cellValue }: any) => dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
        },
        {
          title: '刷新时间',
          field: 'timestamp',
          width: 190,
          formatter: ({ cellValue }: any) => dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
        },
        {
          title: '端点路径',
          field: 'application.managementUrl',
          minWidth: 200
        },
        {
          title: '客户端地址',
          field: 'application.clientUrl',
          minWidth: 200,
          slots: {
            default: 'table-clientUrl'
          }
        },
        {
          title: '状态检测时间间隔(S)',
          field: 'statusInterval',
          width: 150
        },
        {
          title: '下线时间间隔(S)',
          field: 'offlineInterval',
          width: 150
        },
        {
          title: '{common.table.operation}',
          field: 'option',
          width: 200,
          fixed: 'right',
          slots: {
            default: 'table-option'
          }
        }
      ]
    }
  }
})
</script>

<style scoped></style>
