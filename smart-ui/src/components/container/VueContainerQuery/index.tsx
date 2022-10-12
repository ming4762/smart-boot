import { defineComponent, getCurrentInstance, watch, toRefs } from 'vue'
import type { ComponentInternalInstance, PropType } from 'vue'

import { vueContainerQueryVueSupport } from './Base'

import { isQueriesEqual } from './utils'

const kErrMsg = '<vue-Container-query> can only render' + ' one, and exactly one child component'

/**
 * vue-container-query vue3重写
 */
export default defineComponent({
  name: 'VueContainerQuery',
  props: {
    initialSize: {
      type: Object as PropType<object>
    },
    query: {
      required: true,
      type: Object as PropType<object>
    }
  },
  setup(props, { slots }) {
    const { query, initialSize } = toRefs(props)
    const { emit } = getCurrentInstance() as ComponentInternalInstance
    const handleChange = (params: any) => {
      emit('containerChange', params)
    }
    const containerQueryVueSupport = vueContainerQueryVueSupport(initialSize, query, handleChange)
    watch(query, (newVal, oldVal) => {
      if (containerQueryVueSupport.cqCore && isQueriesEqual(oldVal, newVal)) {
        containerQueryVueSupport.disposeObserver()
        containerQueryVueSupport.startObserving(newVal)
      }
      }, {
      deep: true,
      immediate: true
    })
    return () => {
      const slot = slots.default
      if (slot) {
        const vNodes = slot()
        if (vNodes.length === 1) {
          return vNodes[0]
        }
      }
      throw new Error(kErrMsg)
    }
  }
})
