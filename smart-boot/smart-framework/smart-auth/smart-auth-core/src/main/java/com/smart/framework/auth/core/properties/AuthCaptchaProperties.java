package com.smart.framework.auth.core.properties;

import com.smart.framework.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * 认证验证码参数
 * @author shizhongming
 * 2024/11/6 16:55
 * @since 5.0.0
 */
@Getter
@Setter
public class AuthCaptchaProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2897174151158289493L;
    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type = CaptchaTypeEnum.TEXT_PNG;

    /**
     * 验证码开关
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 文本验证码参数
     */
    private AuthProperties.TextCaptchaProperties text = new AuthProperties.TextCaptchaProperties();

    /**
     * 图片验证码参数
     */
    private AuthProperties.ImageCaptchaProperties image = new AuthProperties.ImageCaptchaProperties();

    /**
     * 验证码创建地址
     */
    private String createUrl = "/auth/createCaptcha";

    /**
     * 过期时间 默认5分钟
     */
    private Duration expireIn = Duration.ofMinutes(5);
}
