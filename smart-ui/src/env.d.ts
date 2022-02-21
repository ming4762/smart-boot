/// <reference types="vite/client" />

declare module '*.vue' {
  import { DefineComponent } from 'vue'
  // eslint-disable-next-line @typescript-eslint/no-explicit-any, @typescript-eslint/ban-types
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare module 'blueimp-md5'

declare module 'nprogress'

declare module 'sortablejs'

declare module 'vue-echart5'

declare module 'file-saver'
