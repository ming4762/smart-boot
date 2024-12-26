package com.smart.module.auth.pojo.dto;

import com.smart.framework.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author shizhongming
 * 2024/3/7 17:29
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class AuthPropertiesDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7159379144515537898L;

    private CaptchaProperties captcha;

    /**
     * 验证码参数
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CaptchaProperties implements Serializable {
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
}
