<template>
  <div id="codeTemplateContainer" class="code-container full-height">
    <a-layout class="full-height">
      <a-layout-sider theme="light" style="margin-right: 5px" width="200px">
        <TemplateGroup @change="handleGroupChange" />
      </a-layout-sider>
      <a-layout-content class="full-height">
        <div class="full-height" style="padding: 5px; background: white">
          <vxe-grid
            ref="gridRef"
            highlight-current-row
            :data="data"
            height="auto"
            stripe
            :size="tableSizeConfig"
            highlight-hover-row
            border
            align="center"
            :toolbar-config="toolbarConfig"
            :loading="tableDataLoading"
            :columns="columns"
            @current-change="handleCurrentChange">
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
            <template #table-tools>
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
            <template #table-buttons>
              <a-form layout="inline">
                <a-form-item :label="$t('generator.views.template.table.name')">
                  <a-input v-model:value="searchModel.name" :size="formSizeConfig" />
                </a-form-item>
                <a-form-item>
                  <a-button :size="buttonSizeConfig" type="primary" @click="loadData">
                    {{ $t('common.button.search') }}
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
                  <a-menu @click="({ key }) => handleSelectActions(row.templateId, key)">
                    <a-menu-item key="edit" :disabled="getEditDisable(row)">
                      {{ $t('common.button.edit') }}
                    </a-menu-item>
                    <a-menu-item key="look">{{ $t('common.button.look') }}</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </vxe-grid>
        </div>
      </a-layout-content>
      <a-layout-sider theme="light" class="layout-user-group" width="200px">
        <TemplateSetUserGroup :template="currentRow" />
      </a-layout-sider>
    </a-layout>
    <a-modal
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      :body-style="{ height: 'calc(100% - 110px)' }"
      width="100%"
      style="top: 0; height: 100%; padding-bottom: 0"
      :ok-button-props="{ disabled: isShow }"
      :get-container="modalContainer"
      :confirm-loading="saveLoading"
      :visible="addEditModalVisible"
      @ok="handleSave"
      @cancel="addEditModalVisible = false">
      <a-spin style="height: 100%" :spinning="getLoading">
        <a-form
          ref="saveFormRef"
          class="full-height"
          :wrapper-col="wrapperCol"
          :label-col="labelCol"
          :model="formModel"
          :rules="formRules">
          <a-row>
            <a-col :span="6">
              <a-form-item
                :label="$t('generator.views.template.table.templateType')"
                name="templateType">
                <a-select
                  v-model:value="formModel.templateType"
                  :size="formSizeConfig"
                  :disabled="isShow"
                  :placeholder="$t('generator.views.template.validate.templateType')">
                  <a-select-option
                    v-for="(value, key) in templateType"
                    :key="key"
                    :value="value.value">
                    {{ $t(value.label) }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="6">
              <a-form-item :label="$t('generator.views.template.table.name')" name="name">
                <a-input
                  v-model:value="formModel.name"
                  :size="formSizeConfig"
                  :disabled="isShow"
                  :placeholder="$t('generator.views.template.validate.name')"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item :label="$t('generator.views.template.table.remark')" name="remark">
                <a-input
                  v-model:value="formModel.remark"
                  :size="formSizeConfig"
                  :disabled="isShow"
                  :placeholder="$t('generator.views.template.validate.remark')"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item
                :label="$t('generator.views.template.table.filenameSuffix')"
                name="filenameSuffix">
                <a-input
                  v-model:value="formModel.filenameSuffix"
                  :size="formSizeConfig"
                  :disabled="isShow"
                  placeholder="请输入文件名后缀"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="6">
              <a-form-item :label="$t('generator.views.template.table.language')">
                <a-select
                  v-model:value="formModel.language"
                  :size="formSizeConfig"
                  :disabled="isShow">
                  <a-select-option
                    v-for="(value, key) in extensionLanguageMap"
                    :key="key"
                    :value="key">
                    {{ value }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <div class="code-edit-container">
            <Codemirror
              ref="codemirror"
              :read-only="isShow"
              :code="code"
              :mode="formModel.language" />
          </div>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, createVNode } from 'vue'
import { useI18n } from 'vue-i18n'

import type { VxeTableInstance } from 'vxe-table'

import { DownOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue'
import { message, Modal } from 'ant-design-vue'

import Codemirror from '@/components/codemirror/Codemirror.vue'

import ApiService from '@/common/utils/ApiService'
import { vueRelateUserGroup, vueLoadData, vueAddEdit } from './CodeTemplateSupport'

import TemplateSetUserGroup from './components/TemplateSetUserGroup.vue'
import TemplateGroup from './components/TemplateGroup.vue'
import { getCurrentUserId, getUserRole, isSuperAdmin } from '@/common/auth/AuthUtils'
import { TemplateType as templateTypeConstants } from '@/modules/generator/constants/DatabaseConstants'
import dayjs from 'dayjs'

import SizeConfigHoops from '@/components/config/SizeConfigHooks'

/**
 * 模板管理页面
 */
export default defineComponent({
  name: 'CodeTemplateList',
  components: {
    Codemirror,
    DownOutlined,
    TemplateSetUserGroup,
    TemplateGroup
  },
  setup() {
    const gridRef = ref({} as VxeTableInstance)
    const { t } = useI18n()
    // 关联用户组相关操作
    const relateUserGroupVue = vueRelateUserGroup()
    // 数据加载支持
    const loadDataVue = vueLoadData()
    const addEditVue = vueAddEdit(loadDataVue.loadData, t, loadDataVue.groupIdRef)
    const sizeConfigHoops = SizeConfigHoops()

    /**
     * 操作点击
     */
    const handleSelectActions = (id: number, action: string) => {
      switch (action) {
        case 'edit': {
          addEditVue.handleShowEdit(id)
          break
        }
        case 'look': {
          addEditVue.handleShowTemplate(id)
          break
        }
      }
    }
    /**
     * 删除功能
     */
    const handleDelete = () => {
      const selectRecords = gridRef.value.getCheckboxRecords()
      if (selectRecords.length === 0) {
        message.error(t('common.notice.deleteChoose'))
        return
      }
      const templateIdList = selectRecords.map((item) => item.templateId)
      Modal.confirm({
        title: t('common.notice.deleteConfirm'),
        icon: createVNode(ExclamationCircleOutlined),
        onOk() {
          // 验证数据删除权限
          const noDelete = selectRecords.some((row) => row.createUserId !== getCurrentUserId)
          if (!getUserRole().includes('SUPERADMIN') && noDelete) {
            message.error(t('generator.views.template.notice.onlyDeleteMy'))
            return false
          }
          ApiService.postAjax('db/code/template/batchDeleteById', templateIdList)
            .then(() => {
              message.success(t('common.message.deleteSuccess'))
              loadDataVue.loadData()
            })
            .catch((error) => {
              console.error(error)
              message.error(t('common.message.deleteFail'))
            })
        }
      })
    }
    /**
     * 获取编辑按钮的显示状态
     * @param row
     */
    const getEditDisable = (row: any) => {
      return !(row.createUserId === getCurrentUserId() || isSuperAdmin())
    }
    return {
      gridRef,
      ...addEditVue,
      ...relateUserGroupVue,
      handleDelete,
      handleSelectActions,
      getEditDisable,
      ...loadDataVue,
      ...sizeConfigHoops
    }
  },
  data() {
    return {
      labelCol: {
        span: 9
      },
      wrapperCol: {
        span: 14
      },
      modalContainer: () => document.getElementById('codeTemplateContainer'),
      toolbarConfig: {
        slots: {
          tools: 'table-tools',
          buttons: 'table-buttons'
        }
      },
      templateType: templateTypeConstants,
      columns: [
        {
          type: 'checkbox',
          width: 60,
          fixed: 'left'
        },
        {
          field: 'name',
          title: '{generator.views.template.table.name}',
          width: 200,
          fixed: 'left',
          align: 'left',
          headerAlign: 'center'
        },
        {
          field: 'templateType',
          title: '{generator.views.template.table.templateType}',
          width: 140,
          formatter: ({ row }: any) => {
            const templateType = templateTypeConstants[row.templateType]
            if (templateType) {
              return this.$t(templateType.label)
            }
            return ''
          }
        },
        {
          field: 'language',
          title: '{generator.views.template.table.language}',
          width: 200
        },
        {
          field: 'remark',
          title: '{generator.views.template.table.remark}',
          minWidth: 200,
          align: 'left',
          headerAlign: 'center'
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
.code-container {
  padding: 10px;
  ::v-deep(.ant-modal-content) {
    height: 100%;
  }
  ::v-deep(.ant-modal-body) {
    padding: 10px;
  }
  ::v-deep(.ant-modal) {
    max-width: 100%;
  }
  ::v-deep(.ant-spin-nested-loading) {
    height: 100%;
  }
}
.code-edit-container {
  margin-top: 10px;
  height: calc(100% - 112px);
  border: 1px solid gray;
}
.set-group-trans {
  ::v-deep(.ant-transfer-list) {
    width: 46% !important;
    flex: none;
    height: 450px;
  }
}
.layout-user-group {
  margin-left: 5px;
}
</style>
