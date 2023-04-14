package com.smart.starter.wxjava.provider;

import com.smart.starter.wxjava.model.WechatAppConfig;
import com.smart.starter.wxjava.properties.WechatAppProperties;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/4
 */
public class DefaultWechatAppConfigPropertiesProvider implements WechatAppConfigProvider {


    private final WechatAppProperties wechatAppProperties;

    public DefaultWechatAppConfigPropertiesProvider(WechatAppProperties wechatAppProperties) {
        this.wechatAppProperties = wechatAppProperties;
    }

    /**
     * 获取微信小程序配置列表
     *
     * @return WechatAppConfig 列表
     */
    @Override
    public List<WechatAppConfig> get() {
        return this.wechatAppProperties.getConfigs();
    }
}
