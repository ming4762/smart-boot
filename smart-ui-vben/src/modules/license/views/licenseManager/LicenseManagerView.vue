<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :column-config="columnConfig"
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
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
          ]" />
      </template>
      <template #table-operation="{ row }">
        <div>
          <a-button
            type="link"
            :disabled="!hasPermission('sys:license:update')"
            @click="() => handleAddEdit(false, row.id)"
            :size="buttonSizeConfig">
            {{ $t('common.button.edit') }}
          </a-button>
          <a-dropdown>
            <template #overlay>
              <a-menu @click="({ key }) => handleMenu(key, row)">
                <a-menu-item :disabled="!hasPermission('sys:license:generator')" key="generator">
                  <CheckOutlined />
                  {{ $t('smart.license.button.generator') }}
                </a-menu-item>
                <a-menu-item
                  :disabled="!(row.status === 'GENERATOR' && hasPermission('sys:license:download'))"
                  key="download">
                  <DownloadOutlined />
                  {{ $t('common.button.download') }}
                </a-menu-item>
              </a-menu>
            </template>
            <a-button :size="buttonSizeConfig" type="link">
              Actions
              <DownOutlined />
            </a-button>
          </a-dropdown>
        </div>
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              @click="() => handleAddEdit(true, null)"
              style="margin-left: 5px">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              type="primary"
              danger
              @click="handleDeleteByCheckbox"
              style="margin-left: 5px">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('smart.license.title.licenseCode')">
            <a-input
              v-model:value="searchModel.licenseCode"
              style="width: 140px"
              :size="formSizeConfig"
              :placeholder="$t('smart.license.validate.licenseCode')" />
          </a-form-item>
          <a-form-item :label="$t('smart.license.title.version')">
            <a-input
              v-model:value="searchModel.version"
              style="width: 140px"
              :size="formSizeConfig"
              :placeholder="$t('smart.license.validate.version')" />
          </a-form-item>
          <a-form-item :label="$t('smart.license.title.status')">
            <a-input
              v-model:value="searchModel.status"
              :size="formSizeConfig"
              :placeholder="$t('smart.license.validate.status')" />
          </a-form-item>
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
            <a-button :size="buttonSizeConfig" style="margin-left: 5px" @click="handleReset">
              {{ $t('common.button.reset') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
    </vxe-grid>
    <a-modal v-bind="modalProps" width="1000px">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          style="padding: 10px"
          :rules="rules"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 17 }"
          v-bind="formProps">
          <a-row>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.licenseCode')" name="licenseCode">
                <a-input
                  v-model:value="formProps.model.licenseCode"
                  :placeholder="$t('smart.license.validate.licenseCode')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :rules="timesRules"
                :label="$t('smart.license.title.times')"
                name="times">
                <a-range-picker v-model:value="formProps.model.times" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.macAddress')" name="macAddress">
                <a-input
                  v-model:value="formProps.model.macAddress"
                  :placeholder="$t('smart.license.validate.macAddress')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.ipAddress')" name="ipAddress">
                <a-input
                  v-model:value="formProps.model.ipAddress"
                  :placeholder="$t('smart.license.validate.ipAddress')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.cpuSerial')" name="cpuSerial">
                <a-input
                  v-model:value="formProps.model.cpuSerial"
                  :placeholder="$t('smart.license.validate.cpuSerial')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item
                :label="$t('smart.license.title.mainBoardSerial')"
                name="mainBoardSerial">
                <a-input
                  v-model:value="formProps.model.mainBoardSerial"
                  :placeholder="$t('smart.license.validate.mainBoardSerial')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.storePath')" name="storePath">
                <a-input
                  v-model:value="formProps.model.storePath"
                  :placeholder="$t('smart.license.validate.storePath')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.licensePath')" name="licensePath">
                <a-input
                  v-model:value="formProps.model.licensePath"
                  :placeholder="$t('smart.license.validate.licensePath')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.storePassword')" name="storePassword">
                <a-input
                  v-model:value="formProps.model.storePassword"
                  :placeholder="$t('smart.license.validate.storePassword')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.keyPassword')" name="keyPassword">
                <a-input
                  v-model:value="formProps.model.keyPassword"
                  :placeholder="$t('smart.license.validate.keyPassword')" />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.alias')" name="alias">
                <a-input
                  v-model:value="formProps.model.alias"
                  :placeholder="$t('smart.license.validate.alias')" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item :label="$t('smart.license.title.subject')" name="subject">
                <a-input
                  v-model:value="formProps.model.subject"
                  :placeholder="$t('smart.license.validate.subject')" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import dayjs, { Dayjs } from 'dayjs'
import { DownOutlined, CheckOutlined, DownloadOutlined } from '@ant-design/icons-vue'

import { useVxeTable, useAddEdit, useVxeDelete } from '/@/hooks/page/CrudHooks'
import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { hasPermission } from '/@/common/auth/AuthUtils'

import {
  handleLoadData,
  handleGetById,
  handleSaveUpdate,
  handleDelete,
  useGeneratorLicense,
} from './SmartAuthLicenseHook'

export default defineComponent({
  name: 'LicenseManagerView',
  components: {
    DownOutlined,
    CheckOutlined,
    DownloadOutlined,
  },
  setup() {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, searchModel, loadData } = useVxeTable(
      handleLoadData,
      {
        paging: true,
      },
    )
    onMounted(loadData)

    /**
     * 添加保存hook
     */
    const {
      handleAddEdit,
      formModel,
      formProps,
      modalProps,
      spinning,
      handleEditByCheckbox,
      formRef,
    } = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'id',
    })

    const timesRules = [
      {
        required: true,
        validator: async (_, value: Array<Dayjs> | null) => {
          if (value === null) {
            return Promise.reject(t('smart.license.validate.times'))
          }
          if (value[1].isBefore(dayjs())) {
            return Promise.reject(t('smart.license.validate.timeValidate'))
          }
        },
      },
    ]

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, {
      idField: 'id',
      listHandler: loadData,
    })

    const { handleGenerator } = useGeneratorLicense(t, loadData)

    const handleMenu = (key, { id, status }) => {
      switch (key) {
        case 'generator': {
          handleGenerator(id, status)
          break
        }
        case 'download': {
          break
        }
      }
    }

    return {
      dayjs,
      gridRef,
      ...useSizeSetting(),
      handleAddEdit,
      formRef,
      spinning,
      handleEditByCheckbox,
      formProps,
      modalProps,
      formModel,
      searchModel,
      ...deleteHook,
      tableProps,
      pageProps,
      handleReset,
      loadData,
      timesRules,
      hasPermission,
      handleMenu,
    }
  },
  data() {
    return {
      columnConfig: {},
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools',
          buttons: 'toolbar_buttons',
        },
      },
      rules: {
        licenseCode: [
          {
            required: true,
            message: this.$t('smart.license.validate.licenseCode'),
            trigger: 'blur',
          },
        ],
        storePath: [
          {
            required: true,
            message: this.$t('smart.license.validate.storePath'),
            trigger: 'blur',
          },
        ],
        licensePath: [
          {
            required: true,
            message: this.$t('smart.license.validate.licensePath'),
            trigger: 'blur',
          },
        ],
        storePassword: [
          {
            required: true,
            message: this.$t('smart.license.validate.storePassword'),
            trigger: 'blur',
          },
        ],
        keyPassword: [
          {
            required: true,
            message: this.$t('smart.license.validate.keyPassword'),
            trigger: 'blur',
          },
        ],
        alias: [
          {
            required: true,
            message: this.$t('smart.license.validate.alias'),
            trigger: 'blur',
          },
        ],
        subject: [
          {
            required: true,
            message: this.$t('smart.license.validate.subject'),
            trigger: 'blur',
          },
        ],
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left',
        },
        {
          field: 'licenseCode',
          title: '{smart.license.title.licenseCode}',
          width: 120,
          fixed: 'left',
        },
        {
          field: 'macAddress',
          title: '{smart.license.title.macAddress}',
          width: 200,
        },
        {
          field: 'ipAddress',
          title: '{smart.license.title.ipAddress}',
          width: 200,
        },
        {
          field: 'cpuSerial',
          title: '{smart.license.title.cpuSerial}',
          width: 200,
        },
        {
          field: 'mainBoardSerial',
          title: '{smart.license.title.mainBoardSerial}',
          width: 200,
        },
        {
          field: 'effectiveTime',
          title: '{smart.license.title.effectiveTime}',
          width: 160,
        },
        {
          field: 'expirationTime',
          title: '{smart.license.title.expirationTime}',
          width: 160,
        },
        {
          field: 'status',
          title: '{smart.license.title.status}',
          width: 120,
        },
        {
          field: 'createTime',
          title: '{common.table.createTime}',
          width: 160,
        },
        {
          field: 'createUser',
          title: '{common.table.createUser}',
          width: 120,
        },
        {
          field: 'updateTime',
          title: '{common.table.updateTime}',
          width: 160,
        },
        {
          field: 'updateUser',
          title: '{common.table.updateUser}',
          width: 120,
        },
        {
          title: '{common.table.operation}',
          field: 'operation',
          width: 145,
          fixed: 'right',
          slots: {
            default: 'table-operation',
          },
        },
      ],
    }
  },
})
</script>
