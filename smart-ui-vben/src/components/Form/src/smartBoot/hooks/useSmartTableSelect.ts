import { reactive } from 'vue'

export const useSmartTableSelect = () => {
  const selectValues = reactive({
    value: [],
    // 是否select change触发
    change: false,
  })

  return {
    selectValues,
  }
}
