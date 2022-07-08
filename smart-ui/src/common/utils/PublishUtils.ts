import PubSub from 'pubsub-js'

/**
 * 发布消息
 * @param key 消息的key
 * @param data 消息内容
 */
export function publish(key: string | symbol, data: any) {
  PubSub.publish(key, data)
}

/**
 * 同步发布消息
 * @param key 消息的key
 * @param data 消息内容
 */
export function publishSync(key: string | symbol, data: any) {
  PubSub.publishSync(key, data)
}

/**
 * 订阅消息
 * @param key key
 * @param handler handler
 */
export function subscribe(key: string | symbol, handler: Function): string {
  // @ts-ignore
  return PubSub.subscribe(key, handler)
}

/**
 * 取消订阅
 * @param token 订阅TOKEN
 */
export function unsubscribe(token: string) {
  PubSub.unsubscribe(token)
}

/**
 * 清空订阅
 */
export function clearAllSubscriptions() {
  PubSub.clearAllSubscriptions()
}

export function getPubSub(): typeof PubSub {
  return PubSub
}
