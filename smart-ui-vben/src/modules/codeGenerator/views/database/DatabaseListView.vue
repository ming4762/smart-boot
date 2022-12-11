<template>
  <div class="full-height database-container">
    <a-spin class="full-height" :spinning="pageLoading">
      <a-layout class="full-height">
        <a-layout-content class="full-height">
          <div class="full-height" style="padding: 5px; background: white">
            <Grid
              ref="gridRef"
              :size="tableSizeConfig"
              border
              align="left"
              stripe
              highlight-current-row
              :loading="loading"
              :data="data"
              height="auto"
              highlight-hover-row
              :toolbar-config="toolbarConfig"
              :columns="columns"
              @current-change="handleCurrentChange">
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
              <template #toolbar_tools>
                <a-button :size="buttonSizeConfig" type="primary" @click="handleShowAdd">
                  {{ $t('common.button.add') }}
                </a-button>
                <a-button
                  :size="buttonSizeConfig"
                  style="margin-left: 5px"
                  type="primary"
                  danger
                  @click="handleDelete">
                  {{ $t('common.button.delete') }}
                </a-button>
              </template>
              <template #toolbar_buttons>
                <a-form layout="inline" :model="searchModel">
                  <a-form-item>
                    <a-input
                      v-model:value="searchModel.connectionName"
                      :size="formSizeConfig"
                      :placeholder="$t('generator.views.database.table.connectionName')" />
                  </a-form-item>
                  <a-form-item>
                    <a-input
                      v-model:value="searchModel.databaseName"
                      :size="formSizeConfig"
                      :placeholder="$t('generator.views.database.table.databaseName')" />
                  </a-form-item>
                  <a-form-item>
                    <a-input
                      v-model:value="searchModel.project"
                      :size="formSizeConfig"
                      :placeholder="$t('generator.views.database.table.project')" />
                  </a-form-item>
                  <a-form-item>
                    <a-button :size="buttonSizeConfig" type="primary" @click="resetSearch">
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
              <!--   操作列插槽   -->
              <template #table-operation="{ row }">
                <a-dropdown>
                  <a-button :size="tableButtonSizeConfig" type="primary">
                    Actions
                    <DownOutlined />
                  </a-button>
                  <template #overlay>
                    <a-menu @click="({ key }) => handleActions(row, key)">
                      <a-menu-item key="edit" :disabled="getEditDisable(row)">
                        {{ $t('common.button.edit') }}
                      </a-menu-item>
                      <a-menu-item key="testConnected">
                        {{ $t('generator.views.database.button.testConnected') }}
                      </a-menu-item>
                      <a-menu-item
                        key="createDic"
                        :disabled="!hasPermission('db:connection:createDic')">
                        {{ $t('generator.views.database.button.createDic') }}
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </template>
            </Grid>
          </div>
        </a-layout-content>
        <a-layout-sider
          theme="light"
          style="margin-left: 5px"
          class="layout-user-group"
          width="200px">
          <SetUserGroup
            :save-button-visible="saveButtonVisible"
            :save-handler="handleSetGroup"
            :select-keys="selectGroupIds" />
        </a-layout-sider>
      </a-layout>
    </a-spin>
    <a-modal
      v-model:visible="addEditModalVisible"
      style="width: 700px"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      :confirm-loading="saveLoading"
      @ok="handleSave">
      <a-form
        ref="formRef"
        :model="addEditModel"
        :rules="formRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol">
        <a-form-item
          name="connectionName"
          :label="$t('generator.views.database.table.connectionName')">
          <a-input
            v-model:value="addEditModel.connectionName"
            :placeholder="$t('generator.views.database.validate.connectionName')" />
        </a-form-item>
        <a-form-item name="databaseName" :label="$t('generator.views.database.table.databaseName')">
          <a-input
            v-model:value="addEditModel.databaseName"
            :placeholder="$t('generator.views.database.validate.databaseName')" />
        </a-form-item>
        <a-form-item name="type" :label="$t('generator.views.database.table.type')">
          <a-select
            v-model:value="addEditModel.type"
            :placeholder="$t('generator.views.database.validate.type')">
            <a-select-option v-for="item in dbType" :key="item" :value="item">
              {{ item }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="project" :label="$t('generator.views.database.table.project')">
          <a-input
            v-model:value="addEditModel.project"
            :placeholder="$t('generator.views.database.validate.project')" />
        </a-form-item>
        <a-form-item name="url" :label="$t('generator.views.database.table.url')">
          <a-input
            v-model:value="addEditModel.url"
            :placeholder="$t('generator.views.database.validate.url')" />
        </a-form-item>
        <a-form-item name="username" :label="$t('generator.views.database.table.username')">
          <a-input
            v-model:value="addEditModel.username"
            :placeholder="$t('generator.views.database.validate.username')" />
        </a-form-item>
        <a-form-item name="password" :label="$t('generator.views.database.table.password')">
          <a-input-password
            v-model:value="addEditModel.password"
            :placeholder="$t('generator.views.database.validate.password')" />
        </a-form-item>
        <a-form-item name="tableSchema" :label="$t('generator.views.database.table.tableSchema')">
          <a-input v-model:value="addEditModel.tableSchema" />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      v-model:visible="creatDicModalVisible"
      width="600px"
      :title="$t('generator.views.database.common.chooseTemplate')"
      @ok="handleCreate">
      <TemplateSelected
        ref="templateSelectedRef"
        template-type="template_db_dict"
        @template-change="handleTemplateChange" />
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { DownOutlined } from '@ant-design/icons-vue'
import { Grid, Pager } from 'vxe-table'
import {
  Form,
  FormItem,
  Input,
  Menu,
  MenuItem,
  Spin,
  Modal,
  Select,
  SelectOption,
  Layout,
  LayoutContent,
  Dropdown,
  Divider,
} from 'ant-design-vue'

import {
  vueLoadData,
  vueAddEdit,
  vueAction,
  vueSetUserGroup,
  vueCreateDict,
} from './DatabaseListSupport'

import TemplateSelected from './components/TemplateSelected.vue'
import SetUserGroup from '/@/modules/system/components/user/SetUserGroup.vue'

import { hasPermission, isSuperAdmin } from '/@/common/auth/AuthUtils'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import dayjs from 'dayjs'

/**
 * 数据库连接列表
 */
export default defineComponent({
  name: 'DatabaseListView',
  components: {
    TemplateSelected,
    DownOutlined,
    SetUserGroup,
    Grid,
    Pager,
    ASpin: Spin,
    ALayout: Layout,
    ALayoutContent: LayoutContent,
    AInput: Input,
    AForm: Form,
    AFormItem: FormItem,
    AMenu: Menu,
    AMenuItem: MenuItem,
    ASelect: Select,
    ASelectOption: SelectOption,
    AModal: Modal,
    ADropdown: Dropdown,
  },
  setup() {
    const templateSelectedRef = ref()
    const gridRef = ref()
    const formRef = ref()
    const i18n = useI18n()
    const currentRow = ref<any>()
    const loadDataVue = vueLoadData()
    const addEditVue = vueAddEdit(loadDataVue.loadData, formRef, i18n.t)
    const actionVue = vueAction(gridRef, loadDataVue.loadData, i18n.t)
    const setUserGroupVue = vueSetUserGroup(i18n.t)
    const pageLoading = ref(false)
    const createDictVue = vueCreateDict(currentRow, i18n.t)
    const sizeConfigHoops = useSizeSetting()
    /**
     * 操作事件
     * @param row
     * @param action
     */
    const handleActions = (row: any, action: string) => {
      currentRow.value = row
      switch (action) {
        case 'edit': {
          addEditVue.handleShowEdit(row.id)
          break
        }
        case 'testConnected': {
          // 测试连接
          actionVue.handleTestConnected(row, pageLoading)
          break
        }
        case 'createDic': {
          createDictVue.handleShowCreateDictModal()
          if (templateSelectedRef.value) {
            templateSelectedRef.value.loadData()
          }
          break
        }
      }
    }
    return {
      templateSelectedRef,
      ...loadDataVue,
      ...addEditVue,
      ...actionVue,
      ...setUserGroupVue,
      handleActions,
      formRef,
      gridRef,
      pageLoading,
      ...createDictVue,
      ...sizeConfigHoops,
    }
  },
  data() {
    return {
      dbType: ['MYSQL', 'SQL_SERVER', 'ORACLE'],
      labelCol: {
        span: 5,
      },
      wrapperCol: {
        span: 18,
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left',
        },
        {
          title: '{generator.views.database.table.connectionName}',
          field: 'connectionName',
          width: 160,
          fixed: 'left',
        },
        {
          title: '{generator.views.database.table.databaseName}',
          field: 'databaseName',
          width: 160,
          fixed: 'left',
        },
        {
          title: '{generator.views.database.table.type}',
          field: 'type',
          width: 120,
        },
        {
          title: '{generator.views.database.table.project}',
          field: 'project',
          width: 120,
        },
        {
          title: '{generator.views.database.table.url}',
          field: 'url',
          minWidth: 200,
          showOverflow: 'tooltip',
        },
        {
          title: '{generator.views.database.table.username}',
          field: 'username',
          width: 120,
        },
        {
          title: '{generator.views.database.table.tableSchema}',
          field: 'tableSchema',
          width: 120,
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
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
          buttons: 'toolbar_buttons',
        },
      },
    }
  },
  methods: {
    hasPermission(permission: string) {
      return isSuperAdmin() || hasPermission(permission)
    },
  },
})
</script>

<style lang="less" scoped>
.database-container {
  ::v-deep(.ant-spin-nested-loading) {
    height: 100%;
  }
  ::v-deep(.ant-spin-container) {
    height: 100%;
    overflow: hidden;
  }
}
</style>
