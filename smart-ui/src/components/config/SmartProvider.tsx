import { defineComponent, toRefs, provide } from 'vue'

export const smartProviderProps = {
  buttonSize: {
    type: String,
    default: 'small'
  },
  tableSize: {
    type: String,
    default: 'small'
  },
  formSize: {
    type: String,
    default: 'small'
  }
}

export default defineComponent({
  name: 'SmartProvider',
  props: smartProviderProps,
  setup(props) {
    const propRef: any = toRefs(props)
    Object.keys(propRef).forEach((key) => {
      provide(key, propRef[key])
    })
  },
  render() {
    return this.$slots.default!()
  }
})
