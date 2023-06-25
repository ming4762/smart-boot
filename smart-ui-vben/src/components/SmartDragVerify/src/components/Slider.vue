<template>
  <div class="slider" :style="computedSliderStyle">
    <div class="content" :style="computedContentStyle">
      <div class="bg-img-div">
        <img ref="bgImageRef" :src="captchaDataRef.backgroundImage" alt />
      </div>
      <div :style="computedSliderTemplateStyle" class="slider-img-div">
        <img ref="sliderImageRef" :src="captchaDataRef.templateImage" alt />
      </div>
    </div>
    <div class="slider-move">
      <BasicDragVerify
        :width="props.width"
        @end="handleEnd"
        @start="handleStart"
        is-slot
        @move="handleMove"
        :value="verifySuccessRef" />
    </div>
  </div>
</template>

<script lang="ts" setup>
import type { MoveData, VerifyType } from '/@/components/SmartDragVerify'

import { computed, nextTick, ref, unref, onMounted } from 'vue'

import BasicDragVerify from '../BasicDragVerify.vue'
import { loadCaptchaApi, validateCaptchaApi } from '../SmartDragVerify.api'
import { useSmartDragVerify } from '../hooks/useSmartDragVerify'
import { errorMessage } from '/@/common/utils/SystemNotice'
import { useI18n } from '/@/hooks/web/useI18n'
import { propTypes } from '/@/utils/propTypes'

const props = defineProps({
  type: {
    type: String as PropType<VerifyType>,
    required: true,
    default: 'SLIDER',
  },
  // 图片宽度
  width: propTypes.number.def(320),
})

const emit = defineEmits(['success'])

const { t } = useI18n()

const { initConfig, start, move, end, createCaptchaParameter } = useSmartDragVerify()
const captchaDataRef = ref<any>({})
const moveXRef = ref(0)
// 是否验证成功
const verifySuccessRef = ref(false)
const bgImageRef = ref()
const sliderImageRef = ref()

const handleStart = (event: MouseEvent | TouchEvent) => {
  start(event)
}
/**
 * 滑块样式计算属性
 */
const computedSliderTemplateStyle = computed(() => {
  const movePx = moveXRef.value
  return {
    transform: `translate(${movePx}px, 0px)`,
  }
})

/**
 * 刷新验证码
 */
const refresh = async () => {
  captchaDataRef.value = await loadCaptchaApi(props.type)
  const bgImage = unref(bgImageRef)
  const sliderImage = unref(sliderImageRef)
  moveXRef.value = 0
  await nextTick(() => {
    initConfig(
      captchaDataRef.value.key,
      bgImage.width,
      bgImage.height,
      sliderImage.width,
      sliderImage.height,
    )
  })
}
defineExpose({ refresh })
onMounted(refresh)

const handleEnd = async (e: MouseEvent | TouchEvent) => {
  end(e)
  const parameter = createCaptchaParameter()
  const captchaToken = await validateCaptchaApi(parameter)
  console.log(captchaToken)
  if (captchaToken == null) {
    errorMessage(t('common.message.captchaValidateFail'))
    verifySuccessRef.value = false
    refresh()
  } else {
    verifySuccessRef.value = true
    emit('success', captchaToken)
  }
}

const handleMove = (data: MoveData) => {
  moveXRef.value = data.moveX
  move(data.event)
}

const computedContentHeight = computed(() => {
  return props.width / 1.64
})

const computedSliderStyle = computed(() => {
  return {
    width: `${props.width}px`,
    height: `${unref(computedContentHeight) + 40}px`,
  }
})

const computedContentStyle = computed(() => {
  return {
    height: `${unref(computedContentHeight)}px`,
  }
})
</script>

<style lang="less" scoped>
.slider {
  background-color: #fff;
  height: 260px;
  z-index: 999;
  box-sizing: border-box;
  user-select: none;

  .content {
    width: 100%;
    position: relative;
  }

  .slider-move {
    width: 100%;
    position: relative;
  }

  .bottom {
    height: 19px;
    width: 100%;
  }
}

.bg-img-div {
  width: 100%;
  height: 100%;
  position: absolute;
  transform: translate(0px, 0px);
}

.slider-img-div {
  height: 100%;
  position: absolute;
  transform: translate(0px, 0px);
}

.bg-img-div img {
  width: 100%;
}

.slider-img-div img {
  height: 100%;
}

.refresh-btn,
.close-btn {
  display: inline-block;
}

.slider-move {
  .slider-move-track {
    line-height: 38px;
    font-size: 14px;
    text-align: center;
    white-space: nowrap;
    color: #88949d;
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
  }

  .slider-move-btn {
    transform: translate(0px, 0px);
    background-position: -5px 11.79625%;
    position: absolute;
    top: -12px;
    left: 0;
    width: 66px;
    height: 66px;
  }
}

.slider-move-btn:hover,
.close-btn:hover,
.refresh-btn:hover {
  cursor: pointer;
}
</style>
