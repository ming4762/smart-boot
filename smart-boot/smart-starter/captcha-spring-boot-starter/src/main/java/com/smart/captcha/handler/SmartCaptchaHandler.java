package com.smart.captcha.handler;

import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;

import java.time.Duration;

/**
 * 图片验证码服务
 * @author shizhongming
 * 2024/3/6 17:33
 * @since 3.0.0
 */
public interface SmartCaptchaHandler {

    /**
     * 默认的过期时间
     */
    Duration DEFAULT_EXPIRE_IN = Duration.ofMinutes(10);

    /**
     * 支持的验证码类型
     * @param type 验证码类型
     * @return 是否支持
     */
    boolean support(CaptchaTypeEnum type);

    /**
     * 获取缓存的key
     * @param key 验证码key
     * @return 缓存的key
     */
    default String getCacheKey(String key) {
        return "smart_captcha_cache_" + key;
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
