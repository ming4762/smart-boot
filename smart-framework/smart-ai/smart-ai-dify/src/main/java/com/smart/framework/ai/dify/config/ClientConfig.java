package com.smart.framework.ai.dify.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 客户端配置
 * @author shizhongming
 * 2025/2/8 20:19
 * @since 5.0.0
 */
@Getter
@Setter
public class ClientConfig {

    /**
     * Service API 使用 API-Key 进行鉴权
     */
    private String apiKey;

    /**
     * 服务地址
     */
    private String apiUrl;
}
