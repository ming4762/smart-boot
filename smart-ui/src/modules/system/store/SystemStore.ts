import { StoreOptions } from 'vuex'

const systemStore: StoreOptions<any> = {
  state: {
    exception: {
      // 弹窗状态
      modalVisible: false,
      noList: []
    }
  },
  mutations: {
    /**
     * 显示弹窗
     * @param state
     * @param exceptionNo
     */
    handleShowExceptionModal: (state, exceptionNo) => {
      if (state.exception.modalVisible === false) {
        state.exception.noList = []
      }
      state.exception.modalVisible = true
      state.exception.noList.push(exceptionNo)
    },
    /**
     * 隐藏弹窗
     * @param state
     */
    handleHideExceptionModal: (state) => {
      state.exception.noList = []
      state.exception.modalVisible = false
    }
  },
  getters: {
    exception: state => state.exception
  }
}


export default systemStore
