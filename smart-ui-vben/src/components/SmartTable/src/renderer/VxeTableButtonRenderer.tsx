import VXETable from 'vxe-table'
import type { VxeGlobalRendererHandles } from 'vxe-table'
import { SmartTableButton } from '../types/SmartTableButton'
import { omit } from 'lodash-es'
import { hasButtonPermission } from '../hooks/userSmartTableButtonAuth'
import { isString } from '/@/utils/is'
import { unref } from 'vue'

export type VxeTableRenderer = 'VxeTableToolButtonAntRenderer' | 'VxeTableToolButtonSlotRenderer'

export const VxeTableToolButtonAntRenderer: VxeTableRenderer = 'VxeTableToolButtonAntRenderer'

VXETable.renderer.add(VxeTableToolButtonAntRenderer, {
  renderToolbarButton(
    _: VxeGlobalRendererHandles.RenderButtonOptions,
    params: VxeGlobalRendererHandles.RenderButtonParams,
  ): VxeGlobalRendererHandles.RenderResult {
    const button = params.button as SmartTableButton
    let buttonPros = {
      ...button,
      ...(unref(button.props) || {}),
    }
    buttonPros = omit(buttonPros, ['props', 'buttonRender'])
    // 权限处理
    const hasAuth = hasButtonPermission(button)
    if (!hasAuth) {
      const auth = button.auth
      if (!isString(auth)) {
        if (auth?.displayMode === 'hide') {
          return ''
        }
      }
      buttonPros.disabled = true
    }
    return <a-button {...buttonPros}>{buttonPros.name}</a-button>
  },
})
