package com.smart.framework.auth.core.remember;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.constants.LoginTypeEnum;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.commons.core.utils.IpUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.time.ZonedDateTime;

/**
 * @author shizhongming
 * 2024/5/22 17:21
 * @since 3.0.0
 */
public class SmartAuthPersistentTokenRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private static final String REMEMBER_ME_LOG_URL = "/auth/rememberLogin";

    private Boolean useSecureCookie = null;

    private String cookieDomain;

    public SmartAuthPersistentTokenRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }


    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(getCookieName(), cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setPath(doGetCookiePath(request));
        if (this.cookieDomain != null) {
            cookie.setDomain(this.cookieDomain);
        }
        cookie.setSecure((this.useSecureCookie != null) ? this.useSecureCookie : request.isSecure());
        cookie.setHttpOnly(false);
        response.addCookie(cookie);
    }

    private String doGetCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return (!contextPath.isEmpty()) ? contextPath : "/";
    }


    @Override
    public void setUseSecureCookie(boolean useSecureCookie) {
        super.setUseSecureCookie(useSecureCookie);
        this.useSecureCookie = useSecureCookie;
    }


    @Override
    public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(REMEMBER_ME_LOG_URL);
        if (!requestMatcher.matches(request)) {
            return null;
        }
        return super.autoLogin(request, response);
    }

    @Override
    public void setCookieDomain(String cookieDomain) {
        super.setCookieDomain(cookieDomain);
        this.cookieDomain = cookieDomain;
    }


    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = super.processAutoLoginCookie(cookieTokens, request, response);
        if (userDetails instanceof RestUserDetailsImpl restUserDetails) {
            restUserDetails.setLoginIp(IpUtils.getIpAddr(request));
            restUserDetails.setLoginType(LoginTypeEnum.REMEMBER);
            restUserDetails.setLoginTime(ZonedDateTime.now());
            restUserDetails.setAuthType(AuthTypeEnum.USERNAME);
        }
        return userDetails;
    }
}
