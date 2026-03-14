package com.smart.framework.auth.core.userdetails;

import com.smart.framework.auth.common.userdetails.PermissionGrantedAuthority;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.userdetails.RestUserDetailsImpl;
import com.smart.framework.auth.common.userdetails.RoleGrantedAuthority;
import com.smart.framework.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.framework.auth.core.exception.MaxConnectionAuthenticationException;
import com.smart.framework.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.token.CompositeSmartTokenRepository;
import com.smart.framework.auth.core.token.TokenCacheData;
import com.smart.framework.commons.core.dto.auth.MaxConnectionsPolicyEnum;
import com.smart.framework.commons.core.dto.auth.UserAccountDTO;
import com.smart.framework.commons.core.dto.auth.UserAccountData;
import com.smart.framework.commons.core.dto.auth.UserAccountStatusEnum;
import com.smart.framework.commons.core.i18n.I18nUtils;
import com.smart.framework.commons.core.tenant.SmartTenantHolder;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.dto.QueryUserAccountDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.util.CollectionUtils;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2024/4/9 9:56
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class DefaultUserDetailsBuilderImpl implements UserDetailsBuilder {

    private final SystemAuthUserApi systemAuthUserApi;
    private final CompositeSmartTokenRepository tokenRepository;
    private final AuthProperties authProperties;

    /**
     * 构建 RestUserDetails
     *
     * @param user 用户信息
     * @return RestUserDetails
     */
    @Override
    public RestUserDetails buildUserDetails(@Nullable AuthUserDTO user) {
        if (user == null) {
            return null;
        }
        boolean enabled = Boolean.TRUE.equals(user.getUseYn());
        RestUserDetailsImpl.RestUserDetailsImplBuilder userDetailsImplBuilder = RestUserDetailsImpl.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .password(user.getPassword())
                .enabled(enabled);
        if (!enabled) {
            // 如果用户已停用，直接返回，提升性能
            return userDetailsImplBuilder.build();
        }
        UserAccountData userAccountData = this.systemAuthUserApi.queryUserAccount(new QueryUserAccountDTO(user.getUserId(), SmartTenantHolder.getTenantId()));
        if (userAccountData == null) {
            return null;
        }
        // 验证账户
        boolean validateAccount = this.validateAccount(user, userAccountData);
        if (!validateAccount) {
            return null;
        }
        UserAccountDTO userAccount = userAccountData.getAccount();

        RestUserDetailsImpl restUserDetails = userDetailsImplBuilder
                .loginFailTime(userAccount.getLoginFailTime())
                .ipWhiteList(
                        Optional.ofNullable(userAccount.getIpWhiteList())
                                .map(
                                        item -> Arrays.stream(item.split(";"))
                                                .map(String::trim)
                                                .filter(org.springframework.util.StringUtils::hasText)
                                                .toList()
                                ).orElse(new ArrayList<>(0))
                )
                .accountNonLocked(UserAccountStatusEnum.NORMAL.equals(userAccount.getAccountStatus()))
                // 设置JWT模式下是否开启权限缓存
                .permissionCache(Boolean.TRUE.equals(this.authProperties.getJwt().getPermissionCache()))
                .build();

        // 设置账户锁定状态
        if (UserAccountStatusEnum.LOGIN_FAIL_LOCKED.equals(userAccount.getAccountStatus())) {
            // 用户登录失败锁定执行解锁策略
            restUserDetails.setAccountNonLocked(this.unLockPasswordErrorLock(user, userAccountData));
        }
        // 添加角色
        restUserDetails.setRoles(
                userAccountData.getRoleCodes().stream()
                        .map(RoleGrantedAuthority::new).collect(Collectors.toSet())
        );
        // 添加权限
        restUserDetails.setPermissions(
                userAccountData.getPermissions().stream()
                        .map(PermissionGrantedAuthority::new).collect(Collectors.toSet())
        );

        // 设置租户信息
        restUserDetails.setUserTenant(userAccountData.getTenant());
        if (!CollectionUtils.isEmpty(user.getAuthDomainList())) {
            restUserDetails.setUserAuthDomains(user.getAuthDomainList().stream().map(Enum::name).collect(Collectors.toSet()));
        }
        return restUserDetails;
    }

    protected boolean unLockPasswordErrorLock(AuthUserDTO user, UserAccountData userAccountData) {
        UserAccountDTO userAccount = userAccountData.getAccount();
        Long unlockSecond = userAccount.getPasswordErrorUnlockSecond();
        if (unlockSecond <= 0) {
            // 未设置自动解锁时间
            return false;
        }
        ZonedDateTime lockTime = userAccount.getLockTime();
        if (ZonedDateTime.now().isAfter(lockTime.plusSeconds(unlockSecond))) {
            return this.systemAuthUserApi.unlockAccount(new UserAccountUnLockParameter(user.getUserId(), UserAccountStatusEnum.LOGIN_FAIL_LOCKED));
        }
        return false;
    }

    /**
     * 验证用户账户
     * @param user 用户信息
     * @return 是否正常成功
     */
    protected boolean validateAccount(AuthUserDTO user, UserAccountData userAccountData) {
        if (Boolean.FALSE.equals(userAccountData.getTenant().getUseYn())) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_DISABLED));
        }
        UserAccountDTO userAccount = userAccountData.getAccount();
        if (userAccount == null) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_CREATED));
        }
        // 验证是否长时间未登录
        if (userAccount.getMaxDaysSinceLogin() > 0 && userAccount.getLastLoginTime().plusDays(userAccount.getMaxDaysSinceLogin()).isBefore(ZonedDateTime.now())) {
            throw new LongTimeNoLoginLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_LOGIN_LOCKED), new LongTimeNoLoginLockedException.User(user.getUserId(), user.getUsername(), user.getFullName()));
        }
        // 验证是否长时间未修改密码
        if (userAccount.getPasswordLifeDays() > 0 && userAccount.getPasswordModifyTime().plusDays(userAccount.getPasswordLifeDays()).isBefore(ZonedDateTime.now())) {
            throw new PasswordNoLifeLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_PASSWORD_NO_MODIFY_LOCKED), new PasswordNoLifeLockedException.User(user.getUserId(), user.getUsername(), user.getFullName()));
        }
        // 验证用户登录数
        this.loginConnectionNum(user, userAccountData);
        return true;
    }

    /**
     * 处理用户连接数
     * 超出连接数的按照策略进行处理
     * @param user 用户信息
     */
    protected void loginConnectionNum(AuthUserDTO user, UserAccountData userAccountData) {
        UserAccountDTO userAccount = userAccountData.getAccount();
        Long connectionNum = userAccount.getMaxConnections();
        if (connectionNum <= 0) {
            return;
        }
        List<TokenCacheData> tokenDataList = this.tokenRepository.listToken(user.getUsername(), userAccountData.getTenant().getTenantId());
        if (tokenDataList.size() < connectionNum) {
            // 未达到连接数上限
            return;
        }
        MaxConnectionsPolicyEnum maxConnectionsPolicy = userAccount.getMaxConnectionsPolicy();
        if (MaxConnectionsPolicyEnum.LOGIN_NOT_ALLOW.equals(maxConnectionsPolicy)) {
            // 达到最大连接数不允许登录
            throw new MaxConnectionAuthenticationException(I18nUtils.get(AuthI18nMessage.MAX_CONNECTION_LOGIN_FAIL));
        }
        if (MaxConnectionsPolicyEnum.FIRST_USER_LOGOUT.equals(maxConnectionsPolicy)) {
            // 最早刷新token的用户执行登出操作
            tokenDataList.stream()
                    .min(Comparator.comparing(TokenCacheData::getRefreshTime))
                    .ifPresent(tokenData -> this.tokenRepository.invalidateByToken(tokenData.getToken()));
        }
    }
}
