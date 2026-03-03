package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.authentication.checker.RestUserDetailsChecker;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * 认证检查提供器抽象类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-02 22:06
 * @since 5.0.0
 */
public abstract class AbstractAuthenticationCheckProvider implements AuthenticationProvider {

    @Setter
    private UserDetailsChecker userDetailsChecker;

    protected AbstractAuthenticationCheckProvider() {
        this.userDetailsChecker = new RestUserDetailsChecker();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RestUserDetails userDetails = this.retrieveUser(authentication);
        // 校验用户
        this.userDetailsChecker.check(userDetails);
        return this.createSuccessAuthentication(authentication, userDetails);
    }

    /**
     * 获取用户信息
     * @param authentication 认证信息
     * @return 用户信息
     */
    protected abstract RestUserDetails retrieveUser(Authentication authentication);

    /**
     * 创建成功的认证信息
     * @param authentication 登录认证信息
     * @param userDetails 用户信息
     * @return 成功的认证信息
     */
    protected abstract Authentication createSuccessAuthentication(Authentication authentication, RestUserDetails userDetails);

}
