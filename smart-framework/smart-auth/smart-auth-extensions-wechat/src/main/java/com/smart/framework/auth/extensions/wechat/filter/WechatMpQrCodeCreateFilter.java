package com.smart.framework.auth.extensions.wechat.filter;

import com.smart.framework.auth.extensions.wechat.model.WechatMpQrcodeResult;
import com.smart.framework.auth.extensions.wechat.provider.WechatMpQrcodeCreateProvider;
import com.smart.framework.commons.core.message.Result;
import com.smart.framework.commons.core.utils.RestJsonWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 用于生成微信服务号扫码登录二维码
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 16:27
 * @since 5.0.0
 */
public class WechatMpQrCodeCreateFilter extends OncePerRequestFilter {

    private final WechatMpQrcodeCreateProvider wechatMpQrcodeCreateProvider;

    public WechatMpQrCodeCreateFilter(WechatMpQrcodeCreateProvider wechatMpQrcodeCreateProvider) {
        this.wechatMpQrcodeCreateProvider = wechatMpQrcodeCreateProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException {
        WechatMpQrcodeResult qrcode = this.wechatMpQrcodeCreateProvider.createQrcode(null);
        RestJsonWriter.writeJson(response, Result.success(qrcode));
    }
}
