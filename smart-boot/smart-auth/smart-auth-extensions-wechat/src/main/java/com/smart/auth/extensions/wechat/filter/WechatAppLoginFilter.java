package com.smart.auth.extensions.wechat.filter;

import com.smart.auth.core.constants.AuthTypeEnum;
import com.smart.auth.extensions.wechat.authentication.WechatAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * wechat小程序登录filter
 * @author zhongming4762
 * 2023/4/3
 */
public class WechatAppLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CODE_PARAMETER = "code";

    private static final String APPID_PARAMETER = "appid";

    /**
     * @param defaultFilterProcessesUrl the default value for <tt>filterProcessesUrl</tt>.
     */
    public WechatAppLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        WechatAuthenticationToken token = new WechatAuthenticationToken(AuthTypeEnum.WECHAT_APP, request.getParameter(APPID_PARAMETER), request.getParameter(CODE_PARAMETER));
        return this.getAuthenticationManager().authenticate(token);
    }
}
