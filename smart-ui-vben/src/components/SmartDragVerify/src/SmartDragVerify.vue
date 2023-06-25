<template>
  <Slider ref="sliderRef" v-bind="$attrs" v-if="props.type === 'SLIDER'" :type="props.type" />
</template>

<script lang="ts" setup>
import type { VerifyType } from './typing'

import { ref } from 'vue'

import Slider from './components/Slider.vue'

const props = defineProps({
  type: {
    type: String as PropType<VerifyType>,
    required: true,
  },
})

const sliderRef = ref()

const refresh = () => {
  sliderRef.value.refresh()
}
defineExpose({ refresh })
</script>

<style scoped></style>
