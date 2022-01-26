package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableList;
import com.smart.auth.core.model.AuthUser;
import com.smart.auth.core.model.Permission;
import com.smart.auth.core.service.AuthUserService;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysRolePO;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2020/9/25 17:05
 * @since 1.0
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysUserAccountService;

    public AuthUserServiceImpl(SysUserService sysUserService, SysUserAccountService sysAuthUserService) {
        this.sysUserService = sysUserService;
        this.sysUserAccountService = sysAuthUserService;
    }


    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public AuthUser getByUsername(@NonNull String username) {
        final SysUserPO user = this.sysUserService.getOne(
                new QueryWrapper<SysUserPO>().lambda()
                        .select(
                                SysUserPO :: getUserId,
                                SysUserPO :: getUsername,
                                SysUserPO :: getFullName,
                                SysUserPO :: getPassword,
                                SysUserPO :: getMobile
                        )
                        .eq(SysUserPO :: getUsername, username)
        );
        if (Objects.isNull(user)) {
            return null;
        }
        return this.createAuthUser(user);
    }

    protected AuthUser createAuthUser(SysUserPO user) {
        AuthUser authUser = AuthUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .loginFailTime(0)
                .build();
        // 查询用户账户状态
        SysUserAccountPO userAccount = this.sysUserAccountService.getById(user.getUserId());

        if (userAccount != null) {
            if (UserAccountStatusEnum.LOGIN_FAIL_LOCKED.equals(userAccount.getAccountStatus()) || UserAccountStatusEnum.LONG_TIME_LOCKED.equals(userAccount.getAccountStatus())) {
                authUser.setLocked(true);
            }
            // 验证是否长时间未登录锁定
            // TODO: 未开发
            // 设置登录失败次数
            authUser.setLoginFailTime(userAccount.getLoginFailTime());
        }

        return authUser;
    }

    @Nullable
    @Override
    public AuthUser getByPhone(@NonNull String phone) {
        final List<SysUserPO> userList = this.sysUserService.list(
                new QueryWrapper<SysUserPO>().lambda()
                        .select(
                                SysUserPO :: getUserId,
                                SysUserPO :: getUsername,
                                SysUserPO :: getFullName,
                                SysUserPO :: getPassword,
                                SysUserPO :: getMobile
                        )
                        .eq(SysUserPO :: getMobile, phone)
        );
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        if (userList.size() > 1) {
            // TODO:异常错误信息待优化
            throw new BadCredentialsException("查询到多个用户，请检查手机号是否正确，phone：" + phone);
        }
        final SysUserPO user = userList.get(0);
        return this.createAuthUser(user);
    }

    /**
     * 查询角色列表
     * @param authUser 用户信息
     * @return 角色列表
     */
    @Override
    public Set<String> listRoleCode(@NonNull AuthUser authUser) {
        return this.sysUserService.listRole(authUser.getUserId()).stream()
                .map(SysRolePO::getRoleCode)
                .collect(Collectors.toSet());
    }

    /**
     * 查询权限列表
     * @param authUser 用户信息
     * @return 权限列表
     */
    @Override
    public Set<Permission> listPermission(@NonNull AuthUser authUser) {
        return this.sysUserService.listUserFunction(authUser.getUserId(), ImmutableList.of(FunctionTypeEnum.FUNCTION))
                .stream()
                .map(item -> Permission.builder()
                        .method(item.getHttpMethod())
                        .url(item.getUrl())
                        .authority(item.getPermission())
                        .build())
                .collect(Collectors.toSet());
    }

}
