package com.smart.auth.captcha.service;

import com.smart.module.api.auth.dto.AuthCaptchaDTO;
import com.smart.module.api.auth.parameter.AuthCaptchaCreateParameter;
import com.smart.module.api.auth.parameter.AuthCaptchaValidateParameter;

/**
 * 验证码服务类
 * @author zhongming4762
 * 2023/6/16
 */
public interface SmartAuthCaptchaService {

    /**
     * 生成
     * @return 验证数据
     */
    AuthCaptchaDTO generate();


    /**
     * 生成验证码
     * @param parameter 验证码参数
     * @return 验证码数据
     */
    AuthCaptchaDTO generate(AuthCaptchaCreateParameter parameter);

    /**
     * 验证码验证
     * @param parameter 参数
     * @return 是否验证成功
     */
    boolean validate(AuthCaptchaValidateParameter parameter);
}
