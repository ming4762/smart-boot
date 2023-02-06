<template>
  <BasicModal @register="registerModal" :title="$t('common.title.details')" width="1000px">
    <template #footer>
      <a-button type="primary" @click="closeModal">
        {{ $t('common.title.close') }}
      </a-button>
    </template>
    <a-descriptions bordered>
      <a-descriptions-item
        :labelStyle="firstLabelStyle"
        :label="$t('system.views.log.title.operationType')">
        {{ detailsData.operationType }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.log.title.requestPath')">
        {{ detailsData.requestPath }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.log.title.statusCode')">
        <a-tag
          :color="
            detailsData.statusCode >= 200 && detailsData.statusCode < 300 ? '#2db7f5' : '#f50'
          ">
          {{ detailsData.statusCode }}
        </a-tag>
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.log.title.operation')" :span="2">
        {{ detailsData.operation }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.log.title.logSource')">
        {{ detailsData.logSource }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.log.title.createTime')" :span="2">
        {{ detailsData.createTime }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.log.title.ip')">
        {{ detailsData.ip }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.log.title.method')" :span="2">
        {{ detailsData.method }}
      </a-descriptions-item>
      <a-descriptions-item :label="$t('system.views.log.title.useTime')">
        {{ detailsData.useTime }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.log.title.params')" :span="3">
        {{ detailsData.params }}
      </a-descriptions-item>

      <a-descriptions-item :label="$t('system.views.log.title.result')" :span="3">
        {{ detailsData.result }}
      </a-descriptions-item>
    </a-descriptions>
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useModalInner, BasicModal } from '/@/components/Modal'

import { getByIdApi } from '../SystemLogComponent.api'

const firstLabelStyle = {
  width: '120px',
}

const detailsData = ref<any>({})
const [registerModal, { changeLoading, closeModal }] = useModalInner(async (id) => {
  detailsData.value = {}
  try {
    changeLoading(true)
    detailsData.value = await getByIdApi(id)
  } finally {
    changeLoading(false)
  }
})
</script>

<style scoped></style>
