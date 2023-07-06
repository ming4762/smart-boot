<template>
  <div :class="[prefixCls, { fullscreen }]">
    <a-upload
      name="file"
      multiple
      @change="handleChange"
      :action="imageAction"
      :headers="computedHeaders"
      :showUploadList="false"
      accept=".jpg,.jpeg,.gif,.png,.webp">
      <a-button type="primary" v-bind="{ ...getButtonProps }">
        {{ t('component.upload.imgUpload') }}
      </a-button>
    </a-upload>
  </div>
</template>
<script lang="ts">
import { defineComponent, computed, PropType } from 'vue'

import { useDesign } from '/@/hooks/web/useDesign'
import { useI18n } from '/@/hooks/web/useI18n'
import { getToken } from '/@/utils/auth'

export default defineComponent({
  name: 'TinymceImageUpload',
  props: {
    fullscreen: {
      type: Boolean,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
    imageAction: {
      type: [String, Function] as PropType<string | Function>,
    },
  },
  emits: ['uploading', 'done', 'error'],
  setup(props, { emit }) {
    let uploading = false

    const { t } = useI18n()
    const { prefixCls } = useDesign('tinymce-img-upload')

    const getButtonProps = computed(() => {
      const { disabled } = props
      return {
        disabled,
      }
    })

    const computedHeaders = computed(() => {
      return {
        Authorization: getToken(),
      }
    })

    function handleChange(info: Record<string, any>) {
      console.log(info)
      const file = info.file
      const status = file?.status
      const data = file?.response?.data
      const name = file?.name

      if (status === 'uploading') {
        if (!uploading) {
          emit('uploading', name)
          uploading = true
        }
      } else if (status === 'done') {
        emit('done', name, data)
        uploading = false
      } else if (status === 'error') {
        emit('error')
        uploading = false
      }
    }

    return {
      prefixCls,
      handleChange,
      t,
      getButtonProps,
      computedHeaders,
    }
  },
})
</script>
<style lang="less" scoped>
@prefix-cls: ~'@{namespace}-tinymce-img-upload';

.@{prefix-cls} {
  position: absolute;
  z-index: 20;
  top: 4px;
  right: 10px;

  &.fullscreen {
    position: fixed;
    z-index: 10000;
  }
}
</style>
