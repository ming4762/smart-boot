package com.smart.framework.auth.extensions.wechat.provider;

import com.smart.framework.auth.common.constants.AuthTypeEnum;
import com.smart.framework.auth.extensions.wechat.model.WechatLoginResult;

/**
 *
 * @author zhongming4762
 * 2023/4/3
 */
public interface WechatLoginProvider {

    /**
     * 微信登录
     * @param appid appid
     * @param credentials 认证信息
     * @return 登录结果
     */
    WechatLoginResult login(String appid, Object credentials);

    /**
     * 获取支持的登录类型
     * @return WechatLoginTypeEnum
     */
    AuthTypeEnum supportLoginType();
}
