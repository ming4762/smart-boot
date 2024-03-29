<template>
  <div class="full-height">
    <vxe-grid
      height="auto"
      :toolbar-config="toolbarConfig"
      size="small"
      stripe
      :loading="dataLoading"
      :data="tableData"
      :columns="columns">
      <template #table-tools>
        <div style="margin-right: 5px">
          <a-button v-permission="permissions.add" style="margin-right: 5px;" type="primary" :size="buttonSizeConfig" @click="() => handleShowModal(true, null)">{{ $t('common.button.add') }}</a-button>
          <a-button v-permission="permissions.delete" type="primary" danger :size="buttonSizeConfig">{{ $t('common.button.delete') }}</a-button>
        </div>
      </template>

      <template #table-operation="{ row }">
        <a-button v-permission="permissions.update" :size="tableButtonSizeConfig" type="primary" @click.stop="() => handleShowModal(false, row.i18nItemId)">{{ $t('common.button.edit') }}</a-button>
      </template>
    </vxe-grid>

    <a-modal
      v-model:visible="addEditModalVisible"
      :confirm-loading="saveLoading"
      :title="isAdd ? $t('common.button.add') : $t('common.button.edit')"
      @ok="handleSave">
      <a-spin :spinning="getLoading">
        <a-form
          ref="formRef"
          :rules="rules"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 17 }"
          :model="saveModel">
          <a-form-item name="locale" :label="$t('system.views.i18n.i18nItem.titleLocale')">
            <a-input v-model:value="saveModel.locale" :placeholder="$t('system.views.i18n.i18nItem.localeValidate')" />
          </a-form-item>
          <a-form-item name="value" :label="$t('system.views.i18n.i18nItem.titleValue')">
            <a-input v-model:value="saveModel.value" :placeholder="$t('system.views.i18n.i18nItem.valueValidate')" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, PropType, toRefs, Ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

import dayjs from 'dayjs'
import { message } from 'ant-design-vue'

import ApiService from '@/common/utils/ApiService'
import { SystemPermissions } from '@/modules/system/constants/SystemConstants'
import SizeConfigHoops from '@/components/config/SizeConfigHooks'

const loadDataVueSupport = (i18nId: Ref<number | null | undefined>) => {
  const dataLoading = ref(false)
  const tableData = ref([])

  /**
   * 加载数据函数
   */
  const loadData = async () => {
    dataLoading.value = true
    try {
      tableData.value = await ApiService.postAjax('sys/i18nItem/list', {
        parameter: {
          'i18nId@=': i18nId.value
        }
      })
    } catch (e: any) {
      message.error(e && e.message)
    } finally {
      dataLoading.value = false
    }
  }
  watch(i18nId, () => {
    if (i18nId && i18nId.value) {
      loadData()
    }
    tableData.value = []
  })

  return {
    dataLoading,
    tableData,
    loadData
  }
}

const addEditVueSupport = (loadData: Function, i18nId: Ref) => {
  const formRef = ref()
  const i18n = useI18n().t
  const addEditModalVisible = ref(false)
  const isAdd = ref(false)
  const saveLoading = ref(false)
  const getLoading = ref(false)
  const saveModel = ref<any>({})
  const handleShowModal = async (add: boolean, id: number | null) => {
    saveModel.value = {}
    isAdd.value = add
    if (!add) {
      addEditModalVisible.value = true
      getLoading.value = true
      try {
        saveModel.value = await ApiService.postAjax('sys/i18nItem/getById', id) || {}
      } finally {
        getLoading.value = false
      }
    } else {
      if (!(i18nId && i18nId.value)) {
        message.error(i18n('system.views.i18n.i18nItem.i18nIdValidate'))
        return false
      }
      addEditModalVisible.value = true
      saveModel.value.i18nId = i18nId.value
    }
  }
  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    try {
      await formRef.value.validate()
    } catch (e) {
      console.error(e)
      return false
    }
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/i18nItem/saveUpdate', saveModel.value)
    } catch (e: any) {
      console.error(e)
      message.error(e && e.message)
      return false
    } finally {
      saveLoading.value = false
    }
    addEditModalVisible.value = false
    loadData()
  }
  return {
    handleShowModal,
    addEditModalVisible,
    isAdd,
    handleSave,
    saveLoading,
    getLoading,
    saveModel,
    formRef
  }
}

export default defineComponent({
  name: 'I18nItemList',
  props: {
    i18nId: Number as PropType<number | null>
  },
  setup (props) {
    const { i18nId } = toRefs(props)
    const loadDataVue = loadDataVueSupport(i18nId)
    const addEditVue = addEditVueSupport(loadDataVue.loadData, i18nId)
    return {
      ...loadDataVue,
      ...SizeConfigHoops(),
      ...addEditVue
    }
  },
  data () {
    return {
      permissions: SystemPermissions.i18n,
      toolbarConfig: {
        slots: {
          tools: 'table-tools'
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
          title: '{system.views.i18n.i18nItem.titleLocale}',
          field: 'locale',
          width: 120
        },
        {
          title: '{system.views.i18n.i18nItem.titleValue}',
          field: 'value',
          minWidth: 160
        },
        {
          field: 'createTime',
          title: '{common.table.createTime}',
          width: 160,
          formatter: ({cellValue}: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return null
          }
        },
        {
          field: 'createUser',
          title: '{common.table.createUser}',
          width: 120
        },
        {
          field: 'i18nItemId',
          title: '{common.table.operation}',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
      ],
      rules: {
        locale: [
          { required: true, message: this.$t('system.views.i18n.i18nItem.localeValidate'), trigger: 'blur' }
        ],
        value: [
          { required: true, message: this.$t('system.views.i18n.i18nItem.valueValidate'), trigger: 'blur' }
        ]
      }
    }
  }
})
</script>

<style scoped>

</style>
