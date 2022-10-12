<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :columns="columns"
      height="auto"
      highlight-hover-row
      :toolbar-config="toolbarConfig"
      stripe>
      <template #table-operation="{ row }">
        <a-button
          v-permission="'sys:auth:offline'"
          :size="tableButtonSizeConfig"
          type="primary"
          @click="() => handleOffline(row.username, null)">
          {{ $t('system.views.onlineUser.button.offline') }}
        </a-button>
      </template>
      <template #table-expand="{ row }">
        <vxe-grid
          class="expand-wrapper"
          border
          highlight-hover-row
          :columns="expandColumns"
          :data="row.userLoginDataList"
          stripe>
          <template #expand-table-operation="data">
            <a-button
              v-permission="'sys:auth:offline'"
              :size="tableButtonSizeConfig"
              type="primary"
              @click="() => handleOffline(null, data.row.token)">
              {{ $t('system.views.onlineUser.button.offline') }}
            </a-button>
          </template>
        </vxe-grid>
      </template>
      <template #toolbar_buttons>
        <a-form style="margin-left: 10px" layout="inline" :model="searchModel">
          <a-form-item>
            <a-input
              v-model:value="searchModel.username"
              :size="formSizeConfig"
              :placeholder="$t('system.views.user.table.username')" />
          </a-form-item>
          <a-form-item>
            <a-button :size="buttonSizeConfig" type="primary" @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_tools></template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { createVNode, defineComponent, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'

import { Modal, message } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'

import { useVxeTable } from '@/components/hooks'
import { errorMessage } from '@/components/notice/SystemNotice'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'
import { tableBooleanColumn } from '@/components/common/TableCommon'
import dayjs from 'dayjs'

/**
 * 加载数据函数
 * @param parameter
 * @param searchParameter
 */
const doLoadData = async (parameter: any, searchParameter: any) => {
  try {
    return await ApiService.postAjax(
      'auth/listOnlineUser',
      Object.assign({}, parameter, searchParameter)
    )
  } catch (e) {
    errorMessage(e)
  }
}

export default defineComponent({
  name: 'OnlineUserListView',
  setup() {
    const { t } = useI18n()
    // 表格信息
    const { tableProps, loadData, searchModel } = useVxeTable(doLoadData, {
      paging: false
    })

    /**
     * 执行登出操作
     * @param username 用户名
     * @param token token
     */
    const handleOffline = (username?: string, token?: string) => {
      Modal.confirm({
        title: t('common.notice.confirm'),
        icon: createVNode(ExclamationCircleOutlined),
        content: t('system.views.onlineUser.message.offlineConfirm'),
        onOk: async () => {
          try {
            await ApiService.postAjax('auth/offline', { username, token })
            message.success(t('system.views.onlineUser.message.offlineSuccess'))
          } catch (e) {
            errorMessage(e)
          }
        }
      })
    }

    onMounted(loadData)
    return {
      tableProps,
      loadData,
      ...SizeConfigHooks(),
      searchModel,
      handleOffline
    }
  },
  data() {
    return {
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools'
        }
      },
      columns: [
        {
          title: '#',
          type: 'expand',
          fixed: 'left',
          width: 80,
          slots: {
            content: 'table-expand'
          }
        },
        {
          title: '{system.views.user.table.username}',
          field: 'username',
          width: 120,
          fixed: 'left'
        },
        {
          title: '{system.views.user.table.fullName}',
          field: 'fullName',
          width: 120,
          fixed: 'left'
        },
        {
          title: '{system.views.user.table.email}',
          field: 'email',
          minWidth: 160
        },
        {
          title: '{system.views.user.table.mobile}',
          field: 'mobile',
          minWidth: 140
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
      ],
      expandColumns: [
        {
          title: '{system.views.onlineUser.title.authType}',
          field: 'authType',
          width: 120
        },
        {
          title: '{system.views.onlineUser.title.loginType}',
          field: 'loginType',
          width: 120
        },
        {
          title: '{system.views.onlineUser.title.loginTime}',
          field: 'loginTime',
          sortable: true,
          width: 180,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          }
        },
        {
          title: '{system.views.onlineUser.title.loginIp}',
          field: 'loginIp',
          minWidth: 200
        },
        {
          ...tableBooleanColumn(
            this.$t,
            '{system.views.onlineUser.title.bindIp}',
            'bindIp'
          ).createColumn(),
          width: 120
        },
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'expand-table-operation'
          }
        }
      ]
    }
  }
})
</script>

<style lang="less" scoped>
.expand-wrapper {
  padding: 15px;
}
</style>
