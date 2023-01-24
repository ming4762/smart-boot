import { defineComponent, computed, unref, StyleValue } from 'vue'

import './TableSearchLayout.less'

export default defineComponent({
  name: 'TableSearchLayout',
  setup(_, { slots }) {
    const { search, default: tableSlot } = slots
    const hasSearchForm = computed(() => search !== undefined)

    const getTableContainerStyle = computed((): StyleValue => {
      let height = '100%'
      if (unref(hasSearchForm)) {
        height = 'calc(100% - 70px)'
      }
      return {
        height,
      }
    })

    return () => (
      <div class="smart-table-search-layout">
        {unref(hasSearchForm) ? <div class="smart-search-container">{search && search()}</div> : ''}
        <div class="smart-table-container" style={unref(getTableContainerStyle)}>
          {tableSlot && tableSlot()}
        </div>
      </div>
    )
  },
})
