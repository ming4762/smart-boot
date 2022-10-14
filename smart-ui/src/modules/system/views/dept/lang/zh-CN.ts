/**
 * 部门表 国际化信息
 */
export default {
  trans: true,
  key: 'system.views.dept',
  data: {
    title: {
      deptId: '部门ID',
      parent: '上级部门',
      deptCode: '部门编码',
      deptType: '部门类型',
      deptName: '部门名称',
      email: '邮箱',
      director: '负责人',
      phone: '电话',
      baseMessage: '基本信息'
    },
    validate: {
      deptId: '请输入部门ID',
      parentId: '请输入上级ID',
      deptCode: '请输入部门编码',
      deptType: '请输入部门类型',
      deptName: '请输入部门名称',
      email: '请输入邮箱',
      director: '请输入负责人',
      phone: '请输入电话',
      deleteYn: '请输入删除状态',
      createUserId: '请输入创建人ID',
      createTime: '请输入创建时间',
      updateUserId: '请输入更新人员ID',
      updateTime: '请输入更新时间'
    },
    rules: {
      deptCode_NOT_EMPTY: '部门编码不能为空',
      deptType_NOT_EMPTY: '请选择部门类型',
      deptName_NOT_EMPTY: '请输入部门名称'
    },
    search: {
      deptCode: '请输入部门编码',
      deptName: '请输入部门名称'
    },
    button: {
      addChild: '添加下级'
    },
    message: {
      selectDeptError: '请选择上级部门'
    }
  }
}
