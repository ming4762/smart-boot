<template>
  <div class="full-height" style="padding: 10px">
    <Grid
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
        <Pager
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
            'Total',
          ]"
          :total="tablePage.total"
          @page-change="handlePageChange" />
      </template>
      <template #table-operation="{ row }">
        <Dropdown>
          <a-button :size="tableButtonSizeConfig" type="primary">
            Actions
            <DownOutlined />
          </a-button>
          <template #overlay>
            <Menu @click="({ key }) => handleActions(row, key)">
              <MenuItem
                key="edit"
                :disabled="
                  !hasPermission(permissions.update) || !hasSystemUserUpdate(row.userType)
                ">
                <edit-outlined />
                {{ $t('common.button.edit') }}
              </MenuItem>
              <MenuItem
                key="showAccount"
                :disabled="
                  !hasPermission('sys:account:query') || !hasSystemUserUpdate(row.userType)
                ">
                <user-outlined />
                {{ $t('system.views.user.button.showAccount') }}
              </MenuItem>
            </Menu>
          </template>
        </Dropdown>
      </template>
      <template #toolbar_buttons>
        <Form style="margin-left: 10px" layout="inline" :model="searchModel">
          <FormItem>
            <Input
              v-model:value="searchModel.username"
              style="width: 110px"
              :size="formSizeConfig"
              :placeholder="$t('system.views.user.table.username')" />
          </FormItem>
          <FormItem>
            <Input
              v-model:value="searchModel.fullName"
              style="width: 110px"
              :size="formSizeConfig"
              :placeholder="$t('system.views.user.table.fullName')" />
          </FormItem>
          <FormItem>
            <Input
              v-model:value="searchModel.email"
              style="width: 110px"
              :size="formSizeConfig"
              :placeholder="$t('system.views.user.table.email')" />
          </FormItem>
          <FormItem :label="$t('common.table.useYn')">
            <Select
              v-model:value="searchModel.useYn"
              :size="formSizeConfig"
              style="width: 80px"
              :placeholder="$t('common.table.useYn')">
              <SelectOption v-for="item in ynList" :key="item.key" :value="item.key">
                {{ item.value }}
              </SelectOption>
            </Select>
          </FormItem>
          <FormItem :label="$t('common.table.deleteYn')">
            <Select
              v-model:value="searchModel.deleteYn"
              :size="formSizeConfig"
              style="width: 80px"
              :placeholder="$t('common.table.deleteYn')">
              <SelectOption v-for="item in ynList" :key="item.key" :value="item.key">
                {{ item.value }}
              </SelectOption>
            </Select>
          </FormItem>
          <FormItem>
            <a-button :size="buttonSizeConfig" type="primary" @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
          </FormItem>
        </Form>
      </template>
      <template #toolbar_tools>
        <Form layout="inline">
          <FormItem>
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
              v-permission="permissions.createAccount"
              :size="buttonSizeConfig"
              class="button-margin"
              type="primary"
              @click="handleCreateAccount">
              {{ $t('system.views.user.button.createAccount') }}
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
          </FormItem>
        </Form>
      </template>
      <template #table-userType="{ row }">
        <span>
          {{ userTypeMap[row.userType] }}
        </span>
      </template>
    </Grid>
    <a-modal
      v-model:visible="modalVisible"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      width="600px"
      :confirm-loading="saveLoading"
      @ok="handleOk">
      <Spin :spinning="formLoading">
        <Form
          ref="formRef"
          style="padding: 10px"
          :rules="rules"
          :label-col="{ span: 4 }"
          :wrapper-col="{ span: 19 }"
          :model="addEditModel">
          <FormItem name="username" :label="$t('system.views.user.table.username')">
            <Input
              v-model:value="addEditModel.username"
              :placeholder="$t('system.views.user.validate.username')" />
          </FormItem>
          <FormItem name="fullName" :label="$t('system.views.user.table.fullName')">
            <Input
              v-model:value="addEditModel.fullName"
              :placeholder="$t('system.views.user.validate.fullName')" />
          </FormItem>
          <FormItem :label="$t('system.views.user.table.email')">
            <Input
              v-model:value="addEditModel.email"
              :placeholder="$t('system.views.user.validate.email')" />
          </FormItem>
          <FormItem name="userType" :label="$t('system.views.user.table.userType')">
            <Select v-model:value="addEditModel.userType">
              <SelectOption v-if="hasPermissionUpdateSystemUser" :value="SYS_USER_TYPE">
                系统用户
              </SelectOption>
              <SelectOption
                v-for="item in userTypeList"
                :key="'userType_' + item.dictItemCode"
                :value="item.dictItemCode">
                {{ item.dictItemName }}
              </SelectOption>
            </Select>
          </FormItem>
          <FormItem :label="$t('system.views.user.table.mobile')">
            <Input
              v-model:value="addEditModel.mobile"
              :placeholder="$t('system.views.user.validate.mobile')" />
          </FormItem>
          <FormItem :label="$t('common.table.seq')">
            <InputNumber style="width: 100%" v-model:value="addEditModel.seq" :default-value="1" />
          </FormItem>
          <FormItem name="deptId" :label="$t('system.views.user.form.dept')">
            <TreeSelect
              :tree-data="deptTreeData"
              :field-names="deptTreeFieldNames"
              :placeholder="$t('system.views.user.validate.selectDept')"
              show-search
              allow-clear
              :disabled="dataScopeDisable"
              v-model:value="addEditModel.deptId" />
          </FormItem>
          <FormItem
            name="dataScopeList"
            :rules="[
              {
                required: needCheckDataScope,
                message: $t('system.views.user.validate.selectDataScope'),
              },
            ]"
            :label="$t('system.views.user.form.dataScope')">
            <Select
              mode="multiple"
              :disabled="dataScopeDisable"
              v-model:value="addEditModel.dataScopeList">
              <SelectOption
                v-for="item in dataScopeList"
                :key="'data-scope_' + item.key"
                :value="item.key">
                {{ $t(item.value) }}
              </SelectOption>
            </Select>
          </FormItem>
        </Form>
      </Spin>
    </a-modal>
    <!--  更新账户信息  -->
    <UserAccountUpdateModal ref="userAccountRef" />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref, reactive } from 'vue'
import { useI18n } from 'vue-i18n'

import { DownOutlined, EditOutlined, UserOutlined } from '@ant-design/icons-vue'
import {
  MenuItem,
  Menu,
  Dropdown,
  FormItem,
  Form,
  Input,
  Select,
  SelectOption,
  InputNumber,
  TreeSelect,
  Spin,
  Button,
  Modal,
} from 'ant-design-vue'

import { SYS_USER_TYPE, DATA_SCOPE } from '../../constants/SystemConstants'
import { Grid, Pager } from 'vxe-table'

import {
  vueLoadData,
  vueAddEdit,
  userOperationHoops,
  useCreateAccount,
  useLoadUserType,
} from './UserListSupport'

import { SystemPermissions } from '../../constants/SystemConstants'
import dayjs from 'dayjs'
import { tableUseYn, tableDeleteYn } from '/@/components/common/TableCommon'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { hasPermission } from '/@/common/auth/AuthUtils'
import UserAccountUpdateModal from './account/UserAccountUpdateModal.vue'
import { useLoadDeptTreeData } from '/@/modules/system/hooks/dept/SysDeptHooks'

export default defineComponent({
  name: 'UserListPage',
  components: {
    UserAccountUpdateModal,
    DownOutlined,
    EditOutlined,
    UserOutlined,
    Grid,
    Pager,
    MenuItem,
    Menu,
    Dropdown,
    FormItem,
    Form,
    Input,
    Select,
    SelectOption,
    TreeSelect,
    Spin,
    InputNumber,
    AModal: Modal,
    AButton: Button,
  },
  setup() {
    const tableRef = ref()
    const userAccountRef = ref()
    const { t } = useI18n()
    /**
     * 是否有修改系统用户的权限
     */
    const hasPermissionUpdateSystemUser = hasPermission('sys:systemUser:update')
    const hasSystemUserUpdate = (type: string) => {
      return hasPermissionUpdateSystemUser || type !== SYS_USER_TYPE
    }
    const loadDataVue = vueLoadData()
    const addEditVue = vueAddEdit(loadDataVue.loadData, t)
    // 用户操作hoops
    const userOperationVue = userOperationHoops(
      tableRef,
      t,
      loadDataVue.loadData,
      hasPermissionUpdateSystemUser,
    )

    const handleActions = (row: any, key: string) => {
      const { userId } = row
      switch (key) {
        case 'edit': {
          addEditVue.handleShowUpdate(userId)
          break
        }
        case 'showAccount': {
          userAccountRef.value.show(userId)
          break
        }
      }
    }
    const { userTypeList } = useLoadUserType()
    const userTypeMap = computed(() => {
      const result: { [index: string]: string } = {}
      result[SYS_USER_TYPE] = '系统用户'
      for (let userType of userTypeList.value) {
        result[userType.dictItemCode] = userType.dictItemName
      }
      return result
    })
    return {
      tableRef,
      hasPermissionUpdateSystemUser,
      hasSystemUserUpdate,
      ...userOperationVue,
      ...loadDataVue,
      ...useSizeSetting(),
      ...addEditVue,
      permissions: SystemPermissions.user,
      ...useCreateAccount(tableRef, t, hasPermissionUpdateSystemUser),
      handleActions,
      hasPermission,
      userAccountRef,
      userTypeList,
      userTypeMap,
      dataScopeList: reactive(DATA_SCOPE),
      ...useLoadDeptTreeData(),
    }
  },
  data() {
    return {
      SYS_USER_TYPE,
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons',
          tools: 'toolbar_tools',
        },
      },
      deptTreeFieldNames: {
        children: 'children',
        label: 'deptName',
        value: 'deptId',
      },
      sortConfig: {
        defaultSort: {
          field: 'seq',
          order: 'asc',
        },
        remote: true,
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left',
        },
        {
          title: '{system.views.user.table.username}',
          field: 'username',
          width: 120,
          fixed: 'left',
        },
        {
          title: '{system.views.user.table.fullName}',
          field: 'fullName',
          width: 120,
          fixed: 'left',
        },
        {
          title: '{system.views.user.table.userType}',
          field: 'userType',
          width: 120,
          slots: {
            default: 'table-userType',
          },
        },
        {
          title: '{system.views.user.table.email}',
          field: 'email',
          minWidth: 160,
        },
        {
          title: '{system.views.user.table.mobile}',
          field: 'mobile',
          minWidth: 140,
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true,
        },
        {
          ...tableDeleteYn(this.$t).createColumn(),
          sortable: true,
        },
        {
          title: '{common.table.seq}',
          field: 'seq',
          width: 100,
          sortable: true,
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
          sortable: true,
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
          },
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
          sortable: true,
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
          },
        },
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation',
          },
        },
      ],
      rules: {
        username: [
          {
            required: true,
            message: this.$t('system.views.user.validate.username'),
            trigger: 'blur',
          },
        ],
        fullName: [
          {
            required: true,
            message: this.$t('system.views.user.validate.fullName'),
            trigger: 'blur',
          },
        ],
        userType: [
          {
            required: true,
            message: this.$t('system.views.user.validate.selectUserType'),
            trigger: 'change',
          },
        ],
      },
      ynList: [
        {
          key: -1,
          value: 'ALL',
        },
        {
          key: 1,
          value: 'Y',
        },
        {
          key: 0,
          value: 'N',
        },
      ],
    }
  },
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
