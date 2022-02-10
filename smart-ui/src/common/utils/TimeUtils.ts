/**
 * 时间工具类
 */
export default class TimeUtils {
  /**
   * 时间段（毫秒）转天时分秒
   * @param time
   */
  public static convertDuration (time: number): string {
    let result = ''
    // 秒数
    const second = parseInt((time / 1000) + '')
    // 获取天
    const day = Math.floor(second / (24 * 60 * 60))
    if (day > 0) {
      result += (day + '天')
    }
    // 获取剩余秒数
    const hourSecond = second % (24 * 60 * 60)
    const hour = Math.floor(hourSecond / 3600)
    if (hour > 0) {
      result += (hour + '小时')
    }
    const minuteSecond = hourSecond % 3600
    const minute = Math.floor(minuteSecond / 60)
    if (minute > 0) {
      result += (minute + '分')
    }
    const ss = second % 60
    if (ss > 0) {
      result += (ss + '秒')
    }
    return result
  }
}
