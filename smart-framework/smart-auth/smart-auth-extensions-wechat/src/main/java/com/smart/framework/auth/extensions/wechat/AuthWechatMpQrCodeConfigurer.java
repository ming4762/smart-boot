package com.smart.framework.auth.extensions.wechat;

import com.google.common.collect.Lists;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.extensions.wechat.filter.WechatMpQrCodeCreateFilter;
import com.smart.framework.auth.extensions.wechat.provider.WechatMpQrcodeCreateProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.List;

/**
 * 微信服务号二维码扫码登录配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-27 16:29
 * @since 5.0.0
 */
public class AuthWechatMpQrCodeConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private static final String BASE_URL = "/auth/wechat/mp";
    private static final String QRCODE_CREATE_URL = BASE_URL + "/createQrcode";

    private AuthWechatMpQrCodeConfigurer() {}

    public static <H extends HttpSecurityBuilder<H>> AuthWechatMpQrCodeConfigurer<H> mp() {
        return new AuthWechatMpQrCodeConfigurer<>();
    }

    public H config(Customizer<AuthWechatMpQrCodeConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        builder.addFilterBefore(
                this.createFilterChainProxy(),
                BasicAuthenticationFilter.class
        );
    }

    /**
     * 创建过滤器链
     * @return 过滤器链代理
     */
    private FilterChainProxy createFilterChainProxy() {
        List<SecurityFilterChain> chains = Lists.newArrayList();
        chains.add(createQrcodeCreateFilter());
        return new FilterChainProxy(chains);
    }

    /**
     * 创建二维码生成拦截器
     * @return 二维码生成拦截器
     */
    protected DefaultSecurityFilterChain createQrcodeCreateFilter() {
        WechatMpQrCodeCreateFilter filter = new WechatMpQrCodeCreateFilter(this.getBean(WechatMpQrcodeCreateProvider.class));
        return new DefaultSecurityFilterChain(
                PathPatternRequestMatcher.withDefaults().matcher(QRCODE_CREATE_URL),
                filter
        );
    }
}
