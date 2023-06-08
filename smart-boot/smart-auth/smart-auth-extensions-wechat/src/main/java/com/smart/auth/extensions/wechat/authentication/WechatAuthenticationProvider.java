package com.smart.auth.extensions.wechat.authentication;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.core.i18n.AuthI18nMessage;
import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.auth.extensions.wechat.exception.WechatNotBoundException;
import com.smart.auth.extensions.wechat.model.WechatAppLoginResult;
import com.smart.auth.extensions.wechat.model.WechatLoginResult;
import com.smart.auth.extensions.wechat.provider.WechatLoginProvider;
import com.smart.auth.extensions.wechat.userdetails.WechatUserDetailService;
import com.smart.commons.core.i18n.I18nUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/4/3
 */
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final Map<AuthTypeEnum, WechatLoginProvider> wechatLoginProviderMap;

    private final WechatUserDetailService userDetailService;

    private final WechatAuthConfigProvider wechatAuthConfigProvider;

    public WechatAuthenticationProvider(List<WechatLoginProvider> wechatLoginProviderList, WechatUserDetailService userDetailService, WechatAuthConfigProvider wechatAuthConfigProvider) {
        this.wechatLoginProviderMap = wechatLoginProviderList.stream()
                .collect(Collectors.toMap(WechatLoginProvider::supportLoginType, item -> item));
        this.userDetailService = userDetailService;
        this.wechatAuthConfigProvider = wechatAuthConfigProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken token = (WechatAuthenticationToken) authentication;
        // 调用微信接口进行认证
        WechatLoginProvider wechatLoginProvider = this.wechatLoginProviderMap.get(token.getAuthType());
        if (wechatLoginProvider == null) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.WECHAT_LOGIN_TYPE_NOT_SUPPORT));
        }
        WechatLoginResult loginResult;
        String appid = token.getAppid();
        if (appid == null) {
            appid = wechatAuthConfigProvider.getDefaultAppid(token.getAuthType());
        }
        if (appid == null) {
            throw new AuthenticationServiceException(I18nUtils.get(AuthI18nMessage.WECHAT_APP_APPID_NOT_CONFIG));
        }
        try {
            loginResult = wechatLoginProvider.login(appid, (String) token.getCredentials());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        String credentials = (String) token.getCredentials();
        if (loginResult instanceof WechatAppLoginResult appLoginResult) {
            credentials = appLoginResult.getSessionKey();
        }
        RestUserDetails userDetails = null;
        // 调用微信登录成功后，获取系统用户消息
        if (StringUtils.isNotBlank(loginResult.getUnionid())) {
            userDetails = this.userDetailService.loadUserByUnionid(token.getAuthType(), appid, loginResult.getUnionid());
        }
        if (userDetails == null) {
            userDetails = this.userDetailService.loadUserByAppOpenid(token.getAuthType(), appid, loginResult.getOpenid());
        }
        if (userDetails == null) {
            // 微信用户未绑定
            throw new WechatNotBoundException(loginResult, I18nUtils.get(AuthI18nMessage.WECHAT_USER_NOT_BOND));
        }
        WechatAuthenticationToken authenticationToken = new WechatAuthenticationToken(AuthTypeEnum.WECHAT_APP, token.getAppid(), credentials, userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
