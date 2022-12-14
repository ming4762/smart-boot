<template>
  <div class="full-height dept-container" style="padding: 10px">
    <div class="full-height left-tree" style="padding: 10px; background: white">
      <div>
        <a-button
          v-permission="'sys:dept:save'"
          type="primary"
          :size="buttonSizeConfig"
          @click="handleAdd">
          <plus-outlined />
          {{ $t('common.button.add') }}
        </a-button>
        <a-button
          type="primary"
          v-permission="'sys:dept:save'"
          :size="buttonSizeConfig"
          style="margin-left: 5px"
          @click="handleAddChild">
          <plus-outlined />
          {{ $t('system.views.dept.button.addChild') }}
        </a-button>
        <a-button
          type="primary"
          style="margin-left: 5px"
          v-permission="'sys:dept:delete'"
          danger
          :size="buttonSizeConfig"
          @click="() => handleDeleteById(currentDeptCode)">
          <delete-outlined />
          {{ $t('common.button.delete') }}
        </a-button>
      </div>
      <div style="margin: 10px 0 5px 0">
        <a-input-search v-model:value="searchValue" />
      </div>
      <sys-dept-tree ref="treeRef" :search="searchValue" @select="handleTreeSelect" />
    </div>
    <div class="full-height right-tab">
      <a-tabs>
        <a-tab-pane :tab="$t('system.views.dept.title.baseMessage')">
          <sys-dept-edit
            :rules="rules"
            :dept-code="currentDeptCode"
            @update="() => treeRef.loadData()" />
        </a-tab-pane>
      </a-tabs>
    </div>
    <a-modal v-bind="modalProps">
      <a-spin :spinning="spinning">
        <a-form
          v-bind="formProps"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 17 }"
          :rules="rules">
          <a-input v-model:value="formProps.model.deptId" style="display: none" />
          <a-input v-model:value="formProps.model.parentId" style="display: none" />
          <a-form-item
            v-show="parentFieldVisible"
            name="parent"
            :label="$t('system.views.dept.title.parent')">
            <a-input v-model:value="formProps.model.parentId" disabled />
          </a-form-item>
          <a-form-item name="deptCode" :label="$t('system.views.dept.title.deptCode')">
            <a-input
              v-model:value="formProps.model.deptCode"
              :placeholder="$t('system.views.dept.validate.deptCode')" />
          </a-form-item>
          <a-form-item name="deptType" :label="$t('system.views.dept.title.deptType')">
            <a-select v-model:value="formProps.model.deptType">
              <a-select-option
                v-for="item in dictData"
                :key="item.dictCode + item.dictItemCode"
                :value="item.dictItemCode">
                {{ item.dictItemName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item :label="$t('system.views.dept.title.deptName')" name="deptName">
            <a-input
              v-model:value="formProps.model.deptName"
              :placeholder="$t('system.views.dept.validate.deptName')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.dept.title.email')" name="email">
            <a-input
              v-model:value="formProps.model.email"
              :placeholder="$t('system.views.dept.validate.email')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.dept.title.director')" name="director">
            <a-input
              v-model:value="formProps.model.director"
              :placeholder="$t('system.views.dept.validate.director')" />
          </a-form-item>
          <a-form-item :label="$t('system.views.dept.title.phone')" name="phone">
            <a-input
              v-model:value="formProps.model.phone"
              :placeholder="$t('system.views.dept.validate.phone')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.seq')" name="seq">
            <a-input-number
              v-model:value="formProps.model.seq"
              style="width: 100%"
              :placeholder="$t('common.formValidate.seq')" />
          </a-form-item>
          <a-form-item :label="$t('common.table.remark')" name="remark">
            <a-input
              v-model:value="formProps.model.remark"
              :placeholder="$t('common.formValidate.remark')" />
          </a-form-item>
        </a-form>
      </a-spin>
    </a-modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useI18n } from 'vue-i18n'

import { PlusOutlined, DeleteOutlined } from '@ant-design/icons-vue'

import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
import { useAddEdit, useVxeDelete } from '/@/hooks/page/CrudHooks'
import { useLoadDictItem } from '/@/modules/system/hooks/dict/SysDictHooks'

import SysDeptTree from './components/SysDeptTree.vue'
import SysDeptEdit from './components/SysDeptEdit.vue'

import { handleGetById, handleSaveUpdate, handleDelete, useAddChild } from './SysDeptHook'

/**
 * 部门管理树
 */
export default defineComponent({
  name: 'SysDeptTreeList',
  components: {
    SysDeptTree,
    PlusOutlined,
    DeleteOutlined,
    SysDeptEdit,
  },
  setup() {
    const gridRef = ref()
    const treeRef = ref()

    const { t } = useI18n()
    const dictCode = ref('SYSTEM_DEPT_TYPE')
    const parentFieldVisible = ref(false)

    /**
     * 当前选中节点的code
     */
    const currentDeptCode = ref<number | null>(null)
    const handleTreeSelect = (selectedKeys: Array<number>) => {
      if (selectedKeys.length > 0) {
        currentDeptCode.value = selectedKeys[0]
      } else {
        currentDeptCode.value = null
      }
    }

    /**
     * 添加修改hook
     */
    const addEditHook = useAddEdit(
      gridRef,
      handleGetById,
      () => treeRef.value.loadData(),
      handleSaveUpdate,
      t,
      {
        idField: 'deptId',
        defaultModel: {
          seq: 1,
        },
      },
    )

    const { handleSetModel, handleAddEdit } = addEditHook
    const addChildHook = useAddChild(
      t,
      handleSetModel,
      handleAddEdit,
      currentDeptCode,
      parentFieldVisible,
    )

    /**
     * 添加操作函数
     */
    const handleAdd = () => {
      parentFieldVisible.value = false
      handleSetModel({
        parentId: 0,
      })
      handleAddEdit(true, null)
    }

    /**
     * 删除hook
     */
    const deleteHook = useVxeDelete(gridRef, t, handleDelete, {
      idField: 'deptId',
      listHandler: () => treeRef.value.loadData(),
    })
    return {
      gridRef,
      treeRef,
      ...addEditHook,
      handleTreeSelect,
      currentDeptCode,
      parentFieldVisible,
      ...addChildHook,
      ...deleteHook,
      ...useLoadDictItem(dictCode),
      ...useSizeSetting(),
      searchValue: ref(),
      handleAdd,
    }
  },
  data() {
    return {
      rules: {
        deptCode: [
          {
            required: true,
            trigger: ['blur'],
            message: this.$t('system.views.dept.rules.deptCode_NOT_EMPTY'),
          },
        ],
        deptType: [
          {
            required: true,
            trigger: ['change'],
            message: this.$t('system.views.dept.rules.deptType_NOT_EMPTY'),
          },
        ],
        deptName: [
          {
            required: true,
            trigger: ['blur'],
            message: this.$t('system.views.dept.rules.deptName_NOT_EMPTY'),
          },
        ],
      },
    }
  },
})
</script>

<style lang="less" scoped>
@leftWidth: 40%;
.left-tree {
  width: @leftWidth;
  display: inline-block;
}
.right-tab {
  display: inline-block;
  float: right;
  width: calc(60% - 10px);
  background: white;
  margin-left: 10px;
  padding: 10px;
}
</style>
