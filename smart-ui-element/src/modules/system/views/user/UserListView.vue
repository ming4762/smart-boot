<template>
  <div>
    用户页面
    <el-input v-model="model" />
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const model = ref('')
</script>

<style scoped></style>
