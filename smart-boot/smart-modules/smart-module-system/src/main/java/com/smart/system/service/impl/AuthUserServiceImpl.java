package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableList;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.MaxConnectionAuthenticationException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.model.AuthUser;
import com.smart.auth.core.model.Permission;
import com.smart.auth.core.service.AuthUserService;
import com.smart.auth.extensions.jwt.data.JwtData;
import com.smart.auth.extensions.jwt.store.CacheJwtStore;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.system.constants.FunctionTypeEnum;
import com.smart.system.constants.MaxConnectionsPolicyEnum;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysRolePO;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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

    private final CacheJwtStore cacheJwtStore;

    public AuthUserServiceImpl(SysUserService sysUserService, SysUserAccountService sysAuthUserService, CacheJwtStore cacheJwtStore) {
        this.sysUserService = sysUserService;
        this.sysUserAccountService = sysAuthUserService;
        this.cacheJwtStore = cacheJwtStore;
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

    /**
     * 验证账户
     * @param userAccount 用户账户
     */
    protected boolean validateAccount(@NonNull SysUserPO user, SysUserAccountPO userAccount) {
        if (userAccount == null) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_CREATED));
        }
        // 验证是否长时间未登录
        if (userAccount.getMaxDaysSinceLogin() > 0 && userAccount.getLastLoginTime().plusDays(userAccount.getMaxDaysSinceLogin()).isBefore(LocalDateTime.now())) {
            throw new LongTimeNoLoginLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_LOGIN_LOCKED));
        }
        // 验证是否长时间未修改密码
        if (userAccount.getPasswordLifeDays() > 0 && userAccount.getPasswordModifyTime().plusDays(userAccount.getPasswordLifeDays()).isBefore(LocalDateTime.now())) {
            throw new PasswordNoLifeLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_PASSWORD_NO_MODIFY_LOCKED));
        }
        Long connectionNum = userAccount.getMaxConnections();
        // 验证账户登录数
        if (connectionNum > 0) {
            // 获取当前登录数
            List<JwtData> loginDataList = this.cacheJwtStore.listData(user.getUsername());
            if (loginDataList.size() >= connectionNum) {
                // 登录用户数量已达上限
                MaxConnectionsPolicyEnum maxConnectionsPolicy = userAccount.getMaxConnectionsPolicy();
                if (maxConnectionsPolicy.equals(MaxConnectionsPolicyEnum.LOGIN_NOT_ALLOW)) {
                    throw new MaxConnectionAuthenticationException(I18nUtils.get(AuthI18nMessage.MAX_CONNECTION_LOGIN_FAIL));
                }
                if (maxConnectionsPolicy.equals(MaxConnectionsPolicyEnum.FIRST_USER_LOGOUT)) {
                    // 移除最早登录的用户
                    loginDataList.stream()
                            .min(Comparator.comparing(JwtData::getRefreshTime))
                            .ifPresent(jwtData -> this.cacheJwtStore.invalidateByToken(user.getUsername(), jwtData.getJwt()));
                }
            }
        }

        return true;
    }

    /**
     * 通过系统用户信息创建账户
     * @param user 系统用户
     * @return AuthUser
     */
    protected AuthUser createAuthUser(SysUserPO user) {
        AuthUser authUser = AuthUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .loginFailTime(0L)
                .build();
        // 查询用户账户状态
        SysUserAccountPO userAccount = this.sysUserAccountService.getById(user.getUserId());

        // 验证账户
        boolean result = this.validateAccount(user, userAccount);
        if (!result) {
            return null;
        }

        if (UserAccountStatusEnum.LOGIN_FAIL_LOCKED.equals(userAccount.getAccountStatus())) {
            authUser.setLocked(!this.unLockPasswordErrorLock(userAccount));
        }
        // 设置IP白名单
        authUser.setIpWhiteList(
                Optional.ofNullable(userAccount.getIpWhiteList())
                    .map(
                            item -> Arrays.stream(item.split(";"))
                                    .map(String::trim)
                                    .filter(StringUtils::isNotBlank)
                                    .collect(Collectors.toList())
                    ).orElse(new ArrayList<>(0))
        );
        authUser.setLoginFailTime(userAccount.getLoginFailTime());
        return authUser;
    }

    /**
     * 执行自动解锁账户
     * @param userAccount 用户账户信息
     */
    protected boolean unLockPasswordErrorLock(SysUserAccountPO userAccount) {
        Long unlockSecond = userAccount.getPasswordErrorUnlockSecond();
        if (unlockSecond <= 0) {
            return false;
        }
        LocalDateTime lockTime = userAccount.getLockTime();

        if (LocalDateTime.now().isAfter(lockTime.plusSeconds(unlockSecond))) {
            // 已经达到自动解锁时间，执行解锁
            return this.sysUserAccountService.unlock(userAccount, UserAccountStatusEnum.LOGIN_FAIL_LOCKED);
        }
        return false;
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
