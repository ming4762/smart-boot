package com.smart.auth.extensions.appsecret.authentication;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.extensions.appsecret.constants.AppsecretHttpStatus;
import com.smart.auth.extensions.appsecret.exception.AuthAppsecretException;
import com.smart.auth.extensions.appsecret.service.AppSecretService;
import com.smart.commons.core.i18n.I18nUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ShiZhongMing
 * @since 1.0.7
 */
public class AppsecretAuthenticationProvider implements AuthenticationProvider {
    private final AppSecretService appSecretService;

    public AppsecretAuthenticationProvider(AppSecretService secretService) {
        this.appSecretService = secretService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AppsecretAuthenticationToken token = (AppsecretAuthenticationToken) authentication;
        String appId = (String) token.getPrincipal();
        String secret = (String) token.getCredentials();
        String ip = token.getIp();
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(secret)) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_ID_SECRET_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_ID_SECRET_ERROR));
        }
        // 验证appId secret是否有效
        RestUserDetails user = this.appSecretService.loadByAppId(appId);
        if (user == null || !StringUtils.equals(secret, user.getPassword())) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_ID_SECRET_ERROR.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_ID_SECRET_ERROR));
        }
        // 验证白名单
        if (!this.inWhiteList(ip, user.getIpWhiteList())) {
            throw new AuthAppsecretException(AppsecretHttpStatus.APP_IP_NOT_IN_WHITE_LIST.getCode(), I18nUtils.get(AppsecretHttpStatus.APP_IP_NOT_IN_WHITE_LIST));
        }
        // 鉴权成功
        AppsecretAuthenticationToken appsecretAuthenticationToken = new AppsecretAuthenticationToken(user, secret, ip, user.getAuthorities());
        appsecretAuthenticationToken.setDetails(user);
        return appsecretAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppsecretAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected boolean inWhiteList(@NonNull String ip, List<String> whiteList) {
        if (CollectionUtils.isEmpty(whiteList)) {
            // 如果白名单为null，不验证
            return true;
        }
        return whiteList.contains(ip);
    }
}
