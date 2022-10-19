import { ref, onMounted } from 'vue'

import { errorMessage } from '@/components/notice/SystemNotice'
import ApiService from '@/common/utils/ApiService'
import TreeUtils from '@/common/utils/TreeUtils'

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
    let data = []
    try {
      data = await ApiService.postAjax('sys/dept/list', {
        sortName: 'seq',
        propertyList: ['deptId', 'deptName', 'parentId']
      })
    } catch (e) {
      errorMessage(e)
      return false
    }
    deptTreeData.value = TreeUtils.convertList2Tree(data, ['deptId', 'parentId'], 0) || []
  }
  onMounted(loadDeptData)
  return {
    deptTreeData
  }
}
