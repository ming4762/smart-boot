<template>
  <div>
    <a-descriptions bordered title="User Account Info">
      <a-descriptions-item label="状态">
        {{ data.userAccount.accountStatus }}
      </a-descriptions-item>
    </a-descriptions>
    <div>{{ data }}</div>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent, PropType } from 'vue'
export default defineComponent({
  name: 'UserAccount',
  props: {
    data: {
      type: Object as PropType<any>,
      default: () => {}
    }
  },
  setup () {

  }
})
</script>

<style scoped>

</style>
