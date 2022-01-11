import ResizeObserverLite from 'resize-observer-lite'
import matchQueries from 'container-query-toolkit/lib/matchQueries'
import { isShallowEqual } from './utils'

export default class ContainerQueryCore {

  private result: any = {}

  private rol: ResizeObserverLite | null = null

  constructor(query: any, callback: Function) {
    this.rol = new ResizeObserverLite(size => {
      const result = matchQueries(query)(size)
      if (!isShallowEqual(this.result, result)) {
        callback(result)
        this.result = result
      }
    })
  }

  observe (element: any) {
    this.rol!.observe(element)
  }

  disconnect () {
    this.rol!.disconnect()
  }
}
