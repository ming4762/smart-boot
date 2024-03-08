package com.smart.captcha.service;

import com.smart.captcha.handler.SmartCaptchaHandler;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;

import java.util.List;

/**
 * 默认的验证码服务类
 * @author shizhongming
 * 2024/3/7 9:40
 * @since 3.0.0
 */
public class DefaultSmartCaptchaServiceImpl implements SmartCaptchaService {

    private final List<SmartCaptchaHandler> captchaHandlerList;

    public DefaultSmartCaptchaServiceImpl(List<SmartCaptchaHandler> captchaHandlerList) {
        this.captchaHandlerList = captchaHandlerList;
    }

    /**
     * 生成验证码
     *
     * @param parameter 参数
     * @return 验证码信息
     */
    @Override
    public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {
        return this.getCaptchaHandler(parameter.getType()).generate(parameter);
    }

    /**
     * 验证是否成功
     *
     * @param parameter 参数
     * @return 是否验证成功
     */
    @Override
    public boolean validate(CaptchaValidateParameter parameter) {
        return this.getCaptchaHandler(parameter.getType()).validate(parameter);
    }

    protected SmartCaptchaHandler getCaptchaHandler(CaptchaTypeEnum type) {
        return this.captchaHandlerList.stream()
                .filter(item -> item.support(type))
                .findFirst()
                .orElse(null);
    }
}
