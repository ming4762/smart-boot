package com.smart.auth.security.api.remote;

import com.smart.captcha.service.SmartCaptchaService;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.module.api.auth.AuthCaptchaApi;
import com.smart.module.api.auth.constants.SmartAuthApiUrlConstants;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码模块远程调用接口
 * @author zhongming4762
 * 2023/6/27
 */
@RestController
@RequestMapping
public class RemoteAuthCaptchaApiController implements AuthCaptchaApi {

    private final SmartCaptchaService smartCaptchaService;

    public RemoteAuthCaptchaApiController(SmartCaptchaService smartCaptchaService) {
        this.smartCaptchaService = smartCaptchaService;
    }

    /**
     * 创建验证码
     *
     * @param parameter 参数
     * @return 生成的验证码
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_GENERATE)
    public CaptchaGenerateDTO generate(@RequestBody @Valid CaptchaGenerateParameter parameter) {
        return this.smartCaptchaService.generate(parameter);
    }

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 验证码生成的临时token
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_VALIDATE)
    public boolean validate(@RequestBody @Valid CaptchaValidateParameter parameter) {
        return this.smartCaptchaService.validate(parameter);
    }

}
