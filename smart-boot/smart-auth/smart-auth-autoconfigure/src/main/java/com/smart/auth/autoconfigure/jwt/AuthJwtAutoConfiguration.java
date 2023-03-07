package com.smart.auth.autoconfigure.jwt;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.smart.auth.core.authentication.RestAuthenticationProvider;
import com.smart.auth.core.handler.AuthLogoutSuccessHandler;
import com.smart.auth.core.handler.AuthSuccessDataHandler;
import com.smart.auth.core.handler.SecurityLogoutHandler;
import com.smart.auth.core.properties.AuthProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.auth.extensions.jwt.AuthJwtConfigure;
import com.smart.auth.extensions.jwt.handler.JwtAuthSuccessDataHandler;
import com.smart.auth.extensions.jwt.handler.JwtLogoutHandler;
import com.smart.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.auth.extensions.jwt.service.JwtService;
import com.smart.auth.extensions.jwt.token.JwtTokenRepository;
import com.smart.commons.core.utils.auth.RsaUtils;
import com.smart.commons.jwt.JwtDecoder;
import com.smart.commons.jwt.JwtEncoder;
import com.smart.commons.jwt.NimbusJwtDecoder;
import com.smart.commons.jwt.NimbusJwtEncoder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;

/**
 * @author ShiZhongMing
 * @since 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthJwtConfigure.class)
public class AuthJwtAutoConfiguration {

    private static final String CLASSPATH_BEGIN = "classpath:";

    /**
     * 创建JwtService
     * @return JwtService
     */
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService() {
        return new JwtService();
    }

    /**
     * 构建jwt解码器
     * @param authProperties AuthProperties
     * @return JwtDecoder
     * @throws IOException IOException
     */
    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    public JwtDecoder jwtDecoder(AuthProperties authProperties) throws IOException {
        try (InputStream pubKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPublicKey())) {
            return NimbusJwtDecoder.withPublicKey((RSAPublicKey) RsaUtils.generaPublicKey(pubKeyInputStream)).build();
        }
    }

    @Bean
    @ConditionalOnMissingBean(JwtEncoder.class)
    public JwtEncoder jwtEncoder(AuthProperties authProperties) throws IOException {
        try (
                InputStream pubKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPublicKey());
                InputStream priKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPrivateKey());
                ) {
            JWK jwk = new RSAKey.Builder((RSAPublicKey) RsaUtils.generaPublicKey(pubKeyInputStream))
                    .privateKey(RsaUtils.generaPrivateKey(priKeyInputStream))
                    .build();
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
            return new NimbusJwtEncoder(jwkSource);
        }
    }

    /**
     * 获取key输入流
     * @param path key的路径
     * @return inputStream
     */
    @SneakyThrows
    private InputStream getKeyInputStream(String path) {
        if (path.startsWith("classpath:")) {
            path = StringUtils.removeStart(path, "classpath:");
            return new ClassPathResource(path).getInputStream();
        } else {
            return new FileInputStream(path);
        }
    }

    /**
     * 创建 LogoutSuccessHandler
     * @return LogoutSuccessHandler
     */
    @Bean
    @ConditionalOnMissingBean(LogoutSuccessHandler.class)
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new AuthLogoutSuccessHandler();
    }

    /**
     * 创建登出执行器
     * @param tokenRepository tokenRepository
     * @return SecurityLogoutHandler
     */
    @Bean
    @ConditionalOnMissingBean(SecurityLogoutHandler.class)
    public SecurityLogoutHandler jwtLogoutHandler(JwtTokenRepository tokenRepository) {
        return new JwtLogoutHandler(tokenRepository);
    }

    /**
     * 创建 RestAuthenticationProvider
     * @param userDetailsService userDetailsService
     * @return RestAuthenticationProvider
     */
    @Bean
    @ConditionalOnMissingBean(RestAuthenticationProvider.class)
    public RestAuthenticationProvider restAuthenticationProvider(UserDetailsService userDetailsService) {
        return new RestAuthenticationProvider(userDetailsService);
    }

    /**
     * 创建 AuthSuccessDataHandler
     * @return JwtAuthSuccessDataHandler
     */
    @Bean
    @ConditionalOnMissingBean(JwtAuthSuccessDataHandler.class)
    public AuthSuccessDataHandler jwtAuthSuccessDataHandler() {
        return new JwtAuthSuccessDataHandler();
    }

    /**
     * 创建JWT token存储器
     * @param authProperties properties
     * @param authCache 缓存器
     * @param jwtResolver JWT解析器
     * @return JwtTokenRepository
     */
    @Bean
    @ConditionalOnMissingBean(JwtTokenRepository.class)
    public JwtTokenRepository jwtTokenRepository(AuthProperties authProperties, AuthCache<String, Object> authCache, JwtResolver jwtResolver) {
        return new JwtTokenRepository(authProperties, authCache, jwtResolver);
    }

}
