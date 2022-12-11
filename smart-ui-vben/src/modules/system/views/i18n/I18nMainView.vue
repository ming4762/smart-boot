<template>
  <div class="full-height" style="padding: 10px">
    <Layout class="full-height i18n-main-container">
      <LayoutSider theme="light" width="300px" class="full-height i18n-group-container">
        <!--    国际化分组    -->
        <I18nGroupList @change="(id) => (groupId = id)" />
      </LayoutSider>
      <Layout>
        <LayoutContent class="i18n-container" style="background: #f0f2f5; margin-bottom: 2px">
          <I18nList :group-id="groupId" @change="(id) => (i18nId = id)" />
        </LayoutContent>
        <LayoutContent class="i18n-item-container" style="background: antiquewhite">
          <I18nItemList :i18n-id="i18nId" />
        </LayoutContent>
      </Layout>
    </Layout>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

import { Layout, LayoutContent, LayoutSider } from 'ant-design-vue'

import I18nGroupList from './components/I18nGroupList.vue'
import I18nList from './components/I18nList.vue'
import I18nItemList from './components/I18nItemList.vue'

export default defineComponent({
  name: 'I18nMainView',
  components: {
    I18nGroupList,
    I18nList,
    I18nItemList,
    Layout,
    LayoutContent,
    LayoutSider,
  },
  setup() {
    const groupId = ref<number>()
    const i18nId = ref<number>()
    return {
      groupId,
      i18nId,
    }
  },
})
</script>

<style lang="less" scoped>
.i18n-main-container {
  .i18n-group-container {
    margin-right: 5px;
  }
  .i18n-container {
    height: 60%;
  }
  .i18n-item-container {
    height: 40%;
  }
}
</style>
