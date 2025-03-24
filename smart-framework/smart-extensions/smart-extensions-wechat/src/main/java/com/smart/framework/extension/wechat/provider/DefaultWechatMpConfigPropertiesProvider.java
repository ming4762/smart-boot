package com.smart.framework.extension.wechat.provider;

import com.smart.framework.extension.wechat.model.WechatMpConfig;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/7
 */
public class DefaultWechatMpConfigPropertiesProvider implements WechatMpConfigProvider {

    private final List<WechatMpConfig> wechatMpConfigList;

    public DefaultWechatMpConfigPropertiesProvider(List<WechatMpConfig> wechatMpConfigList) {
        this.wechatMpConfigList = wechatMpConfigList;
    }

    /**
     * 获取微信小程序配置列表
     *
     * @return WechatAppConfig 列表
     */
    @Override
    public List<WechatMpConfig> get() {
        return this.wechatMpConfigList;
    }
}
