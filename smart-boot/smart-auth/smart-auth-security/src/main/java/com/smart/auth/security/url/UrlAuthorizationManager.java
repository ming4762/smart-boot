package com.smart.auth.security.url;

import com.smart.auth.core.authentication.url.UrlAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * URL权限验证
 * @author ShiZhongMing
 * 2022/10/26
 * @since 3.0.0
 */
public class UrlAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private UrlAuthenticationProvider authenticationProvider;

    @Nullable
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        var result = this.authenticationProvider.hasPermission(object.getRequest(), authentication.get());
        return new AuthorizationDecision(result);
    }

    @Autowired
    public void setAuthenticationProvider(UrlAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
