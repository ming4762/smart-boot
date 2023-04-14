package com.smart.starter.wxjava.provider;

import com.smart.starter.wxjava.model.WechatAppConfig;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/4
 */
public interface WechatAppConfigProvider {

    /**
     * 获取微信小程序配置列表
     * @return WechatAppConfig 列表
     */
    List<WechatAppConfig> get();
}
