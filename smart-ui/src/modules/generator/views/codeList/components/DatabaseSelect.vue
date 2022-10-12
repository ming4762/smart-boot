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

import ApiService from '@/common/utils/ApiService'

import SizeConfigHoops from '@/components/config/SizeConfigHooks'
/**
 * 数据库连接下拉列
 */
export default defineComponent({
  name: 'DatabaseSelect',
  props: {
    parameter: {
      type: Object as PropType<any>,
      default: () => {
        return {}
      }
    }
  },
  setup(props) {
    const data = ref<Array<any>>([])
    const sizeConfigHoops = SizeConfigHoops()
    const loadData = async () => {
      const result = await ApiService.postAjax(
        '/db/connection/listByAuth',
        Object.assign(
          {
            sortName: 'seq'
          },
          props.parameter
        )
      )
      data.value = result.map((item: any) => {
        return {
          key: item.id + '',
          value: item.connectionName
        }
      })
    }
    onMounted(loadData)
    return {
      data,
      ...sizeConfigHoops
    }
  }
})
</script>

<style scoped></style>
