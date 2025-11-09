package com.smart.framework.auth.extensions.dingtalk.exception;

import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * 钉钉用户未绑定异常
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2025/11/7 18:43
 * @since 5.0.0
 */
@Getter
public class DingtalkNotBoundException extends BadCredentialsException  {

    private final GetUserResponseBody dingtalkUser;

    public DingtalkNotBoundException(String msg, GetUserResponseBody dingtalkUser) {
        super(msg);
        this.dingtalkUser = dingtalkUser;
    }
}
