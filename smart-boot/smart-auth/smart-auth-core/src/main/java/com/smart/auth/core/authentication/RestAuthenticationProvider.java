package com.smart.auth.core.authentication;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.exception.IpBindAuthenticationException;
import com.smart.auth.core.exception.LoginInfoMissAuthenticationException;
import com.smart.auth.core.exception.RestUsernameNotFoundException;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.model.RestUserDetailsImpl;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.commons.core.i18n.I18nUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 登录管理
 * @author shizhongming
 * 2020/1/23 8:17 下午
 */
public class RestAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements InitializingBean {


    private static final String NONE_PROVIDED = "NONE_PROVIDED";

    private final UserDetailsService restUserDetailsService;


    public RestAuthenticationProvider(UserDetailsService restUserDetailsService) {
        this.restUserDetailsService = restUserDetailsService;
        this.setPreAuthenticationChecks(new RestPreUserDetailsChecker());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        RestUserDetails user = (RestUserDetails) userDetails;
        final String password = authentication.getCredentials().toString();
        if (!StringUtils.equals(userDetails.getPassword(), password)) {
            logger.debug("登录失败：密码错误");
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.USERNAME_PASSWORD_ERROR, user.getLoginFailTime() + 1));
        }
        List<String> ipWhiteList = user.getIpWhiteList();
        if (CollectionUtils.isNotEmpty(ipWhiteList) && !ipWhiteList.contains(user.getLoginIp())) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ACCOUNT_IP_NOT_IN_WHITELIST));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        String password = (String) authentication.getCredentials();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.equals(NONE_PROVIDED, username)) {
            throw new LoginInfoMissAuthenticationException(I18nUtils.get(AuthI18nMessage.USERNAME_PASSWORD_NULL));
        }
        RestUserDetailsImpl user = (RestUserDetailsImpl) this.restUserDetailsService.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new RestUsernameNotFoundException(I18nUtils.get(AuthI18nMessage.USER_NOT_FOUND_ERROR));
        }
        RestUsernamePasswordAuthenticationToken token = (RestUsernamePasswordAuthenticationToken) authentication;
        user.setLoginType(token.getLoginType());
        user.setAuthType(AuthTypeEnum.USERNAME);
        user.setBindIp(token.getBindIp());
        user.setLoginIp(token.getLoginIp());
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 验证用户状态
     */
    private static class RestPreUserDetailsChecker implements UserDetailsChecker {

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                throw new LockedException(I18nUtils.get(AuthI18nMessage.ACCOUNT_LOCKED));
            }
            if (!user.isEnabled()) {
                throw new DisabledException(I18nUtils.get(AuthI18nMessage.ACCOUNT_DISABLED));
            }
            if (!user.isAccountNonExpired()) {
                throw new AccountExpiredException(I18nUtils.get(AuthI18nMessage.ACCOUNT_EXPIRED));
            }
        }
    }
}
