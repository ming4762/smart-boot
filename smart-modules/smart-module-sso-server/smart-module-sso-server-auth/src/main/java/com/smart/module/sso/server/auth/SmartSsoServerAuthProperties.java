package com.smart.module.sso.server.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SSO单点服务端-认证端口配置
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-02-19 12:24
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.sso.server.auth")
public class SmartSsoServerAuthProperties {

    /**
     * 默认的客户端密钥
     */
    private String defaultSecret;
}
