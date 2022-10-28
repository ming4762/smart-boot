import { defineComponent, ref, computed, watch, toRefs, watchEffect } from 'vue'
import type { PropType, Ref, CSSProperties } from 'vue'
import { useDraggable } from '@vueuse/core'

import { FullscreenOutlined, FullscreenExitOutlined, CloseOutlined } from '@ant-design/icons-vue'

import './ModalDrag.less'

const modalDefaultClass = 'modal-diag'

/**
 * modal拖拽支持
 * @param titleRef
 * @param isFullscreen
 * @param draggable
 */
const useModalMove = (
  titleRef: Ref<HTMLElement | null>,
  isFullscreen: Ref<boolean>,
  draggable: Ref<boolean>
) => {
  const { x, y, isDragging } = useDraggable(titleRef)
  // 是否开启拖拽
  const startedDrag = ref(false)
  const startX = ref<number>(0)
  const startY = ref<number>(0)
  const transformX = ref(0)
  const transformY = ref(0)
  const preTransformX = ref(0)
  const preTransformY = ref(0)
  const dragRect = ref({ left: 0, right: 0, top: 0, bottom: 0 })
  watch([x, y], () => {
    if (!startedDrag.value) {
      startX.value = x.value
      startY.value = y.value
      const bodyRect = document.body.getBoundingClientRect()
      // @ts-ignore
      const titleRect = titleRef.value.getBoundingClientRect()
      dragRect.value.right = bodyRect.width - titleRect.width
      dragRect.value.bottom = bodyRect.height - titleRect.height
      preTransformX.value = transformX.value
      preTransformY.value = transformY.value
    }
    startedDrag.value = true
  })
  watch(isDragging, () => {
    if (!isDragging) {
      startedDrag.value = false
    }
  })
  watchEffect(() => {
    if (startedDrag.value) {
      transformX.value =
        preTransformX.value +
        Math.min(Math.max(dragRect.value.left, x.value), dragRect.value.right) -
        startX.value
      transformY.value =
        preTransformY.value +
        Math.min(Math.max(dragRect.value.top, y.value), dragRect.value.bottom) -
        startY.value
    }
  })
  const transformStyle = computed<CSSProperties>(() => {
    if (isFullscreen.value || !draggable.value) {
      return {}
    }
    return {
      transform: `translate(${transformX.value}px, ${transformY.value}px)`
    }
  })
  return {
    transformStyle
  }
}

/**
 * 可拖拽放大Modal
 * @author shizhongming
 */
export default defineComponent({
  name: 'ModalDrag',
  props: {
    // 标题，也可通过slot:title设置，slot优先级高
    title: {
      type: String as PropType<string>
    },
    /**
     * 是否默认全屏
     */
    defaultFullscreen: {
      type: Boolean as PropType<boolean>,
      default: false
    },
    /**
     * 显示状态支持v-model
     */
    visible: {
      type: Boolean as PropType<boolean>,
      default: false
    },
    // 是否可拖拽
    draggable: {
      type: Boolean as PropType<boolean>,
      default: false
    }
  },
  emits: ['update:visible', 'cancel'],
  setup(props, { attrs, slots, emit }) {
    const { visible, draggable } = toRefs(props)
    const getContainer = () => document.getElementById('modal_container')
    const isFullscreen = ref(props.defaultFullscreen)
    const titleRef = ref<HTMLElement | null>(null)
    // 拖动是否初始化
    const isDragInit = ref(false)
    /**
     * 开启关闭全屏
     * @param status 状态
     */
    const handleFullscreen = (status: boolean) => {
      isFullscreen.value = status
    }
    /**
     * 点击取消事件
     */
    const handleCancel = () => {
      emit('update:visible', false)
      emit('cancel')
    }
    watch(visible, (value) => {
      if (value === true) {
        isFullscreen.value = props.defaultFullscreen
      }
    })

    /**
     * modal样式计算属性
     */
    const computedModalClass = computed(() => {
      return isFullscreen.value ? [modalDefaultClass, 'modal-fullscreen'] : [modalDefaultClass]
    })
    /**
     * 渲染title函数
     */
    const renderTitle = () => {
      return [
        <div
          ref={titleRef}
          style={props.draggable && !isFullscreen.value ? { cursor: 'move' } : {}}>
          {slots.title ? slots.title() : props.title}
        </div>,
        <div class="top-button-container">
          {isFullscreen.value ? (
            <a-button class="button" onClick={() => handleFullscreen(false)}>
              <FullscreenExitOutlined />
            </a-button>
          ) : (
            <a-button className="button" onClick={() => handleFullscreen(true)}>
              <FullscreenOutlined />
            </a-button>
          )}
          <a-button class="button" onClick={handleCancel}>
            <CloseOutlined />
          </a-button>
        </div>
      ]
    }
    const modalSlots: any = {
      // 不渲染默认的关闭按钮，关闭按钮单独渲染
      closeIcon: () => '',
      title: renderTitle,
      default: slots.default
    }
    watch(draggable, (value) => {
      if (value && !isDragInit.value) {
        const { transformStyle } = useModalMove(titleRef, isFullscreen, draggable)
        // 添加拖拽支持
        modalSlots.modalRender = ({ originVNode }: any) => (
          <div style={transformStyle.value}>{originVNode}</div>
        )
      }
    })
    return () => {
      return (
        <a-modal
          {...attrs}
          class={computedModalClass.value}
          visible={props.visible}
          onCancel={handleCancel}
          getContainer={getContainer}>
          {modalSlots}
        </a-modal>
      )
    }
  }
})
