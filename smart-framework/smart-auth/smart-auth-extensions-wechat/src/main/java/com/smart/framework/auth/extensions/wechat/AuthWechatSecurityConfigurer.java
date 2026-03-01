package com.smart.framework.auth.extensions.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.google.common.collect.Lists;
import com.smart.framework.auth.core.config.SmartSecurityConfigurerAdapter;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.wechat.WechatAuthConfigProvider;
import com.smart.framework.auth.extensions.wechat.authentication.WechatAuthenticationProvider;
import com.smart.framework.auth.extensions.wechat.filter.WechatMiniappLoginFilter;
import com.smart.framework.auth.extensions.wechat.filter.WechatMpQrCodeCreateFilter;
import com.smart.framework.auth.extensions.wechat.provider.DefaultWechatMpQrcodeCreateProviderImpl;
import com.smart.framework.auth.extensions.wechat.provider.WechatLoginProvider;
import com.smart.framework.auth.extensions.wechat.provider.WechatMiniappLoginProvider;
import com.smart.framework.auth.extensions.wechat.provider.WechatMpQrcodeCreateProvider;
import com.smart.framework.auth.extensions.wechat.userdetails.WechatUserDetailService;
import com.smart.framework.commons.core.exception.SystemException;
import lombok.Getter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 微信登录配置类
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-01 20:19
 * @since 5.0.0
 */
public class AuthWechatSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SmartSecurityConfigurerAdapter<H> {

    private static final String BASE_URL = "/auth/wechat";

    private AuthWechatSecurityConfigurer() {}

    private final ServiceProvider serviceProvider = new ServiceProvider();

    /**
     * 微信登录配置
     * @return AuthWechatSecurityConfigurer
     * @param <H> HttpSecurityBuilder 类型
     */
    public static <H extends HttpSecurityBuilder<H>> AuthWechatSecurityConfigurer<H> wechat() {
        return new AuthWechatSecurityConfigurer<>();
    }

    /**
     * 开启微信服务号二维码登录配置
     * @return MpQrcodeConfig
     */
    public MpQrcodeConfig mpQrcode() {
        MpQrcodeConfig qrcodeConfig = new MpQrcodeConfig();
        this.serviceProvider.qrcodeConfig = qrcodeConfig;
        return qrcodeConfig;
    }

    /**
     * 开启微信小程序登录配置
     * @return MiniappConfig
     */
    public MiniappConfig miniapp() {
        MiniappConfig miniappConfig = new MiniappConfig();
        this.serviceProvider.miniappConfig = miniappConfig;
        return miniappConfig;
    }

    @Override
    public void configure(H builder) {
        builder
                .authenticationProvider(this.createAuthenticationProvider());
        if (this.serviceProvider.getQrcodeConfig() != null) {
            this.configureQrcode(builder);
        }
        if (this.serviceProvider.getMiniappConfig() != null) {
            this.configureMiniapp(builder);
        }
    }

    private WechatAuthenticationProvider createAuthenticationProvider() {
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        List<WechatLoginProvider> wechatLoginProviderList = Arrays.stream(applicationContext.getBeanNamesForType(WechatLoginProvider.class))
                .map(item -> {
                    WechatLoginProvider loginProvider = applicationContext.getBean(item, WechatLoginProvider.class);
                    // WechatMiniappLoginProvider手动创建，排除掉
                    if (loginProvider instanceof WechatMiniappLoginProvider) {
                        return null;
                    }
                    return loginProvider;
                }).filter(Objects::nonNull)
                .toList();
        WechatUserDetailService wechatUserDetailService = this.getBean(WechatUserDetailService.class);
        WechatAuthConfigProvider wechatAuthConfigProvider = this.getBean(WechatAuthConfigProvider.class);

        ArrayList<WechatLoginProvider> wechatLoginProviders = new ArrayList<>(wechatLoginProviderList);
        wechatLoginProviders.add(this.getWechatMiniappLoginProvider());

        return new WechatAuthenticationProvider(wechatLoginProviders, wechatUserDetailService, wechatAuthConfigProvider);
    }

    // -------------- 微信小程序登录 ----------------------

    private void configureMiniapp(H builder) {
        builder
                .addFilterBefore(this.createMiniappFilter(), BasicAuthenticationFilter.class);
    }

    /**
     * 配置微信小程序登录
     * @return 微信小程序登录过滤器链代理
     */
    private FilterChainProxy createMiniappFilter() {
        List<SecurityFilterChain> chains = new ArrayList<>(1);
        chains.add(
                new DefaultSecurityFilterChain(PathPatternRequestMatcher.withDefaults().matcher(this.serviceProvider.getMiniappConfig().getLoginUrl()), this.createMiniappLoginFilter())
        );
        return new FilterChainProxy(chains);
    }

    /**
     * 创建微信小程序登录拦截器
     * @return 微信小程序登录拦截器
     */
    private WechatMiniappLoginFilter createMiniappLoginFilter() {
        WechatMiniappLoginFilter loginFilter = new WechatMiniappLoginFilter(this.serviceProvider.getMiniappConfig().getLoginUrl());
        loginFilter.setAuthenticationManager(this.getBuilder().getSharedObject(AuthenticationManager.class));
        loginFilter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class));
        loginFilter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class));
        return loginFilter;
    }

    private WechatMiniappLoginProvider getWechatMiniappLoginProvider() {
        return this.getBeanOrElse(WechatMiniappLoginProvider.class, () -> {
            WxMaService maService = this.getBean(WxMaService.class);
            if (maService == null) {
                throw new SystemException("WxMaService Bean未创建");
            }
            return new WechatMiniappLoginProvider(maService);
        });
    }

    // -------------- 微信服务号二维码登录 ------------------
    /**
     * 配置微信服务号二维码登录
     * @param builder 构建器
     */
    private void configureQrcode(H builder) {
        builder.addFilterBefore(
                this.createQrcodeLoginFilterChainProxy(),
                BasicAuthenticationFilter.class
        );
    }

    /**
     * 创建过滤器链
     * @return 过滤器链代理
     */
    private FilterChainProxy createQrcodeLoginFilterChainProxy() {
        List<SecurityFilterChain> chains = Lists.newArrayList();
        chains.add(createQrcodeCreateFilter());
        return new FilterChainProxy(chains);
    }

    /**
     * 创建二维码生成拦截器
     * @return 二维码生成拦截器
     */
    protected DefaultSecurityFilterChain createQrcodeCreateFilter() {
        WechatMpQrCodeCreateFilter filter = new WechatMpQrCodeCreateFilter(this.getWechatMpQrcodeCreateProvider());
        return new DefaultSecurityFilterChain(
                PathPatternRequestMatcher.withDefaults().matcher(this.serviceProvider.getQrcodeConfig().getCreateUrl()),
                filter
        );
    }

    private WechatMpQrcodeCreateProvider getWechatMpQrcodeCreateProvider() {
        WechatMpQrcodeCreateProvider provider = this.getBean(WechatMpQrcodeCreateProvider.class);
        if (provider != null) {
            return provider;
        }
        WxMpService mpService = this.getBean(WxMpService.class);
        if (mpService == null) {
            throw new SystemException("WxMpService Bean未创建");
        }
        return new DefaultWechatMpQrcodeCreateProviderImpl(
                mpService,
                this.getBean(AuthCache.class),
                this.getBean(WechatAuthConfigProvider.class)
        );
    }

    // --------------------------------------------------------

    @Getter
    private class ServiceProvider {
        // 微信服务号二维码登录配置
        private MpQrcodeConfig qrcodeConfig;
        // 微信小程序登录配置
        private MiniappConfig miniappConfig;
    }

    /**
     * 微信服务号二维码登录配置
     */
    @Getter
    public class MpQrcodeConfig {
        private static final String QRCODE_BASE_URL = BASE_URL + "/mp";

        private String createUrl = QRCODE_BASE_URL + "/createQrcode";
        private String loginUrl = QRCODE_BASE_URL + "/login";

        public MpQrcodeConfig createUrl(String createUrl) {
            this.createUrl = createUrl;
            return this;
        }

        public MpQrcodeConfig loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public AuthWechatSecurityConfigurer<H> config() {
            return AuthWechatSecurityConfigurer.this;
        }
    }

    /**
     * 微信小程序登录配置
     */
    @Getter
    public class MiniappConfig {
        private static final String MINIAPP_BASE_URL = BASE_URL + "/miniapp";

        private String loginUrl = MINIAPP_BASE_URL + "/login";

        public MiniappConfig loginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
            return this;
        }

        public AuthWechatSecurityConfigurer<H> config() {
            return AuthWechatSecurityConfigurer.this;
        }
    }
}
