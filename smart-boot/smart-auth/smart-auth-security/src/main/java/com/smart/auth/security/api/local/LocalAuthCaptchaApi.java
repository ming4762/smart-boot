package com.smart.auth.security.api.local;

import com.smart.captcha.service.SmartCaptchaService;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.module.api.auth.AuthCaptchaApi;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author zhongming4762
 * 2023/6/9
 */
@Component
@Primary
public class LocalAuthCaptchaApi implements AuthCaptchaApi {

    private final SmartCaptchaService smartCaptchaService;

    public LocalAuthCaptchaApi(SmartCaptchaService smartCaptchaService) {
        this.smartCaptchaService = smartCaptchaService;
    }

    /**
     * 创建验证码
     *
     * @param parameter 参数
     * @return 生成的验证码
     */
    @Override
    public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {
        return this.smartCaptchaService.generate(parameter);
    }

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 是否验证成功
     */
    @Override
    public boolean validate(CaptchaValidateParameter parameter) {
        return this.smartCaptchaService.validate(parameter);
    }
}
