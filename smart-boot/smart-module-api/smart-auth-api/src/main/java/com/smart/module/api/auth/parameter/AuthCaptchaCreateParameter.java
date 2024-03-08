package com.smart.module.api.auth.parameter;

import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * 验证码生成参数
 * @author zhongming4762
 * 2023/6/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthCaptchaCreateParameter implements Serializable {


    @Serial
    private static final long serialVersionUID = -2200589203737334372L;
    /**
     * 验证码类型
     */
    @NonNull
    private CaptchaTypeEnum type;

    /**
     * 过期时间
     */
    private Duration expireIn;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TextParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = 5442275289834738337L;

        /**
         * 字符长度
         */
        private Integer length = 4;

        /**
         * 宽度
         */
        private Integer width = 130;

        /**
         * 高度
         */
        private Integer height = 48;
    }
}
