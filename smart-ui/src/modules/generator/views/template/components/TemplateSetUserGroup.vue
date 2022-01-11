<template>
  <a-layout class="full-height">
    <a-layout-header style="height: 56px; background: white; text-align: center">
      <h3>{{ $t('generator.views.template.title.userGroup') }}</h3>
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
import { defineComponent, PropType, toRefs, onMounted, ref, reactive, watch, computed } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { isSuperAdmin, getCurrentUserId } from '@/common/auth/AuthUtils'

import { message } from 'ant-design-vue'

/**
 * 模板设置关联的用户组
 */
export default defineComponent({
  name: 'TemplateSetUserGroup',
  props: {
    template: {
      type: Object as PropType<any>,
      default: null
    }
  },
  setup (props) {
    const { template } = toRefs(props)
    const dataLoading = ref(false)
    // 保存状态
    const saveLoading = ref(false)
    const allUserGroup = ref<Array<any>>([])
    const rowSelection = reactive({
      columnWidth: 60,
      selectedRowKeys: [] as Array<number>,
      onChange: (selectedRowKeys: Array<number>) => {
        rowSelection.selectedRowKeys = selectedRowKeys
      }
    })
    /**
     * 保存安装显示状态
     */
    const saveButtonVisible = computed(() => {
      if (isSuperAdmin()) {
        return true
      }
      const currentUserId = getCurrentUserId
      const templateValue: any = template.value
      return templateValue ? (templateValue.createUserId === currentUserId) : false
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
      } finally {
        dataLoading.value = false
      }
    }
    /**
     * 加载模板对应的用户组信息
     */
    const loadTemplateUserGroup = async () => {
      const templateValue: any = template.value
      if (templateValue === null) {
        rowSelection.selectedRowKeys = []
      }
      try {
        dataLoading.value = true
        const result: Array<any> = await ApiService.postAjax('db/code/template/listUserGroupByTemplate', templateValue.templateId)
        rowSelection.selectedRowKeys = result.map(item => item.groupId)
      } finally {
        dataLoading.value = false
      }
    }
    /**
     * 保存操作
     */
    const handleSave = async () => {
      const templateValue: any = template.value
      if (!templateValue) {
        message.error('请先指定模板')
        return false
      }
      try {
        saveLoading.value = true
        await ApiService.postAjax('db/code/template/saveTemplateUserGroup', {
          templateId: templateValue.templateId,
          groupIdList: rowSelection.selectedRowKeys
        })
        message.success('保存成功')
      } finally {
        saveLoading.value = false
      }
    }
    watch(template, loadTemplateUserGroup)
    onMounted(loadAllUserGroup)
    return {
      dataLoading,
      allUserGroup,
      rowSelection,
      handleSave,
      saveLoading,
      saveButtonVisible
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
