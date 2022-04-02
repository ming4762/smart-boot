package com.smart.system.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.exception.LongTimeNoLoginLockedException;
import com.smart.auth.core.exception.PasswordNoLifeLockedException;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录失败锁定接口
 * @author ShiZhongMing
 * 2021/12/31
 * @since 1.0
 */
public class AuthEventLockedHandler implements AuthEventHandler {

    private final SysUserService sysUserService;

    private final SysUserAccountService sysAuthUserService;

    public AuthEventLockedHandler(SysUserService sysUserService, SysUserAccountService sysAuthUserService) {
        this.sysUserService = sysUserService;
        this.sysAuthUserService = sysAuthUserService;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 登录成功事件监听
     * 登录成功后,重置登录失败次数
     * @param event 事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleLoginSuccess(AuthenticationSuccessEvent event) {
        RestUserDetails user = (RestUserDetails) event.getAuthentication().getPrincipal();
        // 查询用户账户信息
        SysUserAccountPO authUser = this.sysAuthUserService.getById(user.getUserId());
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO:: getUserId, user.getUserId())
                .set(SysUserAccountPO :: getLastLoginTime, LocalDateTime.now());
        if (authUser.getLoginFailTime() > 0) {
            updateWrapper.set(SysUserAccountPO:: getLoginFailTime, 0L);
        }
        this.sysAuthUserService.update(updateWrapper);
    }

    /**
     * 登录失败事件监听
     * 触发BadCredentialsException会增加登录失败次数
     * 触发阈值会锁定用户
     * @param event 事件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleLoginFail(AbstractAuthenticationFailureEvent event) {
        if (event instanceof AuthenticationFailureLockedEvent) {
            this.handleLocked((AuthenticationFailureLockedEvent) event);
            return;
        }
        if (!(event instanceof AuthenticationFailureBadCredentialsEvent)) {
            // 只处理BadCredentialsException
            return;
        }
        SysUserAccountPO authUser = this.getUserAccount((String) event.getAuthentication().getPrincipal());
        if (authUser == null) {
            return;
        }
        Long time = authUser.getLoginFailTime() + 1;
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO :: getUserId, authUser.getUserId())
                .set(SysUserAccountPO :: getLoginFailTime, time);

        if (time > authUser.getLoginFailTimeLimit()) {
            // 锁定用户
            updateWrapper.set(SysUserAccountPO :: getAccountStatus, UserAccountStatusEnum.LOGIN_FAIL_LOCKED);
        }
        this.sysAuthUserService.update(updateWrapper);
    }

    /**
     * 登录锁定
     * @param event 事件
     */
    private void handleLocked(AuthenticationFailureLockedEvent event) {
        AuthenticationException exception = event.getException();
        // 用户名
        SysUserAccountPO userAccount = this.getUserAccount((String) event.getAuthentication().getPrincipal());
        if (userAccount == null) {
            return;
        }
        LambdaUpdateWrapper<SysUserAccountPO> updateWrapper = new UpdateWrapper<SysUserAccountPO>().lambda()
                .eq(SysUserAccountPO :: getUserId, userAccount.getUserId());
        if (exception instanceof LongTimeNoLoginLockedException) {
            // 长时间未登录锁定
            updateWrapper.set(SysUserAccountPO::getAccountStatus, UserAccountStatusEnum.LONG_TIME_LOCKED);
        } else if (exception instanceof PasswordNoLifeLockedException) {
            updateWrapper.set(SysUserAccountPO::getAccountStatus, UserAccountStatusEnum.LONG_TIME_PASSWORD_MODIFY_LOCKED);
        }
        this.sysAuthUserService.update(updateWrapper);
    }

    private SysUserAccountPO getUserAccount(String username) {
        // 通过用户名查询用户ID
        List<SysUserPO> userList = this.sysUserService.list(
                new QueryWrapper<SysUserPO>().lambda()
                        .select(SysUserPO :: getUserId)
                        .eq(SysUserPO :: getUsername, username)
        );
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        }
        SysUserPO user = userList.get(0);
        // 查询用户锁定次数
        return this.sysAuthUserService.getById(user.getUserId());
    }
}
