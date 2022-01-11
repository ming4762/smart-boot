import { defineComponent } from 'vue'
import VueTypes from '@/common/utils/VueTypes'

import './GridContent.less'

function _defineProperty(obj: any, key: string, value: any) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }


export default defineComponent({
  name: 'GridContent',
  functional: true,
  props: {
    contentWidth: {
      type: String,
      validator: VueTypes.oneOf(['Fluid', 'Fixed']).validator,
      default: 'Fluid'
    }
  },
  render () {
    const { contentWidth } = this
    let _classNames
    const propsContentWidth = contentWidth !== 'Fluid'
    const classNames = (_classNames = {}, _defineProperty(_classNames, 'ant-pro-grid-content', true), _defineProperty(_classNames, 'wide', propsContentWidth), _classNames)
    return (
      <div style="overflow: auto;" class={[classNames, 'full-height']}>
        { this.$slots.default ? this.$slots.default() : null }
      </div>
    )
  }
})
