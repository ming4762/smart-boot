import {  defineComponent } from 'vue'

import SiderMenu, { SiderMenuProps } from './SiderMenu'

export default defineComponent({
  name: 'SiderMenuWrapper',
  props: SiderMenuProps,
  render () {
    const { layout } = this
    const isTopMenu = layout === 'topmenu'
    return this.isMobile ? renderMobile() : !isTopMenu && renderWeb(Object.assign({}, {
      class: 'ant-pro-sider-menu'
    }, this.$props))
  }
})

/**
 * 渲染移动端
 */
const renderMobile = () => {
  return (
    <a-drawer/>
  )
}

const renderWeb = (props: any) => {
  return (
    <SiderMenu {...props}/>
  )
}
