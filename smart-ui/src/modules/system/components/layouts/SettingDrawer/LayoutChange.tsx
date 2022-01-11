import { PropType, defineComponent, inject } from 'vue'
import VuePropTypes from '@/common/utils/VueTypes'

export const renderLayoutSettingItem = (item: any) => {
  const action = Object.assign({}, item.action)
  return (
    <a-tooltip placement="left" title={item.disabled ? item.disabledReason : ''}>
      <a-list-item actions={[action]}>
        <span style={{opacity: item.disabled ? 0.5 : 1}}>
          {item.title}
        </span>
      </a-list-item>
    </a-tooltip>
  )
}

export const LayoutSettingProps = {
  contentWidth: {
    type: String as PropType<string>,
    validator: VuePropTypes.oneOf(['Fluid', 'Fixed']).validator,
    default: 'Fluid'
  },
  fixedHeader: Boolean as PropType<boolean>,
  fixSiderbar: Boolean as PropType<boolean>,
  hasMultiTab: Boolean as PropType<boolean>,
  layout: {
    type: String as PropType<string>,
    validator: VuePropTypes.oneOf(['sidemenu', 'topmenu']).validator
  },
  i18nRender: {
    type: [Function, Boolean] as PropType<Function | boolean>,
    default: false
  }
}

export default defineComponent({
  name: 'LayoutChange',
  props: LayoutSettingProps,
  emits: ['change'],
  render () {
    const { contentWidth, layout, fixSiderbar, fixedHeader, hasMultiTab } = this
    const i18n: any = this.i18nRender || inject('locale')
    const handleChange = (type: string, value: any) => {
      this.$emit('change', {
        type: type,
        value: value
      })
    }
    const dataSource = [
      {
        title: i18n('system.setting.contentWidth.title'),
        action: <a-select
          onSelect={(value: string) => handleChange('contentWidth', value)}
          size="small"
          stype="width: 80px"
          value={contentWidth}>
          {
            layout === 'sidemenu' ? null : <a-select-option value="Fixed">
              { i18n('system.setting.contentWidth.fixed') }
            </a-select-option>
          }
          <a-select-option value="Fluid">
            { i18n('system.setting.contentWidth.fluid') }
          </a-select-option>
        </a-select>
      },
      {
        title: i18n('system.setting.fixedheader'),
        action: <a-switch
          checked={fixedHeader}
          onChange={(checked: any) => handleChange('fixedHeader', checked)}
          size="small"/>
      },
      {
        title: i18n('system.setting.fixedsidebar.title'),
        disabled: layout === 'topmenu',
        disabledReason: i18n('system.setting.fixedsidebar.hint'),
        action: <a-switch
          onChange={(checked: any) => handleChange('fixSiderbar', checked)}
          disabled={layout === 'topmenu'}
          checked={fixSiderbar}
          size="small"/>
      },
      {
        title: i18n('system.setting.hasMultiTab'),
        action: <a-switch
          checked={hasMultiTab}
          onChange={(checked: boolean) => handleChange('hasMultiTab', checked)}
          size="small"/>
      }
    ]
    return (
      <a-list split={false} dataSource={dataSource} renderItem={({item}: any) => renderLayoutSettingItem(item)} />
    )
  }
})
