package com.smart.starter.wxjava.provider;

import com.smart.starter.wxjava.model.WechatMpConfig;
import com.smart.starter.wxjava.properties.WechatMpProperties;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/7
 */
public class DefaultWechatMpConfigPropertiesProvider implements WechatMpConfigProvider {

    private final WechatMpProperties mpProperties;

    public DefaultWechatMpConfigPropertiesProvider(WechatMpProperties mpProperties) {
        this.mpProperties = mpProperties;
    }

    /**
     * 获取微信小程序配置列表
     *
     * @return WechatAppConfig 列表
     */
    @Override
    public List<WechatMpConfig> get() {
        return this.mpProperties.getConfigs();
    }
}
