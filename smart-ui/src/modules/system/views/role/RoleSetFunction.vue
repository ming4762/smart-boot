<template>
  <a-layout class="full-height">
    <a-layout-header style="height: 48px; background: white; text-align: center; line-height: 48px">
      <h3>设置功能</h3>
    </a-layout-header>
    <a-divider style="margin: 0" />
    <a-layout-content style="background: white;">
      <a-spin :spinning="dataLoading">
        <a-tree
          v-model:checkedKeys="checkedKeysModel"
          check-strictly
          :tree-data="functionTreeData"
          checkable></a-tree>
      </a-spin>
    </a-layout-content>
    <a-divider style="margin: 0" />
    <a-layout-footer style="height: 50px; text-align: center; padding: 10px 0; background: white;">
      <div style="padding: 0 5px">
        <a-button v-permission="permissions.setFunction" :loading="saveLoading" block type="primary" @click="handleSave">{{ $t('common.button.save') }}</a-button>
      </div>
    </a-layout-footer>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, PropType, toRefs, watch } from 'vue'

import { message } from 'ant-design-vue'

import TreeUtils from '@/common/utils/TreeUtils'
import ApiService from '@/common/utils/ApiService'

import { SystemPermissions } from '@/modules/system/constants/SystemConstants'

/**
 * 设置角色对应的功能
 */
export default defineComponent({
  name: 'RoleSetFunction',
  props: {
    roleId: {
      type: Number as PropType<number>,
      default: null
    }
  },
  setup (props) {
    const { roleId } = toRefs(props)
    // 树形控件数据
    const functionTreeData = ref<Array<any>>([])
    const dataLoading = ref(false)
    const saveLoading = ref(false)
    const checkedKeysModel = ref<any>([])

    /**
     * 加载功能树函数
     */
    const loadFunctionTreeData = async () => {
      dataLoading.value = true
      try {
        const result: Array<any> = await ApiService.postAjax('sys/function/list', { sortName: 'seq' })
        functionTreeData.value = TreeUtils.convertList2Tree(result.map(({ functionId, functionName, parentId }: any) => {
          return {
            key: functionId,
            title: functionName,
            parentId: parentId
          }
        }), ['key', 'parentId'], 0) || []
      } finally {
        dataLoading.value = false
      }
    }
    /**
     * 加载角色对应的功能ID
     */
    const loadRoleFunctions = async () => {
      if (roleId.value !== null) {
        dataLoading.value = true
        try {
          checkedKeysModel.value = await ApiService.postAjax('sys/role/listFunctionId', roleId.value)
        } finally {
          dataLoading.value = false
        }
      }
    }
    /**
     * 执行保存操作
     */
    const handleSave = async () => {
      if (roleId.value === null) {
        message.error('请先选定角色')
        return false
      }
      saveLoading.value = true
      try {
        await ApiService.postAjax('sys/role/saveRoleMenu', {
          roleId: roleId.value,
          functionIdList: checkedKeysModel.value.checked
        })
        message.success('保存成功')
      } finally {
        saveLoading.value = false
      }
    }
    watch(roleId, loadRoleFunctions)
    onMounted(loadFunctionTreeData)
    return {
      functionTreeData,
      dataLoading,
      saveLoading,
      checkedKeysModel,
      handleSave,
      permissions: SystemPermissions.role
    }
  }
})
</script>

<style scoped>

</style>
