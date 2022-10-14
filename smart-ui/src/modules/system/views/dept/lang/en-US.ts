/**
 * 部门表 国际化信息
 */
export default {
  trans: true,
  key: 'system.views.dept',
  data: {
    title: {
      deptId: '部门ID',
      parent: 'Parent dept',
      deptCode: 'Dept code',
      deptType: 'Dept type',
      deptName: 'Dept name',
      email: 'Email',
      director: 'Director',
      phone: 'Phone',
      baseMessage: 'Basic message'
    },
    validate: {
      deptId: '请输入部门ID',
      parentId: '请输入上级ID',
      deptCode: 'Please enter dept code',
      deptType: 'Please select dept type',
      deptName: 'Please enter dept name',
      email: 'Please enter email',
      director: 'Please enter director',
      phone: 'Please enter phone'
    },
    rules: {
      deptCode_NOT_EMPTY: 'Dept code cannot be empty',
      deptType_NOT_EMPTY: 'Please select dept type',
      deptName_NOT_EMPTY: 'Dept name cannot be empty'
    },
    search: {
      deptCode: 'Please enter dept code',
      deptName: 'Please enter dept name'
    },
    button: {
      addChild: 'Add child'
    },
    message: {
      selectDeptError: 'Please select parent dept'
    }
  }
}
