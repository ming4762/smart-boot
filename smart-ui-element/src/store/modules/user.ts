import { defineStore } from 'pinia'
import { UserInfo } from '@/types/store'
import { PERMISSION_KEY, ROLES_KEY, TOKEN_KEY, USER_INFO_KEY } from '@/enums/cacheEnum'
import { loginOutApi } from '@/api/login'
import router from '@/router'
import { PageEnum } from '@/enums/pageEnum'
import { useCache } from '@/hooks/web/useCache'

interface UserState {
  userInfo: Nullable<UserInfo>
  token?: string
  roleList: string[]
  permissionList: string[]
}

const { wsCache } = useCache()

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    token: undefined,
    roleList: [],
    permissionList: [],
  }),
  getters: {
    getUserInfo(): UserInfo | undefined {
      return this.userInfo || wsCache.get(USER_INFO_KEY) || undefined
    },
    getToken(): string {
      return this.token || wsCache.get(TOKEN_KEY)
    },
    getRoleList(): string[] {
      return (this.roleList.length > 0 ? this.roleList : wsCache.get(ROLES_KEY)) || []
    },
  },
  actions: {
    setToken(token: string | undefined) {
      this.token = token ? token : '' // for null or undefined value
      wsCache.set(TOKEN_KEY, token)
    },
    setRoleList(roleList: string[]) {
      this.roleList = roleList
      wsCache.set(ROLES_KEY, roleList)
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info
      wsCache.set(USER_INFO_KEY, info)
    },
    setPermissionList(permissionList: string[]) {
      this.permissionList = permissionList
      wsCache.set(PERMISSION_KEY, permissionList)
    },
    /**
     * 登出操作
     * @param goLogin
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await loginOutApi()
        } catch (e) {
          console.log('注销Token失败')
        }
      }
      this.setToken(undefined)
      this.setUserInfo(null)
      wsCache.clear()
      router.push(PageEnum.BASE_LOGIN)
    },
  },
})
