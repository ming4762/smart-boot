import { createStore } from 'vuex'

import systemStore from '@/modules/system/store/SystemStore'
import AppStore from './modules/AppStore'

export default createStore({
  modules: {
    system: systemStore,
    app: {
      namespaced: true,
      ...AppStore
    }
  }
})
