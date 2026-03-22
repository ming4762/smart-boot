package com.smart.framework.extension.wechat.provider;

import com.smart.framework.extension.wechat.model.WechatMiniappConfig;

import java.util.List;

/**
 * @author zhongming4762
 * 2023/4/4
 */
public interface WechatminiAppConfigProvider {

    /**
     * 获取微信小程序配置列表
     * @return WechatMiniappConfig 列表
     */
    List<WechatMiniappConfig> get();
}
