<template>
  <div class="full-height" style="padding: 10px; height: 100%">
    <div style="padding-bottom: 6px">
      <a-button type="primary" @click="handleDownloadAll">下载全部</a-button>
    </div>
    <a-tabs class="code-container">
      <a-tab-pane v-for="item in data" :key="item.templateId" :tab="item.templateName">
        <Codemirror read-only :mode="item.language" :code="item.code" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'

import { Codemirror } from '/@/components/Codemirror'

import ApiService from '/@/common/utils/ApiService'
import FileUtils from '/@/utils/file/FileUtils'

import { extensionLanguageMap } from './CodeCreateSupport'

/**
 * 代码生成页面
 */
export default defineComponent({
  name: 'CodeCreateView',
  components: {
    Codemirror,
  },
  // @ts-ignore
  setup(props, { attrs }: any) {
    const data = ref<Array<any>>([])
    const dataLoading = ref(false)
    /**
     * 加载数据函数
     */
    const loadData = async () => {
      dataLoading.value = true
      try {
        data.value = await ApiService.postAjax(
          'db/code/main/createCode',
          Object.assign({}, attrs, {
            templateIdList: attrs.templateIdList.split(','),
          }),
        )
      } finally {
        dataLoading.value = false
      }
    }
    /**
     * 下载全部
     */
    const handleDownloadAll = () => {
      data.value.forEach((item): any => {
        const filename = `${item.filename}.${extensionLanguageMap[item.language]}`
        FileUtils.createAndDownload(filename, item.code)
      })
    }
    onMounted(loadData)
    return {
      data,
      handleDownloadAll,
    }
  },
})
</script>

<style lang="less" scoped>
.code-container {
  height: calc(100% - 38px);
  border: 1px solid gainsboro;
  ::v-deep(.ant-tabs-content) {
    height: calc(100% - 60px);
  }
}
</style>
