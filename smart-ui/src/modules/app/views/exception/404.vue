<template>
  <a-result status="404" title="404" :sub-title="$t('app.title.404')">
    <template #extra>
      <a-button type="primary" @click="toHome">
        {{ $t('app.title.backHome') }}
      </a-button>
    </template>
  </a-result>
</template>

<script lang="ts">
/**
 * 404错误页面
 */
export default {
  name: 'Exception404Conponent',
  methods: {
    toHome() {
      this.$router.push({
        path: '/'
      })
    }
  }
}
</script>

<style scoped>

</style>
