package com.smart.framework.extension.wechat.provider;

import com.smart.framework.extension.wechat.model.WechatAppConfig;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/4
 */
public class DefaultWechatAppConfigPropertiesProvider implements WechatAppConfigProvider {


    private final List<WechatAppConfig> wechatAppConfigList;

    public DefaultWechatAppConfigPropertiesProvider(List<WechatAppConfig> wechatAppConfigList) {
        this.wechatAppConfigList = wechatAppConfigList;
    }

    /**
     * 获取微信小程序配置列表
     *
     * @return WechatAppConfig 列表
     */
    @Override
    public List<WechatAppConfig> get() {
        return this.wechatAppConfigList;
    }
}
