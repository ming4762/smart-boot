// Used to configure the general configuration of some components without modifying the components

// @ts-ignore
import type { SorterResult } from '../components/Table'
import type { VxePagerPropTypes } from 'vxe-table/types/pager'

export default {
  // scrollbar setting
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    native: false,
  },
}
