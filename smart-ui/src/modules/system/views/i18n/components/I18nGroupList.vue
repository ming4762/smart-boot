<template>
  <div class="full-height">
    <div class="table-container">
      <vxe-grid
        highlight-current-row
        stripe
        height="auto"
        size="small"
        :columns="columns"
        :data="data"
        :loading="tableLoading">
        <template #table-groupName="{ row }">
          <div style="cursor: pointer" @click="() => handleChangeGroup(row.groupId)">
            <ContextMenu event="contextmenu">
              {{ row.groupName }}
              <template #menu>
                <a-menu @click="({ key, domEvent }) => handleMenuClick(key, row.groupId, domEvent)">
                  <a-menu-item key="edit">
                    <EditOutlined />
                    &nbsp;&nbsp;{{ $t('common.button.edit') }}
                  </a-menu-item>
                  <a-menu-item key="delete">
                    <DeleteOutlined />
                    &nbsp;&nbsp;{{ $t('common.button.delete') }}
                  </a-menu-item>
                </a-menu>
              </template>
            </ContextMenu>
          </div>
        </template>
      </vxe-grid>
    </div>
    <div class="button-container">
      <a-button
        v-permission="permissions.add"
        class="button"
        block
        type="primary"
        @click="() => handleShowModal(true, null)">
        {{ $t('common.button.add') }}
      </a-button>
    </div>

    <a-modal
      v-model:visible="modalVisible"
      :title="computedTitle"
      :confirm-loading="saveLoading"
      @ok="handleSaveUpdate">
      <a-spin :spinning="getLoading">
        <a-form
          ref="formRef"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 17 }"
          :rules="rules"
          :model="editModel">
          <a-form-item name="groupName" :label="$t('system.views.i18n.group.groupName')">
            <a-input
              v-model:value="editModel.groupName"
              :placeholder="$t('system.views.i18n.group.groupNameValidate')" />
          </a-form-item>
          <a-form-item name="seq" :label="$t('system.views.i18n.group.seq')">
            <a-input-number
              v-model:value="editModel.seq"
              style="width: 120px"
              :placeholder="$t('system.views.i18n.group.seqValidate')" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, createVNode } from 'vue'
import { useI18n } from 'vue-i18n'

import { message, Modal } from 'ant-design-vue'
import { EditOutlined, DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue'

import ContextMenu from '@/components/ContextMenu/ContextMenu.vue'

import ApiService from '@/common/utils/ApiService'
import { SystemPermissions } from '@/modules/system/constants/SystemConstants'
import { errorMessage } from '@/components/notice/SystemNotice'

const ListVueSupport = () => {
  const tableLoading = ref<boolean>(false)
  const data = ref<Array<any>>([])
  const loadTableData = async () => {
    try {
      tableLoading.value = true
      data.value = await ApiService.postAjax('sys/i18n/listGroup')
    } finally {
      tableLoading.value = false
    }
  }
  onMounted(loadTableData)
  return {
    data,
    tableLoading,
    loadTableData
  }
}

const deleteVueSupport = (i18nRender: Function, loadData: Function) => {
  const handleDelete = (id: number) => {
    Modal.confirm({
      title: i18nRender('common.notice.deleteConfirm'),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        await ApiService.postAjax('sys/i18n/deleteGroup', [id])
        loadData()
      }
    })
  }
  return {
    handleDelete
  }
}

/**
 * 添加编辑支持
 * @param loadData 加载数据
 */
const addEditVueSupport = (loadData: Function) => {
  const formRef = ref()
  const i18n = useI18n().t
  const modalVisible = ref(false)
  const isAdd = ref(true)
  const saveLoading = ref(false)
  const getLoading = ref(false)
  const editModel = ref<any>({})
  const computedTitle = computed(() => {
    return isAdd.value ? i18n('common.button.add') : i18n('common.button.edit')
  })
  const handleShowModal = async (add: boolean, groupId: number | null) => {
    isAdd.value = add
    modalVisible.value = true
    if (!isAdd.value) {
      try {
        getLoading.value = true
        editModel.value = await ApiService.postAjax('sys/i18n/getGroupById', groupId)
      } catch (e) {
        errorMessage(e)
      } finally {
        getLoading.value = false
      }
    } else {
      editModel.value = {}
    }
  }
  const handleSaveUpdate = async () => {
    try {
      await formRef.value.validate()
    } catch (e) {
      return false
    }
    // 执行保存操作
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/i18n/saveOrUpdateGroup', editModel.value)
    } catch (e: any) {
      console.error(e)
      message.error(e && e.message)
      return false
    } finally {
      saveLoading.value = false
    }
    // 关闭弹窗
    modalVisible.value = false
    // 重新加载数据
    loadData()
  }
  return {
    modalVisible,
    handleShowModal,
    computedTitle,
    saveLoading,
    editModel,
    handleSaveUpdate,
    formRef,
    getLoading
  }
}

export default defineComponent({
  name: 'I18nGroupList',
  components: {
    ContextMenu,
    EditOutlined,
    DeleteOutlined
  },
  emits: ['change'],
  setup(props, { emit }) {
    const i18nRender = useI18n().t
    const listVue = ListVueSupport()
    const addEditVue = addEditVueSupport(listVue.loadTableData)
    const deleteVue = deleteVueSupport(i18nRender, listVue.loadTableData)
    const handleChangeGroup = (id: number) => {
      emit('change', id)
    }

    const handleMenuClick = (ident: string, groupId: number, event: Event) => {
      event.preventDefault()
      switch (ident) {
        case 'edit': {
          addEditVue.handleShowModal(false, groupId)
          break
        }
        case 'delete': {
          deleteVue.handleDelete(groupId)
          break
        }
      }
    }
    return {
      ...listVue,
      ...addEditVue,
      handleChangeGroup,
      handleMenuClick
    }
  },
  data() {
    return {
      permissions: SystemPermissions.i18n,
      columns: [
        {
          title: '{system.views.i18n.group.groupName}',
          field: 'groupName',
          slots: {
            default: 'table-groupName'
          }
        }
      ],
      rules: {
        groupName: [
          {
            required: true,
            message: this.$t('system.views.i18n.group.groupNameValidate'),
            trigger: 'blur'
          }
        ],
        seq: [
          {
            required: true,
            message: this.$t('system.views.i18n.group.seq'),
            trigger: 'blur',
            type: 'number'
          }
        ]
      }
    }
  }
})
</script>

<style lang="less" scoped>
@buttonContainerHeight: 60px;
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
