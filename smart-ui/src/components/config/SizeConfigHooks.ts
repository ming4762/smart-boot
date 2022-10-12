import { computed, inject } from 'vue'

const tableButtonSizeMap: any = {
  medium: 'middle',
  small: 'small',
  mini: 'small'
}

/**
 * 尺寸配置
 */
export default function () {
  const tableSizeConfig = inject('tableSize')
  const buttonSizeConfig = inject('buttonSize')
  const formSizeConfig = inject('formSize')
  const tableButtonSizeConfig = computed(() => {
    // @ts-ignore
    return tableButtonSizeMap[tableSizeConfig.value] || 'small'
  })
  return {
    tableSizeConfig,
    buttonSizeConfig,
    formSizeConfig,
    tableButtonSizeConfig
  }
}
