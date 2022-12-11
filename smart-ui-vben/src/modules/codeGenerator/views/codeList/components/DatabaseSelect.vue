<template>
  <a-select :size="formSizeConfig" v-bind="$attrs">
    <a-select-option v-for="item in data" :key="item.key" :value="item.key">
      {{ item.value }}
    </a-select-option>
  </a-select>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import type { PropType } from 'vue'

import { Select, SelectOption } from 'ant-design-vue'

import ApiService from '/@/common/utils/ApiService'

import { useSizeSetting } from '/@/hooks/setting/UseSizeSetting'
/**
 * 数据库连接下拉列
 */
export default defineComponent({
  name: 'DatabaseSelect',
  components: {
    ASelect: Select,
    ASelectOption: SelectOption,
  },
  props: {
    parameter: {
      type: Object as PropType<any>,
      default: () => {
        return {}
      },
    },
  },
  setup(props) {
    const data = ref<Array<any>>([])
    const loadData = async () => {
      const result = await ApiService.postAjax(
        '/db/connection/listByAuth',
        Object.assign(
          {
            sortName: 'seq',
          },
          props.parameter,
        ),
      )
      data.value = result.map((item: any) => {
        return {
          key: item.id + '',
          value: item.connectionName,
        }
      })
    }
    onMounted(loadData)
    return {
      data,
      ...useSizeSetting(),
    }
  },
})
</script>

<style scoped></style>
