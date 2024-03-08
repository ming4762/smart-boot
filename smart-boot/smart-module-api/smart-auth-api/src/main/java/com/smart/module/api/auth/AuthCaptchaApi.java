package com.smart.module.api.auth;

import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;

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
    CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter);


    /**
     * 验证码验证
     * @param parameter 参数
     * @return 验证码生成的临时token
     */
    boolean validate(CaptchaValidateParameter parameter);

}
