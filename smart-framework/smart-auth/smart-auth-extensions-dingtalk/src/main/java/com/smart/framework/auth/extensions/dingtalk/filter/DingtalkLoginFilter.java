package com.smart.framework.auth.extensions.dingtalk.filter;

import com.smart.framework.auth.extensions.dingtalk.authentication.DingtalkAuthenticationToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 钉钉登录filter
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/7 18:33
 * @since 5.0.0
 */
public class DingtalkLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CODE_PARAMETER = "code";

    @Setter
    private String authDomain;

    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    public DingtalkLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter(CODE_PARAMETER);
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("code must be not empty");
        }
        DingtalkAuthenticationToken token = new DingtalkAuthenticationToken(code);
        token.setAuthDomain(this.authDomain);
        return this.getAuthenticationManager().authenticate(token);
    }
}
