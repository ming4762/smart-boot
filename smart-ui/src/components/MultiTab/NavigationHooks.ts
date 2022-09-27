import { computed, ref, Ref, watch } from 'vue'
import XEUtils from 'xe-utils'

import { NodeItem } from '@/common/utils/LinkList'

/**
 * 判断是否是激活的菜单
 * @param element
 */
const isActive = (element: Element) => {
  return element.classList.contains('active')
}

/**
 * 获取菜单宽度
 * @param firstNode
 */
const getMenusWidth = (firstNode: NodeItem<Element>) => {
  if (firstNode.prev !== null) {
    throw new Error('入参检查失败：不是第一个节点')
  }
  let currentNode = firstNode
  let sumWidth = currentNode.value.clientWidth + 1
  while (currentNode.next !== null) {
    currentNode = currentNode.next
    sumWidth += (currentNode.value.clientWidth + 1)
  }
  return sumWidth
}

/**
 * 获取激活的节点
 * @param firstNode
 */
const getActiveNode = (firstNode: NodeItem<Element>) => {
  if (isActive(firstNode.value)) {
    return firstNode
  }
  let activeNode
  let nodeItem = firstNode
  while (nodeItem.hasNext()) {
    nodeItem = nodeItem.getNext()
    if (isActive(nodeItem.value)) {
      activeNode = nodeItem
      break
    }
  }
  if (!activeNode) {
    activeNode = firstNode
  }
  return activeNode
}

/**
 * Tab 移动hook
 */
export const useTabsMove = (firstElementRef: Ref<Element | null>, tabContainerRef: Ref, activeValue: Ref, dataRef: Ref) => {

  // 导航栏外层div ref
  const outContainerRef = ref()

  /**
   * 获取 tab container 外层宽度
   */
  const getTabContainerOuterWidth = () => {
    const children = outContainerRef.value.children
    const elementList: Array<Element> = []
    for (let i=0; i<children.length; i++) {
      elementList.push(children[i])
    }
    // 排除中间区域的按钮宽度
    const buttonsWidth = XEUtils.sum(elementList
      .filter((item: any) => item !== tabContainerRef.value.parentElement)
      .map((item: any) => item.clientWidth))

    return outContainerRef.value.clientWidth - buttonsWidth
  }

  /**
   * 获取右侧最后一个元素
   */
  const getLastRightElement = () => {
    const menuElementList: HTMLCollection = tabContainerRef.value.children
    // 获取当前的第一个元素
    let firstElement = firstElementRef.value
    if (firstElement == null) {
      firstElement = menuElementList[0]
    }
    // tabs 容器宽度
    const tabContainerOuterWidth = getTabContainerOuterWidth()
    let sumWidth = 0
    let begin = false
    let result: Element| null = null;
    for (let i=0; i<menuElementList.length; i++) {
      const item = menuElementList[i]
      if (item === firstElement) {
        begin = true
      }
      if (begin) {
        sumWidth += item.clientWidth
        if (sumWidth > tabContainerOuterWidth) {
          result = menuElementList[i -1]
          break
        }
      }
    }
    if (result == null) {
      // 如果剩余元素的宽度比容器宽度小，则不用更变first元素
      if (sumWidth < tabContainerOuterWidth) {
        return firstElement
      }
      result = menuElementList[menuElementList.length - 1]
    }
    return result;
  }
  /**
   * 点击右侧移动按钮
   */
  const handleMoveRight = () => {
    firstElementRef.value = getLastRightElement()
  }

  /**
   * 点击左侧移动
   */
  const handleMoveLeft = () => {
    if (firstElementRef.value == null) {
      return false
    }
    const menuElementList: HTMLCollection = tabContainerRef.value.children
    const tabContainerOuterWidth = getTabContainerOuterWidth()
    let numWidth = 0
    let begin = false
    let result = null
    for (let i=menuElementList.length -1; i>=0; i--) {
      const item = menuElementList[i]
      if (firstElementRef.value === item) {
        begin = true
      }
      if (begin) {
        numWidth += item.clientWidth
        if (numWidth > tabContainerOuterWidth) {
          result = menuElementList[i + 1]
          break
        }
      }
    }
    if (numWidth <= tabContainerOuterWidth) {
      result = menuElementList[0]
    }
    firstElementRef.value = result
  }


  /**
   * 获取菜单节点
   */
  const getMenuElementNode = (): NodeItem<Element> => {
    const htmlCollection: HTMLCollection = tabContainerRef.value.children
    const firstNode: NodeItem<Element> = new NodeItem(htmlCollection[0], null, null)
    let currentNode = firstNode
    for (let i=1; i<htmlCollection.length; i++) {
      const nodeItem = new NodeItem(htmlCollection[i], currentNode, null)
      currentNode.next = nodeItem
      currentNode = nodeItem
    }
    return firstNode
  }

  const locationCurrent = () => {
    const firstMenuNode: NodeItem<Element> = getMenuElementNode()
    const activeElementNode = getActiveNode(firstMenuNode)
    // 获取激活的菜单和当前第一个菜单
    let firstElement = firstElementRef.value
    if (firstElement == null) {
      firstElement = firstMenuNode.value
    }
    // 判断菜单宽度是否超过容器宽度，没有超过不做任何操作
    // tabs 容器宽度
    const tabContainerOuterWidth = getTabContainerOuterWidth()
    const menusWidth = getMenusWidth(firstMenuNode)
    if (menusWidth < tabContainerOuterWidth) {
      // 如果第一个元素激活，则前一个元素为首元素
      if (activeElementNode.value === firstElement && activeElementNode.prev !== null) {
        firstElementRef.value = activeElementNode.prev.value
        return
      }
      return false
    }
    // 1、如果当前激活的元素 +下一个元素宽度小于 容器宽度 则第一个元素为首元素
    let currentNode = activeElementNode
    let sumWidth = currentNode.value.clientWidth + 1
    while (currentNode.prev !== null) {
      currentNode = currentNode.prev
      sumWidth += (currentNode.value.clientWidth + 1)
    }
    if (activeElementNode.next !== null) {
      sumWidth += (activeElementNode.next.value.clientWidth + 1)
    }
    if (sumWidth < tabContainerOuterWidth) {
      firstElementRef.value = firstMenuNode.value
      return
    }

    // 如果第一个元素激活，则前一个元素为首元素
    if (activeElementNode.value === firstElement && activeElementNode.prev !== null) {
      firstElementRef.value = activeElementNode.prev.value
      return
    }

    // 获取首元素node
    let firstElementNode = null
    currentNode = firstMenuNode
    if (firstElement === currentNode.value) {
      firstElementNode = currentNode
    }
    while (currentNode.next != null) {
      currentNode = currentNode.next
      if (firstElement === currentNode.value) {
        firstElementNode = currentNode
        break
      }
    }

    // 3、剩余菜单宽度小于容器宽度，首元素不变
    if (firstElementNode == null) {
      return
    }
    sumWidth = (firstElementNode.value.clientWidth + 1)
    currentNode = firstElementNode
    while (currentNode.next !== null) {
      currentNode = currentNode.next
      sumWidth += (currentNode.value.clientWidth + 1)
    }
    if (sumWidth < tabContainerOuterWidth && firstElementNode !== activeElementNode) {
      return
    }

    if (activeElementNode.prev !== null) {
      firstElementRef.value = activeElementNode.prev.value
    }
  }

  /**
   * 监控激活菜单变化
   */
  watch([activeValue], () => {
    locationCurrent()
  }, {
    flush: 'post'
  })

  return {
    outContainerRef,
    tabContainerRef,
    handleMoveRight,
    handleMoveLeft,
    locationCurrent
  }
}

/**
 * 样式计算属性
 * @param firstElementRef
 * @param tabContainerRef
 */
export const useTabContainerStyle = (firstElementRef: Ref<Element | null>, tabContainerRef: Ref) => {
  const computedTabContainerStyle = computed(() => {
    const element = firstElementRef.value
    if (element == null) {
      return {}
    }
    if (!tabContainerRef.value) {
      return {}
    }
    const menuElementList: HTMLCollection = tabContainerRef.value.children
    let sumWidth = 0
    for (let i=0; i<menuElementList.length; i++) {
      const item = menuElementList[i]
      if (item === element) {
        break
      }
      sumWidth += (item.clientWidth + 1)
    }
    return {
      'margin-left': `${0 - sumWidth}px`
    }
  })
  return {
    computedTabContainerStyle
  }
}
