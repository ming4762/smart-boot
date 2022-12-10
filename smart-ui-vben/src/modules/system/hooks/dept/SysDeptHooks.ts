import { ref, onMounted } from 'vue'

import TreeUtils from '/@/utils/TreeUtils'
import { defHttp } from '/@/utils/http/axios'

/**
 * 加载部门树形数据
 */
export const useLoadDeptTreeData = () => {
  /**
   * 树形数据
   */
  const deptTreeData = ref<Array<any>>([])
  /**
   * 加载数据函数
   */
  const loadDeptData = async () => {
    const data = await defHttp.post({
      url: '/sys/dept/list',
      data: {
        sortName: 'seq',
        propertyList: ['deptId', 'deptName', 'parentId'],
      },
    })
    deptTreeData.value =
      TreeUtils.convertList2Tree(
        data,
        (item) => item.deptId,
        (item) => item.parentId,
        0,
      ) || []
  }
  onMounted(loadDeptData)
  return {
    deptTreeData,
  }
}
