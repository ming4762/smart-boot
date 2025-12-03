package com.smart.module.auth.url;

import com.smart.framework.auth.core.authentication.url.UrlAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
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

    @Autowired
    public void setAuthenticationProvider(UrlAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Determines if access is granted for a specific authentication and object.
     *
     * @param authentication the {@link Supplier} of the {@link Authentication} to
     *                       authorize
     * @param object         the {@link T} object to authorize
     * @return an {@link AuthorizationResult}
     * @since 6.4
     */
    @Override
    public @org.jspecify.annotations.Nullable AuthorizationResult authorize(Supplier<? extends @org.jspecify.annotations.Nullable Authentication> authentication, RequestAuthorizationContext object) {
        var result = this.authenticationProvider.hasPermission(object.getRequest(), authentication.get());
        return new AuthorizationDecision(result);
    }
}
