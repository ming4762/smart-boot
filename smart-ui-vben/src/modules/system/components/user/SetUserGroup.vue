<template>
  <Layout class="full-height">
    <LayoutHeader style="height: 56px; background: white; text-align: center">
      <h3>{{ $t('system.views.userGroup.title') }}</h3>
    </LayoutHeader>
    <Divider style="margin: 0" />
    <LayoutContent style="background: white">
      <div class="full-height">
        <a-spin class="full-height" :spinning="dataLoading">
          <Table
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
    </LayoutContent>
    <a-divider style="margin: 0" />
    <LayoutFooter style="height: 50px; text-align: center; padding: 10px 0; background: white">
      <div style="padding: 0 5px">
        <a-button
          :disabled="!saveButtonVisible"
          :loading="saveLoading"
          block
          type="primary"
          @click="handleSave">
          {{ $t('common.button.save') }}
        </a-button>
      </div>
    </LayoutFooter>
  </Layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref, toRefs, watch } from 'vue'
import type { PropType } from 'vue'

import {
  Layout,
  LayoutHeader,
  Divider,
  LayoutContent,
  LayoutFooter,
  Table,
  Spin,
} from 'ant-design-vue'

import ApiService from '/@/common/utils/ApiService'

/**
 * 设置用户组组件
 */
export default defineComponent({
  name: 'SetUserGroup',
  components: {
    Layout,
    LayoutHeader,
    Divider,
    LayoutContent,
    LayoutFooter,
    Table,
    ADivider: Divider,
    ASpin: Spin,
  },
  props: {
    saveButtonVisible: {
      type: Boolean as PropType<boolean>,
      default: true,
    },
    selectKeys: {
      type: Array as PropType<Array<number>>,
      default: () => [],
    },
    saveHandler: Function,
  },
  setup(props) {
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
      },
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
            'useYn@=': true,
          },
        })
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
      handleSave,
    }
  },
  data() {
    return {
      columns: [
        {
          dataIndex: 'groupName',
        },
      ],
    }
  },
})
</script>

<style scoped></style>
