package com.smart.framework.n8n.service;

import com.smart.framework.n8n.api.SmartN8nWebhookApi;
import com.smart.framework.n8n.properties.SmartN8nProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Default N8n Service Implementation
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-07 20:23
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class DefaultSmartN8nServiceImpl implements SmartN8nService {

    private final SmartN8nProperties properties;

    /**
     * 获取N8n配置属性
     *
     * @return N8n配置属性
     */
    @Override
    public SmartN8nProperties getProperties() {
        return this.properties;
    }

    /**
     * 获取N8n Webhook API
     *
     * @param webhookId N8n Webhook ID
     * @return N8n Webhook API
     */
    @Override
    public SmartN8nWebhookApi webhookApi(@NonNull String webhookId) {
        return new SmartN8nWebhookApi(webhookId, this);
    }
}
