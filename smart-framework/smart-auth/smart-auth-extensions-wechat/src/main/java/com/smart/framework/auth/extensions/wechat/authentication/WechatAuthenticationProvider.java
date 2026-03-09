package com.smart.framework.auth.extensions.wechat.authentication;

import com.smart.framework.auth.common.constants.AuthDomainConstants;
import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.common.exception.AuthException;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.authentication.checker.RestUserDetailsChecker;
import com.smart.framework.auth.core.i18n.AuthI18nMessage;
import com.smart.framework.auth.core.model.RestUserDetailsImpl;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.exception.WechatNotBoundException;
import com.smart.framework.auth.extensions.wechat.model.WechatAppLoginResult;
import com.smart.framework.auth.extensions.wechat.model.WechatLoginResult;
import com.smart.framework.auth.extensions.wechat.provider.WechatLoginProvider;
import com.smart.framework.auth.extensions.wechat.userdetails.RestUserWechatExtraData;
import com.smart.framework.auth.extensions.wechat.userdetails.WechatUserDetailService;
import com.smart.framework.commons.core.i18n.I18nUtils;
import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhongming4762
 * 2023/4/3
 */
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final Map<AuthTypeEnum, WechatLoginProvider> wechatLoginProviderMap;

    private final WechatUserDetailService userDetailService;

    private final WechatAuthConfigProvider wechatAuthConfigProvider;

    @Setter
    private RestUserDetailsChecker restUserDetailsChecker;

    public WechatAuthenticationProvider(List<WechatLoginProvider> wechatLoginProviderList, WechatUserDetailService userDetailService, WechatAuthConfigProvider wechatAuthConfigProvider) {
        this.wechatLoginProviderMap = wechatLoginProviderList.stream()
                .collect(Collectors.toMap(WechatLoginProvider::supportLoginType, item -> item));
        this.userDetailService = userDetailService;
        this.wechatAuthConfigProvider = wechatAuthConfigProvider;
        this.restUserDetailsChecker = new RestUserDetailsChecker();
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
            loginResult = wechatLoginProvider.login(appid, token.getCredentials());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        String credentials = (String) token.getCredentials();
        if (loginResult instanceof WechatAppLoginResult appLoginResult) {
            credentials = appLoginResult.getSessionKey();
        }
        RestUserDetails userDetails = null;
        // 调用微信登录成功后，获取系统用户消息
        if (StringUtils.hasText(loginResult.getUnionid())) {
            userDetails = this.userDetailService.loadUserByUnionid(token.getAuthType(), appid, loginResult.getUnionid());
        }
        if (userDetails == null) {
            userDetails = this.userDetailService.loadUserByAppOpenid(token.getAuthType(), appid, loginResult.getOpenid());
        }
        if (userDetails == null) {
            // 微信用户未绑定
            throw new WechatNotBoundException(appid, loginResult);
        }
        RestUserDetailsImpl restUserDetails = (RestUserDetailsImpl) userDetails;
        restUserDetails.setAuthType(token.getAuthType());

        // 认证域校验
        String authDomain = token.getAuthDomain();
        if (StringUtils.hasText(authDomain) && !AuthDomainConstants.AUTH_DOMAIN_NONE.equals(authDomain)) {
            // 校验用户是否拥有该认证域
            Set<String> authDomains = restUserDetails.getUserAuthDomains();
            if (CollectionUtils.isEmpty(authDomains) || !authDomains.contains(authDomain)) {
                throw new AuthException(String.format("微信用户不在权限域[%s]内", authDomain));
            }
            // 设置当前登录的认证域
            restUserDetails.setCurrentAuthDomain(authDomain);
        }

        // 设置额外信息
        RestUserWechatExtraData extraData = RestUserWechatExtraData.builder()
                .appid(appid)
                .openid(loginResult.getOpenid())
                .unionid(loginResult.getUnionid())
                .build();
        restUserDetails.setExtra(extraData);
        this.restUserDetailsChecker.check(userDetails);
        WechatAuthenticationToken authenticationToken = new WechatAuthenticationToken(token.getAuthType(), token.getAppid(), credentials, userDetails, userDetails.getAuthorities());
        authenticationToken.setDetails(userDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
