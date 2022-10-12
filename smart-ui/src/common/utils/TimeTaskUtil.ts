/**
 * handler存储
 */
type HandlerStore = {
  delay: number
  lookup: number
  data: Map<string, Function>
}

/**
 * handler 分组存储
 */
const handlerGroupStore = new Map<string, HandlerStore>()

export default class TimeTaskUtil {
  /**
   * 循环执行
   * @param targetFunction 执行函数
   * @param times 循环次数
   * @param delay 延迟事件
   * @param parameter
   */
  public static loop(targetFunction: Function, times = -1, delay: number, ...parameter: any[]): number {
    let num = 0
    const loop = setInterval(() => {
      // 执行目标函数
      try {
        targetFunction(loop, ...parameter)
      } catch (error) {
        console.error(error)
      }
      num++
      if (times > 0 && num === times) {
        clearInterval(loop)
      }
    }, delay)
    // @ts-ignore
    return loop
  }

  /**
   * 移除循环组
   * @param group
   */
  public static removeLoopGroup(group: string) {
    const groupData = handlerGroupStore.get(group)
    if (groupData) {
      handlerGroupStore.delete(group)
      // 清楚循环
      clearInterval(groupData.lookup)
    }
  }

  /**
   * 移除循环
   * @param group
   * @param key
   */
  public static removeLoop(group: string, key: string) {
    const groupData = handlerGroupStore.get(group)
    if (groupData) {
      groupData.data.delete(key)
    }
  }

  /**
   * 添加循环
   * @param group 循环组
   * @param key 循环key
   * @param handler 执行函数
   */
  public static addLoop(group: string, key: string, handler: Function) {
    const groupData = handlerGroupStore.get(group)
    if (!groupData) {
      throw new Error('请先添加循环分组')
    }
    groupData.data.set(key, handler)
  }

  /**
   * 添加循环组/更改循环组执行时间间隔
   * @param group
   * @param delay
   */
  public static addLoopGroup(group: string, delay: number) {
    let groupData = handlerGroupStore.get(group)
    if (!groupData) {
      groupData = {
        delay: delay,
        data: new Map(),
        lookup: -1
      }
      groupData.lookup = this.loop(() => this.execute(groupData!), -1, delay * 1000)
      handlerGroupStore.set(group, groupData)
    } else if (groupData.delay !== delay) {
      // 如果延迟时间不一致，重新执行
      clearInterval(groupData.lookup)
      groupData.lookup = this.loop(() => this.execute(groupData!), -1, delay * 1000)
      groupData.delay = delay
    }
  }

  /**
   * 执行函数
   * @param groupData
   * @private
   */
  private static execute(groupData: HandlerStore) {
    groupData.data.forEach((value) => {
      value()
    })
  }
}
