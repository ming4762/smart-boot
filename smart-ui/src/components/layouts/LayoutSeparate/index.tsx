import { defineComponent, computed, toRefs, ref, watch, watchEffect } from 'vue'
import type { PropType, Ref, StyleValue } from 'vue'
import { useDraggable } from '@vueuse/core'

import { isFinite, endsWith, replace, parseInt, toNumber, throttle } from 'lodash'

import './index.less'

enum Layout {
  'LEFT_RIGHT_LAYOUT' = 'leftRight',
  'TOP_BOTTOM_LAYOUT' = 'topBottom'
}

/**
 * 支持分隔拖拽的layout
 * @author shizhongming
 */
export default defineComponent({
  name: 'LayoutSeparate',
  props: {
    //布局，默认左右布局
    layout: {
      type: String as PropType<string>,
      default: Layout.LEFT_RIGHT_LAYOUT,
      validator(value: string) {
        // @ts-ignore
        return [Layout.LEFT_RIGHT_LAYOUT, Layout.TOP_BOTTOM_LAYOUT].includes(value)
      }
    },
    // 是否可拖拽
    draggable: {
      type: Boolean as PropType<boolean>,
      default: false
    },
    // 尺寸，如果是number类型，按照百分比分隔
    firstSize: {
      type: [Number, String] as PropType<number | string>,
      default: 50
    }
  },
  setup(props, { slots }) {
    const { layout, draggable, firstSize } = toRefs(props)
    // 拖拽是否初始化
    const dragInit = ref(false)
    const dividerRef = ref<HTMLElement | null>(null)
    let dragVue: any = {}
    // 启动拖拽
    watchEffect(() => {
      if (draggable.value && !dragInit.value) {
        dragVue = useDrag(dividerRef)
      }
    })
    /**
     * 外层DIV class 计算属性
     */
    const computedClass = computed(() => {
      // 外层DIV容器class
      const containerClassList = ['smart-layout-separate']
      // 分割线样式
      const dividerClassList: Array<string> = []
      if (layout.value === Layout.LEFT_RIGHT_LAYOUT) {
        containerClassList.push('row-flex-direction')
        dividerClassList.push('row-divider')
      } else {
        containerClassList.push('column-flex-direction')
        dividerClassList.push('column-divider')
      }
      return {
        containerClassList,
        dividerClassList
      }
    })
    /**
     * 分割线样式计算属性
     */
    const computedDividerStyle = computed<StyleValue>(() => {
      // 分隔线样式
      if (draggable.value === false) {
        return {}
      }
      let cursor = 'n-resize'
      if (draggable.value) {
        if (layout.value === Layout.LEFT_RIGHT_LAYOUT) {
          cursor = 'e-resize'
        }
      }
      return {
        cursor
      }
    })
    const layoutStyle = computed(() => {
      const firstStyle: StyleValue = {}
      const secondStyle: StyleValue = {}
      const firstSizeValue = firstSize.value
      let firstValue = ''
      let secondValue = ''
      if (isFinite(firstSizeValue) || isFinite(toNumber(firstValue))) {
        // 按照百分比处理
        firstValue = firstSizeValue + '%'
        // @ts-ignore
        secondValue = 100 - firstSizeValue + '%'
      } else {
        if (endsWith(firstSizeValue, '%')) {
          const firstSize = parseInt(replace(firstSizeValue, '%'))
          firstValue = firstSize + '%'
          secondValue = 100 - firstSize + '%'
        } else {
          // @ts-ignore
          firstValue = firstSizeValue
        }
      }
      if (layout.value === Layout.LEFT_RIGHT_LAYOUT) {
        firstStyle.width = firstValue
        if (secondValue !== '') {
          secondStyle.width = secondValue
        }
      } else {
        firstStyle.height = firstValue
        if (secondValue !== '') {
          secondStyle.height = secondValue
        }
      }
      if (secondValue === '') {
        secondStyle.flex = 1
      }
      return {
        firstStyle,
        secondStyle
      }
    })

    return () => {
      console.log(layoutStyle.value.secondStyle)
      const { first, second } = slots
      return (
        <div class={computedClass.value.containerClassList}>
          {/* 第一块区域，左或者上 */}
          <div style={layoutStyle.value.firstStyle}>{first ? first() : ''}</div>
          <div
            ref={dividerRef}
            style={computedDividerStyle.value}
            class={computedClass.value.dividerClassList}>
            <a-divider
              type={layout.value === Layout.LEFT_RIGHT_LAYOUT ? 'vertical' : 'horizontal'}
            />
          </div>
          {/* 第二块区域，右或者下 */}
          <div style={layoutStyle.value.secondStyle}>{second ? second() : ''}</div>
        </div>
      )
    }
  }
})

/**
 * 支持拖拽
 */
const useDrag = (targetRef: Ref<HTMLElement | null>) => {
  const { x, y, isDragging } = useDraggable(targetRef)
  watch([x, y], throttle(() => {
    console.log(isDragging)
  }, 500))
  return {}
}
