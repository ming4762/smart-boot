<template>
  <a-tabs :animated="true">
    <a-tab-pane key="1" class="full-height" :tab="$t('generator.views.code.title.dbMessage')">
      <TableFiledList v-bind="$attrs" class="full-height" />
    </a-tab-pane>
  </a-tabs>
</template>

<script lang="ts" setup>
import TableFiledList from './TableFiledList.vue'
</script>

<style scoped></style>
