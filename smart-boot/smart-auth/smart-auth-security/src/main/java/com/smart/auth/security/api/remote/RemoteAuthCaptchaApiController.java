package com.smart.auth.security.api.remote;

import com.smart.auth.security.api.local.LocalAuthCaptchaApi;
import com.smart.module.api.auth.AuthCaptchaApi;
import com.smart.module.api.auth.constants.SmartAuthApiUrlConstants;
import com.smart.module.api.auth.dto.AuthCaptchaDTO;
import com.smart.module.api.auth.parameter.AuthCaptchaCreateParameter;
import com.smart.module.api.auth.parameter.AuthCaptchaValidateParameter;
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

    private final LocalAuthCaptchaApi localAuthCaptchaApi;

    public RemoteAuthCaptchaApiController(LocalAuthCaptchaApi localAuthCaptchaApi) {
        this.localAuthCaptchaApi = localAuthCaptchaApi;
    }

    /**
     * 创建验证码
     *
     * @param parameter 参数
     * @return 生成的验证码
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_GENERATE)
    public AuthCaptchaDTO generate(@RequestBody @Valid AuthCaptchaCreateParameter parameter) {
        return this.localAuthCaptchaApi.generate(parameter);
    }

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 验证码生成的临时token
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_VALIDATE)
    public String validate(@RequestBody @Valid AuthCaptchaValidateParameter parameter) {
        return this.localAuthCaptchaApi.validate(parameter);
    }

    /**
     * 验证 验证码token是否有效
     *
     * @param captchaToken 验证码token
     * @return 是否验证成功
     */
    @Override
    @PostMapping(SmartAuthApiUrlConstants.AUTH_CAPTCHA_VALIDATE_TOKEN)
    public boolean validateToken(@RequestBody String captchaToken) {
        return this.localAuthCaptchaApi.validateToken(captchaToken);
    }
}
