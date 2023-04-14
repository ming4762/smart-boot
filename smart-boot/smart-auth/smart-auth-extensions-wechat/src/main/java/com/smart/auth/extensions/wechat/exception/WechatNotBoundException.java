package com.smart.auth.extensions.wechat.exception;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author zhongming4762
 * 2023/4/6
 */
@Getter
public class WechatNotBoundException extends BadCredentialsException {

    private final transient Object data;

    public WechatNotBoundException(Object data, String msg) {
        super(msg);
        this.data = data;
    }
}
