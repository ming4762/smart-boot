import { Ref, getCurrentInstance, ref, onMounted, onUnmounted } from 'vue'

import matchQueries from 'container-query-toolkit/lib/matchQueries'
import ContainerQueryCore from './ContainerQueryCore'
import { ComponentInternalInstance } from '@vue/runtime-core'


const vueContainerQueryVueSupport = (initialSize: Ref, query: Ref, handleChange: Function) => {
  const params = initialSize.value ? matchQueries(query.value)(initialSize.value) : {}
  handleChange(params)

  const cqCore = ref<ContainerQueryCore | null>(null)
  const { slots, vnode } = getCurrentInstance() as ComponentInternalInstance
  const startObserving = (query: any) => {
    cqCore.value = new ContainerQueryCore(query, (params: any) => {
      handleChange(params)
    })
    const element = vnode.el || slots.default!()[0].el
    cqCore.value?.observe(element)
  }

  const disposeObserver = () => {
    if (cqCore.value) {
      cqCore.value?.disconnect()
    }
    cqCore.value = null
  }

  onMounted(() => startObserving(query.value))
  onUnmounted(disposeObserver)

  return {
    cqCore,
    disposeObserver,
    startObserving
  }
}

export {
  vueContainerQueryVueSupport
}
