<template>
  <a-card
    v-bind="$attrs"
    :head-style="{
      'box-shadow': '0 0.125em 0.25em rgb(10 10 10 / 10%)',
      'font-weight': 'bold'
    }"
    style="box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%)">
    <slot />
  </a-card>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'ClientCard'
})
</script>

<style scoped></style>
