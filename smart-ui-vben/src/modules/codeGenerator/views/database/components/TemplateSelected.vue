<template>
  <a-transfer
    class="db-template-selected"
    :data-source="transDataSource"
    :target-keys="targetKeysModel"
    show-search
    :render="(item: any) => item.title"
    @change="handleTransChange" />
</template>

<script lang="ts">
import { defineComponent, ref, toRefs, onMounted } from 'vue'
import type { PropType } from 'vue'

import { Transfer } from 'ant-design-vue'

import ApiService from '/@/common/utils/ApiService'

/**
 * 模板选择组件
 */
export default defineComponent({
  name: 'TemplateSelected',
  components: {
    ATransfer: Transfer,
  },
  props: {
    templateType: {
      type: String as PropType<string>,
    },
  },
  emits: ['templateChange'],
  setup(props, content) {
    const { templateType } = toRefs(props)
    // 所有模板数据
    const transDataSource = ref([])
    const dataLoading = ref(false)
    const targetKeysModel = ref<Array<string>>([])
    /**
     * 加载模板数据
     */
    const loadData = async () => {
      dataLoading.value = true
      targetKeysModel.value = []
      try {
        const result = await ApiService.postAjax('db/code/template/list', {
          parameter: {
            'templateType@=': templateType.value,
          },
        })
        transDataSource.value = result.map((item: any) => {
          return {
            key: item.templateId + '',
            title: item.name,
          }
        })
      } finally {
        dataLoading.value = false
      }
    }
    onMounted(loadData)
    const handleTransChange = (targetKeys: Array<string>) => {
      content.emit('templateChange', targetKeys)
      targetKeysModel.value = targetKeys
    }
    return {
      transDataSource,
      targetKeysModel,
      handleTransChange,
      loadData,
    }
  },
})
</script>

<style lang="less" scoped>
.db-template-selected {
  ::v-deep(.ant-transfer-list) {
    width: 46%;
    flex: none;
    height: 450px;
  }
}
</style>
