import { defineComponent, inject, PropType, ref } from 'vue'

import { CloseOutlined, SettingOutlined, NotificationOutlined } from '@ant-design/icons-vue'
// @ts-ignore
// import omit from 'ant-design-vue/es/_util/omit'

import BlockCheckbox from './BlockCheckbox'
import ThemeColor from './ThemeColor'
import LayoutSetting, { renderLayoutSettingItem } from './LayoutChange'
import { updateTheme, updateColorWeak } from '@/components/layouts/utils/dynamicTheme'
// import { genStringToTheme } from '@/common/utils/utils'

import VuePropTypes from '@/common/utils/VueTypes'

import './SettingDrawer.less'

export const setting = {
  theme: {
    type: String,
    validator: VuePropTypes.oneOf(['dark', 'light', 'realDark']).validator
  },
  primaryColor: String as PropType<string>,
  layout: {
    type: String,
    validator: VuePropTypes.oneOf(['sidemenu', 'topmenu']).validator
  },
  colorWeak: Boolean as PropType<boolean>,
  contentWidth: {
    type: String,
    validator: VuePropTypes.oneOf(['Fluid', 'Fixed']).validator,
    default: 'Fluid'
  },
  fixedHeader: Boolean as PropType<boolean>,
  fixSiderbar: Boolean as PropType<boolean>,
  hideHintAlert: Boolean as PropType<boolean>,
  hideCopyButton: Boolean as PropType<boolean>
}

export const SettingDrawerProps = {
  getContainer: Function as PropType<Function>,
  i18nRender: {
    type: [Function, Boolean] as PropType<Function | boolean>,
    default: false
  },
  settings: null
}

const baseClassName = 'ant-pro-setting-drawer'

const iconStyle = {
  color: '#fff',
  fontSize: 20
}

const getThemeList = (i18nRender: Function) => {
  // @ts-ignore
  const list = window.umi_plugin_ant_themeVar || [];
  const themeList: Array<any> = [{
    key: 'light',
    url: '/src/modules/system/assets/settingDrawer/menu_light.svg',
    title: i18nRender('system.setting.pageStyle.light')
  }, {
    key: 'dark',
    url: '/src/modules/system/assets/settingDrawer/layout_side.svg',
    title: i18nRender('system.setting.pageStyle.dark')
  }]
  const darkColorList = [{
    key: '#1890ff',
    color: '#1890ff',
    theme: 'dark'
  }]
  const lightColorList = [{
    key: '#1890ff',
    color: '#1890ff',
    theme: 'dark'
  }]

  if (list.find(function (item: any) {
    return item.theme === 'dark';
  })) {
    themeList.push({
      // disable click
      disable: true,
      key: 'realDark',
      url: '/src/modules/system/assets/settingDrawer/real_dark.svg',
      title: i18nRender('system.setting.pageStyle.realdark')
    });
  } // insert  theme color List


  list.forEach(function (item: any) {
    const color = (item.modifyVars || {})['@primary-color'];

    if (item.theme === 'dark' && color) {
      darkColorList.push(Object.assign({
        color: color
      }, item));
    }

    if (!item.theme || item.theme === 'light') {
      lightColorList.push(Object.assign({
        color: color
      }, item));
    }
  });
  return {
    colorList: {
      dark: darkColorList,
      light: lightColorList
    },
    themeList: themeList
  };
}

const Body = defineComponent({
  name: 'Body',
  props: {
    title: {
      type: String as PropType<string>,
      default: ''
    }
  },
  render () {
    const { title } = this
    return (
      <div style="margin-botton: 24">
        <h3 class={`${baseClassName}-title`}>
          { title }
        </h3>
        {
          this.$slots.default && this.$slots.default()
        }
      </div>
    )
  }
})

const renderHandle = (show: boolean, setShow: Function) => {
  return () => {
    return (
      <div onClick={() => setShow(!show)} class={`${baseClassName}-handle`}>
        {
          show ? <CloseOutlined style={iconStyle} /> : <SettingOutlined style={iconStyle} />
        }
      </div>
    )
  }
}

const defaultI18nRender = (t: any) => t


// eslint-disable-next-line @typescript-eslint/no-unused-vars,no-unused-vars
const handleChangeSetting = (key: string, value: any, hideMessageLoading: any) => {
  if (key === 'primaryColor') {
    // 更新主色调
    updateTheme(value);
  }

  if (key === 'colorWeak') {
    updateColorWeak(value);
  }
}
//
// const genCopySettingJson = (settings: any) => {
//   return JSON.stringify(omit(Object.assign({}, settings, {
//     primaryColor: genStringToTheme(settings.primaryColor)
//   }), ['colorWeak']), null, 2)
// }

export default defineComponent({
  name: 'SettingDrawer',
  props: SettingDrawerProps,
  emits: ['change'],
  setup () {
    const show = ref(false)
    const setShow = (showValue: boolean) => {
      show.value = showValue
    }
    return {
      show,
      setShow
    }
  },
  render () {
    const { show, setShow, settings } = this
    const theme = settings.theme === void 0 ? 'dark' : settings.theme
    const primaryColor = settings.primaryColor === void 0 ? 'daybreak' : settings.primaryColor
    const layout = settings.layout === void 0 ? 'sidemenu' : settings.layout
    const { contentWidth, hideHintAlert, hideCopyButton, colorWeak } = settings
    const fixedHeader = settings.fixedHeader === void 0 ? false : settings.fixedHeader
    const fixSiderbar = settings.fixSiderbar === void 0 ? false : settings.fixSiderbar
    const hasMultiTab = settings.hasMultiTab === void 0 ? false : settings.hasMultiTab
    const isTopMenu = layout === 'topmenu'
    const drawerProps = {
      visible: show,
      width: 300,
      placement: 'right',
      onClose: () => setShow(false)
    }
    const changeSetting = (type: string, value: any) => {
      this.$emit('change', {
        type: type,
        value: value
      })
      handleChangeSetting(type, value, false)
    }
    const i18n: any = this.i18nRender || inject('locale', defaultI18nRender)
    const themeList = getThemeList(i18n)
    const slots = {
      handle: renderHandle(show, setShow),
      default: () => (
        <div class={`${baseClassName}-content`}>
          <Body title={i18n('system.setting.pageStyle.title')}>
            <BlockCheckbox
              // @ts-ignore
              onChange={(val: string) => changeSetting('theme', val)}
              i18nRender={i18n}
              value={theme}
              list={themeList.themeList}/>
          </Body>
          <ThemeColor
            i18nRender={i18n}
            value={primaryColor === void 0 ? 'daybreak' : primaryColor}
            colors={themeList.colorList[theme === 'reakDark' ? 'dark' : 'light']}
            onChange={(color) => changeSetting('primaryColor', color)}
            title={i18n('system.setting.themeColor')} />
          <a-divider/>
          <Body title={i18n('system.setting.navigationMode')}>
            <BlockCheckbox
              // @ts-ignore
              onChange={(value1: any) => changeSetting('layout', value1)}
              value={layout}
              i18nRender={i18n}/>
          </Body>
          <LayoutSetting
            contentWidth={contentWidth}
            fixedHeader={fixedHeader}
            hasMultiTab={hasMultiTab}
            fixSiderbar={isTopMenu ? false : fixSiderbar}
            layout={layout}
            onChange={(data: any) => changeSetting(data.type, data.value)}
            i18nRender={i18n}/>
          <a-divider/>
          <Body title={i18n('system.setting.sizeSettings.title')}>
            <a-list
              renderItem={({item}: any) => renderLayoutSettingItem(item)}
              dataSource={[
                {
                  title: i18n('system.setting.sizeSettings.buttonSize'),
                  action: <a-select
                    value={settings.buttonSize}
                    style="width: 80px"
                    onSelect={(value: string) => changeSetting('buttonSize', value)}
                    size="small">
                    <a-select-option value="middle">middle</a-select-option>
                    <a-select-option value="large">large</a-select-option>
                    <a-select-option value="small">small</a-select-option>
                  </a-select>
                },
                {
                  title: i18n('system.setting.sizeSettings.tableSize'),
                  action: <a-select
                    value={settings.tableSize}
                    style="width: 80px"
                    onSelect={(value: string) => changeSetting('tableSize', value)}
                    size="small">
                    <a-select-option value="medium">medium</a-select-option>
                    <a-select-option value="small">small</a-select-option>
                    <a-select-option value="mini">mini</a-select-option>
                  </a-select>
                },
                {
                  title: i18n('system.setting.sizeSettings.formSize'),
                  action: <a-select
                    value={settings.formSize}
                    style="width: 80px"
                    onSelect={(value: string) => changeSetting('formSize', value)}
                    size="small">
                    <a-select-option value="large">large</a-select-option>
                    <a-select-option value="default">default</a-select-option>
                    <a-select-option value="small">small</a-select-option>
                  </a-select>
                }
              ]}
              split={false}/>
          </Body>
          <a-divider/>
          <Body title={i18n('system.setting.otherSettings')}>
            <a-list
              renderItem={({item}: any) => renderLayoutSettingItem(item)}
              dataSource={[
                {
                  title: i18n('system.setting.weakmode'),
                  action: <a-switch
                    checked={colorWeak}
                    onChange={(checked: boolean) => changeSetting('colorWeak', checked)}
                    size="small"/>
                }
              ]}
              split={false}/>
          </Body>
          {
            hideHintAlert && hideCopyButton ? null : <a-divider/>
          }
          {
            hideHintAlert ? null : <a-alert
              message={i18n('system.setting.production.hint')}
              showIcon={true}
              icon={(
                <NotificationOutlined />
              )}
              style="margin-botton: 16px"
              type="warning"/>
          }
          {
            // hideCopyButton ? null : <CopyToClipboard
            //   onCopy={() => message.success(i18n('system.setting.copyinfo'))}
            //   text={genCopySettingJson(settings)}>
            //   <a-button block={true}>
            //     <CopyOutlined />
            //     { i18n('system.setting.copy') }
            //   </a-button>
            // </CopyToClipboard>
          }
          <div class={`${baseClassName}-content-footer`}>
            {
              this.$slots.default && this.$slots.default()
            }
          </div>
        </div>
      )
    }
    return (
      <a-drawer v-slots={slots} style="z-index:999" {...drawerProps}>
      </a-drawer>
    )
  }
})
