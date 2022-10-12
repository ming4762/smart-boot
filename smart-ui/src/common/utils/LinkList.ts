export class NodeItem<T> {
  value: T

  prev: NodeItem<T> | null

  next: NodeItem<T> | null

  constructor(value: T, prev: NodeItem<T> | null, next: NodeItem<T> | null) {
    this.value = value
    this.prev = prev
    this.next = next
  }

  hasNext() {
    return this.next !== null
  }

  getNext(): NodeItem<T> {
    if (this.next == null) {
      throw new Error('下一个不能为null')
    }
    return this.next
  }
}
