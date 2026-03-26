package com.smart.framework.n8n.service;

import com.smart.framework.n8n.api.SmartN8nWebhookApi;
import com.smart.framework.n8n.properties.SmartN8nProperties;
import org.jspecify.annotations.NonNull;

/**
 * N8n Service
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:16
 * @since 5.0.0
 */
public interface SmartN8nService {

    /**
     * 获取N8n配置属性
     * @return N8n配置属性
     */
    SmartN8nProperties getProperties();

    /**
     * 获取N8n Webhook API
     * @param webhookId N8n Webhook ID
     * @return N8n Webhook API
     */
    SmartN8nWebhookApi webhookApi(@NonNull String webhookId);
}
