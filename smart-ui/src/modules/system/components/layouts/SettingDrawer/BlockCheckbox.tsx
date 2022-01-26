import { defineComponent, PropType, computed, toRefs, inject, getCurrentInstance } from 'vue'
import { ComponentInternalInstance } from '@vue/runtime-core'

import { CheckOutlined } from '@ant-design/icons-vue'

const baseClassName = 'ant-pro-setting-drawer-block-checbox'

const disableStyle = {
  cursor: 'not-allowed'
}

export default defineComponent({
  name: 'BlockCheckbox',
  props: {
    value: String as PropType<string>,
    list: Array as PropType<Array<string>>,
    i18nRender: [Function, Boolean] as PropType<Function | boolean>
  },
  setup (props) {
    const { list, i18nRender } = toRefs(props)
    const { emit } = getCurrentInstance() as ComponentInternalInstance
    const i18n: any = (i18nRender ? i18nRender.value : null) || inject('locale')
    const computedItems = computed(() => {
      return list.value || [{
        key: 'sidemenu',
        url: '/src/modules/system/assets/settingDrawer/layout_side.svg',
        title: i18n('system.setting.sidemenu')
      }, {
        key: 'topmenu',
        url: 'src/modules/system/assets/settingDrawer/layout_top.svg',
        title: i18n('system.setting.topmenu')
      }]
    })
    const handleChange = (key: string) => {
      emit('change', key)
    }
    return {
      computedItems,
      handleChange
    }
  },
  render () {
    const { value } = this
    return (
      <div class={baseClassName}>
        {
          this.computedItems.map((item: any) => {
            return <a-tooltip key={item.key} title={item.title}>
              <div
                class={`${baseClassName}-item`}
                onClick={() => !item.disable && this.handleChange(item.key)}
                style={item.disable && disableStyle}>
                <img src={item.url} alt={item.key}/>
                <div class={`${baseClassName}-selectIcon`} style={{display: value === item.key ? 'block' : 'none'}}>
                  <CheckOutlined />
                </div>
              </div>
            </a-tooltip>
          })
        }
      </div>
    )
  }
})
