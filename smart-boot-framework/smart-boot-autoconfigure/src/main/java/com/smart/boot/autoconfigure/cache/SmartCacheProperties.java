package com.smart.boot.autoconfigure.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author shizhongming
 * 2025/7/30 16:29
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.cache")
public class SmartCacheProperties {

    @Value("${spring.application.name}")
    private String prefix;
}
