<template>
  <div class="full-height codemirror-container">
    <textarea ref="textareaRef" style="height: 600px"></textarea>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, ref, onMounted, watch, unref } from 'vue'

import { propTypes } from '/@/utils/propTypes'
import { debounce } from 'lodash-es'

import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
// 引入主题
import 'codemirror/theme/idea.css'

import 'codemirror/mode/clike/clike.js'
import 'codemirror/mode/css/css.js'
import 'codemirror/mode/xml/xml.js'
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/htmlmixed/htmlmixed.js'

let coder: any = null

const initialize = (props: any, emit: Function) => {
  const { code, options, mode, theme, readOnly, value: valueRef } = toRefs(props)
  const textareaRef = ref()
  onMounted(() => {
    coder = CodeMirror.fromTextArea(
      textareaRef.value,
      Object.assign({}, options.value, {
        // 主题，对应主题库 JS 需要提前引入
        theme: theme.value,
        mode: mode.value,
        readOnly: readOnly.value,
      }),
    )
    coder.on(
      'change',
      debounce(() => {
        emit('update:value', getValue())
      }, 300),
    )
    // 设置初始值
    coder.setValue(code.value)
  })

  const refresh = () => {
    setTimeout(() => {
      coder.refresh()
    }, 200)
  }

  const setValue = (value: string) => {
    if (coder) {
      const currentValue = getValue()
      if (currentValue !== value) {
        coder.setValue(value)
        refresh()
      }
    }
  }

  const getValue = (): string => {
    if (!coder) {
      return ''
    }
    return coder.getValue()
  }

  watch(code, () => {
    if (coder) {
      coder.setValue(code.value)
      refresh()
    }
  })
  watch(mode, () => {
    if (coder) {
      coder.setOption('mode', mode.value)
    }
  })
  watch(readOnly, () => {
    if (coder) {
      coder.setOption('readOnly', readOnly.value)
    }
  })
  watch(valueRef, () => {
    setValue(unref(valueRef))
  })
  return {
    textareaRef,
    getCode: () => {
      return getValue()
    },
    setCode: (code: string) => {
      setValue(code)
    },
  }
}

/**
 * 代码编辑器
 */
export default defineComponent({
  name: 'CodemirrorVue',
  props: {
    code: {
      type: String,
      default: '',
    },
    value: propTypes.string.def(''),
    options: {
      type: Object,
      default: () => {
        return {
          // 显示行号
          lineNumbers: true,
          line: true,
          lineWrapping: true,
        }
      },
    },
    mode: {
      type: String,
      default: 'xml',
    },
    theme: {
      type: String,
      default: 'idea',
    },
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const initializeVue = initialize(props, emit)
    // 编辑器实例
    return {
      ...initializeVue,
    }
  },
})
</script>

<style lang="less" scoped>
.codemirror-container {
  ::v-deep(.CodeMirror) {
    height: 100%;
  }
}
</style>
