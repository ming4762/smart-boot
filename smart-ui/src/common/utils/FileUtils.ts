/**
 * 文件工具类
 */
export default class FileUtils {
  /**
   * 创建文件并下载
   * @param filename
   * @param text
   */
  public static createAndDownload (filename: string, text: string): void {
    const element = document.createElement('a')
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text))
    element.setAttribute('download', filename)

    element.style.display = 'none'
    document.body.appendChild(element)

    element.click()

    document.body.removeChild(element)
  }
}
