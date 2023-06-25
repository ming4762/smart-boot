package com.smart.module.api.auth;

import com.smart.module.api.auth.dto.AuthCaptchaDTO;
import com.smart.module.api.auth.parameter.AuthCaptchaCreateParameter;
import com.smart.module.api.auth.parameter.AuthCaptchaValidateParameter;

/**
 * 验证码API
 * @author zhongming4762
 * 2023/6/9
 */
public interface AuthCaptchaApi {

    /**
     * 创建验证码
     * @param parameter 参数
     * @return 生成的验证码
     */
    AuthCaptchaDTO generate(AuthCaptchaCreateParameter parameter);


    /**
     * 验证码验证
     * @param parameter 参数
     * @return 验证码生成的临时token
     */
    String validate(AuthCaptchaValidateParameter parameter);


    /**
     * 验证 验证码token是否有效
     * @param captchaToken 验证码token
     * @return 是否验证成功
     */
    boolean validateToken(String captchaToken);
}
