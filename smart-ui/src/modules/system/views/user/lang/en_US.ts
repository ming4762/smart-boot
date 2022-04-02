
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
          createAccount: 'Create Account'
        },
        validate: {
          username: 'Please enter username',
          fullName: 'Please enter full name',
          email: 'Please enter email',
          mobile: 'Please enter mobile',
          selectUser: 'Please select user',
          sysUserNoDelete: 'System user cannot delete!',
          setUserUseYn: 'Are you sure you want to {userYn} user？',
          createAccountConfirm: 'Are you sure you want to create account?'
        },
        message: {
          deleteUserNotCreateAccount: 'Deleted user cannot create account',
          noUseUserNotCreateAccount: 'Disabled user cannot create account',
          createAccountSuccess: 'Account created successfully'
        }
      }
    }
  }
}
