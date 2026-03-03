package com.smart.framework.auth.core.authentication;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.authentication.checker.RestUserDetailsChecker;
import com.smart.framework.auth.core.exception.IpBindAuthenticationException;
import com.smart.framework.auth.core.exception.LoginInfoMissAuthenticationException;
import com.smart.framework.auth.core.exception.RestUsernameNotFoundException;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.commons.core.i18n.I18nUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

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
        this.setPreAuthenticationChecks(new RestUserDetailsChecker());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        RestUserDetails user = (RestUserDetails) userDetails;
        final String password = authentication.getCredentials().toString();
        authentication.setDetails(user);

        if (!password.equals(userDetails.getPassword())) {
            logger.debug("登录失败：密码错误");
            throw new BadCredentialsException(I18nUtils.get(AuthI18nMessage.USERNAME_PASSWORD_ERROR, user.getLoginFailTime() + 1));
        }
        List<String> ipWhiteList = user.getIpWhiteList();
        if (!CollectionUtils.isEmpty(ipWhiteList) && !ipWhiteList.contains(user.getLoginIp())) {
            throw new IpBindAuthenticationException(I18nUtils.get(AuthI18nMessage.ACCOUNT_IP_NOT_IN_WHITELIST));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        String password = (String) authentication.getCredentials();
        if (!org.springframework.util.StringUtils.hasText(username) || !org.springframework.util.StringUtils.hasText(password) || NONE_PROVIDED.equals(username)) {
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
        user.setLoginTime(ZonedDateTime.now());
        // 设置权限域
        if (StringUtils.hasText(token.getAuthDomain())) {
            user.setAuthDomains(Set.of(token.getAuthDomain()));
        }
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
