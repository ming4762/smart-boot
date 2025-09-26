package com.smart.boot.autoconfigure.ai.dify;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * dify 配置参数
 * @author shizhongming
 * 2025/2/8 21:21
 * @since 5.0.0
 */
@ConfigurationProperties(prefix = "smart.ai.dify")
@Getter
@Setter
public class SmartAiDifyProperties {

    private String apiUrl;

    private String apiKey;
}
