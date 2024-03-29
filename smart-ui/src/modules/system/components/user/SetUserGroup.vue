<template>
  <a-layout class="full-height">
    <a-layout-header style="height: 56px; background: white; text-align: center">
      <h3>{{ $t('system.views.userGroup.title') }}</h3>
    </a-layout-header>
    <a-divider style="margin: 0" />
    <a-layout-content style="background: white;">
      <div class="full-height">
        <a-spin class="full-height" :spinning="dataLoading">
          <a-table
            class="full-height"
            size="small"
            row-key="groupId"
            :row-selection="rowSelection"
            :columns="columns"
            :show-header="false"
            :pagination="false"
            :data-source="allUserGroup" />
        </a-spin>
      </div>
    </a-layout-content>
    <a-divider style="margin: 0" />
    <a-layout-footer style="height: 50px; text-align: center; padding: 10px 0; background: white;">
      <div style="padding: 0 5px">
        <a-button :disabled="!saveButtonVisible" :loading="saveLoading" block type="primary" @click="handleSave">{{ $t('common.button.save') }}</a-button>
      </div>
    </a-layout-footer>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, PropType, reactive, ref, toRefs, watch } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 设置用户组组件
 */
export default defineComponent({
  name: 'SetUserGroup',
  props: {
    saveButtonVisible: {
      type: Boolean as PropType<boolean>,
      default: true
    },
    selectKeys: {
      type: Array as PropType<Array<number>>,
      default: () => []
    },
    saveHandler: Function
  },
  setup (props) {
    const { selectKeys } = toRefs(props)
    // 数据加载状态
    const dataLoading = ref(false)
    const allUserGroup = ref<Array<any>>([])
    const saveLoading = ref(false)
    const rowSelection = reactive({
      columnWidth: 60,
      selectedRowKeys: [] as Array<number>,
      onChange: (selectedRowKeys: Array<number>) => {
        rowSelection.selectedRowKeys = selectedRowKeys
      }
    })
    /**
     * 加载所有用户组数据
     */
    const loadAllUserGroup = async () => {
      try {
        dataLoading.value = true
        allUserGroup.value = await ApiService.postAjax('sys/userGroup/list', {
          sortName: 'seq',
          parameter: {
            'useYn@=': true
          }
        })
      } catch (e) {
        errorMessage(e)
      } finally {
        dataLoading.value = false
      }
    }
    watch(selectKeys, () => {
      rowSelection.selectedRowKeys = selectKeys.value
    })
    const handleSave = async () => {
      saveLoading.value = true
      try {
        if (props.saveHandler) {
          await props.saveHandler(rowSelection.selectedRowKeys)
        }
      } finally {
        saveLoading.value = false
      }
    }
    onMounted(loadAllUserGroup)
    return {
      dataLoading,
      rowSelection,
      allUserGroup,
      saveLoading,
      handleSave
    }
  },
  data () {
    return {
      columns: [
        {
          dataIndex: 'groupName'
        }
      ]
    }
  }
})
</script>

<style scoped>

</style>
