/**
 * 字符串处理工具类
 */
export default class StringUtils {
  /**
   * 判断String 是为未空
   * @param value
   */
  public static hasLength(value: string | null | undefined): boolean {
    return !!value && value.length > 0
  }
}
