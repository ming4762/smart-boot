package com.smart.framework.auth.extensions.dingtalk.authentication;

import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponseBody;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.core.properties.AuthDingtalkProperties;
import com.smart.framework.auth.extensions.dingtalk.exception.DingtalkNotBoundException;
import com.smart.framework.auth.extensions.dingtalk.userdetails.DingtalkUserDetailService;
import com.smart.framework.extension.dingtalk.DingtalkApi;
import com.smart.framework.extension.dingtalk.api.AccessSecureApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 钉钉登录认证提供者
 * @author shizhongming
 * 2025/10/31 14:27
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class DingtalkAuthenticationProvider implements AuthenticationProvider {

    private final DingtalkApi dingtalkApi;
    private final AuthDingtalkProperties properties;
    private final DingtalkUserDetailService dingtalkUserDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DingtalkAuthenticationToken token = (DingtalkAuthenticationToken) authentication;
        // 切换到指定的应用
        this.dingtalkApi.switchover(properties.getClientId());

        String code = (String) token.getCredentials();

        AccessSecureApi accessSecureApi = this.dingtalkApi.accessSecureApi();
        GetUserTokenResponseBody userAccessTokenByAuthCode = accessSecureApi.getUserAccessTokenByAuthCode(code);
        // 所选企业ID
        String corpId = userAccessTokenByAuthCode.getCorpId();
        // 获取用户信息
        GetUserResponseBody userByUserToken = this.dingtalkApi.userApi().getUserByUserToken(userAccessTokenByAuthCode.getAccessToken());
        // 钉钉用户唯一ID
        String unionId = userByUserToken.getUnionId();
        RestUserDetails restUserDetails = this.dingtalkUserDetailService.loadUserByUnionId(unionId);
        if (restUserDetails == null) {
            throw new DingtalkNotBoundException("钉钉用户未绑定", userByUserToken);
        }
        DingtalkAuthenticationToken authenticationToken = new DingtalkAuthenticationToken(corpId, code, restUserDetails, restUserDetails.getAuthorities());
        authenticationToken.setDetails(restUserDetails);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DingtalkAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
