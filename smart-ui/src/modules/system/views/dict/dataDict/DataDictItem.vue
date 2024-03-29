<template>
  <div class="full-height" style="padding: 10px">
    <vxe-grid
      ref="gridRef"
      v-bind="tableProps"
      :size="tableSizeConfig"
      border
      :toolbar-config="toolbarConfig"
      :columns="columns"
      height="auto"
      stripe
      highlight-hover-row>
      <template #pager>
        <vxe-pager
          v-bind="pageProps"
          :page-sizes="[500, 1000, 2000, 5000]"
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
      </template>
      <template #toolbar_tools>
        <a-form layout="inline">
          <a-form-item>
            <a-button
              v-permission="'sys:dictItem:save'"
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="handleShowAdd">
              {{ $t('common.button.add') }}
            </a-button>
            <a-button
              v-permission="'sys:dictItem:update'"
              :size="buttonSizeConfig"
              type="primary"
              style="margin-left: 5px"
              @click="handleEditByCheckbox">
              {{ $t('common.button.edit') }}
            </a-button>
            <a-button
              v-permission="'sys:dictItem:delete'"
              :size="buttonSizeConfig"
              type="primary"
              danger
              style="margin-left: 5px"
              @click="handleDeleteByCheckbox">
              {{ $t('common.button.delete') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
    </vxe-grid>
    <a-modal
      width="600px"
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{span: 6}"
          :wrapper-col="{span: 17}"
          v-bind="formProps">
          <a-row>
            <a-col :span="24">
              <a-form-item :label="$t('system.views.dictGroup.title.dictCode')">
                <a-input v-model:value="formProps.model.dictCode" disabled />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dictItem.title.dictItemCode')"
                name="dictItemCode">
                <a-input
                  v-model:value="formProps.model.dictItemCode"
                  :placeholder="$t('system.views.dictItem.validate.dictItemCode')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('system.views.dictItem.title.dictItemName')"
                name="dictItemName">
                <a-input
                  v-model:value="formProps.model.dictItemName"
                  :placeholder="$t('system.views.dictItem.validate.dictItemName')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.seq')"
                name="seq">
                <a-input-number
                  v-model:value="formProps.model.seq"
                  style="width: 100%"
                  :placeholder="$t('common.formValidate.seq')" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.useYn')"
                name="useYn">
                <a-switch v-model:checked="formProps.model.useYn" />
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item
                :label="$t('common.table.remark')"
                name="remark">
                <a-input
                  v-model:value="formProps.model.remark"
                  :placeholder="$t('common.formValidate.remark')" />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, PropType, ref, toRefs, watch } from 'vue'
import { useI18n } from 'vue-i18n'

import dayjs from 'dayjs'
import { message } from 'ant-design-vue'

import { useVxeTable, useAddEdit, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'
import { tableUseYn } from '@/components/common/TableCommon'

export default defineComponent({
  name: 'SysDictItemListView',
  props: {
    dictCode: {
      type: String as PropType<string>
    }
  },
  setup (props) {
    const { t } = useI18n()
    const gridRef = ref()
    const { dictCode } = toRefs(props)

    /**
     * 加载数据
     * @param params 参数
     * @param searchParameter 查询参数
     */
    const handleLoadData = async (params: any, searchParameter: any) => {
      try {
        if (!dictCode.value) {
          return {
            rows: [],
            total: 0
          }
        }
        return await ApiService.postAjax('sys/dictItem/list', {
          ...params,
          parameter: {
            ...searchParameter,
            'dictCode@=': dictCode.value
          }
        })
      } catch (e) {
        errorMessage(e)
        throw e
      }
    }

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, searchModel, loadData } = useVxeTable(handleLoadData, {
      paging: true
    })
    watch(dictCode, () => {
      loadData()
    })

    const handleGetById = async (id: string) => {
      try {
        return await ApiService.postAjax('sys/dictItem/get', {
          dictCode: dictCode.value,
          dictItemCode: id
        })
      } catch (e) {
        errorMessage(e)
        throw e
      }
    }
    const handleSaveUpdate = async (model: any) => {
      if (!model.dictCode) {
        model.dictCode = dictCode.value
      }
      try {
        await ApiService.postAjax('sys/dictItem/saveUpdate', model)
      } catch (e) {
        errorMessage(e)
        throw e
      }
    }

    /**
     * 添加保存hook
     */
    const addEditHook = useAddEdit(gridRef, handleGetById, loadData, handleSaveUpdate, t, {
      idField: 'dictItemCode',
      defaultModel: {
        useYn: true,
        seq: 1
      }
    })

    const handleDelete = async (idList: Array<any>) => {
      const data = idList.map(item => {
        return {
          dictCode: dictCode.value,
          dictItemCode: item
        }
      })
      try {
        await ApiService.postAjax('sys/dictItem/batchDelete', data)
      } catch (e) {
        errorMessage(e)
        throw e
      }
    }
    const handleShowAdd = () => {
      if (!dictCode.value) {
        message.error(t('system.views.dictItem.message.dictCodeNull'))
        return false
      }

      addEditHook.handleAddEdit(true, null)
        .then(() => {
          addEditHook.handleSetModel({
            dictCode: dictCode.value,
            ...addEditHook.formProps.value.model
          })
        })
    }

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'dictItemCode', listHandler: loadData })

    return {
      gridRef,
      ...SizeConfigHooks(),
      ...addEditHook,
      searchModel,
      ...deleteHook,
      tableProps,
      pageProps,
      handleReset,
      loadData,
      handleShowAdd
    }
  },
  data () {
    return {
      toolbarConfig: {
        slots: {
          tools: 'toolbar_tools'
        }
      },
      rules: {
        dictItemCode: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('system.views.dictItem.rules.dictItemCode_NOT_EMPTY')
          }
        ],
        dictItemName: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('system.views.dictItem.rules.dictItemName_NOT_EMPTY')
          }
        ],
        seq: [
          {
            required: true,
            trigger: [
              'blur'
            ],
            message: this.$t('common.formValidate.seq')
          }
        ]
      },
      columns: [
        {
          type: 'checkbox',
          width: 60,
          align: 'center',
          fixed: 'left'
        },
        {
          field: 'dictCode',
          hidden: true,
          title: '{system.views.dictItem.title.dictCode}',
          width: 120
        },
        {
          field: 'dictItemCode',
          fixed: 'left',
          title: '{system.views.dictItem.title.dictItemCode}',
          width: 160
        },
        {
          field: 'dictItemName',
          fixed: 'left',
          title: '{system.views.dictItem.title.dictItemName}',
          width: 180
        },
        {
          field: 'seq',
          sortable: true,
          title: '{common.table.seq}',
          width: 100
        },
        {
          field: 'remark',
          title: '{common.table.remark}',
          width: 200
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true
        },
        {
          field: 'createUserId',
          title: '{common.table.createUser}',
          formatter: ({ row }: any) => {
            if (row.createUser) {
              return row.createUser.fullName
            }
            return ''
          },
          width: 120
        },
        {
          field: 'createTime',
          title: '{common.table.createTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 180
        },
        {
          field: 'updateUserId',
          title: '{common.table.updateUser}',
          formatter: ({ row }: any) => {
            if (row.updateUser) {
              return row.updateUser.fullName
            }
            return ''
          },
          width: 120
        },
        {
          field: 'updateTime',
          title: '{common.table.updateTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 180
        }
      ]
    }
  }
})
</script>
