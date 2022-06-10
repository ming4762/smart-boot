<template>
  <div class="full-height">
    <vxe-grid
      highlight-current-row
      ref="gridRef"
      :toolbar-config="toolbarConfig"
      size="small"
      :loading="dataLoading"
      height="auto"
      :data="tableData"
      :columns="columns"
      stripe
      @current-change="handleCurrentChange">
      <template #table-buttons>
        <a-form
          style="margin-left: 5px"
          layout="inline">
          <a-form-item :label="$t('system.views.i18n.i18n.titleI18nCode')">
            <a-input v-model:value="searchModel.i18nCode" size="small" />
          </a-form-item>
          <a-form-item>
            <a-button :size="buttonSizeConfig" type="primary" @click="loadData">{{ $t('common.button.search') }}</a-button>
          </a-form-item>
        </a-form>
      </template>
      <template #table-tools>
        <div style="margin-right: 5px">
          <a-button v-permission="permissions.reload" type="primary" :size="buttonSizeConfig" @click="handleReload">
            {{ $t('system.views.i18n.i18n.button.reload') }}
          </a-button>
          <a-button v-permission="permissions.add" type="primary" class="button-margin" :size="buttonSizeConfig" @click="() => handleShowModal(true, null)">
            {{ $t('common.button.add') }}
          </a-button>
          <a-button v-permission="permissions.delete" type="primary" danger class="button-margin" :size="buttonSizeConfig" @click="handleDeleteByCheckbox">
            {{ $t('common.button.delete') }}
          </a-button>
        </div>
      </template>
      <template #table-operation="{row}">
        <a-button v-permission="permissions.update" :size="tableButtonSizeConfig" type="primary" @click.stop="() => handleShowModal(false, row.i18nId)">{{ $t('common.button.edit') }}</a-button>
      </template>

      <template #pager>
        <vxe-pager
          :page-sizes="[100, 200, 500, 1000]"
          :layouts="['PrevPage', 'JumpNumber', 'NextPage', 'FullJump', 'Sizes', 'Total']"
          :page-size="tablePage.pageSize"
          :total="tablePage.total"
          :current-page="tablePage.currentPage"
          @page-change="handlePageChange" />
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
          :model="addEditModel">
          <a-form-item name="platform" :label="$t('system.views.i18n.i18n.titlePlatform')">
            <a-select
              v-model:value="addEditModel.platform">
              <a-select-option
                v-for="item in platformList"
                :key="item.key"
                :value="item.key">
                {{ $t(item.label) }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item name="i18nCode" :label="$t('system.views.i18n.i18n.titleI18nCode')">
            <a-input v-model:value="addEditModel.i18nCode" :placeholder="$t('system.views.i18n.i18n.platformValidate')" />
          </a-form-item>
          <a-form-item name="remark" :label="$t('common.table.remark')">
            <a-input v-model:value="addEditModel.remark" :placeholder="$t('common.formValidate.remark')" />
          </a-form-item>
          <a-form-item name="seq" :label="$t('common.table.seq')">
            <a-input-number v-model:value="addEditModel.seq" style="width: 100%;" :placeholder="$t('common.formValidate.seq')" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import {defineComponent, PropType, Ref, ref, toRefs, onMounted, watch, reactive, createVNode} from 'vue'
import { useI18n } from 'vue-i18n'

import { message, Modal } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

import ApiService from '@/common/utils/ApiService'
import { SystemPermissions } from '@/modules/system/constants/SystemConstants'
import SizeConfigHoops from '@/components/config/SizeConfigHooks'
import { tableUseYn } from '@/components/common/TableCommon'
import { errorMessage } from '@/components/notice/SystemNotice'
import { useVxeDelete } from '@/components/hooks/CrudHooks'

const platformListV = [
  {
    key: 'backstage',
    label: 'system.views.i18n.i18n.platform.backstage'
  }
]

const loadDataVueSupport = (groupId: Ref<number | undefined>, emit: Function) => {
  const dataLoading = ref(false)
  const tableData = ref([])
  // 搜索model
  const searchModel: any = reactive({
    i18nCode: null
  })
  // 分页信息
  const tablePage = reactive({
    currentPage: 1,
    pageSize: 100,
    total: 0
  })
  /**
   * 加载数据函数
   */
  const loadData = async () => {
    dataLoading.value = true
    try {
      const parameter: any = {}
      if (groupId && groupId.value) {
        parameter['groupId@='] = groupId.value
      }
      Object.keys(searchModel).forEach(key => {
        const value = searchModel[key]
        if (value && value.trim() !== '') {
          parameter[`${key}@like`] = value
        }
      })
      const { rows, total } = await ApiService.postAjax('sys/i18n/list', {
        limit: tablePage.pageSize,
        page: tablePage.currentPage,
        parameter: parameter
      })
      tablePage.total = total
      tableData.value = rows
    } catch (e) {
      errorMessage(e)
    } finally {
      dataLoading.value = false
    }
  }
  /**
   * 分页变化触发
   */
  const handlePageChange = ({ currentPage, pageSize }: any) => {
    tablePage.currentPage = currentPage
    tablePage.pageSize = pageSize
    loadData()
  }
  const currentChange = (id: number | null) => {
    emit('change', id)
  }
  onMounted(loadData)
  watch(groupId, () => {
    loadData()
    currentChange(null)
  })
  return {
    dataLoading,
    loadData,
    searchModel,
    tablePage,
    handlePageChange,
    tableData,
    currentChange
  }
}

const addEditVueSupport = (loadData: Function, groupId: Ref<number | undefined>) => {
  const i18n = useI18n().t
  const formRef = ref()
  const addEditModalVisible = ref(false)
  const saveLoading = ref(false)
  const getLoading = ref(false)

  // 是否是添加
  const isAdd = ref(true)
  const addEditModel = ref<any>({})
  const currentId = ref<number | null>()

  const handleShowModal = async (add: boolean, id: number | null) => {
    addEditModel.value = {}
    currentId.value = id
    isAdd.value = add
    if (!add) {
      addEditModalVisible.value = true
      try {
        getLoading.value = true
        addEditModel.value = await ApiService.postAjax('sys/i18n/getById', id)
      } catch (e) {
        errorMessage(e)
      } finally {
        getLoading.value = false
      }
    } else {
      // 验证是否选择国际化分组
      if (!(groupId && groupId.value)) {
        message.error(i18n('system.views.i18n.i18n.groupIdValidate'))
        return false
      }
      addEditModalVisible.value = true
      addEditModel.value.groupId = groupId.value
    }
  }
  /**
   * 刷新国际化信息
   */
  const handleReload = async () => {
    Modal.confirm({
      title: i18n('system.views.i18n.i18n.message.reloadConfirm'),
      content: i18n('system.views.i18n.i18n.message.reloadContent'),
      icon: createVNode(ExclamationCircleOutlined),
      onOk: async () => {
        try {
          await ApiService.postAjax('sys/i18n/reload')
          message.success(i18n('system.views.i18n.i18n.message.reloadSuccess'))
        } catch (e) {
          errorMessage(e)
        }
      }
    })
  }
  /**
   * 执行保存操作
   */
  const handleSave = async () => {
    try {
      await formRef.value.validate()
    } catch (e: any) {
      console.error(e)
      return false
    }
    saveLoading.value = true
    try {
      await ApiService.postAjax('sys/i18n/saveUpdate', addEditModel.value)
    } catch (e: any) {
      errorMessage(e)
      return false
    } finally {
      saveLoading.value = false
    }
    addEditModalVisible.value = false
    // 加载数据
    loadData()
  }
  return {
    addEditModalVisible,
    saveLoading,
    getLoading,
    handleShowModal,
    addEditModel,
    handleSave,
    isAdd,
    formRef,
    platformList: reactive(platformListV),
    handleReload
  }
}

/**
 * 国际化信息列表
 * TODO:待开发
 * 1、删除功能
 * 2、默认SEQ排序
 */
export default defineComponent({
  name: 'I18nList',
  props: {
    groupId: Number as PropType<number>
  },
  emits: ['change'],
  setup (props, { emit }) {
    const gridRef = ref()
    const { t } = useI18n()
    const { groupId } = toRefs(props)
    const loadDataVue = loadDataVueSupport(groupId, emit)
    const addEditVue = addEditVueSupport(loadDataVue.loadData, groupId)
    const handleCurrentChange = ({ row }: any) => {
      loadDataVue.currentChange(row.i18nId)
    }
    const { handleDeleteByCheckbox } = useVxeDelete(gridRef, t, async (idList: Array<number>) => {
      try {
        await ApiService.postAjax('sys/i18n/batchDeleteById', idList)
      } catch (e) {
        errorMessage(e)
      }
    }, {
      idField: 'i18nId',
      listHandler: loadDataVue.loadData
    } )
    return {
      ...loadDataVue,
      ...SizeConfigHoops(),
      ...addEditVue,
      handleCurrentChange,
      gridRef,
      handleDeleteByCheckbox
    }
  },
  data () {
    return {
      permissions: SystemPermissions.i18n,
      toolbarConfig: {
        slots: {
          buttons: 'table-buttons',
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
          field: 'platform',
          title: '{system.views.i18n.i18n.titlePlatform}',
          width: 120
        },
        {
          field: 'i18nCode',
          title: '{system.views.i18n.i18n.titleI18nCode}',
          minWidth: 260
        },
        {
          field: 'remark',
          title: '{common.table.remark}',
          width: 200
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
          field: 'updateTime',
          title: '{common.table.updateTime}',
          width: 160,
          formatter: ({cellValue}: any) => {
            if (cellValue) {
              return dayjs(cellValue).format('YYYY-MM-DD HH:mm:ss')
            }
            return null
          }
        },
        {
          field: 'updateUser',
          title: '{common.table.updateUser}',
          width: 120
        },
        {
          ...tableUseYn(this.$t).createColumn(),
          sortable: true
        },
        {
          field: 'seq',
          title: '{common.table.seq}',
          width: 120,
          sortable: true
        },
        {
          field: 'i18nId',
          title: '{common.table.operation}',
          width: 120,
          fixed: 'right',
          slots: {
            default: 'table-operation'
          }
        }
      ],
      rules: {
        platform: [
          { required: true, message: this.$t('system.views.i18n.i18n.platformValidate'), trigger: 'change' }
        ],
        i18nCode: [
          { required: true, message: this.$t('system.views.i18n.i18n.i18nCodeValidate'), trigger: 'blur' }
        ],
        seq: [
          { required: true, message: this.$t('common.formValidate.seq'), trigger: 'blur', type: 'number' }
        ]
      }
    }
  }
})
</script>

<style lang="less" scoped>
.button-margin {
  margin-left: 5px;
}
</style>
