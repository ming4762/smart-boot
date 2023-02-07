<template>
  <ApiSelect v-bind="$attrs" value-field="dictItemCode" label-field="dictItemName" :api="api" />
</template>

<script lang="ts" setup>
import ApiSelect from '../../components/ApiSelect.vue'
import { propTypes } from '/@/utils/propTypes'
import { defHttp } from '/@/utils/http/axios'

const props = defineProps({
  dictCode: propTypes.string.isRequired,
})

const api = () => {
  return defHttp.post({
    url: 'sys/dict/listItemByCode',
    data: props.dictCode,
  })
}
</script>
