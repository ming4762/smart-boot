package com.smart.framework.n8n.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * N8n Properties
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:17
 * @since 5.0.0
 */
@Getter
@Setter
@ToString
public class SmartN8nProperties implements Serializable {

    private String baseUrl;

    /**
     * 认证配置
     */
    @NestedConfigurationProperty
    private SmartN8nAuthProperties auth;
}
