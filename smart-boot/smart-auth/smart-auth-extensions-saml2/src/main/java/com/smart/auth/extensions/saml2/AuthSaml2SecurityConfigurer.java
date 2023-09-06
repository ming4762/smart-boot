package com.smart.auth.extensions.saml2;

import com.google.common.collect.Lists;
import com.smart.auth.extensions.saml2.constants.SamlUrlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.saml.*;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.log.SAMLLogger;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * SAML2配置类
 * @author ShiZhongMing
 * 2021/1/6 10:26
 * @since 1.0
 */
@Slf4j
public class AuthSaml2SecurityConfigurer<H extends HttpSecurityBuilder<H>> extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, H> {

    private static final String URL_SUFFIX = "/**";

    public static <H extends HttpSecurityBuilder<H>> AuthSaml2SecurityConfigurer<H> saml2() {
        return new AuthSaml2SecurityConfigurer<>();
    }

    public H config(Customizer<AuthSaml2SecurityConfigurer<H>> customizer) {
        customizer.customize(this);
        return this.getBuilder();
    }

    @Override
    public void configure(H builder) {
        // 创建 ExtendedMetadata
        builder
                .authenticationProvider(this.getBean(SAMLAuthenticationProvider.class));
        // 添加Filter
        builder.addFilterBefore(this.createMetadataGeneratorFilter(), ChannelProcessingFilter.class)
                .addFilterAfter(this.createSamlFilter(), BasicAuthenticationFilter.class);
    }

    private String getUrl(SamlUrlConstants samlUrlConstants) {
        return samlUrlConstants.getUrl() + URL_SUFFIX;
    }

    /**
     * 创建 MetadataGeneratorFilter
     * @return MetadataGeneratorFilter
     */
    private MetadataGeneratorFilter createMetadataGeneratorFilter() {
        final MetadataGeneratorFilter filter = new MetadataGeneratorFilter(this.getBean(MetadataGenerator.class));
        filter.setManager(this.getBean(MetadataManager.class));
        return filter;
    }

    /**
     * 创建SAML过滤器链
     * @return FilterChainProxy
     */
    private FilterChainProxy createSamlFilter() {
        List<SecurityFilterChain> chains = Lists.newArrayList();
        // 登录过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SamlUrlConstants.LOGIN)), this.getBean(SAMLEntryPoint.class)));

        // 登出过滤器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SamlUrlConstants.LOGOUT)), this.samlLogoutFilter()));

        // 添加 SAMLDiscovery
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SamlUrlConstants.DISCOVERY)), this.createSamlDiscovery()));

        // 添加登录拦截器
        chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher(this.getUrl(SamlUrlConstants.SSO)), this.createSamlProcessingFilter()));
        return new FilterChainProxy(chains);
    }

    private SAMLDiscovery createSamlDiscovery() {
        final SAMLDiscovery samlDiscovery = new SAMLDiscovery();
        samlDiscovery.setMetadata(this.getBean(MetadataManager.class));
        samlDiscovery.setContextProvider(this.getBean(SAMLContextProvider.class));
        return samlDiscovery;
    }

    /**
     * 创建 SAMLProcessingFilter
     * @return SAMLProcessingFilter
     */
    private SAMLProcessingFilter createSamlProcessingFilter() {
        final SAMLProcessingFilter filter = new SAMLProcessingFilter();
        filter.setAuthenticationManager(this.getBean(AuthenticationManager.class));
        filter.setContextProvider(this.getBean(SAMLContextProvider.class));
        filter.setSAMLProcessor(this.getBean(SAMLProcessor.class));
        filter.setAuthenticationSuccessHandler(this.getBean(AuthenticationSuccessHandler.class));
        filter.setAuthenticationFailureHandler(this.getBean(AuthenticationFailureHandler.class));
        return filter;
    }

    /**
     * 创建 SAMLLogoutFilter
     * @return SAMLLogoutFilter
     */
    private SAMLLogoutFilter samlLogoutFilter() {
        final SAMLLogoutFilter samlLogoutFilter =  new SAMLLogoutFilter(
                Objects.requireNonNull(this.getBean(LogoutSuccessHandler.class)),
                new LogoutHandler[]{this.getBean(LogoutHandler.class)},
                new LogoutHandler[]{this.getBean(LogoutHandler.class)}
                );
        samlLogoutFilter.setProfile(this.getBean(SingleLogoutProfile.class));
        samlLogoutFilter.setContextProvider(this.getBean(SAMLContextProvider.class));
        samlLogoutFilter.setSamlLogger(this.getBean(SAMLLogger.class));
        return samlLogoutFilter;
    }

    /**
     * 从容器中获取指定类型bean
     * @param clazz 类型
     * @param <T> 类型
     * @return bean实体
     */
    private <T> T getBean(Class<T> clazz) {
        ApplicationContext applicationContext = this.getBuilder().getSharedObject(ApplicationContext.class);
        try {
            return Optional.ofNullable(applicationContext).map(item -> item.getBean(clazz)).orElse(null);
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("获取bean发生错误: " + e.getMessage());
            return null;
        }
    }

}
