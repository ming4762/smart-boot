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
          :layouts="['Sizes', 'PrevJump', 'PrevPage', 'Number', 'NextPage', 'NextJump', 'FullJump', 'Total']" />
      </template>
      <template #toolbar_buttons>
        <a-form layout="inline" style="margin-left: 10px">
          <a-form-item :label="$t('system.views.file.title.fileName')">
            <a-input v-model:value="searchModel.fileName" :size="formSizeConfig" :placeholder="$t('system.views.file.validate.fileName')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.file.title.type')">
          </a-form-item>
          <a-form-item :label="$t('system.views.file.title.handlerType')">
            <a-input v-model:value="searchModel.handlerType" :size="formSizeConfig" :placeholder="$t('system.views.file.validate.handlerType')" />
          </a-form-item>
          <a-form-item>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              type="primary"
              @click="loadData">
              {{ $t('common.button.search') }}
            </a-button>
            <a-button
              :size="buttonSizeConfig"
              style="margin-left: 5px"
              @click="handleReset">
              {{ $t('common.button.reset') }}
            </a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #table-operation="{ row }">
      </template>
    </vxe-grid>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import dayjs from 'dayjs'

import { useVxeTable, useVxeDelete } from '@/components/hooks'
import SizeConfigHooks from '@/components/config/SizeConfigHooks'

import { handleLoadData, handleDelete } from './SysFileHook'

export default defineComponent({
  name: 'SysFileListView',
  components: {

  },
  setup () {
    const { t } = useI18n()
    const gridRef = ref()

    /**
     * 查询数据hook
     */
    const { tableProps, handleReset, pageProps, loadData } = useVxeTable(handleLoadData, {
      paging: true
    })

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, { idField: 'fileId', listHandler: loadData })

    onMounted(loadData)
    return {
      gridRef,
      ...SizeConfigHooks(),
      ...deleteHook,
      tableProps,
      pageProps,
      handleReset,
      loadData
    }
  },
  data () {
    return {
      columnConfig: {
      },
      toolbarConfig: {
        slots: {
          buttons: 'toolbar_buttons'
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
          field: 'fileId',
          title: '{system.views.file.title.fileId}',
          width: 120
        },
        {
          field: 'fileName',
          title: '{system.views.file.title.fileName}',
          width: 200
        },
        {
          field: 'type',
          title: '{system.views.file.title.type}',
          width: 120
        },
        {
          field: 'contentType',
          title: '{system.views.file.title.contentType}',
          width: 120
        },
        {
          field: 'fileSize',
          title: '{system.views.file.title.fileSize}',
          width: 120
        },
        {
          field: 'md5',
          title: '{system.views.file.title.md5}',
          width: 120
        },
        {
          field: 'seq',
          title: '{common.table.seq}',
          width: 80
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
          sortable: true,
          title: '{common.table.createTime}',
          formatter: ({ cellValue }: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return ''
          },
          width: 160
        },
        {
          field: 'handlerType',
          title: '{system.views.file.title.handlerType}',
          width: 120
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
