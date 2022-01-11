import { defineComponent } from 'vue'
import VueTypes from '@/common/utils/VueTypes'

import GridContent from './GridContent'

/**
 * TODO:开发中
 */
export default defineComponent({
  name: 'WrapContent',
  components: {
    GridContent
  },
  props: {
    contentWidth: {
      type: String,
      validator: VueTypes.oneOf(['Fluid', 'Fixed']).validator,
      default: 'Fluid'
    }
  },
  render () {
    const { contentWidth } = this
    return (
      <a-layout-content>
        <div style="overflow: hidden;" class="ant-pro-basicLayout-children-content-wrap full-height">
          <GridContent v-slots={this.$slots} contentWidth={contentWidth} />
        </div>
      </a-layout-content>
    )
  }
})
