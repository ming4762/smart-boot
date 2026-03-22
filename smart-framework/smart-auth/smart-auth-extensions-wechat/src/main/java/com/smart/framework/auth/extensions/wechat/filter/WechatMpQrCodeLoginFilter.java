package com.smart.framework.auth.extensions.wechat.filter;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.wechat.authentication.WechatAuthenticationToken;
import com.smart.framework.auth.extensions.wechat.cache.WechatMpQrcodeCacheData;
import com.smart.framework.auth.extensions.wechat.constants.WechatLoginCodeEnum;
import com.smart.framework.auth.extensions.wechat.model.WechatQrcodeLoginParameter;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.RestJsonWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.io.IOException;

/**
 * 微信服务号扫码登录filter
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 21:59
 * @since 5.0.0
 */
public class WechatMpQrCodeLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthCache authCache;

    @Setter
    private String authDomain;

    public WechatMpQrCodeLoginFilter(String loginUrl, AuthCache authCache) {
        super(loginUrl);
        this.authCache = authCache;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 获取登录参数
        WechatQrcodeLoginParameter parameter = getLoginParameter(request);
        // 从缓存中获取二维码登录数据
        WechatMpQrcodeCacheData cacheData = authCache.getValue(parameter.getScene());
        if (cacheData == null) {
            // 二维码已过期失效
            RestJsonWriter.writeJson(response, Result.ofStatus(WechatLoginCodeEnum.MP_QRCODE_EXPIRED));
            return null;
        }
        if (!cacheData.isValidated()) {
            // 二维码未扫描
            RestJsonWriter.writeJson(response, Result.ofStatus(WechatLoginCodeEnum.MP_QRCODE_NO_VALID));
            return null;
        }
        // 构建认证token
        WechatAuthenticationToken authenticationToken = new WechatAuthenticationToken(
                AuthTypeEnum.WECHAT_MP_QRCODE,
                cacheData.getAppid(),
                cacheData.getOpenId()
        );
        authenticationToken.setAuthDomain(this.authDomain);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 获取登录参数
     * @param request HttpServletRequest
     * @return WechatQrcodeLoginParameter
     */
    private WechatQrcodeLoginParameter getLoginParameter(HttpServletRequest request) {
        WechatQrcodeLoginParameter parameter = new WechatQrcodeLoginParameter();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(parameter);
        binder.bind(request);
        return parameter;
    }
}
