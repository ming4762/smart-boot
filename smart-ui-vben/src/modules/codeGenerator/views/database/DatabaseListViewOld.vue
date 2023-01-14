<template>
  <div class="full-height database-container">
    <SmartTable use-search-form />
<!--    <a-spin class="full-height" :spinning="pageLoading">-->
<!--      <a-layout class="full-height">-->
<!--        <a-layout-content class="full-height">-->
<!--          <basic-form @register="registerSearch" @submit="loadData" :size="getFormSize" />-->
<!--          <vxe-grid-->
<!--            ref="gridRef"-->
<!--            :size="tableSizeConfig"-->
<!--            border-->
<!--            align="left"-->
<!--            stripe-->
<!--            highlight-current-row-->
<!--            :loading="loading"-->
<!--            :data="data"-->
<!--            height="auto"-->
<!--            highlight-hover-row-->
<!--            :toolbar-config="toolbarConfig"-->
<!--            :columns="columns"-->
<!--            @current-change="handleCurrentChange">-->
<!--            <template #pager>-->
<!--              <vxe-pager-->
<!--                v-model:current-page="tablePage.currentPage"-->
<!--                v-model:page-size="tablePage.pageSize"-->
<!--                :page-sizes="[500, 1000, 2000, 5000]"-->
<!--                :layouts="[-->
<!--                  'Sizes',-->
<!--                  'PrevJump',-->
<!--                  'PrevPage',-->
<!--                  'Number',-->
<!--                  'NextPage',-->
<!--                  'NextJump',-->
<!--                  'FullJump',-->
<!--                  'Total',-->
<!--                ]"-->
<!--                :total="tablePage.total"-->
<!--                @page-change="handlePageChange" />-->
<!--            </template>-->
<!--            <template #toolbar_tools>-->
<!--              <a-button-->
<!--                :size="buttonSizeConfig"-->
<!--                type="primary"-->
<!--                @click="() => openModal(true, { isAdd: true })">-->
<!--                {{ $t('common.button.add') }}-->
<!--              </a-button>-->
<!--              <a-button-->
<!--                :size="buttonSizeConfig"-->
<!--                style="margin-left: 5px"-->
<!--                type="primary"-->
<!--                danger-->
<!--                @click="handleDelete">-->
<!--                {{ $t('common.button.delete') }}-->
<!--              </a-button>-->
<!--            </template>-->
<!--            <template #toolbar_buttons>-->
<!--              <a-form layout="inline" :model="searchModel">-->
<!--                <a-form-item>-->
<!--                  <a-input-->
<!--                    v-model:value="searchModel.connectionName"-->
<!--                    :size="formSizeConfig"-->
<!--                    :placeholder="$t('generator.views.database.table.connectionName')" />-->
<!--                </a-form-item>-->
<!--                <a-form-item>-->
<!--                  <a-input-->
<!--                    v-model:value="searchModel.databaseName"-->
<!--                    :size="formSizeConfig"-->
<!--                    :placeholder="$t('generator.views.database.table.databaseName')" />-->
<!--                </a-form-item>-->
<!--                <a-form-item>-->
<!--                  <a-input-->
<!--                    v-model:value="searchModel.project"-->
<!--                    :size="formSizeConfig"-->
<!--                    :placeholder="$t('generator.views.database.table.project')" />-->
<!--                </a-form-item>-->
<!--                <a-form-item>-->
<!--                  <a-button :size="buttonSizeConfig" type="primary" @click="resetSearch">-->
<!--                    {{ $t('common.button.reset') }}-->
<!--                  </a-button>-->
<!--                  <a-button-->
<!--                    :size="buttonSizeConfig"-->
<!--                    style="margin-left: 5px"-->
<!--                    type="primary"-->
<!--                    @click="loadData">-->
<!--                    {{ $t('common.button.search') }}-->
<!--                  </a-button>-->
<!--                </a-form-item>-->
<!--              </a-form>-->
<!--            </template>-->
<!--            &lt;!&ndash;   操作列插槽   &ndash;&gt;-->
<!--            <template #table-operation="{ row }">-->
<!--              <a-dropdown>-->
<!--                <a-button :size="tableButtonSizeConfig" type="primary">-->
<!--                  Actions-->
<!--                  <DownOutlined />-->
<!--                </a-button>-->
<!--                <template #overlay>-->
<!--                  <a-menu @click="({ key }) => handleActions(row, key)">-->
<!--                    <a-menu-item key="edit" :disabled="getEditDisable(row)">-->
<!--                      {{ $t('common.button.edit') }}-->
<!--                    </a-menu-item>-->
<!--                    <a-menu-item key="testConnected">-->
<!--                      {{ $t('generator.views.database.button.testConnected') }}-->
<!--                    </a-menu-item>-->
<!--                    <a-menu-item-->
<!--                      key="createDic"-->
<!--                      :disabled="!hasPermission('db:connection:createDic')">-->
<!--                      {{ $t('generator.views.database.button.createDic') }}-->
<!--                    </a-menu-item>-->
<!--                  </a-menu>-->
<!--                </template>-->
<!--              </a-dropdown>-->
<!--            </template>-->
<!--          </vxe-grid>-->
<!--        </a-layout-content>-->
<!--        <a-layout-sider-->
<!--          theme="light"-->
<!--          style="margin-left: 5px"-->
<!--          class="layout-user-group"-->
<!--          width="200px">-->
<!--          <SetUserGroup-->
<!--            :save-button-visible="saveButtonVisible"-->
<!--            :save-handler="handleSetGroup"-->
<!--            :select-keys="selectGroupIds" />-->
<!--        </a-layout-sider>-->
<!--      </a-layout>-->
<!--    </a-spin>-->
    <!--  添加修改表单  -->
    <DatabaseListViewAddEditModal @register="registerModal" @success="loadData" />
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

import { TableSearchLayout } from '/@/components/Layout'

import DatabaseListViewAddEditModal from './components/DatabaseListViewAddEditModal.vue'
import { SmartTable } from '/@/components/SmartTable'

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
import { useModal } from '/@/components/Modal'
import BasicForm from '/@/components/Form/src/BasicForm.vue'

/**
 * 数据库连接列表
 */
export default defineComponent({
  name: 'DatabaseListView',
  components: {
    BasicForm,
    TemplateSelected,
    DownOutlined,
    SetUserGroup,
    DatabaseListViewAddEditModal,
    TableSearchLayout,
    SmartTable,
  },
  setup() {
    const templateSelectedRef = ref()
    const gridRef = ref()
    const i18n = useI18n()
    const currentRow = ref<any>()
    const loadDataVue = vueLoadData(i18n.t)
    const addEditVue = vueAddEdit()
    const actionVue = vueAction(gridRef, loadDataVue.loadData, i18n.t)
    const setUserGroupVue = vueSetUserGroup(i18n.t)
    const pageLoading = ref(false)
    const createDictVue = vueCreateDict(currentRow, i18n.t)
    const sizeConfigHoops = useSizeSetting()

    const [registerModal, { openModal }] = useModal()
    /**
     * 操作事件
     * @param row
     * @param action
     */
    const handleActions = (row: any, action: string) => {
      currentRow.value = row
      switch (action) {
        case 'edit': {
          openModal(true, {
            id: row.id,
            isAdd: false,
          })
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
      gridRef,
      pageLoading,
      ...createDictVue,
      ...sizeConfigHoops,
      registerModal,
      openModal,
    }
  },
  data() {
    return {
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
