package com.smart.framework.auth.extensions.sms.filter;

import com.smart.framework.auth.core.model.SmsLoginParameter;
import com.smart.framework.auth.extensions.sms.authentication.SmsAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;


/**
 * @author ShiZhongMing
 * 2021/6/4 11:24
 * @since 1.0
 */
public class SmsLoginFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 认证域
     */
    @Setter
    private String authDomain;

    public SmsLoginFilter(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final SmsLoginParameter smsLoginParameter = SmsLoginParameter.create(request);
        final SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(smsLoginParameter.getPhone(), smsLoginParameter.getCode());
        smsAuthenticationToken.setAuthDomain(this.authDomain);
        return this.getAuthenticationManager().authenticate(smsAuthenticationToken);
    }
}
