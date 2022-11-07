<template>
  <a-modal
    :visible="computedModalVisible"
    :footer="null"
    @cancel="handleHideModal">
    <div class="exception-modal">
      <div class="exception-modal-body">
        <CloseCircleOutlined class="icon" />
        <span class="title">{{ $t('system.common.exceptionTitle') }}</span>
        <div class="content">
          <span style="white-space: pre">NO: {{ computedNoList.join(' ') }}</span>
          <a-textarea
            v-model:value="model.feedbackMessage"
            style="margin-top: 10px"
            :placeholder="$t('common.formValidate.enter')"
            :rows="5" />
        </div>
      </div>
      <div class="exception-modal-button">
        <a-button @click="handleHideModal">{{ $t('common.button.cancel') }}</a-button>
        <a-button type="primary" :loading="submitLoading" style="margin-left: 5px" @click="handleSubmit">{{ $t('common.button.submit') }}</a-button>
      </div>
    </div>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent, computed, Ref, ref} from 'vue'
import { useStore } from 'vuex'
import { useI18n } from 'vue-i18n'

import { CloseCircleOutlined } from '@ant-design/icons-vue'

import ApiService from '@/common/utils/ApiService'
import { successMessage } from '@/components/notice/SystemNotice'

export default defineComponent({
  name: 'ExceptionModal',
  components: {
    CloseCircleOutlined
  },
  setup () {
    const store = useStore()
    const { t } = useI18n()

    const computedModalVisible = computed(() => store.getters['system/exception'].modalVisible)
    const computedNoList: Ref<Array<number>> = computed(() => store.getters['system/exception'].noList)
    const model = ref<any>({})
    const submitLoading = ref(false)
    /**
     * 隐藏弹窗
     */
    const handleHideModal = () => {
      store.commit('system/handleHideExceptionModal')
    }
    /**
     * 提交操作
     */
    const handleSubmit = async () => {
      submitLoading.value = true
      try {
        await ApiService.postAjax('sys/exception/feedback', {
          idList: computedNoList.value,
          ...model.value
        })
        handleHideModal()
        successMessage(t('common.message.submitSuccess'))
      } catch (e) {
        console.log(e)
      } finally {
        submitLoading.value = false
      }
    }
    return {
      computedModalVisible,
      handleHideModal,
      computedNoList,
      model,
      handleSubmit,
      submitLoading
    }
  }
})
</script>

<style lang="less" scoped>
.exception-modal {
  &:after {
    content:"";
    display: block;
    clear: both;
  }
  .exception-modal-body {
    .icon {
      color: red;
      font-size: 22px;
      margin-right: 16px;
    }
    .title {
      font-weight: 500;
      font-size: 16px;
      line-height: 1.4;
      display: inline;
      overflow: hidden;
      color: #000000d9;
    }
    .content {
      margin-top: 8px;
      font-size: 14px;
    }
  }
  .exception-modal-button {
    float: right;
    margin-top: 24px;
  }
}
</style>
