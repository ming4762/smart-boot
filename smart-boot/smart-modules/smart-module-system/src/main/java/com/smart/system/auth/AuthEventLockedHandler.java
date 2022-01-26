package com.smart.system.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.auth.core.event.AuthEventHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.system.constants.UserAccountStatusEnum;
import com.smart.system.model.SysUserAccountPO;
import com.smart.system.model.SysUserPO;
import com.smart.system.service.SysUserAccountService;
import com.smart.system.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
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

    private final AuthProperties properties;

    private final SysUserService sysUserService;

    private final SysUserAccountService sysAuthUserService;

    public AuthEventLockedHandler(AuthProperties properties, SysUserService sysUserService, SysUserAccountService sysAuthUserService) {
        this.properties = properties;
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
        if (authUser != null) {
            if ( authUser.getLoginFailTime() > 0) {
                this.sysAuthUserService.update(
                        new UpdateWrapper<SysUserAccountPO>().lambda()
                                .set(SysUserAccountPO:: getLoginFailTime, 0)
                                .set(SysUserAccountPO :: getLastLoginTime, LocalDateTime.now())
                                .eq(SysUserAccountPO:: getUserId, user.getUserId())
                );
            }
        } else {
            authUser = new SysUserAccountPO();
            authUser.setUserId(user.getUserId());
            authUser.setCreateTime(LocalDateTime.now());
            authUser.setLastLoginTime(LocalDateTime.now());
            authUser.setLoginFailTime(0);
            this.sysAuthUserService.save(authUser);
        }
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
        if (!(event instanceof AuthenticationFailureBadCredentialsEvent)) {
            // 只处理BadCredentialsException
            return;
        }
        // 用户名
        String username = (String) event.getAuthentication().getPrincipal();
        // 通过用户名查询用户ID
        List<SysUserPO> userList = this.sysUserService.list(
                new QueryWrapper<SysUserPO>().lambda()
                .select(SysUserPO :: getUserId)
                .eq(SysUserPO :: getUsername, username)
        );
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }
        SysUserPO user = userList.get(0);
        // 查询用户锁定次数
        SysUserAccountPO authUser = this.sysAuthUserService.getById(user.getUserId());
        if (authUser == null) {
            authUser = new SysUserAccountPO();
            authUser.setUserId(user.getUserId());
            authUser.setLoginFailTime(0);
            authUser.setCreateTime(LocalDateTime.now());
        }
        int time = authUser.getLoginFailTime() + 1;
        if (time >= this.properties.getStatus().getLoginFailLockTime()) {
            // 锁定用户
            authUser.setAccountStatus(UserAccountStatusEnum.LOGIN_FAIL_LOCKED);
        }
        authUser.setLoginFailTime(time);
        // 更新次数
        this.sysAuthUserService.saveOrUpdate(authUser);
    }
}
