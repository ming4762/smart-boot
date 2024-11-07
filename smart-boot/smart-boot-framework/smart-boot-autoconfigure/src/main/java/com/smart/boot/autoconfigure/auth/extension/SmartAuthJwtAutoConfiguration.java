package com.smart.boot.autoconfigure.auth.extension;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.smart.framework.auth.core.handler.SecurityLogoutHandler;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.extensions.jwt.AuthJwtSecurityConfigurer;
import com.smart.framework.auth.extensions.jwt.context.JwtSecurityContextRepository;
import com.smart.framework.auth.extensions.jwt.handler.JwtLogoutHandler;
import com.smart.framework.auth.extensions.jwt.resolver.JwtResolver;
import com.smart.framework.auth.extensions.jwt.service.JwtService;
import com.smart.framework.auth.extensions.jwt.token.JwtTokenRepository;
import com.smart.framework.commons.core.utils.auth.RsaUtils;
import com.smart.framework.commons.jwt.JwtDecoder;
import com.smart.framework.commons.jwt.JwtEncoder;
import com.smart.framework.commons.jwt.NimbusJwtDecoder;
import com.smart.framework.commons.jwt.NimbusJwtEncoder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;

/**
 * @author shizhongming
 * 2024/9/10 17:16
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(AuthJwtSecurityConfigurer.class)
@EnableConfigurationProperties(AuthProperties.class)
public class SmartAuthJwtAutoConfiguration {

    private static final String CLASSPATH_BEGIN = "classpath:";

    /**
     * 创建JwtService
     * @return JwtService
     */
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService(AuthProperties authProperties) {
        return new JwtService(authProperties.getJwt().getPermissionCache());
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
        InputStream pubKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPublicKey());
        try (pubKeyInputStream) {
            return NimbusJwtDecoder.withPublicKey((RSAPublicKey) RsaUtils.generaPublicKey(pubKeyInputStream)).build();
        }
    }

    @Bean
    @ConditionalOnMissingBean(JwtEncoder.class)
    public JwtEncoder jwtEncoder(AuthProperties authProperties) throws IOException {
        InputStream pubKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPublicKey());
        InputStream priKeyInputStream = this.getKeyInputStream(authProperties.getJwt().getPrivateKey());

        try (pubKeyInputStream; priKeyInputStream) {
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
    @SneakyThrows(IOException.class)
    private InputStream getKeyInputStream(String path) {
        if (path.startsWith(CLASSPATH_BEGIN)) {
            path = StringUtils.removeStart(path, CLASSPATH_BEGIN);
            return new ClassPathResource(path).getInputStream();
        } else {
            return new FileInputStream(path);
        }
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


    @Bean
    @ConditionalOnMissingBean
    public SecurityContextRepository securityContextRepository() {
        return new JwtSecurityContextRepository();
    }
}
