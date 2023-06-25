package com.smart.module.api.auth.parameter;

import com.smart.module.api.auth.constants.AuthCaptchaTypeEnum;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;

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


    /**
     * 验证码类型
     */
    @NonNull
    private AuthCaptchaTypeEnum type;
}
