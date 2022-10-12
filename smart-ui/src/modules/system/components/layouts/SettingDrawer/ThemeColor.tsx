import { defineComponent, inject } from 'vue'
import type { PropType } from 'vue'
import { genThemeToString } from '@/common/utils/utils'

import { CheckOutlined } from '@ant-design/icons-vue'

import './ThemeColor.less'

const baseClassName = 'theme-color'

const Tag = defineComponent({
  name: 'TagComponent',
  props: {
    color: String as PropType<string>,
    check: Boolean as PropType<boolean>
  },
  render() {
    const { color, check } = this
    const divProps = {
      style: {
        backgroundColor: color
      }
    }
    return (
      <div {...divProps}>
        {
          check ? <CheckOutlined /> : null
        }
      </div>
    )
  }
})

export default defineComponent({
  name: 'ThemeColor',
  props: {
    colors: Array as PropType<Array<object>>,
    title: String as PropType<string>,
    value: String as PropType<string>,
    i18nRender: [Function, Boolean] as PropType<Function | boolean>
  },
  emits: ['change'],
  render () {
    const { title, colors, value, i18nRender } = this
    const colorList = colors === void 0 ? [] : colors
    const i18n: any = i18nRender || inject('locale')
    const handleChange = (key: string) => {
      this.$emit('change', key)
    }
    return (
      <div class={baseClassName} ref="ref">
        <h3 class={`${baseClassName}-title`}>
          {title}
        </h3>
        <div class={`${baseClassName}-content`}>
          {
            colorList.map((item: any) => {
              const themeKey = genThemeToString(item.key)
              const check = value === item.key || genThemeToString(value) === item.key
              return <a-tooltip
                title={themeKey ? `${i18n('system.setting.themeColor')}${themeKey}` : item.key}
                key={item.color}>
                <Tag
                  // @ts-ignore
                  onClick={() => handleChange(item.key)}
                  color={item.color}
                  check={check}
                  class={`${baseClassName}-block`}/>
              </a-tooltip>
            })
          }
        </div>
      </div>
    )
  }
})
