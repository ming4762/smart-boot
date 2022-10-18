
export default {
  system: {
    views: {
      user: {
        form: {
          systemUser: 'System user',
          businessUser: 'Business user'
        },
        table: {
          username: 'Username',
          fullName: 'Full name',
          email: 'Email',
          mobile: 'Mobile',
          userType: 'User type'
        },
        button: {
          createAccount: 'Create account',
          showAccount: 'Account info'
        },
        account: {
          title: 'Account info',
          accountSet: 'Account setting',
          createTime: 'Account create Time',
          accountStatus: 'Status',
          passwordModifyTime: 'Password modify time',
          lockTime: 'Locked time',
          loginFailTime: 'Login fail number',
          initialPasswordYn: 'Initial Password',

          ipWhiteList: 'IP white list',
          maxConnections: 'Maximum access connections',
          maxDaysSinceLogin: 'Maximum days since login',
          passwordLifeDays: 'Password life days',
          maxConnectionsPolicy: 'Maximum connections policy',
          loginFailTimeLimit: 'Login fail limit',
          passwordErrorUnlockSecond: 'Password error unlock',

          loginNotAllow: 'Login not allowed',
          firstUserLogout: 'Earliest login user logout',

          ipWhiteListPlaceholder: 'IP white list, multiple IPS separated by commas'
        },
        validate: {
          username: 'Please enter username',
          fullName: 'Please enter full name',
          email: 'Please enter email',
          mobile: 'Please enter mobile',
          selectUser: 'Please select user',
          sysUserNoDelete: 'System user cannot delete!',
          setUserUseYn: 'Are you sure you want to {msg} user？',
          createAccountConfirm: 'Are you sure you want to create account?'
        },
        message: {
          deleteUserNotCreateAccount: 'Deleted user cannot create account',
          noUseUserNotCreateAccount: 'Disabled user cannot create account',
          createAccountSuccess: 'Account created successfully',
          deleteValidate: 'Are you sure you want to {msg} this user?',
          noAccount: 'The user has not created an account'
        }
      }
    }
  }
}
