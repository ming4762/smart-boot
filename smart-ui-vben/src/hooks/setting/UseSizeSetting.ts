import { useAppStore } from '/@/store/modules/app'
import { computed } from 'vue'

export const useSizeSetting = () => {
  const appStore = useAppStore()

  const getButtonSize = computed(() => appStore.getSizeSetting.button)
  const getTableSize = computed(() => appStore.getSizeSetting.table)
  const getFormSize = computed(() => appStore.getSizeSetting.form)

  return {
    getButtonSize,
    getTableSize,
    getFormSize,
  }
}
