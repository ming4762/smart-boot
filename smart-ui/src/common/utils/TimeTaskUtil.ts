export default class TimeTaskUtil {
  /**
   * 循环执行
   * @param targetFunction 执行函数
   * @param times 循环次数
   * @param delay 延迟事件
   * @param parameter
   */
  public static loop (targetFunction: Function, times = -1, delay: number, ...parameter: any[]): number {
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
    return loop
  }
}
