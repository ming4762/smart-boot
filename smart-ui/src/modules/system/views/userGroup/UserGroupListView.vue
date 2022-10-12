<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      :data="data"
      height="auto"
      :sort-config="sortConfig"
      align="left"
      border
      stripe
      :toolbar-config="toolbarConfig"
      highlight-hover-row
      :size="tableSizeConfig"
      :columns="columns"
      :loading="loading"
      @sort-change="handleSortChange">
      <template #pager>
        <vxe-pager
          v-model:current-page="tablePage.currentPage"
          v-model:page-size="tablePage.pageSize"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="[
            'Sizes',
            'PrevJump',
            'PrevPage',
            'Number',
            'NextPage',
            'NextJump',
            'FullJump',
            'Total'
          ]"
          :total="tablePage.total"
          @page-change="handlePageChange"></vxe-pager>
      </template>
      <template #toolbar_buttons>
        <a-form style="margin-left: 10px" layout="inline" :model="searchModel">
          <a-form-item>
            <a-input
              v-model:value="searchModel.groupCode"
              :size="formSizeConfig"
              :placeholder="$t('system.views.userGroup.table.groupCode')" />
          </a-form-item>
          <a-form-item>
            <a-input
              v-model:value="searchModel.groupName"
              :size="formSizeConfig"
              :placeholder="$t('system.views.userGroup.table.groupName')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.userGroup.search.useYnTitle')">
            <a-select v-model:value="searchModel.useYn" style="width: 100px" :size="formSizeConfig">
              <a-select-option value="">ALL</a-select-option>
              <a-select-option :value="1">{{ $t('common.form.use') }}</a-select-option>
              <a-select-option :value="0">{{ $t('common.form.noUse') }}</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item>
            <a-button :size="buttonSizeConfig" @click="resetSearch">
              {{ $t('common.button.reset') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
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
              @click="handleShowAdd">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              v-permission="permissions.delete"
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              danger
              @click="handleDelete">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <!--   操作列插槽   -->
      <template #table-operation="{ row }">
        <a-dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <a-menu @click="({ key }) => handleActions(row, key)">
              <a-menu-item key="edit" :disabled="!hasPermission(permissions.update)">
                <EditOutlined />
                {{ $t('common.button.edit') }}
              </a-menu-item>
              <a-menu-item key="setUser" :disabled="!hasPermission(permissions.setUser)">
                <UserAddOutlined />
                {{ $t('system.views.userGroup.button.setUser') }}
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
    </vxe-grid>
    <a-modal
      v-model:visible="addEditModalVisible"
      :confirm-loading="saveLoading"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      @ok="handleSave">
      <a-spin :spinning="getLoading">
        <a-form
          :rules="formRules"
          :model="addEditModel"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 18 }">
          <a-form-item name="groupCode" :label="$t('system.views.userGroup.table.groupCode')">
            <a-input
              v-model:value="addEditModel.groupCode"
              :placeholder="$t('system.views.userGroup.validate.groupCode')" />
          </a-form-item>
          <a-form-item name="groupName" :label="$t('system.views.userGroup.table.groupName')">
            <a-input
              v-model:value="addEditModel.groupName"
              :placeholder="$t('system.views.userGroup.validate.groupName')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.remark')">
            <a-input
              v-model:value="addEditModel.remark"
              :placeholder="$t('common.formValidate.remark')" />
          </a-form-item>
          <a-form-item name="useYn" :label="$t('common.table.useYn')">
            <a-switch v-model:checked="addEditModel.useYn" />
          </a-form-item>
          <a-form-item :label="$t('common.table.seq')">
            <a-input-number v-model:value="addEditModel.seq" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
    <a-modal
      v-model:visible="setUserModalVisible"
      width="800px"
      :confirm-loading="setUserLoading"
      :title="$t('system.views.userGroup.button.setUser')"
      @ok="handleSetUser">
      <a-spin :spinning="loadUserLoading">
        <a-transfer
          class="group-set-user"
          :target-keys="targetKeys"
          :render="(item) => item.title"
          :data-source="allUserData"
          show-search
          @change="handleTransChange" />
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

import { EditOutlined, DownOutlined, UserAddOutlined } from '@ant-design/icons-vue'

import { vueLoadData, vueAddUpdate, vueDelete, vueSetUser } from './UserGroupSupport'
import AuthMixins from '@/modules/system/mixins/AuthMixins'
import { SystemPermissions } from '@/modules/system/constants/SystemConstants'

import SizeConfigHoops from '@/components/config/SizeConfigHooks'

import dayjs from 'dayjs'

import { tableUseYn } from '@/components/common/TableCommon'

/**
 * 用户组管理页面
 */
export default defineComponent({
  name: 'UserGroupView',
  components: {
    EditOutlined,
    DownOutlined,
    UserAddOutlined
  },
  mixins: [AuthMixins],
  setup() {
    const gridRef = ref()
    const loadDataVue = vueLoadData()
    const addUpdateVue = vueAddUpdate(loadDataVue.loadData)
    const deleteVue = vueDelete(gridRef, loadDataVue.loadData)
    const sizeConfigHoops = SizeConfigHoops()
    const setUserVue = vueSetUser()
    /**
     * 操作事件
     * @param row
     * @param action
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        case 'edit': {
          addUpdateVue.handleShowEdit(row.groupId)
          break
        }
        case 'setUser': {
          setUserVue.handleShowSetUser(row)
          break
        }
      }
    }
    return {
      ...loadDataVue,
      ...addUpdateVue,
      ...deleteVue,
      ...setUserVue,
      ...sizeConfigHoops,
      handleActions,
      gridRef
    }
  },
  data() {
    return {
      permissions: SystemPermissions.userGroup,
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
          title: '{system.views.userGroup.table.groupCode}',
          field: 'groupCode',
          fixed: 'left',
          width: 160
        },
        {
          title: '{system.views.userGroup.table.groupName}',
          field: 'groupName',
          fixed: 'left',
          width: 120
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true
        },
        {
          title: '{common.table.remark}',
          field: 'remark',
          minWidth: 160
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
      ]
    }
  }
})
</script>

<style lang="less" scoped>
.group-set-user {
  ::v-deep(.ant-transfer-list) {
    width: 46% !important;
    flex: none;
    height: 450px;
  }
}
</style>
