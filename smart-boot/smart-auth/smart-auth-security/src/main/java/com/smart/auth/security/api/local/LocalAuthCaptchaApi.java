package com.smart.auth.security.api.local;

import com.smart.auth.captcha.service.SmartAuthCaptchaService;
import com.smart.auth.core.service.AuthCache;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.module.api.auth.AuthCaptchaApi;
import com.smart.module.api.auth.dto.AuthCaptchaDTO;
import com.smart.module.api.auth.parameter.AuthCaptchaCreateParameter;
import com.smart.module.api.auth.parameter.AuthCaptchaValidateParameter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author zhongming4762
 * 2023/6/9
 */
@Component
@Primary
public class LocalAuthCaptchaApi implements AuthCaptchaApi {

    private static final String CAPTCHA_TOKEN_KEY = "auth_captcha_token_";

    private final AuthCache<String, Object> authCache;

    private final SmartAuthCaptchaService smartAuthCaptchaService;

    public LocalAuthCaptchaApi(AuthCache<String, Object> authCache, SmartAuthCaptchaService smartAuthCaptchaService) {
        this.authCache = authCache;
        this.smartAuthCaptchaService = smartAuthCaptchaService;
    }

    /**
     * 创建验证码
     *
     * @param parameter 参数
     * @return 生成的验证码
     */
    @Override
    public AuthCaptchaDTO generate(AuthCaptchaCreateParameter parameter) {
        return this.smartAuthCaptchaService.generate(parameter.getType());
    }

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 是否验证成功
     */
    @Override
    public String validate(AuthCaptchaValidateParameter parameter) {
        boolean validate = this.smartAuthCaptchaService.validate(parameter);
        if (validate) {
            String token = IdGenerator.nextId() + "";
            this.authCache.put(this.getCaptchaTokenKey(token), token, Duration.ofMillis(5));
            return token;
        }
        return null;
    }

    /**
     * 验证 验证码token是否有效
     *
     * @param captchaToken 验证码token
     * @return 是否验证成功
     */
    @Override
    public boolean validateToken(String captchaToken) {
        return this.authCache.getAndRemove(this.getCaptchaTokenKey(captchaToken)) != null;
    }

    private String getCaptchaTokenKey(String token) {
        return CAPTCHA_TOKEN_KEY + token;
    }
}
