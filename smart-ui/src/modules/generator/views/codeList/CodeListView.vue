<template>
  <div id="codeListContainer" class="code-container full-height">
    <div class="full-height" style="padding: 10px">
      <vxe-grid
        id="generator_views_codeList"
        ref="gridRef"
        :data="data"
        :size="tableSizeConfig"
        stripe
        highlight-hover-row
        border
        height="auto"
        :loading="tableLoading"
        v-bind="tableAttrs">
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
        <template #table-tools>
          <a-button :size="buttonSizeConfig" type="primary" @click="handleShowAdd">{{ $t('common.button.add') }}</a-button>
          <a-button :size="buttonSizeConfig" style="margin-left: 5px" type="primary" danger @click="handleDelete">{{ $t('common.button.delete') }}</a-button>
        </template>
        <template #table-buttons>
          <a-form style="padding-left: 5px" layout="inline">
            <a-form-item>
              <a-input v-model:value="searchModel.tableName" :size="formSizeConfig" :placeholder="$t('generator.views.code.table.tableName')" />
            </a-form-item>
            <a-form-item>
              <a-select v-model:value="searchModel.type" :size="formSizeConfig" :placeholder="$t('generator.views.code.table.type')" style="width: 120px">
                <a-select-option value="">ALL</a-select-option>
                <a-select-option
                  v-for="(value, key) in tableTypeList"
                  :key="key"
                  :value="key">
                  {{ value }}
                </a-select-option>
              </a-select>
            </a-form-item>
            <a-form-item>
              <a-button :size="buttonSizeConfig" type="primary" @click="doLoadData">{{ $t('common.button.search') }}</a-button>
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
              <a-menu @click="({ key }) => handleActions(row.id, key)">
                <a-menu-item key="edit">{{ $t('common.button.edit') }}</a-menu-item>
                <a-menu-item key="create">{{ $t('generator.views.code.button.createCode') }}</a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </template>
      </vxe-grid>
      <a-modal
        v-model:visible="addEditModalVisible"
        :body-style="{ height: 'calc(100% - 110px)' }"
        :get-container="modalContainer"
        style="top: 0; height: 100%; padding-bottom: 0"
        :title="$t('generator.views.code.title.design')" width="100%"
        :confirm-loading="saveLoading"
        @ok="handleSave">
        <a-spin :spinning="configDataLoading">
          <a-form ref="formRef" :model="formModel" :rules="formRules" :label-col="labelCol" :wrapper-col="wrapperCol">
            <a-list :split="true">
              <a-list-item>
                <a-col :span="span">
                  <a-form-item name="connectionId" :label="$t('generator.views.code.table.connectionName')">
                    <DatabaseSelect v-model:value="formModel.connectionId" :size="formSize" />
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="tableName" :label="$t('generator.views.code.table.tableName')">
                    <a-input v-model:value="formModel.tableName" :size="formSize"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="configName" :label="$t('generator.views.code.table.configName')">
                    <a-input v-model:value="formModel.configName" :size="formSize"></a-input>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="type" :label="$t('generator.views.code.table.type')">
                    <a-select v-model:value="formModel.type" :size="formSize">
                      <a-select-option
                        v-for="(value, key) in tableTypeList"
                        :key="key"
                        :value="key">
                        {{ $t(value) }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-list-item>
              <a-list-item>
                <a-col :span="span">
                  <a-form-item name="showCheckbox" :label="$t('generator.views.code.title.showCheckBox')">
                    <a-radio-group v-model:value="formModel.showCheckbox" :size="formSize">
                      <a-radio :value="true">Yes</a-radio>
                      <a-radio :value="false">No</a-radio>
                    </a-radio-group>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="page" :label="$t('generator.views.code.title.isPage')">
                    <a-radio-group v-model:value="formModel.page" :size="formSize">
                      <a-radio :value="true">Yes</a-radio>
                      <a-radio :value="false">No</a-radio>
                    </a-radio-group>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="invented" :label="$t('generator.views.code.title.invented')">
                    <a-radio-group v-model:value="formModel.invented" :size="formSize">
                      <a-radio :value="true">Yes</a-radio>
                      <a-radio :value="false">No</a-radio>
                    </a-radio-group>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="columnSort" :label="$t('generator.views.code.title.columnSort')">
                    <a-radio-group v-model:value="formModel.columnSort" :size="formSize">
                      <a-radio :value="true">Yes</a-radio>
                      <a-radio :value="false">No</a-radio>
                    </a-radio-group>
                  </a-form-item>
                </a-col>
              </a-list-item>
              <a-list-item>
                <a-col :span="span">
                  <a-form-item :label="$t('generator.views.code.title.leftButton')">
                    <a-select
                      v-model:value="formModel.leftButtonList"
                      :size="formSize"
                      style="width: 100%"
                      mode="multiple">
                      <a-select-option
                        v-for="item in buttonList"
                        :key="item.key"
                        :value="item.key">
                        {{ item.value }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item :label="$t('generator.views.code.title.rightButton')">
                    <a-select
                      v-model:value="formModel.rightButtonList"
                      :size="formSize"
                      style="width: 100%"
                      mode="multiple">
                      <a-select-option
                        v-for="item in buttonList"
                        :key="item.key"
                        :value="item.key">
                        {{ item.value }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item :label="$t('generator.views.code.title.rowButtonType.title')">
                    <a-select v-model:value="formModel.rowButtonType" :size="formSize" style="width: 100%">
                      <a-select-option
                        v-for="item in rowButtonTypeList"
                        :key="item.value"
                        :value="item.value">
                        {{ item.label }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item :label="$t('generator.views.code.title.rowButtonList')">
                    <a-select
                      v-model:value="formModel.rowButtonList"
                      :size="formSize"
                      style="width: 100%"
                      mode="multiple">
                      <a-select-option
                        v-for="item in rowButtonList"
                        :key="item.key"
                        :value="item.key">
                        {{ item.value }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
              </a-list-item>
              <a-list-item>
                <a-col :span="span">
                  <a-form-item name="formColNum" :label="$t('generator.views.code.title.formColNum')">
                    <a-select v-model:value="formModel.formColNum" :size="formSize">
                      <a-select-option :value="1">{{ $t('generator.views.code.title.colNum.one') }}</a-select-option>
                      <a-select-option :value="2">{{ $t('generator.views.code.title.colNum.two') }}</a-select-option>
                      <a-select-option :value="3">{{ $t('generator.views.code.title.colNum.three') }}</a-select-option>
                      <a-select-option :value="4">{{ $t('generator.views.code.title.colNum.four') }}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item name="searchColNum" :label="$t('generator.views.code.title.searchColNum')">
                    <a-select v-model:value="formModel.searchColNum" :size="formSize">
                      <a-select-option :value="1">{{ $t('generator.views.code.title.colNum.one') }}</a-select-option>
                      <a-select-option :value="2">{{ $t('generator.views.code.title.colNum.two') }}</a-select-option>
                      <a-select-option :value="3">{{ $t('generator.views.code.title.colNum.three') }}</a-select-option>
                      <a-select-option :value="4">{{ $t('generator.views.code.title.colNum.four') }}</a-select-option>
                    </a-select>
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item :label="$t('common.table.remark')">
                    <a-input v-model:value="formModel.remark" :size="formSize" />
                  </a-form-item>
                </a-col>
                <a-col :span="span">
                  <a-form-item>
                    <a-button type="primary" :size="buttonSizeConfig" @click="handleSyncTableData">{{ $t('generator.views.code.button.syncTableData') }}</a-button>
                  </a-form-item>
                </a-col>
              </a-list-item>
              <a-list-item>
                <a-col :span="span">
                  <a-form-item :label="$t('generator.views.code.title.relateTable')">
                    <a-tag
                      v-for="(table, index) in choseTableList"
                      :key="index"
                      :size="formSize"
                      style="display: inline-block"
                      closable
                      @close="() => handleRemoveRelateTable(table, index)">
                      {{ table.configName }}
                    </a-tag>
                    <PlusOutlined :style="{ cursor: 'pointer' }" @click="handleShowChoseAddendumModal" />
                  </a-form-item>
                </a-col>
              </a-list-item>
            </a-list>
          </a-form>
          <a-tabs :animated="true">
            <a-tab-pane key="1" class="full-height" :tab="$t('generator.views.code.title.dbMessage')">
              <TableFieldTable class="full-height" :data="computedTableData" :loading="dbDataLoading" />
            </a-tab-pane>
            <a-tab-pane key="2" :tab="$t('generator.views.code.title.tableSetting')">
              <PageTableSetting ref="pageTableSettingRef" :edit-data="editConfigData.codePageConfigList" :table-data="computedTableData" :loading="dbDataLoading" />
            </a-tab-pane>
            <a-tab-pane key="3" :tab="$t('generator.views.code.title.formSetting')">
              <PageFormSetting ref="pageFormSettingRef" :edit-data="editConfigData.codeFormConfigList" :table-data="computedTableData" :loading="dbDataLoading" />
            </a-tab-pane>
            <a-tab-pane key="4" :tab="$t('generator.views.code.title.searchSetting')">
              <PageSearchSetting ref="pageSearchSettingRef" :edit-data="editConfigData.codeSearchConfigList" :table-data="computedTableData" :loading="dbDataLoading" />
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </a-modal>
    </div>
    <a-modal
      width="600px"
      style="top: 15px"
      :title="$t('generator.views.code.button.createCode')"
      :visible="createModalVisible"
      @ok="handleCreateCode"
      @cancel="createModalVisible = false">
      <CodeCreateForm ref="createFormRef" :code-data="codeData" />
    </a-modal>
    <PageAddendumTableChose v-model:visible="choseAddendumModalVisible" :z-index="2000" @chose="handleChoseTable" />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'

import { DownOutlined, PlusOutlined } from '@ant-design/icons-vue'

import SizeConfigHoops from '@/components/config/SizeConfigHooks'

import DatabaseSelect from './components/DatabaseSelect.vue'
import TableFieldTable from './components/TableFieldTable.vue'
import PageTableSetting from './components/PageTableSetting.vue'
import PageFormSetting from './components/PageFormSetting.vue'
import CodeCreateForm from './components/CodeCreateForm.vue'
import PageSearchSetting from './components/PageSearchSetting.vue'
import PageAddendumTableChose from './components/PageAddendumTableChose.vue'

import {
  crateActionsVue,
  loadTableData,
  vueCodeListDeleteSupport,
  vueSaveConfigSupport,
  vueLoadDbDataSupport,
  vueConfigEditSupport,
  vueButtonSupport,
  vueRelateAddendumTable
} from './CodeListViewSupport'
import dayjs from 'dayjs'

const tableTypeList: any = {
  10: 'generator.views.code.title.tableType.single',
  20: 'generator.views.code.title.tableType.main',
  30: 'generator.views.code.title.tableType.addendum'
}

/**
 * 代码生成列表
 */
export default defineComponent({
  name: 'CodeListView',
  components: {
    DatabaseSelect,
    TableFieldTable,
    PageTableSetting,
    PageFormSetting,
    DownOutlined,
    CodeCreateForm,
    PageSearchSetting,
    PageAddendumTableChose,
    PlusOutlined
  },
  setup () {
    const router = useRouter()
    const { t } = useI18n()
    const sizeConfigHoops = SizeConfigHoops()
    const gridRef = ref()
    const createFormRef = ref()
    // 选择的表格列表
    const choseTableList = ref<Array<any>>([])
    // 是否进行表数据同步
    const isSync = ref(false)
    const formRef = ref()
    const addEditModalVisible = ref(false)
    const dbData = ref([])
    const editConfigData = ref<any>({})
    // 按钮配置
    const buttonConfigVue = vueButtonSupport()
    // 加载表格数据
    const loadTableVue = loadTableData()
    // 关联附表Vue
    const relateAddendumTableVue = vueRelateAddendumTable(choseTableList)
    // 保存配置操作
    const vueSaveConfig = vueSaveConfigSupport(t, isSync, loadTableVue.doLoadData, addEditModalVisible, dbData, editConfigData, choseTableList)
    // 加载数据配置信息
    const vueLoadDbData = vueLoadDbDataSupport(isSync, dbData, vueSaveConfig.formModel, formRef)
    // 编辑配置VUE对象
    const { configDataLoading, handleShowEdit } = vueConfigEditSupport(addEditModalVisible, vueSaveConfig.formModel, vueLoadDbData.loadDbData, editConfigData, choseTableList)
    // 操作vue
    const actionsVue = crateActionsVue(handleShowEdit)
    /**
     * 生成代码
     */
    const handleCreateCode = () => {
      createFormRef.value.validate()
        .then(() => {
          actionsVue.createModalVisible.value = false
          // 获取form数据
          const formData = createFormRef.value.getFormData()
          // 跳转到指定页面
          const url = router.resolve({
            path: '/codeCreateView',
            query: {
              ...formData,
              templateIdList: formData.templateIdList.join(',')
            }
          })
          window.open(url.href, '_blank')
        })
    }
    return {
      ...buttonConfigVue,
      ...vueSaveConfig,
      ...loadTableVue,
      ...actionsVue,
      ...vueLoadDbData,
      ...vueCodeListDeleteSupport(gridRef, t, loadTableVue.doLoadData),
      formRef,
      addEditModalVisible,
      createFormRef,
      handleCreateCode,
      gridRef,
      configDataLoading,
      editConfigData,
      choseTableList,
      ...relateAddendumTableVue,
      ...sizeConfigHoops,
      formSize: sizeConfigHoops.formSizeConfig
    }
  },
  data () {
    return {
      span: 6,
      tableTypeList: tableTypeList,
      formRules: {
        connectionId: [{ required: true, message: this.$t('generator.views.code.validate.connectionName'), trigger: 'change' }],
        tableName: [{ required: true, message: this.$t('generator.views.code.validate.tableName'), trigger: 'blur' }],
        configName: [{ required: true, message: this.$t('generator.views.code.validate.configName'), trigger: 'blur' }]
      },
      labelCol: {
        span: 8
      },
      wrapperCol: {
        span: 15
      },
      modalContainer: () => document.getElementById('codeListContainer'),
      rowButtonTypeList: [
        {
          label: this.$t('generator.views.code.title.rowButtonType.none'),
          value: 'none'
        },
        {
          label: this.$t('generator.views.code.title.rowButtonType.single'),
          value: 'single'
        },
        {
          label: this.$t('generator.views.code.title.rowButtonType.more'),
          value: 'more'
        },
        {
          label: this.$t('generator.views.code.title.rowButtonType.text'),
          value: 'text'
        }
      ],
      tableAttrs: {
        align: 'left',
        border: true,
        toolbarConfig: {
          slots: {
            tools: 'table-tools',
            buttons: 'table-buttons'
          }
        },
        columns: [
          {
            type: 'checkbox',
            width: 60,
            fixed: 'left'
          },
          {
            title: '{generator.views.code.table.connectionName}',
            field: 'connectionName',
            width: 160,
            fixed: 'left'
          },
          {
            title: '{generator.views.code.table.configName}',
            field: 'configName',
            width: 120,
            fixed: 'left'
          },
          {
            title: '{generator.views.code.table.tableName}',
            field: 'tableName',
            width: 120,
            fixed: 'left'
          },
          {
            title: '{generator.views.code.table.type}',
            field: 'type',
            width: 120,
            formatter: ({ cellValue }: any) => {
              if (cellValue && tableTypeList[cellValue]) {
                return this.$t(tableTypeList[cellValue])
              }
              return ''
            }
          },
          {
            title: '{generator.views.code.table.remarks}',
            field: 'remarks',
            minWidth: 120
          },
          {
            title: '{common.table.remark}',
            field: 'remark',
            minWidth: 200
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
  }
})
</script>

<style lang="less" scoped>
  .code-container {
    ::v-deep(.ant-modal-content) {
      height: 100%;
    }
    ::v-deep(.ant-modal) {
      max-width: 100%;
    }
    ::v-deep(.ant-form-item) {
      margin-bottom: 0;
    }
    ::v-deep(.ant-list-item) {
      padding: 8px 0;
    }
    ::v-deep(.ant-modal-body) {
      padding: 10px;
    }
    ::v-deep(.ant-tabs-top-content) {
      height: calc(100% - 65px);
    }
    ::v-deep(.ant-spin-nested-loading) {
      height: 100%;
    }
    ::v-deep(.ant-spin-container) {
      height: 100%;
      overflow: auto;
    }
  }
</style>
