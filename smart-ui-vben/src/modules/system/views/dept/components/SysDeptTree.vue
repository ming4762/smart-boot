<template>
  <a-spin :spinning="loading">
    <a-tree
      v-bind="getAttrs"
      :expanded-keys="expandedKeys"
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
import { computed, defineComponent, onMounted, reactive, ref, toRefs, unref, watch } from 'vue'

import { errorMessage } from '/@/common/utils/SystemNotice'
import TreeUtils from '/@/utils/TreeUtils'
import ApiService from '/@/common/utils/ApiService'
import { propTypes } from '/@/utils/propTypes'

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
    // 是否异步加载
    async: propTypes.bool,
  },
  setup(props, { attrs }) {
    const { search, async: asyncRef } = toRefs(props)

    const dataList = ref<Array<any>>([])
    const autoExpandParent = ref(false)
    const expandedKeys = ref<Array<number>>([])
    const loading = ref(false)

    const getAttrs = computed(() => {
      const result: any = {
        ...attrs,
      }
      if (unref(asyncRef)) {
        result.loadData = handleAsyncLoadData
      }
      return result
    })

    /**
     * 树形数据计算属性
     */
    const computedTreeData = computed(() => {
      const async = unref(asyncRef)
      if (async) {
        return unref(dataList)
      }
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

    const handleAsyncLoadData = async (treeNode) => {
      const dataRef = treeNode.dataRef
      dataRef.children = await loadData(dataRef.deptId)
      dataList.value = [...unref(dataList)]
    }

    /**
     * 加载数据函数
     */
    const loadData = async (parentId: number | undefined | null) => {
      const parameter: Recordable = {
        sortName: 'seq',
        sortOrder: 'asc',
      }
      if (parentId !== undefined && parentId !== null) {
        parameter.parameter = {
          'parentId@=': parentId,
        }
      }
      try {
        loading.value = true
        const result = (await ApiService.postAjax('sys/dept/list', parameter)) as any[]

        result.forEach((item) => {
          if (item.hasChild !== true) {
            item.isLeaf = true
          }
        })
        if (unref(asyncRef)) {
          if (parentId === 0) {
            dataList.value = result
          } else {
            return result
          }
        } else {
          dataList.value = result
        }
      } catch (e) {
        errorMessage(e)
      } finally {
        loading.value = false
      }
    }

    /**
     * 加载数据
     */
    onMounted(() => {
      let parentId: number | undefined = undefined
      if (unref(asyncRef)) {
        parentId = 0
      }
      loadData(parentId)
    })

    return {
      computedTreeData,
      autoExpandParent,
      onExpand,
      loadData,
      loading,
      expandedKeys,
      fieldNames: reactive({
        children: 'children',
        title: 'deptName',
        key: 'deptId',
      }),
      getAttrs,
      handleAsyncLoadData,
    }
  },
})
</script>

<style scoped></style>
