package com.smart.cloud.system.controller;

import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.message.Result;
import com.smart.module.api.auth.AuthCaptchaApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shizhongming
 * 2024/3/7 14:33
 * @since 3.0.0
 */
@RestController
@RequestMapping("test")
public class TestController {

    private final AuthCaptchaApi authCaptchaApi;

    public TestController(AuthCaptchaApi authCaptchaApi) {
        this.authCaptchaApi = authCaptchaApi;
    }

    @PostMapping("testGenerateCaptcha")
    public Result<CaptchaGenerateDTO> testGenerateCaptcha(@RequestBody CaptchaGenerateParameter parameter) {
        return Result.success(this.authCaptchaApi.generate(parameter));
    }

    @PostMapping("testGenerateCaptcha2")
    public Result<Object> testGenerateCaptcha2(@RequestBody Map<String, Object> parameter) {
        return Result.success(parameter);
    }
}
