<template>
  <a-spin :spinning="loading">
    <a-tree
      v-bind="$attrs"
      :auto-expand-parent="autoExpandParent"
      @expand="onExpand"
      :field-names="fieldNames"
      :tree-data="computedTreeData">
      <template #title="{ deptName }">
        {{ deptName }}
      </template>
    </a-tree>
  </a-spin>
</template>

<script lang="ts">
import type { PropType } from 'vue'
import { computed, defineComponent, onMounted, reactive, ref, toRefs, watch } from 'vue'

import { errorMessage } from '/@/common/utils/SystemNotice'
import TreeUtils from '/@/utils/TreeUtils'
import ApiService from '/@/common/utils/ApiService'

const getParentKey = (key: number, treeData: Array<any>): number => {
  let parentKey
  for (let i = 0; i < treeData.length; i++) {
    const node = treeData[i]
    if (node.children) {
      if (node.children.some((item: any) => item.deptId === key)) {
        parentKey = node.deptId
      } else {
        const secondParentKey = getParentKey(key, node.children)
        if (secondParentKey) {
          parentKey = secondParentKey
        }
      }
    }
  }
  return parentKey
}

export default defineComponent({
  name: 'SysDeptTree',
  props: {
    search: {
      type: String as PropType<string>,
    },
  },
  setup(props) {
    const { search } = toRefs(props)

    const dataList = ref<Array<any>>([])
    const autoExpandParent = ref(false)
    const expandedKeys = ref<Array<number>>([])
    const loading = ref(false)

    /**
     * 树形数据计算属性
     */
    const computedTreeData = computed(() => {
      return (
        TreeUtils.convertList2Tree(
          dataList.value,
          (item) => item.deptId,
          (item) => item.parentId,
          0,
        ) || []
      )
    })

    const onExpand = (keys: Array<number>) => {
      expandedKeys.value = keys
      autoExpandParent.value = false
    }

    watch(search, (value) => {
      expandedKeys.value = dataList.value
        .map(({ deptName, deptId }: any) => {
          if (deptName.indexOf(value) > -1) {
            return getParentKey(deptId, computedTreeData.value)
          }
          return null
        })
        .filter((item, i, self) => item && self.indexOf(item) === i) as Array<number>
      autoExpandParent.value = true
    })

    /**
     * 加载数据函数
     */
    const loadData = async () => {
      loading.value = true
      try {
        dataList.value = await ApiService.postAjax('sys/dept/list', {
          sortName: 'seq',
          sortOrder: 'asc',
        })
      } catch (e) {
        errorMessage(e)
      } finally {
        loading.value = false
      }
    }

    /**
     * 加载数据
     */
    onMounted(loadData)

    return {
      computedTreeData,
      autoExpandParent,
      onExpand,
      loadData,
      loading,
      fieldNames: reactive({
        children: 'children',
        title: 'deptName',
        key: 'deptId',
      }),
    }
  },
})
</script>

<style scoped></style>
