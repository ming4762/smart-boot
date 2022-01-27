<template>
  <div class="full-height">
    <div class="table-container">
      <vxe-grid
        ref="gridRef"
        highlight-current-row
        stripe
        :columns="columns"
        height="auto"
        size="small"
        v-bind="tableProps"
        @cell-click="handleChange">
        <template #table-groupName="{ row }">
          <div style="cursor: pointer">
            <ContextMenu event="contextmenu">
              {{ row.groupName }}
              <template #menu>
                <a-menu @click="({ key, domEvent }) => handleMenuClick(key, row.groupId, domEvent)">
                  <a-menu-item key="edit"><EditOutlined />&nbsp;&nbsp;{{ $t('common.button.edit') }}</a-menu-item>
                  <a-menu-item key="delete"><DeleteOutlined />&nbsp;&nbsp;{{ $t('common.button.delete') }}</a-menu-item>
                </a-menu>
              </template>
            </ContextMenu>
          </div>
        </template>
      </vxe-grid>
    </div>
    <div class="button-container">
      <a-button class="button" block type="primary" @click="() => handleAddEdit(true, null)">{{ $t('common.button.add') }}</a-button>
    </div>

    <a-modal
      v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          v-bind="formProps"
          :rules="rules"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 17 }">
          <a-form-item :label="$t('generator.views.template.title.templateGroup')" name="groupName">
            <a-input v-model:value="formProps.model.groupName" :placeholder="$t('generator.views.template.validate.templateGroup')" />
          </a-form-item>
          <a-form-item :label="$t('generator.views.template.title.seq')" name="seq">
            <a-input-number v-model:value="formProps.model.seq" style="width: 100%" :placeholder="$t('generator.views.template.validate.seq')" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue'
import { useI18n } from 'vue-i18n'

import { EditOutlined, DeleteOutlined } from '@ant-design/icons-vue'

import ContextMenu from '@/components/ContextMenu/ContextMenu.vue'
import { useVxeTable, useAddEdit, useVxeDelete } from '@/components/hooks/CrudHooks'
import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

const handleLoadData = async () => {
  try {
    const result = [
      {
        groupName: 'ALL'
      }
    ]
    return result.concat(await ApiService.postAjax('db/code/template/listGroup') || [])
  } catch (e) {
    errorMessage(e)
    return false
  }
}

/**
 * 通过ID查询
 */
const handleGet = async (id: any) => {
  try {
    return await ApiService.postAjax('db/code/template/getGroupById', id)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

const handleSaveUpdate = async (model: any) => {
  try {
    await ApiService.postAjax('db/code/template/saveUpdateGroup', model)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

const handleDelete = async (idList: Array<any>) => {
  try {
    await ApiService.postAjax('db/code/template/deleteGroupById', idList)
  } catch (e) {
    errorMessage(e)
    throw e
  }
}

export default defineComponent({
  name: 'TemplateGroup',
  components: {
    EditOutlined, DeleteOutlined,
    ContextMenu
  },
  emits: ['change'],
  setup () {
    const { t } = useI18n()
    const gridRef = ref()
    const { tableProps, loadData } = useVxeTable(handleLoadData, {
      paging: false
    })
    // 编辑
    const { modalProps, handleAddEdit, spinning, formProps, formRef } = useAddEdit(handleGet, loadData, handleSaveUpdate, t, {
      defaultModel: {
        seq: 1
      }
    })

    const { handleDeleteById } = useVxeDelete(gridRef, t, handleDelete, {
      idField: 'groupId',
      listHandler: loadData
    })

    const handleMenuClick = (ident: string, groupId: number, event: Event) => {
      event.preventDefault()
      switch (ident) {
        case 'edit': {
          handleAddEdit(false, groupId)
          break
        }
        case 'delete': {
          handleDeleteById(groupId)
          break
        }
      }
    }

    onMounted(loadData)
    return {
      gridRef,
      tableProps,
      loadData,
      handleMenuClick,
      modalProps,
      handleAddEdit,
      spinning,
      formProps,
      formRef
    }
  },
  data () {
    return {
      columns: [
        {
          title: '{generator.views.template.title.templateGroup}',
          field: 'groupName',
          slots: {
            default: 'table-groupName'
          }
        }
      ],
      rules: {
        groupName: [
          { required: true, message: this.$t('generator.views.template.validate.templateGroup'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('generator.views.template.validate.seq'), trigger: 'blur', type: 'number' }
        ]
      }
    }
  },
  methods: {
    handleChange ({ row }) {
      this.$emit('change', row.groupId)
    }
  }
})
</script>

<style lang="less" scoped>
@buttonContainerHeight: 50px;
.table-container {
  height: calc(100% - @buttonContainerHeight);
}
.button-container {
  height: @buttonContainerHeight;
  line-height: @buttonContainerHeight;
  text-align: center;
  .button {
    width: 90%;

  }
}
</style>
