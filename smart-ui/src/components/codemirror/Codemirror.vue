<template>
  <div class="full-height codemirror-container">
    <textarea ref="textareaRef" style="height: 600px"></textarea>
  </div>
</template>

<script lang="ts">
import { defineComponent, toRefs, ref, onMounted, watch } from 'vue'

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

const initialize = (props: any) => {
  const { code, options, mode, theme, readOnly } = toRefs(props)
  const textareaRef = ref()
  onMounted(() => {
    coder = CodeMirror.fromTextArea(textareaRef.value, Object.assign({}, options.value, {
      // 主题，对应主题库 JS 需要提前引入
      theme: theme.value,
      mode: mode.value,
      readOnly: readOnly.value
    }))
    // 设置初始值
    coder.setValue(code.value)
  })
  watch(code, () => {
    if (coder) {
      coder.setValue(code.value)
      setTimeout(() => {
        coder.refresh()
      }, 200)
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
  return {
    textareaRef,
    getCode: () => {
      if (!coder) {
        return ''
      }
      return coder.getValue()
    },
    setCode: (code: string) => {
      if (coder) {
        coder.setValue(code)
        setTimeout(() => {
          coder.refresh()
        }, 200)
      }
    }
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
      default: ''
    },
    options: {
      type: Object,
      default: () => {
        return {
          // 显示行号
          lineNumbers: true,
          line: true,
          lineWrapping: true
        }
      }
    },
    mode: {
      type: String,
      required: true
    },
    theme: {
      type: String,
      default: 'idea'
    },
    readOnly: {
      type: Boolean,
      default: false
    }
  },
  setup (props) {
    const initializeVue = initialize(props)
    // 编辑器实例
    return {
      ...initializeVue
    }
  }
})
</script>

<style lang="less" scoped>
  .codemirror-container {
    ::v-deep(.CodeMirror) {
      height: 100%;
    }
  }
</style>
