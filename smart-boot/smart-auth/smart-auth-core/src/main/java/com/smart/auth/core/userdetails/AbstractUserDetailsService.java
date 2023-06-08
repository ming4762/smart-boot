package com.smart.auth.core.userdetails;

import com.google.common.collect.Sets;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.MaxConnectionAuthenticationException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.model.PermissionGrantedAuthority;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.model.RoleGrantedAuthority;
import com.smart.auth.core.model.SmartGrantedAuthority;
import com.smart.auth.core.token.TokenData;
import com.smart.auth.core.token.TokenRepository;
import com.smart.commons.core.dto.auth.UserRolePermission;
import com.smart.commons.core.i18n.I18nUtils;
import com.smart.module.api.system.SystemAuthUserApi;
import com.smart.module.api.system.constants.MaxConnectionsPolicyEnum;
import com.smart.module.api.system.constants.UserAccountStatusEnum;
import com.smart.module.api.system.dto.AuthUserDTO;
import com.smart.module.api.system.parameter.UserAccountUnLockParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.DisabledException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/6/7 14:38
 */
public abstract class AbstractUserDetailsService {

    private final List<TokenRepository> tokenRepositoryList;

    private final SystemAuthUserApi systemAuthUserApi;

    protected AbstractUserDetailsService(List<TokenRepository> tokenRepositoryList, SystemAuthUserApi systemAuthUserApi) {
        this.tokenRepositoryList = tokenRepositoryList;
        this.systemAuthUserApi = systemAuthUserApi;
    }

    /**
     * 创建 RestUserDetails
     * @param user 用户信息
     * @return RestUserDetails
     */
    protected RestUserDetails getUserDetails(@Nullable AuthUserDTO user) {
        if (user == null) {
            return null;
        }
        // 验证账户
        boolean validateAccount = this.validateAccount(user);
        if (!validateAccount) {
            return null;
        }
        AuthUserDTO.UserAccountDTO userAccount = user.getAccount();

        RestUserDetailsImpl restUserDetails = new RestUserDetailsImpl();
        restUserDetails.setUserId(user.getUserId());
        restUserDetails.setUsername(user.getUsername());
        restUserDetails.setFullName(user.getFullName());
        restUserDetails.setPassword(user.getPassword());
        restUserDetails.setLoginFailTime(userAccount.getLoginFailTime());
        // IP白名单
        restUserDetails.setIpWhiteList(
                Optional.ofNullable(userAccount.getIpWhiteList())
                        .map(
                                item -> Arrays.stream(item.split(";"))
                                        .map(String::trim)
                                        .filter(StringUtils::isNotBlank)
                                        .toList()
                        ).orElse(new ArrayList<>(0))
        );
        // 设置账户锁定状态
        restUserDetails.setAccountNonLocked(UserAccountStatusEnum.NORMAL.equals(userAccount.getAccountStatus()));
        if (UserAccountStatusEnum.LOGIN_FAIL_LOCKED.equals(userAccount.getAccountStatus())) {
            // 用户登录失败锁定执行解锁策略
            restUserDetails.setAccountNonLocked(this.unLockPasswordErrorLock(user));
        }

        // 设置权限信息
        Set<SmartGrantedAuthority> grantedAuthoritySet = Sets.newHashSet();
        UserRolePermission userRolePermission = this.systemAuthUserApi.queryRolePermission(user.getUserId());
        // 添加角色
        grantedAuthoritySet.addAll(
                userRolePermission.getRoleCodes().stream()
                        .map(RoleGrantedAuthority::new).collect(Collectors.toSet())
        );
        // 添加权限
        grantedAuthoritySet.addAll(
                userRolePermission.getPermissions().stream()
                        .map(PermissionGrantedAuthority::new).toList()
        );
        restUserDetails.setAuthorities(grantedAuthoritySet);
        return restUserDetails;
    }

    protected boolean unLockPasswordErrorLock(AuthUserDTO user) {
        AuthUserDTO.UserAccountDTO userAccount = user.getAccount();
        Long unlockSecond = userAccount.getPasswordErrorUnlockSecond();
        if (unlockSecond <= 0) {
            // 未设置自动解锁时间
            return false;
        }
        LocalDateTime lockTime = userAccount.getLockTime();
        if (LocalDateTime.now().isAfter(lockTime.plusSeconds(unlockSecond))) {
            return this.systemAuthUserApi.unlockAccount(new UserAccountUnLockParameter(user.getUserId(), UserAccountStatusEnum.LOGIN_FAIL_LOCKED));
        }
        return false;
    }

    /**
     * 验证用户账户
     * @param user 用户信息
     * @return 是否正常成功
     */
    protected boolean validateAccount(AuthUserDTO user) {
        AuthUserDTO.UserAccountDTO userAccount = user.getAccount();
        if (userAccount == null) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_CREATED));
        }
        // 验证是否长时间未登录
        if (userAccount.getMaxDaysSinceLogin() > 0 && userAccount.getLastLoginTime().plusDays(userAccount.getMaxDaysSinceLogin()).isBefore(LocalDateTime.now())) {
            throw new LongTimeNoLoginLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_LOGIN_LOCKED), new LongTimeNoLoginLockedException.User(user.getUserId(), user.getUsername(), user.getFullName()));
        }
        // 验证是否长时间未修改密码
        if (userAccount.getPasswordLifeDays() > 0 && userAccount.getPasswordModifyTime().plusDays(userAccount.getPasswordLifeDays()).isBefore(LocalDateTime.now())) {
            throw new PasswordNoLifeLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_PASSWORD_NO_MODIFY_LOCKED), new PasswordNoLifeLockedException.User(user.getUserId(), user.getUsername(), user.getFullName()));
        }
        // 验证用户登录数
        this.loginConnectionNum(user);
        return true;
    }

    /**
     * 处理用户连接数
     * 超出连接数的按照策略进行处理
     * @param user 用户信息
     */
    protected void loginConnectionNum(AuthUserDTO user) {
        AuthUserDTO.UserAccountDTO userAccount = user.getAccount();
        Long connectionNum = userAccount.getMaxConnections();
        if (connectionNum <= 0) {
            return;
        }
        List<TokenData> tokenDataList = this.tokenRepositoryList.stream()
                .flatMap(item -> item.listData(user.getUsername()).stream())
                .toList();
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
                    .min(Comparator.comparing(TokenData::getRefreshTime))
                    .ifPresent(tokenData -> this.tokenRepositoryList.forEach(item -> item.invalidateByToken(tokenData.getToken())));
        }
    }

}
