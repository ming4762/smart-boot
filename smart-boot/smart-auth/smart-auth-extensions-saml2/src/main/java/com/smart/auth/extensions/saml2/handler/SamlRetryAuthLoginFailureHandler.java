package com.smart.auth.extensions.saml2.handler;

import com.smart.auth.core.handler.AuthLoginFailureHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.extensions.saml2.constants.SamlUrlConstants;
import com.smart.auth.extensions.saml2.utils.SamlRetryTimerHolder;
import com.smart.commons.core.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.opensaml.common.SAMLException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ShiZhongMing
 * 2021/3/1 9:28
 * @since 1.0
 */
@Slf4j
public class SamlRetryAuthLoginFailureHandler extends AuthLoginFailureHandler {

    private final AuthProperties authProperties;

    public SamlRetryAuthLoginFailureHandler(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception.getCause() instanceof SAMLException) {
            log.warn("SAML登录失败，进行重试", exception);
            final String key = IpUtils.getIpAddr(request);
            int timer = SamlRetryTimerHolder.get(key);
            if (timer > this.authProperties.getSaml2().getRetry()) {
                // 重置次数
                SamlRetryTimerHolder.reset(key);
                super.onAuthenticationFailure(request, response, exception);
            } else {
                SamlRetryTimerHolder.add(key);
                // 如果是SAML异常进行重试
                response.sendRedirect(request.getContextPath() + SamlUrlConstants.LOGIN.getUrl());
            }
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
