package com.smart.captcha.service;

import com.smart.commons.core.captcha.dto.*;

/**
 * 验证码服务类
 * @author shizhongming
 * 2024/3/7 9:32
 * @since 3.0.0
 */
public interface SmartCaptchaService {

    /**
     * 生成 图片验证码
     * @param parameter 参数
     * @return 生成的结果
     */
    default CaptchaGenerateDTO generateImage(CaptchaGenerateParameter parameter) {
        return this.generate(parameter);
    }


    /**
     * 生成文本验证码
     * @param parameter 参数
     * @return 生成的结果
     */
    default CaptchaGenerateDTO generateText(CaptchaGenerateParameter parameter) {
        return this.generate(parameter);
    }

    /**
     * 生成验证码
     * @param parameter 参数
     * @return 验证码信息
     */
    CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter);

    /**
     * 验证是否成功
     * @param parameter 参数
     * @return 是否验证成功
     */
    boolean validate(CaptchaValidateParameter parameter);
}
