import { useGlobSetting } from '/@/hooks/setting'
import { getCurrentUserId } from '/@/common/auth/AuthUtils'
import { useWebSocket, WebSocketResult } from '@vueuse/core'
import { onUnmounted, unref } from 'vue'

export interface WebSocketHandler {
  open?: () => void
  close?: () => void
  message?: (e: MessageEvent) => void
  error?: (e: MessageEvent) => void
}

let webSocketResult: WebSocketResult<any>

const messageHandlerMap: Map<string, WebSocketHandler> = new Map()

/**
 * 连接web socket
 */
export const initWebSocket = () => {
  const webSocketUrl = getWebSocketUrl()
  console.log('===============')
  webSocketResult = useWebSocket(webSocketUrl, {
    autoReconnect: {
      retries: 5,
      delay: 5000,
    },
    heartbeat: {
      message: 'ping',
      interval: 10 * 1000,
    },
  })
  console.log(webSocketResult)
  if (webSocketResult) {
    webSocketResult.open = onOpen
    webSocketResult.close = onClose

    const ws = unref(webSocketResult.ws)
    if (ws) {
      ws.onmessage = onMessage
      ws.onerror = onError
    }
  }
}

export const useRegisterMessageHandler = (key: string, handler: WebSocketHandler) => {
  messageHandlerMap.set(key, handler)

  onUnmounted(() => {
    messageHandlerMap.delete(key)
  })
}

const getWebSocketUrl = () => {
  const globSetting = useGlobSetting()
  const currentUserId = getCurrentUserId()
  return (
    globSetting.realApiUrl?.replace('https://', 'wss://').replace('http://', 'ws://') +
    '/websocket/' +
    currentUserId
  )
}

const onOpen = () => {
  console.log('[WebSocket] 连接成功')
  messageHandlerMap.forEach((item) => {
    item.open?.apply(this)
  })
}

const onClose = (e) => {
  console.log('[WebSocket] 连接断开：', e)
  messageHandlerMap.forEach((item) => {
    item.close?.apply(this)
  })
}

const onMessage = (e: MessageEvent) => {
  messageHandlerMap.forEach((item) => {
    if (item.message) {
      item.message(e)
    }
  })
}

const onError = (e: MessageEvent) => {
  console.log('[WebSocket] 连接发生错误: ', e)
  messageHandlerMap.forEach((item) => {
    if (item.error) {
      item.error(e)
    }
  })
}
