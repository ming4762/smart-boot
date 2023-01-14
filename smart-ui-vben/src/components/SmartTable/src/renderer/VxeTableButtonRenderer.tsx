import VXETable from 'vxe-table'
import type { VxeGlobalRendererHandles } from 'vxe-table'
import { SmartTableButton } from '../types/SmartTableButton'
import { omit } from 'lodash-es'

export type VxeTableRenderer = 'VxeTableToolButtonRenderer'

export const VxeTableToolButtonRenderer: VxeTableRenderer = 'VxeTableToolButtonRenderer'

VXETable.renderer.add(VxeTableToolButtonRenderer, {
  renderToolbarButton(
    _: VxeGlobalRendererHandles.RenderButtonOptions,
    params: VxeGlobalRendererHandles.RenderButtonParams,
  ): VxeGlobalRendererHandles.RenderResult {
    const button = params.button as SmartTableButton
    let buttonPros = {
      ...button,
      ...(button.props || {}),
    }
    buttonPros = omit(buttonPros, ['props', 'buttonRender'])
    return <a-button {...buttonPros}>{buttonPros.name}</a-button>
  },
})
