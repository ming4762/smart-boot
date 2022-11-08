<template>
  <div class="full-height">
<!--    <div>这是主页</div>-->
    <LayoutSeparate :show-line="false" first-size="40%" draggable style="background: white" layout="leftRight" class="full-height">
      <template #first>
        <div class="full-height" style="width: 200px; background: white">123</div>
      </template>
      <template #second>
        <div class="full-height" style="width: 100%; background: white">second</div>
      </template>
    </LayoutSeparate>
  </div>
</template>

<script lang="ts" setup name="MainComponent">
import LayoutSeparate from '@/components/layouts/LayoutSeparate'
import { ref, watch } from 'vue'

</script>

<style scoped></style>
