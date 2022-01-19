<template>
  <div class="full-height page-container">
    <a-layout class="full-height">
      <a-layout-content class="full-height">
        <vxe-grid
          ref="gridRef"
          :data="data"
          height="auto"
          align="left"
          border
          stripe
          highlight-current-row
          highlight-hover-row
          :size="tableSizeConfig"
          :toolbar-config="toolbarConfig"
          :loading="loading"
          :columns="columns"
          @current-change="handleCurrentChange">
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
          <template #toolbar_buttons>
            <a-form style="margin-left: 5px" layout="inline" :model="searchModel">
              <a-form-item>
                <a-input v-model:value="searchModel.roleName" :size="formSizeConfig" :placeholder="$t('system.views.role.table.roleName')" />
              </a-form-item>
              <a-form-item>
                <a-input v-model:value="searchModel.roleCode" :size="formSizeConfig" :placeholder="$t('system.views.role.table.roleCode')" />
              </a-form-item>
              <a-form-item>
                <a-button type="primary" :size="buttonSizeConfig" @click="resetSearch">{{ $t('common.button.reset') }}</a-button>
                <a-button :size="buttonSizeConfig" style="margin-left: 5px" type="primary" @click="loadRoleList">{{ $t('common.button.search') }}</a-button>
              </a-form-item>
            </a-form>
          </template>
          <template #toolbar_tools>
            <a-form layout="inline">
              <a-form-item>
                <a-button v-permission="permissions.add" :size="buttonSizeConfig" type="primary" @click="handleShowAdd">{{ $t('common.button.add') }}</a-button>
                <a-button v-permission="permissions.delete" :size="buttonSizeConfig" type="primary" style="margin-left: 5px" danger @click="handleDelete">{{ $t('common.button.delete') }}</a-button>
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
                  <a-menu-item key="edit" :disabled="!hasPermission(permissions.update)">{{ $t('common.button.edit') }}</a-menu-item>
                  <a-menu-item key="setUser" :disabled="!hasPermission(permissions.setRoleUser)">{{ $t('system.views.role.button.setRoleUser') }}</a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </template>
        </vxe-grid>
      </a-layout-content>
      <a-layout-sider theme="light" class="layout-set-function" width="240px">
        <RoleSetFunction :role-id="currentRow.roleId" />
      </a-layout-sider>
    </a-layout>
    <a-modal
      v-model:visible="addEditModalVisible"
      :confirm-loading="saveLoading"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      @ok="handleSave">
      <a-spin :spinning="getLoading">
        <a-form
          :rules="formRules"
          :model="addEditModel"
          :label-col="{span: 4}"
          :wrapper-col="{span: 19}">
          <a-form-item name="roleName" :label="$t('system.views.role.table.roleName')">
            <a-input v-model:value="addEditModel.roleName" :placeholder="$t('system.views.role.validate.roleName')" />
          </a-form-item>
          <a-form-item name="roleCode" :label="$t('system.views.role.table.roleCode')">
            <a-input v-model:value="addEditModel.roleCode" :placeholder="$t('system.views.role.validate.roleCode')" />
          </a-form-item>
          <a-form-item name="useYn" :label="$t('common.table.useYn')">
            <a-switch v-model:checked="addEditModel.useYn" />
          </a-form-item>
          <a-form-item name="roleType" :label="$t('system.views.role.table.roleType')">
            <a-input v-model:value="addEditModel.roleType" :placeholder="$t('system.views.role.validate.roleType')" />
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
      :title="$t('system.views.role.button.setRoleUser')"
      @ok="handleSetUser">
      <a-spin :spinning="getUserLoading">
        <a-transfer
          class="group-set-user"
          :render="item => item.title"
          :target-keys="targetKeysModel"
          :data-source="allUserData"
          @change="handleTransChange" />
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, createVNode } from 'vue'
import { useI18n } from 'vue-i18n'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined, DownOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

import { VxeTableInstance } from 'vxe-table'
import AuthMixins from '@/modules/system/mixins/AuthMixins'
import { SystemPermissions } from '@/modules/system/constants/SystemConstants'

import RoleSetFunction from './RoleSetFunction.vue'

import { vueAddUpdate, vueLoadRoleData, vueSetUser } from './RoleListSupport'

import ApiService from '@/common/utils/ApiService'
import { tableUseYn } from '@/components/common/TableCommon'
import SizeConfigHoops from '@/components/config/SizeConfigHooks'



/**
 * 角色管理页面
 */
export default defineComponent({
  name: 'RoleListView',
  components: {
    RoleSetFunction,
    DownOutlined
  },
  mixins: [AuthMixins],
  setup () {
    const { t } = useI18n()
    const gridRef = ref({} as VxeTableInstance)
    // 当前行
    const currentRow = ref<any>({})
    const loadRoleDataVue = vueLoadRoleData()
    const addUpdateVue = vueAddUpdate(loadRoleDataVue.loadRoleList)
    const setUserVue = vueSetUser()
    /**
     * 操作事件
     * @param row
     * @param action
     */
    const handleActions = (row: any, action: string) => {
      switch (action) {
        case 'edit': {
          addUpdateVue.handleShowEdit(row.roleId)
          break
        }
        case 'setUser': {
          setUserVue.handleShowSetUser(row.roleId)
          break
        }
      }
    }
    /**
     * 当前行变更事件
     */
    const handleCurrentChange = ({ row }: any) => {
      currentRow.value = row
    }
    /**
     * 删除操作
     */
    const handleDelete = () => {
      // 获取选中行
      const selectRows = gridRef.value.getCheckboxRecords()
      if (selectRows.length === 0) {
        message.error(t('common.notice.deleteChoose'))
        return false
      }
      Modal.confirm({
        title: t('common.button.confirm'),
        icon: createVNode(ExclamationCircleOutlined),
        content: t('common.notice.deleteConfirm'),
        onOk: async () => {
          await ApiService.postAjax('sys/role/batchDeleteById', selectRows.map(item => item.roleId))
          loadRoleDataVue.loadRoleList()
        }
      })
    }
    return {
      ...loadRoleDataVue,
      ...addUpdateVue,
      ...setUserVue,
      handleActions,
      handleDelete,
      gridRef,
      handleCurrentChange,
      currentRow,
      ...SizeConfigHoops()
    }
  },
  data () {
    return {
      permissions: SystemPermissions.role,
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools'
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
          title: '{system.views.role.table.roleName}',
          field: 'roleName',
          width: 120,
          fixed: 'left'
        },
        {
          title: '{system.views.role.table.roleCode}',
          field: 'roleCode',
          width: 150,
          fixed: 'left'
        },
        {
          title: '{system.views.role.table.roleType}',
          field: 'roleType',
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
.layout-set-function {
  margin-left: 5px;
}
.group-set-user {
  ::v-deep(.ant-transfer-list) {
    width: 46% !important;
    flex: none;
    height: 450px;
  }
}
</style>
