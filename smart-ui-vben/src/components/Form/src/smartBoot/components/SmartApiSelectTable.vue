<!--
  查询表信息
-->
<template>
  <ApiSelect v-bind="$attrs" value-field="value" label-field="label" :api="api" />
</template>

<script lang="ts" setup>
import { propTypes } from '/@/utils/propTypes'
import { defHttp } from '/@/utils/http/axios'
import ApiSelect from '/@/components/Form/src/components/ApiSelect.vue'

const props = defineProps({
  // 实体类类名
  modelClassName: propTypes.string.isRequired,
  valueFieldName: propTypes.string.isRequired,
  labelFieldName: propTypes.string.isRequired,
  parameter: propTypes.object.def({}),
})

const api = () => {
  return defHttp.post({
    url: 'api/component/smart-form/listTableSelect',
    data: {
      ...props,
    },
  })
}
</script>

<style scoped></style>
