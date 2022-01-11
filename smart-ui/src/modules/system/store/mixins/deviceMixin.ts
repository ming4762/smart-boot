import { mapState } from 'vuex'

const deviceMixin = {
  computed: {
    ...mapState({
      isMobile: (state: any) => state.system.isMobile
    })
  }
}

export { deviceMixin }
