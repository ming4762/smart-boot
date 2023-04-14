package com.smart.auth.core.wechat;

import com.smart.auth.core.constants.AuthTypeEnum;

/**
 * @author zhongming4762
 * 2023/4/3 21:05
 */
public interface WechatAuthConfigProvider {

    /**
     * 获取小程序配置
     * @param authType authType
     * @return WechatConfig
     */
    default String getDefaultAppid(AuthTypeEnum authType) {
        return null;
    }
}
