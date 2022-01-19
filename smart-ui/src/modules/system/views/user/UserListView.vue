<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="tableRef"
      :columns="columns"
      :data="data"
      stripe
      :sort-config="sortConfig"
      highlight-hover-row
      :loading="loading"
      height="auto"
      :toolbar-config="toolbarConfig"
      align="left"
      border
      :size="tableSizeConfig"
      @sort-change="handleSortChange">
      <template #pager>
        <vxe-pager
          v-model:current-page="tablePage.currentPage"
          v-model:page-size="tablePage.pageSize"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']"
          :total="tablePage.total"
          @page-change="handlePageChange">
        </vxe-pager>
      </template>
      <template #table-operation="{ row }">
        <a-button v-permission="permissions.update" :size="buttonSizeConfig" type="link" @click="() => handleShowUpdate(row.userId)">{{ $t('common.button.edit') }}</a-button>
      </template>
      <template #toolbar_buttons>
        <a-form style="margin-left: 10px" layout="inline" :model="searchModel">
          <a-form-item>
            <a-input v-model:value="searchModel.username" style="width: 110px" :size="formSizeConfig" :placeholder="$t('system.views.user.table.username')" />
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="searchModel.fullName" style="width: 110px" :size="formSizeConfig" :placeholder="$t('system.views.user.table.fullName')" />
          </a-form-item>
          <a-form-item>
            <a-input v-model:value="searchModel.email" style="width: 110px" :size="formSizeConfig" :placeholder="$t('system.views.user.table.email')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.useYn')">
            <a-select v-model:value="searchModel.useYn" :size="formSizeConfig" style="width: 80px" :placeholder="$t('common.table.useYn')">
              <a-select-option v-for="item in ynList" :key="item.key" :value="item.key">{{ item.value }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('common.table.deleteYn')">
            <a-select v-model:value="searchModel.deleteYn" :size="formSizeConfig" style="width: 80px" :placeholder="$t('common.table.deleteYn')">
              <a-select-option v-for="item in ynList" :key="item.key" :value="item.key">{{ item.value }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button :size="buttonSizeConfig" type="primary" @click="loadData">{{ $t('common.button.search') }}</a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              v-permission="permissions.add"
              :size="buttonSizeConfig"
              type="primary"
              class="button-margin"
              @click="handleShowSave">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              v-permission="permissions.useYn"
              :size="buttonSizeConfig"
              class="button-margin"
              type="primary"
              @click="() => handleSetUseYn(true)">
              {{ $t('common.button.use') }}
            </a-button>
            <a-button
              v-permission="permissions.useYn"
              danger
              class="button-margin"
              :size="buttonSizeConfig"
              type="primary"
              @click="() => handleSetUseYn(false)">
              {{ $t('common.button.noUse') }}
            </a-button>
            <a-button
              v-permission="permissions.delete"
              danger
              class="button-margin"
              :size="buttonSizeConfig"
              type="primary"
              @click="handleDeleteUser">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
    </vxe-grid>
    <a-modal
      v-model:visible="modalVisible"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      width="600px"
      :confirm-loading="saveLoading"
      @ok="handleOk">
      <a-spin :spinning="formLoading">
        <a-form ref="formRef" :rules="rules" :label-col="{span: 4}" :wrapper-col="{span: 19}" :model="addEditModel">
          <a-form-item name="username" :label="$t('system.views.user.table.username')">
            <a-input v-model:value="addEditModel.username" :placeholder="$t('system.views.user.validate.username')" />
          </a-form-item>
          <a-form-item name="fullName" :label="$t('system.views.user.table.fullName')">
            <a-input v-model:value="addEditModel.fullName" :placeholder="$t('system.views.user.validate.fullName')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.user.table.email')">
            <a-input v-model:value="addEditModel.email" :placeholder="$t('system.views.user.validate.email')" />
          </a-form-item>
          <a-form-item name="userType" :label="$t('system.views.user.table.userType')">
            <a-radio-group v-model:value="addEditModel.userType">
              <a-radio value="10">{{ $t('system.views.user.form.systemUser') }}</a-radio>
              <a-radio value="20">{{ $t('system.views.user.form.businessUser') }}</a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item :label="$t('system.views.user.table.mobile')">
            <a-input v-model:value="addEditModel.mobile" :placeholder="$t('system.views.user.validate.mobile')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.seq')">
            <a-input-number v-model:value="addEditModel.seq" :default-value="1" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { Modal } from 'ant-design-vue'

import { vueLoadData, vueAddEdit, userOperationHoops } from './UserListSupport'

import ApiService from '@/common/utils/ApiService'
import { SystemPermissions } from '../../constants/SystemConstants'
import dayjs from 'dayjs'
import { tableUseYn, tableDeleteYn } from '@/components/common/TableCommon'
import SizeConfigHoops from '@/components/config/SizeConfigHooks'


export default defineComponent({
  name: 'UserListPage',
  setup () {
    const tableRef = ref()
    const { t } = useI18n()
    const loadDataVue = vueLoadData()
    const addEditVue = vueAddEdit(loadDataVue.loadData)
    // 用户操作hoops
    const userOperationVue = userOperationHoops(tableRef, t, loadDataVue.loadData)
    /**
     * 设置激活状态
     * @param ident
     * @param userId
     * @param checked
     * @returns {Promise<void>}
     */
    const handleSetYn = (ident: string, userId: number, checked: boolean) => {
      // TODO:国际化
      const content = `确定要${ident === 'use_yn' ? (checked ? '启用' : '停用') : (checked ? '删除' : '启用')}该用户吗`
      Modal.confirm({
        title: '确认',
        content: content,
        onOk: async () => {
          await ApiService.postAjax('monitor/user/setYn', {
            field: ident,
            yn: checked,
            id: userId
          })
          loadDataVue.loadData()
        }
      })
    }
    return {
      tableRef,
      ...userOperationVue,
      ...loadDataVue,
      ...SizeConfigHoops(),
      handleSetYn,
      ...addEditVue,
      permissions: SystemPermissions.user
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools'
        }
      },
      sortConfig: {
        defaultSort: {
          field: 'seq',
          order: 'asc'
        },
        remote: true
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left'
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
          ...tableUseYn(this.$t).createColumn(),
          sortable: true
        },
        {
          ...tableDeleteYn(this.$t).createColumn(),
          sortable: true
        },
        {
          title: '{common.table.seq}',
          field: 'seq',
          width: 100,
          sortable: true
        },
        {
          title: '{common.table.createTime}',
          field: 'createTime',
          width: 165,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          sortable: true
        },
        {
          title: '{common.table.createUser}',
          field: 'createUserId',
          width: 120,
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName
            }
            return ''
          }
        },
        {
          title: '{common.table.updateTime}',
          field: 'updateTime',
          width: 165,
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          sortable: true
        },
        {
          title: '{common.table.updateUser}',
          field: 'updateUserId',
          width: 120,
          formatter: ({ row }: any) => {
            if (row.updateUser) {
              return row.updateUser.fullName
            }
            return ''
          }
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
      rules: {
        username: [
          { required: true, message: this.$t('system.views.user.validate.username'), trigger: 'blur' }
        ],
        fullName: [
          { required: true, message: this.$t('system.views.user.validate.fullName'), trigger: 'blur' }
        ]
      },
      ynList: [
        {
          key: -1,
          value: 'ALL'
        },
        {
          key: 1,
          value: 'Y'
        },
        {
          key: 0,
          value: 'N'
        }
      ]
    }
  }
})
</script>

<style lang="less" scoped>
.app-transfer ::v-deep(.ant-transfer-list) {
  width: 300px;
  height: 400px;
}
.button-margin {
  margin-left: 5px;
}
</style>
