<template>
  <a-select v-bind="$attrs" v-model:value="value">
    <a-select-option
      v-for="item in dataList"
      :key="item.dictCode + item.dictItemCode"
      :value="item.dictItemCode">
      {{ item.dictItemName }}
    </a-select-option>
  </a-select>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, toRefs, watch } from 'vue'
import type { PropType } from 'vue'

import ApiService from '@/common/utils/ApiService'
import { errorMessage } from '@/components/notice/SystemNotice'

/**
 * 字典下拉列
 */
export default defineComponent({
  name: 'DictSelect',
  props: {
    dictCode: {
      type: String as PropType<string>,
      required: true
    },
    selectValue: [String, Number, Array<string | Number>]
  },
  setup(props, { emit }) {
    const { dictCode, selectValue } = toRefs(props)
    const dataList = ref<Array<any>>([])
    const value = ref<any>(selectValue.value)

    const loadData = async () => {
      try {
        dataList.value = await ApiService.postAjax('sys/dictItem/list', {
          sortName: 'seq',
          sortOrder: 'asc',
          parameter: {
            'dictCode@=': dictCode.value
          }
        })
      } catch (e) {
        errorMessage(e)
      }
    }

    watch(dictCode, loadData)

    watch(value, () => {
      emit('update:selectValue', value.value)
    })

    watch(selectValue, () => (value.value = selectValue.value))

    onMounted(loadData)

    return {
      dataList,
      value
    }
  }
})
</script>

<style scoped></style>
