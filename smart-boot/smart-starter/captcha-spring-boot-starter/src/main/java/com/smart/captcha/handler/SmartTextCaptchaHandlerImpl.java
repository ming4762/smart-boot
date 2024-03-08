package com.smart.captcha.handler;

import com.smart.captcha.exception.CaptchaException;
import com.smart.commons.core.cache.CacheService;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.captcha.dto.*;
import com.smart.commons.core.dto.common.SmartFont;
import com.smart.commons.core.utils.IdGenerator;
import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 文本验证码生成器
 * @author shizhongming
 * 2024/3/6 20:57
 * @since 3.0.0
 */
public class SmartTextCaptchaHandlerImpl implements SmartCaptchaHandler {

    private static final SmartFont DEFAULT_FONT = new SmartFont("Verdana", Font.PLAIN, 32);

    private static final Map<CaptchaTypeEnum, Class<? extends Captcha>> SUPPORT_CAPTCHA_TYPE_MAP = Map.of(
            CaptchaTypeEnum.TEXT_PNG, SpecCaptcha.class,
            CaptchaTypeEnum.TEXT_GIF, GifCaptcha.class,
            CaptchaTypeEnum.TEXT_CHINESE, ChineseCaptcha.class,
            CaptchaTypeEnum.TEXT_CHINESE_GIF, ChineseGifCaptcha.class,
            CaptchaTypeEnum.TEXT_ARITHMETIC, ArithmeticCaptcha.class
    );

    private final CacheService cacheService;

    public SmartTextCaptchaHandlerImpl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 支持的验证码类型
     *
     * @param type 验证码类型
     * @return 是否支持
     */
    @Override
    public boolean support(CaptchaTypeEnum type) {
        return CaptchaTypeEnum.TEXT_IDENT.equals(type.getIdent());
    }

    /**
     * 生成验证码
     *
     * @param parameter 参数
     * @return 验证码信息
     */
    @SneakyThrows(Exception.class)
    @Override
    public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {
        Class<? extends Captcha> clazz = SUPPORT_CAPTCHA_TYPE_MAP.get(parameter.getType());
        if (clazz == null) {
            throw new UnsupportedOperationException("不支持的验证码类型，类型：" + parameter.getType().name());
        }
        CaptchaGenerateParameter.TextParameter textParameter = parameter.getText();
        if (textParameter == null) {
            throw new CaptchaException("文本参数为空，无法生成文本验证码");
        }
        Captcha captcha = clazz.getDeclaredConstructor().newInstance();
        captcha.setWidth(textParameter.getWidth());
        captcha.setHeight(textParameter.getHeight());
        captcha.setLen(textParameter.getLength());
        captcha.setFont(Objects.requireNonNullElse(textParameter.getFont(), DEFAULT_FONT).creatFont());
        captcha.setCharType(Objects.requireNonNullElse(textParameter.getCharType(), Captcha.TYPE_DEFAULT));
        String key = IdGenerator.nextId() + "";
        CacheData cacheData = new CacheData();
        cacheData.setText(captcha.text());
        cacheData.setIgnoreCase(textParameter.getIgnoreCase());
        this.cacheService.put(this.getCacheKey(key), cacheData, Objects.requireNonNullElse(parameter.getExpireIn(), DEFAULT_EXPIRE_IN));

        String base64 = captcha.toBase64();
        return CaptchaGenerateDTO.builder()
                .key(key)
                .type(parameter.getType())
                .text(new CaptchaGenerateDTO.TextDTO(base64))
                .build();
    }

    /**
     * 验证是否成功
     *
     * @param parameter 参数
     * @return 是否验证成功
     */
    @Override
    public boolean validate(CaptchaValidateParameter parameter) {
        CaptchaValidateParameter.TextParameter textParameter = parameter.getText();
        CacheData cacheData = (CacheData) this.cacheService.getAndRemove(this.getCacheKey(parameter.getKey()));
        if (cacheData == null) {
            throw new CaptchaException("验证码已过期");
        }
        if (Boolean.TRUE.equals(cacheData.getIgnoreCase())) {
            return StringUtils.equalsIgnoreCase(textParameter.getCode(), cacheData.getText());
        }
        return StringUtils.equals(textParameter.getCode(), cacheData.getText());
    }

    /**
     * 缓存数据
     */
    @Getter
    @Setter
    public static class CacheData implements Serializable {

        @Serial
        private static final long serialVersionUID = -5426159453167591529L;

        private String text;

        private Boolean ignoreCase;
    }
}
