package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author ShiZhongMing
 * 2020/9/25 17:05
 * @since 1.0
 */
@Service
@Primary
public class LocalSystemAuthUserApiImpl implements SystemAuthUserApi {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysUserAccountService;

    public LocalSystemAuthUserApiImpl(SysUserService sysUserService, SysUserAccountService sysAuthUserService) {
        this.sysUserService = sysUserService;
        this.sysUserAccountService = sysAuthUserService;
    }


    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public AuthUserDTO getByUsername(@NonNull String username) {
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

    /**
     * 通过系统用户信息创建账户
     * @param user 系统用户
     * @return AuthUser
     */
    protected AuthUserDTO createAuthUser(SysUserPO user) {
        AuthUserDTO dto = AuthUserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .mobile(user.getMobile())
                .build();
        // 查询用户账户状态
        SysUserAccountPO userAccount = this.sysUserAccountService.getById(user.getUserId());
        if (userAccount != null) {
            AuthUserDTO.UserAccountDTO account = new AuthUserDTO.UserAccountDTO();
            BeanUtils.copyProperties(userAccount, account);
            dto.setAccount(account);
        }
        return dto;
    }

    @Nullable
    @Override
    public AuthUserDTO getByMobile(@NonNull String mobile) {
        final SysUserPO sysUser = this.sysUserService.getOne(
                new QueryWrapper<SysUserPO>().lambda()
                        .select(
                                SysUserPO :: getUserId,
                                SysUserPO :: getUsername,
                                SysUserPO :: getFullName,
                                SysUserPO :: getPassword,
                                SysUserPO :: getMobile
                        )
                        .eq(SysUserPO :: getMobile, mobile)
        );
        if (sysUser == null) {
            return null;
        }
        return this.createAuthUser(sysUser);
    }

    /**
     * 查询用户角色权限信息
     * @param userId 用户ID
     * @return 权限角色信息
     */
    @Override
    public UserRolePermission queryRolePermission(@NonNull Long userId) {
        return this.sysUserService.queryUserRolePermission(userId, List.of(FunctionTypeEnum.FUNCTION));
    }

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByAppOpenid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 通过unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByUnionid(WechatUserQueryParameter parameter) {
        return null;
    }

    /**
     * 结果用户账户
     *
     * @param parameter 参数
     * @return 是否结果成功
     */
    @Override
    public boolean unlockAccount(UserAccountUnLockParameter parameter) {
        return this.sysUserAccountService.unlock(parameter.getUserId(), parameter.getAccountStatus());
    }
}
