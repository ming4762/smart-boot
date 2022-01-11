/**
 * 树形工具类
 */
export default class TreeUtils {

  public static CHILDREN = 'children'
  public static HAS_CHILD = 'hasChild'
  public static HAS_PARENT = 'hasParent'

  /**
   * 将list转为tree
   * @param list 列表数据
   * @param treeCode 标识ID 和parentId
   * @param topParentCode 顶级节点的parentId
   */
  public static convertList2Tree (list: any[], treeCode: string[], topParentCode?: any): any[] | null {
    if (list == null) {
      return null
    }
    if (topParentCode === undefined || topParentCode === null) {
      topParentCode = '0'
    }
    const treeList: any[] = []
    if(treeCode.length !== 2) {
      console.error('请指明treeCode', treeCode)
    }
    const code = treeCode[0]
    const parentCode = treeCode[1]
    for (const value of list) {
      const parentId = value[parentCode]
      // 如果父ID 等于顶级父ID，则是顶级节点
      if (parentId === null || parentId === topParentCode) {
        treeList.push(value)
        continue
      }
      for (const parent of list) {
        const id = parent[code]
        if (id === parentId) {
          if (!parent[this.CHILDREN]) {
            parent[this.CHILDREN] = []
          }
          parent[this.CHILDREN].push(value)
          // 设置节点含有下级
          parent[this.HAS_CHILD] = true
          // 设置节点含有上级
          value[this.HAS_PARENT] = true

        }
      }
    }
    return treeList
  }


}
