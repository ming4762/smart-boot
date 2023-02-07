<template>
  <BasicModal
    defaultFullscreen
    @register="registerModal"
    :title="$t('system.views.exception.title.stackTrace')">
    <div style="overflow: auto; height: 100%">
      <div style="white-space: pre">
        {{ exceptionData.stackTrace }}
      </div>
    </div>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useModalInner, BasicModal } from '/@/components/Modal'

import { getById } from '../SysExceptionListView.api'

const exceptionData = ref<any>({})
const [registerModal, { changeLoading }] = useModalInner(async (id) => {
  exceptionData.value = {}
  try {
    changeLoading(true)
    exceptionData.value = await getById(id)
  } finally {
    changeLoading(false)
  }
})
</script>

<style scoped></style>
