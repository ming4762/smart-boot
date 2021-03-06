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
     * ???????????????????????????
     * @param username ?????????
     * @return ????????????
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
     * ????????????
     * @param userAccount ????????????
     */
    protected boolean validateAccount(@NonNull SysUserPO user, SysUserAccountPO userAccount) {
        if (userAccount == null) {
            throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_CREATED));
        }
        // ??????????????????????????????
        if (userAccount.getMaxDaysSinceLogin() > 0 && userAccount.getLastLoginTime().plusDays(userAccount.getMaxDaysSinceLogin()).isBefore(LocalDateTime.now())) {
            throw new LongTimeNoLoginLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_NOT_LOGIN_LOCKED));
        }
        // ????????????????????????????????????
        if (userAccount.getPasswordLifeDays() > 0 && userAccount.getPasswordModifyTime().plusDays(userAccount.getPasswordLifeDays()).isBefore(LocalDateTime.now())) {
            throw new PasswordNoLifeLockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_PASSWORD_NO_MODIFY_LOCKED));
        }
        Long connectionNum = userAccount.getMaxConnections();
        // ?????????????????????
        if (connectionNum > 0) {
            // ?????????????????????
            List<JwtData> loginDataList = this.cacheJwtStore.listData(user.getUsername());
            if (loginDataList.size() >= connectionNum) {
                // ??????????????????????????????
                MaxConnectionsPolicyEnum maxConnectionsPolicy = userAccount.getMaxConnectionsPolicy();
                if (maxConnectionsPolicy.equals(MaxConnectionsPolicyEnum.LOGIN_NOT_ALLOW)) {
                    throw new MaxConnectionAuthenticationException(I18nUtils.get(AuthI18nMessage.MAX_CONNECTION_LOGIN_FAIL));
                }
                if (maxConnectionsPolicy.equals(MaxConnectionsPolicyEnum.FIRST_USER_LOGOUT)) {
                    // ???????????????????????????
                    loginDataList.stream()
                            .min(Comparator.comparing(JwtData::getRefreshTime))
                            .ifPresent(jwtData -> this.cacheJwtStore.invalidateByToken(user.getUsername(), jwtData.getJwt()));
                }
            }
        }

        return true;
    }

    /**
     * ????????????????????????????????????
     * @param user ????????????
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
        // ????????????????????????
        SysUserAccountPO userAccount = this.sysUserAccountService.getById(user.getUserId());

        // ????????????
        boolean result = this.validateAccount(user, userAccount);
        if (!result) {
            return null;
        }

        if (UserAccountStatusEnum.LOGIN_FAIL_LOCKED.equals(userAccount.getAccountStatus())) {
            authUser.setLocked(!this.unLockPasswordErrorLock(userAccount));
        }
        // ??????IP?????????
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
     * ????????????????????????
     * @param userAccount ??????????????????
     */
    protected boolean unLockPasswordErrorLock(SysUserAccountPO userAccount) {
        Long unlockSecond = userAccount.getPasswordErrorUnlockSecond();
        if (unlockSecond <= 0) {
            return false;
        }
        LocalDateTime lockTime = userAccount.getLockTime();

        if (LocalDateTime.now().isAfter(lockTime.plusSeconds(unlockSecond))) {
            // ?????????????????????????????????????????????
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
            // TODO:???????????????????????????
            throw new BadCredentialsException("?????????????????????????????????????????????????????????phone???" + phone);
        }
        final SysUserPO user = userList.get(0);
        return this.createAuthUser(user);
    }

    /**
     * ??????????????????
     * @param authUser ????????????
     * @return ????????????
     */
    @Override
    public Set<String> listRoleCode(@NonNull AuthUser authUser) {
        return this.sysUserService.listRole(authUser.getUserId()).stream()
                .map(SysRolePO::getRoleCode)
                .collect(Collectors.toSet());
    }

    /**
     * ??????????????????
     * @param authUser ????????????
     * @return ????????????
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
