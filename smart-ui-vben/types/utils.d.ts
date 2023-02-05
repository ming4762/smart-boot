import type { ComputedRef, Ref } from 'vue'

export type DynamicProps<T> = {
  [P in keyof T]: Ref<T[P]> | T[P] | ComputedRef<T[P]>
}

/**
 * 权限
 */
export interface SmartAuth {
  permission: string | string[]
  displayMode: 'hide' | 'disabled'
  multipleMode: 'and' | 'or'
}
