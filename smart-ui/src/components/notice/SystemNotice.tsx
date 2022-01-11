import { createVNode } from 'vue'

import { message, Modal, Collapse, CollapsePanel  } from 'ant-design-vue'
import { ExclamationCircleOutlined } from '@ant-design/icons-vue'

/**
 * 错误信息处理
 * @param e
 */
export const errorMessage = (e: any) => {
  console.error(e)
  const code = e.code
  switch (code) {
    case 500:
      error500Handler(e)
      break
    default:
      message.error(e.message)
  }
}

/**
 * 500 错误提示
 * TODO: 待完善
 * @param e
 */
const error500Handler = (e: any) => {
  const { message, data } = e
  Modal.confirm({
    title: message,
    width: 600,
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode(Collapse, {  }, [
      createVNode(CollapsePanel, {
        header: 'Error Detail'
      }, data)
    ])
  })
}
