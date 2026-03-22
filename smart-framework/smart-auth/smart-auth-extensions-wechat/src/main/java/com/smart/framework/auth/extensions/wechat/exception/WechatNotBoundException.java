package com.smart.framework.auth.extensions.wechat.exception;

import com.smart.framework.auth.common.exception.AuthHttpStatusException;
import com.smart.framework.auth.extensions.wechat.constants.WechatLoginCodeEnum;
import com.smart.framework.auth.extensions.wechat.model.WechatLoginResult;
import lombok.Getter;

/**
 * @author zhongming4762
 * 2023/4/6
 */
@Getter
public class WechatNotBoundException extends AuthHttpStatusException {

    private final WechatLoginResult data;
    private final String appid;

    public WechatNotBoundException(String appid, WechatLoginResult data) {
        super(WechatLoginCodeEnum.USER_NOT_BOUND);
        this.data = data;
        this.appid = appid;
    }
}
