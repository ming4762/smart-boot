<template>
  <div class="dept-edit-container">
    <a-spin :spinning="loading">
      <a-form
        class="edit-form"
        :rules="rules"
        :model="deptData"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 17 }">
        <a-form-item name="parent" :label="$t('system.views.dept.title.parent')">
          <a-input :value="deptData.parentDept ? deptData.parentDept.deptName : ''" disabled />
        </a-form-item>
        <a-form-item name="deptCode" :label="$t('system.views.dept.title.deptCode')">
          <a-input
            v-model:value="deptData.deptCode"
            :placeholder="$t('system.views.dept.validate.deptCode')" />
        </a-form-item>
        <a-form-item name="deptType" :label="$t('system.views.dept.title.deptType')">
          <a-select v-model:value="deptData.deptType">
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
            v-model:value="deptData.deptName"
            :placeholder="$t('system.views.dept.validate.deptName')" />
        </a-form-item>
        <a-form-item name="useYn" :label="$t('common.table.useYn')">
          <a-switch v-model:checked="deptData.useYn" />
        </a-form-item>
        <a-form-item :label="$t('system.views.dept.title.email')" name="email">
          <a-input
            v-model:value="deptData.email"
            :placeholder="$t('system.views.dept.validate.email')" />
        </a-form-item>
        <a-form-item :label="$t('system.views.dept.title.director')" name="director">
          <a-input
            v-model:value="deptData.director"
            :placeholder="$t('system.views.dept.validate.director')" />
        </a-form-item>
        <a-form-item :label="$t('system.views.dept.title.phone')" name="phone">
          <a-input
            v-model:value="deptData.phone"
            :placeholder="$t('system.views.dept.validate.phone')" />
        </a-form-item>
        <a-form-item :label="$t('common.table.seq')" name="seq">
          <a-input-number
            v-model:value="deptData.seq"
            style="width: 100%"
            :placeholder="$t('common.formValidate.seq')" />
        </a-form-item>
        <a-form-item :label="$t('common.table.remark')" name="remark">
          <a-input
            v-model:value="deptData.remark"
            :placeholder="$t('common.formValidate.remark')" />
        </a-form-item>
        <a-form-item :label="$t('common.table.createUser')">
          <a-input disabled :value="deptData.createUser ? deptData.createUser.fullName : ''" />
        </a-form-item>
        <a-form-item :label="$t('common.table.createTime')">
          <a-input disabled :value="deptData.createTime" />
        </a-form-item>
        <a-form-item :label="$t('common.table.updateUser')">
          <a-input disabled :value="deptData.updateUser ? deptData.updateUser.fullName : ''" />
        </a-form-item>
        <a-form-item :label="$t('common.table.updateTime')">
          <a-input disabled :value="deptData.updateTime" />
        </a-form-item>
      </a-form>
      <a-divider />
      <div style="text-align: right">
        <a-button
          v-permission="'sys:dept:update'"
          :disabled="deptCode === null"
          type="primary"
          @click="handleSave">
          {{ $t('common.button.save') }}
        </a-button>
      </div>
    </a-spin>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, ref, watch, onMounted } from 'vue'
import type { PropType } from 'vue'
import { useI18n } from 'vue-i18n'

import { errorMessage } from '/@/common/utils/SystemNotice'
import ApiService from '/@/common/utils/ApiService'
import { useLoadDictItem } from '/@/modules/system/hooks/dict/SysDictHooks'
import { message } from 'ant-design-vue'

export default defineComponent({
  name: 'SysDeptEdit',
  props: {
    deptCode: {
      type: Number as PropType<number>,
      default: null,
    },
    rules: {
      type: Object,
      required: true,
    },
  },
  emits: ['update'],
  setup(props, { emit }) {
    const { t } = useI18n()

    const { deptCode } = toRefs(props)
    const loading = ref(false)
    const deptData = ref<any>({})
    const dictCode = ref('SYSTEM_ORGANIZATION_TYPE')

    /**
     * 加载数据函数
     */
    const loadData = async () => {
      if (deptCode.value === null) {
        deptData.value = {}
        return
      }
      loading.value = true
      try {
        deptData.value = await ApiService.postAjax('sys/dept/getById', deptCode.value)
      } catch (e) {
        errorMessage(e)
      } finally {
        loading.value = false
      }
    }
    onMounted(loadData)
    watch(deptCode, loadData)

    /**
     * 执行保存操作
     */
    const handleSave = async () => {
      const sendData = Object.assign({}, deptData.value)
      delete sendData.createUser
      delete sendData.parentDept
      delete sendData.updateUser
      loading.value = true
      try {
        await ApiService.postAjax('sys/dept/saveUpdate', sendData)
        loadData()
        message.success(t('common.message.editSuccess'))
        emit('update')
      } catch (e) {
        errorMessage(e)
      } finally {
        loading.value = false
      }
    }

    return {
      loading,
      ...useLoadDictItem(dictCode),
      deptData,
      handleSave,
    }
  },
})
</script>

<style scoped lang="less">
.ant-form-item {
  margin: 0 0 15px !important;
}
.dept-edit-container {
  height: 100%;
  :deep(.ant-spin-nested-loading) {
    height: 100%;
    .ant-spin-container {
      height: 100%;
    }
  }
  :deep(.ant-divider) {
    margin: 5px 0 10px;
  }
}
.edit-form {
  height: calc(100% - 48px);
  overflow: auto;
}
</style>
