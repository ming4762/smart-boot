package com.smart.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.DingtalkUserQueryParameter;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import com.smart.module.api.system.parameter.WechatUserQueryParameter;
import com.smart.module.system.constants.SysThirdPlatformTypeEnum;
import com.smart.module.system.model.SysUserPO;
import com.smart.module.system.model.SysUserThirdAccountPO;
import com.smart.module.system.service.SysUserAccountService;
import com.smart.module.system.service.SysUserService;
import com.smart.module.system.service.SysUserThirdAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author ShiZhongMing
 * 2020/9/25 17:05
 * @since 1.0
 */
@Service
@Primary
@RequiredArgsConstructor
public class LocalSystemAuthUserApiImpl implements SystemAuthUserApi {

    private final SysUserService sysUserService;
    private final SysUserAccountService sysUserAccountService;
    private final SysUserThirdAccountService sysUserThirdAccountService;

    /**
     * 通过用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public AuthUserDTO getByUsername(@NonNull String username) {
        final SysUserPO user = this.sysUserService.getOne(
                this.buildCommonQuery()
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
        return AuthUserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .mobile(user.getMobile())
                .useYn(user.getUseYn())
                .build();
    }

    @Nullable
    @Override
    public AuthUserDTO getByMobile(@NonNull String mobile) {
        final SysUserPO sysUser = this.sysUserService.getOne(
                this.buildCommonQuery()
                        .eq(SysUserPO :: getMobile, mobile)
        );
        if (sysUser == null) {
            return null;
        }
        return this.createAuthUser(sysUser);
    }

    /**
     * 查询用户角色权限信息
     * @param parameter 参数
     * @return 权限角色信息
     */
    @Override
    public UserAccountData queryUserAccount(@NonNull QueryUserAccountDTO parameter) {
        return this.sysUserService.queryUserAccount(parameter);
    }

    /**
     * 通过openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByWehchatAppOpenid(WechatUserQueryParameter parameter) {
        SysUserThirdAccountPO userThirdAccount = this.sysUserThirdAccountService.lambdaQuery()
                .eq(SysUserThirdAccountPO::getPlatformType, SysThirdPlatformTypeEnum.WECHAT)
                .eq(SysUserThirdAccountPO::getOpenid, parameter.getOpenid())
                .eq(SysUserThirdAccountPO::getAppid, parameter.getAppid())
                .one();
        if (Objects.isNull(userThirdAccount)) {
            return null;
        }
        SysUserPO sysUser = this.sysUserService.getById(userThirdAccount.getUserId());
        return this.createAuthUser(sysUser);
    }

    /**
     * 通过unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByWechatUnionid(WechatUserQueryParameter parameter) {
        SysUserPO sysUser = this.sysUserService.getOne(
                this.buildCommonQuery()
                        .eq(SysUserPO::getWechatUnionId, parameter.getUnionid())
        );
        if (sysUser == null) {
            return null;
        }
        return this.createAuthUser(sysUser);
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

    /**
     * 通过钉钉openid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByDingtalkOpenId(DingtalkUserQueryParameter parameter) {
        throw new UnsupportedOperationException("getByDingtalkOpenId not supported");
    }

    /**
     * 通过钉钉unionid获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByDingtalkUnionId(DingtalkUserQueryParameter parameter) {
        SysUserPO sysUser = this.sysUserService.getOne(
                this.buildCommonQuery()
                        .eq(SysUserPO::getDingtalkUnionId, parameter.getUnionId())
        );
        if (sysUser == null) {
            return null;
        }
        return this.createAuthUser(sysUser);
    }

    /**
     * 通过钉钉手机号获取用户信息
     *
     * @param parameter 参数
     * @return AuthUser
     */
    @Override
    public AuthUserDTO getByDingtalkMobile(DingtalkUserQueryParameter parameter) {
        return this.getByMobile(parameter.getMobile());
    }

    /**
     * 构建公共查询
     * @return LambdaQueryWrapper
     */
    protected LambdaQueryWrapper<SysUserPO> buildCommonQuery() {
        return Wrappers.lambdaQuery(SysUserPO.class)
                .select(
                        SysUserPO::getUserId,
                        SysUserPO::getUsername,
                        SysUserPO::getFullName,
                        SysUserPO::getPassword,
                        SysUserPO::getMobile,
                        SysUserPO::getUseYn
                );
    }
}
