package com.smart.module.sso.server.auth.authentication;

import com.smart.module.api.system.SysUserApi;
import com.smart.module.api.system.dto.SysUserDTO;
import com.smart.module.sso.server.auth.exception.OAuth2ClientUserNotAllowException;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientPO;
import com.smart.module.sso.server.common.manager.model.SsoOauth2ClientUserPO;
import com.smart.module.sso.server.common.manager.repository.SsoOauth2ClientRepository;
import com.smart.module.sso.server.common.manager.repository.SsoOauth2ClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 校验用户是否授权OAuth2客户端
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-24 10:07
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class OAuth2ClientUserValidator implements Consumer<OAuth2AuthorizationCodeRequestAuthenticationContext> {

    private final SysUserApi sysUserApi;
    private final SsoOauth2ClientRepository ssoOauth2ClientRepository;
    private final SsoOauth2ClientUserRepository ssoOauth2ClientUserRepository;

    @Override
    public void accept(OAuth2AuthorizationCodeRequestAuthenticationContext context) {
        String id = context.getRegisteredClient().getId();
        String username = Optional.ofNullable(context.getAuthorizationConsent())
                .map(OAuth2AuthorizationConsent::getPrincipalName)
                .orElse(null);
        Assert.notNull(username, "系统异常，用户不存在");

        SysUserDTO user = this.sysUserApi.getUserByUsername(username);
        Assert.notNull(user, "系统异常，用户不存在");
        // 校验用户是否授权该客户端
        SsoOauth2ClientUserPO clientUser = this.ssoOauth2ClientUserRepository.lambdaQuery()
                .eq(SsoOauth2ClientUserPO::getClientId, id)
                .eq(SsoOauth2ClientUserPO::getUserId, user.getUserId())
                .eq(SsoOauth2ClientUserPO::getUseYn, Boolean.TRUE)
                .one();
        // 查询客户端信息
        SsoOauth2ClientPO client = this.ssoOauth2ClientRepository.getById(id);
        if (client.isPublic()) {
            if (clientUser == null || clientUser.isAllow()) {
                return;
            }
            if (!clientUser.isAllow()) {
                throwError(OAuth2ErrorCodes.INVALID_CLIENT, "用户未授权该客户端", context.getAuthentication(), context.getRegisteredClient());
            }
        } else {
            if (clientUser == null || !clientUser.isAllow()) {
                throwError(OAuth2ErrorCodes.INVALID_CLIENT, "用户未授权该客户端", context.getAuthentication(), context.getRegisteredClient());
            }
        }
    }

    private static void throwError(
            String errorCode,
            String message,
            OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthentication,
            RegisteredClient registeredClient
    ) {
        OAuth2Error oAuth2Error = new OAuth2Error(errorCode, message, null);
        String redirectUri = StringUtils.hasText(authorizationCodeRequestAuthentication.getRedirectUri())
                ? authorizationCodeRequestAuthentication.getRedirectUri()
                : registeredClient.getRedirectUris().iterator().next();
        OAuth2AuthorizationCodeRequestAuthenticationToken authorizationCodeRequestAuthenticationResult = new OAuth2AuthorizationCodeRequestAuthenticationToken(
                authorizationCodeRequestAuthentication.getAuthorizationUri(),
                authorizationCodeRequestAuthentication.getClientId(),
                (Authentication) authorizationCodeRequestAuthentication.getPrincipal(), redirectUri,
                authorizationCodeRequestAuthentication.getState(), authorizationCodeRequestAuthentication.getScopes(),
                authorizationCodeRequestAuthentication.getAdditionalParameters());
        authorizationCodeRequestAuthenticationResult.setAuthenticated(true);

        throw new OAuth2ClientUserNotAllowException(oAuth2Error, authorizationCodeRequestAuthenticationResult);
    }
}
