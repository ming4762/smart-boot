<!--
  查询表信息
-->
<template>
  <div></div>
</template>

<script lang="ts" setup>
import { propTypes } from '/@/utils/propTypes'

const props = defineProps({
  // 实体类类名
  entityClassName: propTypes.string.isRequired,
  valueFieldName: propTypes.string.isRequired,
  labelFieldName: propTypes.string.isRequired,
})
console.log(props)
</script>

<style scoped></style>
