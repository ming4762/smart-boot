package com.smart.auth.extensions.appsecret.filter;

import com.smart.auth.extensions.appsecret.authentication.AppsecretAuthenticationToken;
import com.smart.commons.core.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
public class AppsecretLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String URL = "/auth/appsecret/token";

    private static final String APP_ID_KEY = "appId";

    private static final String SECRET_KEY = "secret";

    public AppsecretLoginFilter(String url) {
        super(StringUtils.isBlank(url) ? URL : url);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String appId = request.getParameter(APP_ID_KEY);
        String secret = request.getParameter(SECRET_KEY);
        AppsecretAuthenticationToken token = new AppsecretAuthenticationToken(appId, secret, IpUtils.getIpAddr(request));
        return this.getAuthenticationManager().authenticate(token);
    }
}
