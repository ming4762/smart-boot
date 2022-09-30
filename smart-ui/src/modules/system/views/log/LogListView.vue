<template>
  <div class="full-height" style="padding: 10px">
    <div class="form-container">
      <a-form style="padding: 10px; height: 90px; width: 1000px" :model="searchModel">
        <a-row>
          <a-col :span="6">
            <a-form-item :label="$t('system.views.log.title.operation')">
              <a-input v-model:value="searchModel.operation" style="width: 140px" :size="formSizeConfig" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item :label="$t('system.views.log.title.logSource')">
              <a-select
                v-model:value="searchModel.logSource"
                style="width: 120px"
                mode="multiple"
                :size="formSizeConfig"
                :placeholder="$t('common.notice.select')">
                <a-select-option
                  v-for="item in logSourceEnum"
                  :key="item.value"
                  :value="item.value">
                  {{ $t(item.label) }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item :label="$t('system.views.log.title.createUserId')">
              <a-input v-model:value="searchModel.createUserId" style="width: 120px" :size="formSizeConfig" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item :label="$t('system.views.log.title.statusCode')">
              <a-input v-model:value="searchModel.statusCode" style="width: 100px" :size="formSizeConfig" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item :label="$t('system.views.log.title.createTime')">
              <a-range-picker
                v-model:value="searchModel.createTime"
                :size="formSizeConfig"
                style="width: 340px"
                :show-time="{ defaultValue: dayjs('00:00:00', 'HH:mm:ss') }" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item :label="$t('system.views.log.title.operationType')">
              <a-select
                v-model:value="searchModel.operationType"
                style="width: 120px"
                :options="operationTypeEnum"
                :size="formSizeConfig"
                option-label-prop="children"
                :placeholder="$t('common.notice.select')">
                <template #option="{ label }">
                  <span>{{ $t(label) }}</span>
                </template>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item>
              <a-button :size="buttonSizeConfig" @click="handleReset">{{ $t('common.button.reset') }}</a-button>
              <a-button :size="buttonSizeConfig" style="margin-left: 5px" type="primary" @click="loadData">{{ $t('common.button.search') }}</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div style="height: calc(100% - 90px);">
      <vxe-grid
        v-bind="tableProps"
        height="auto"
        :size="tableSizeConfig"
        :columns="columns"
        highlight-hover-row
        border
        show-overflow="tooltip"
        stripe>
        <template #pager>
          <vxe-pager
            v-bind="pageProps"
            :page-sizes="[500, 1000, 2000, 5000]"
            :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
        </template>
        <template #table-operation="{ row }">
          <a-button type="link" :size="tableButtonSizeConfig" @click="() => handleShowDetails(row.logId)">{{ $t('common.title.details') }}</a-button>
        </template>
        <template #table-statusCode="{ row }">
          <a-tag :color="(row.statusCode >= 200 && row.statusCode < 300) ? '#2db7f5' : '#f50'">{{ row.statusCode }}</a-tag>
        </template>
        <template #table-useTime="{ row }">
          <a-tag v-if="row.useTime !== null" :color="getUseTimeTagColor(row.useTime)">{{ row.useTime }}</a-tag>
        </template>
      </vxe-grid>
    </div>
    <a-modal
      v-model:visible="detailsModalVisible"
      width="1000px"
      :title="$t('common.title.details')">
      <template #footer>
        <a-button type="primary" @click="handleCloseDetails">{{ $t('common.title.close') }}</a-button>
      </template>
      <a-spin :spinning="getDetailLoading">
        <a-descriptions bordered>
          <a-descriptions-item :label="$t('system.views.log.title.operationType')">{{ detailsData.operationType }}</a-descriptions-item>
          <a-descriptions-item :label="$t('system.views.log.title.requestPath')">{{ detailsData.requestPath }}</a-descriptions-item>
          <a-descriptions-item :label="$t('system.views.log.title.statusCode')">
            <a-tag :color="(detailsData.statusCode >= 200 && detailsData.statusCode < 300) ? '#2db7f5' : '#f50'">{{ detailsData.statusCode }}</a-tag>
          </a-descriptions-item>

          <a-descriptions-item :label="$t('system.views.log.title.operation')" :span="2">{{ detailsData.operation }}</a-descriptions-item>
          <a-descriptions-item :label="$t('system.views.log.title.logSource')">{{ detailsData.logSource }}</a-descriptions-item>

          <a-descriptions-item :label="$t('system.views.log.title.createTime')" :span="2">{{ detailsData.createTime }}</a-descriptions-item>
          <a-descriptions-item :label="$t('system.views.log.title.ip')">{{ detailsData.ip }}</a-descriptions-item>

          <a-descriptions-item :label="$t('system.views.log.title.method')" :span="2">{{ detailsData.method }}</a-descriptions-item>
          <a-descriptions-item :label="$t('system.views.log.title.useTime')">{{ detailsData.useTime }}</a-descriptions-item>

          <a-descriptions-item :label="$t('system.views.log.title.params')" :span="3">{{ detailsData.params }}</a-descriptions-item>

          <a-descriptions-item :label="$t('system.views.log.title.result')" :span="3">{{ detailsData.result }}</a-descriptions-item>
        </a-descriptions>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from 'vue'

import dayjs from 'dayjs'

import { useVxeTable } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

import { useShowDetails } from './LogListHooks'

const parameterLike = ['operation']
const parameterEq = ['statusCode', 'operationType']

const doLoadData = async (parameter: any, searchParameter: any) => {
  const params: any = {}
  Object.keys(searchParameter).forEach(key => {
    const value = searchParameter[key]
    if (value) {
      if (parameterLike.includes(key)) {
        params[`${key}@like`] = value
      } else if (parameterEq.includes(key)) {
        params[`${key}@=`] = value
      } else if (key === 'logSource' && value.length > 0) {
        params[`${key}@in`] = value
      } else if (key === 'createTime') {
        params[`${key}@>=`] = value[0].format()
        params[`${key}@<=`] = value[1].format()
      }
    }
  })
  try {
    return await ApiService.postAjax('sys/log/list', {
      ...parameter,
      parameter: {
        ...params,
        QUERY_CREATE_UPDATE_USER: true
      }
    })
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

export default defineComponent({
  name: 'LogListView',
  setup () {
    const { tableProps, loadData, searchModel, pageProps, handleReset } = useVxeTable(doLoadData, {
      defaultSorter: {
        sortOrder: 'desc',
        sortName: 'createTime'
      },
      paging: true
    })

    /**
     * 获取使用时间tag颜色
     * @param useTime 时长
     */
    const getUseTimeTagColor = (useTime: number) => {
      if (useTime <= 300) {
        return 'blue'
      }
      if (useTime <= 500) {
        return 'green'
      }
      if (useTime <= 1000) {
        return 'orange'
      }
      return 'pink'
    }

    onMounted(loadData)
    return {
      ...SizeConfigHooks(),
      tableProps,
      loadData,
      searchModel,
      pageProps,
      handleReset,
      dayjs,
      ...useShowDetails(),
      getUseTimeTagColor
    }
  },
  data () {
    return {
      columns: [
        {
          type: 'seq',
          width: 80
        },
        {
          title: '{system.views.log.title.operation}',
          field: 'operation',
          minWidth: 200
        },
        {
          title: '{system.views.log.title.logSource}',
          field: 'logSource',
          width: 180
        },
        {
          title: '{system.views.log.title.createUserId}',
          field: 'createUserId',
          width: 120,
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName || ''
            }
            return ''
          }
        },
        {
          title: '{system.views.log.title.ip}',
          field: 'ip',
          width: 160
        },
        {
          title: '{system.views.log.title.operationType}',
          field: 'operationType',
          headerAlign: 'left',
          align: 'center',
          width: 120
        },
        {
          title: '{system.views.log.title.requestPath}',
          field: 'requestPath',
          width: 200
        },
        {
          title: '{system.views.log.title.statusCode}',
          field: 'statusCode',
          width: 120,
          headerAlign: 'left',
          align: 'center',
          slots: {
            default: 'table-statusCode'
          },
          sortable: true
        },
        {
          title: '{system.views.log.title.method}',
          field: 'method',
          width: 200
        },
        {
          title: '{system.views.log.title.useTime}',
          field: 'useTime',
          width: 120,
          headerAlign: 'left',
          align: 'center',
          slots: {
            default: 'table-useTime'
          }
        },
        {
          title: '{system.views.log.title.createTime}',
          field: 'createTime',
          width: 180,
          sortable: true,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          }
        },
        {
          title: '{common.table.operation}',
          field: 'table-operation',
          width: 100,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
      ],
      logSourceEnum: [
        {
          value: '10',
          label: 'system.views.log.title.logSourceAuto'
        },
        {
          value: '20',
          label: 'system.views.log.title.logSourceManual'
        },
        {
          value: '30',
          label: 'system.views.log.title.logSourceLoginSuccess'
        },
        {
          value: '40',
          label: 'system.views.log.title.logSourceLogout'
        },
        {
          value: '50',
          label: 'system.views.log.title.logSourceLoginFail'
        }
      ],
      operationTypeEnum: [
        {
          value: 'ADD',
          label: 'system.views.log.title.operationTypeAdd'
        },
        {
          value: 'DELETE',
          label: 'system.views.log.title.operationTypeDelete'
        },
        {
          value: 'UPDATE',
          label: 'system.views.log.title.operationTypeUpdate'
        },
        {
          value: 'QUERY',
          label: 'system.views.log.title.operationTypeQuery'
        }
      ]
    }
  }
})
</script>

<style lang="less" scoped>
.form-container {
  background: white;
  ::v-deep(.ant-form-item) {
    margin: 2px;
  }
}
</style>
