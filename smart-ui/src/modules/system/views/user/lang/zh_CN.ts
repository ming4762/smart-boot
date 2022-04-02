
export default {
  system: {
    views: {
      user: {
        form: {
          systemUser: '系统用户',
          businessUser: '业务用户'
        },
        table: {
          username: '用户名',
          fullName: '姓名',
          email: '邮箱',
          mobile: '手机号',
          userType: '用户类型'
        },
        button: {
          createAccount: '创建账户'
        },
        validate: {
          username: '请输入用户名',
          fullName: '请输入姓名',
          email: '请输入邮箱',
          mobile: '请输入手机号',
          selectUser: '请选择用户',
          sysUserNoDelete: '系统用户不能删除！',
          setUserUseYn: '确定要{useYn}用户吗？',
          createAccountConfirm: '确定要创建账户吗？'
        },
        message: {
          deleteUserNotCreateAccount: '已删除用户不能创建账户',
          noUseUserNotCreateAccount: '未启用用户不能创建账户',
          createAccountSuccess: '创建账户成功'
        }
      }
    }
  }
}
