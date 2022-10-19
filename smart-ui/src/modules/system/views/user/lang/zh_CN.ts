export default {
  system: {
    views: {
      user: {
        title: {
          dataAll: '所有数据权限',
          dataDept: '部门数据权限',
          dataDeptAll: '部门及下级部门数据权限',
          dataPersonal: '个人数据权限'
        },
        form: {
          systemUser: '系统用户',
          businessUser: '业务用户',
          dept: '所属部门',
          dataScope: '数据权限范围'
        },
        table: {
          username: '用户名',
          fullName: '姓名',
          email: '邮箱',
          mobile: '手机号',
          userType: '用户类型'
        },
        button: {
          createAccount: '创建账户',
          showAccount: '账户信息'
        },
        account: {
          title: '账户信息',
          accountSet: '账户设置',
          createTime: '账户创建时间',
          accountStatus: '账户状态',
          passwordModifyTime: '密码修改时间',
          lockTime: '账户锁定时间',
          loginFailTime: '登录失败次数',
          initialPasswordYn: '是否初始化密码',

          ipWhiteList: 'IP白名单',
          maxConnections: '最大访问连接数，0：不限制',
          maxDaysSinceLogin: '指定天数未登录账户锁定，0：永不锁定',
          passwordLifeDays: '密码必须修改的天数，0：不限制',
          maxConnectionsPolicy: '超出最大连接数执行策略',
          loginFailTimeLimit: '登录失败锁定次数，0永不锁定',
          passwordErrorUnlockSecond: '多次输入密码错误锁定后，指定秒后自动解锁，0：永不自动解锁',

          loginNotAllow: '不允许登录',
          firstUserLogout: '最早登录用户登出',

          ipWhiteListPlaceholder: 'IP白名单，多个IP以逗号分隔'
        },
        validate: {
          username: '请输入用户名',
          fullName: '请输入姓名',
          email: '请输入邮箱',
          mobile: '请输入手机号',
          selectUser: '请选择用户',
          sysUserNoDelete: '系统用户不能删除！',
          setUserUseYn: '确定要{useYn}用户吗？',
          createAccountConfirm: '确定要创建账户吗？',
          noSysUserUpdatePermission: '操作失败，无修改系统用户权限！',
          selectDept: '请选择部门',
          selectDataScope: '请选择数据权限',
          selectUserType: '请选择用户类型'
        },
        message: {
          deleteUserNotCreateAccount: '已删除用户不能创建账户',
          noUseUserNotCreateAccount: '未启用用户不能创建账户',
          createAccountSuccess: '创建账户成功',
          deleteValidate: '确定要{msg}该用户吗？',
          noAccount: '该用户未创建账户'
        }
      }
    }
  }
}
