/**
 * 单位转换工具类
 */
export default class UnitUtils {

  /**
   * 转换byte字节
   * @param byte
   */
  public static convertByte(byte: number): string {
    let size = ''
    if (byte < 0.1 * 1024) {
      size = byte.toFixed(2) + 'B'
    } else if (byte < 0.1 * 1024 * 1024) {
      size = (byte/1024).toFixed(2) + 'KB'
    } else if (byte < 0.1 * 1024 * 1024 * 1024) {
      size = (byte/(1024 * 1024)).toFixed(2) + 'MB'
    } else {
      size = (byte/(1024 * 1024 * 1024)).toFixed(2) + 'GB'
    }
    const index = size.indexOf('.')
    const dou = size.substring(index + 1 ,2)
    if (dou === '00') {
      return size.substring(0, index) + size.substring(index + 3, 2)
    }
    return size
  }

}
