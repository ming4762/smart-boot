package com.smart.auth.security.pojo.dto;

import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/3/7 17:29
 * @since 3.0.0
 */
@Getter
@Setter
public class AuthPropertiesDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7159379144515537898L;

    /**
     * 是否启用验证码
     */
    private Boolean captchaEnabled;

    /**
     * 验证码类型
     */
    private CaptchaTypeEnum captchaType;

    private String captchaIdent;
}
