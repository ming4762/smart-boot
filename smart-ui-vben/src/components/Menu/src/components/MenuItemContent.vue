<template>
  <span :class="`${prefixCls}- flex items-center `">
    <Icon v-if="getIcon" :icon="getIcon" :size="18" :class="`${prefixCls}-wrapper__icon mr-2`" />
    {{ getI18nName }}
  </span>
</template>
<script lang="ts">
import { computed, defineComponent, unref } from 'vue'

import Icon from '/@/components/Icon/index'
import { useI18n } from '/@/hooks/web/useI18n'
import { useDesign } from '/@/hooks/web/useDesign'
import { contentProps } from '../props'
const { locale } = useI18n()

export default defineComponent({
  name: 'MenuItemContent',
  components: {
    Icon,
  },
  props: contentProps,
  setup(props) {
    console.log(props)
    const { prefixCls } = useDesign('basic-menu-item-content')
    // const getI18nName = computed(() => t(props.item?.name))
    const getI18nName = computed(() => {
      const { title, locales } = (props.item || {}) as any
      if (!locales || !locales[unref(locale)]) {
        return title
      }
      return locales[unref(locale)]
    })
    const getIcon = computed(() => props.item?.icon)

    return {
      prefixCls,
      getI18nName,
      getIcon,
    }
  },
})
</script>
