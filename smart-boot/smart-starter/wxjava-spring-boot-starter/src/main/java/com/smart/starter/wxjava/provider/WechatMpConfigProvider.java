package com.smart.starter.wxjava.provider;

import com.smart.starter.wxjava.model.WechatMpConfig;

import java.util.List;

/**
 * 获取微信公众号配置
 * @author zhongming4762
 * 2023/4/4
 */
public interface WechatMpConfigProvider {

    /**
     * 获取微信小程序配置列表
     * @return WechatAppConfig 列表
     */
    List<WechatMpConfig> get();
}
