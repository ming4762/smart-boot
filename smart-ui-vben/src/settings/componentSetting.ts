// Used to configure the general configuration of some components without modifying the components

// @ts-ignore
import type { SorterResult } from '../components/Table'
import type { VxePagerPropTypes } from 'vxe-table/types/pager'

export default {
  // basic-table setting
  table: {
    // Form interface request general configuration
    // support xxx.xxx.xxx
    fetchSetting: {
      // The field name of the current page passed to the background
      pageField: 'page',
      // The number field name of each page displayed in the background
      sizeField: 'limit',
      // Field name of the form data returned by the interface
      listField: 'rows',
      // Total number of tables returned by the interface field name
      totalField: 'total',
    },
    // Number of pages that can be selected
    pageSizeOptions: [100, 500, 1000, 2000, 5000],
    // Default display quantity on one page
    defaultPageSize: 100,
    pageLayouts: [
      'Sizes',
      'PrevJump',
      'PrevPage',
      'Number',
      'NextJump',
      'FullJump',
      'Total',
    ] as VxePagerPropTypes.Layouts,
    // Default Size
    defaultSize: 'middle',
    // Custom general sort function
    defaultSortFn: (sortInfo: SorterResult) => {
      const { field, order } = sortInfo
      if (field && order) {
        return {
          // The sort field passed to the backend you
          field,
          // Sorting method passed to the background asc/desc
          order,
        }
      } else {
        return {}
      }
    },
    // Custom general filter function
    defaultFilterFn: (data: Partial<Recordable<string[]>>) => {
      return data
    },
  },
  // scrollbar setting
  scrollbar: {
    // Whether to use native scroll bar
    // After opening, the menu, modal, drawer will change the pop-up scroll bar to native
    native: false,
  },
}
