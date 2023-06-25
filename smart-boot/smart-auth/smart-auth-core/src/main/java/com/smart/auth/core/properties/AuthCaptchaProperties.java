package com.smart.auth.core.properties;

import com.smart.module.api.auth.constants.AuthCaptchaTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * 验证码参数
 * @author zhongming4762
 * 2023/6/9
 */
@Getter
@Setter
public class AuthCaptchaProperties {

    /**
     * 图片资源参数
     */
    private Map<AuthCaptchaTypeEnum, List<String>> resources;

    /**
     * 有效期，默认5分钟
     */
    private Duration timeout = Duration.ofMinutes(5);
}
