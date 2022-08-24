<template>
  <table style="width: 100%">
    <template v-for="(value, key) in data.details" :key="data.title + key">
      <tr>
        <td>{{ key }}</td>
        <td>{{ value }}</td>
      </tr>
      <tr>
        <td class="td-divider">
          <a-divider />
        </td>
        <td class="td-divider">
          <a-divider />
        </td>
      </tr>
    </template>
  </table>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'CommonTable',
  props: {
    data: {
      type: Object,
      required: true
    }
  }
})

</script>

<style lang="less" scoped>

  .td-divider {
    .ant-divider-horizontal {
      margin: 6px 0 !important;
    }
  }
</style>
