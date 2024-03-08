package com.smart.auth.captcha.service;

import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.smart.auth.core.properties.AuthCaptchaProperties;
import com.smart.auth.core.service.AuthCache;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.utils.DateUtils;
import com.smart.commons.core.utils.IdGenerator;
import com.smart.module.api.auth.dto.AuthCaptchaDTO;
import com.smart.module.api.auth.parameter.AuthCaptchaCreateParameter;
import com.smart.module.api.auth.parameter.AuthCaptchaValidateParameter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 默认的验证码服务类
 * @author zhongming4762
 * 2023/6/16
 */
@Component
public class DefaultSmartAuthCaptchaServiceImpl implements SmartAuthCaptchaService {

    private static final String CACHE_KEY_PREFIX = "smart_auth_captcha_";

    private final ImageCaptchaGenerator imageCaptchaGenerator;

    private final ImageCaptchaValidator imageCaptchaValidator;

    private final AuthCache<String, Object> authCache;

    private final AuthCaptchaProperties properties;


    public DefaultSmartAuthCaptchaServiceImpl(ImageCaptchaGenerator imageCaptchaGenerator, ImageCaptchaValidator imageCaptchaValidator, AuthCache<String, Object> authCache, AuthCaptchaProperties properties) {
        this.imageCaptchaGenerator = imageCaptchaGenerator;
        this.imageCaptchaValidator = imageCaptchaValidator;
        this.authCache = authCache;
        this.properties = properties;
    }

    /**
     * 生成
     *
     * @return 验证数据
     */
    @Override
    public AuthCaptchaDTO generate() {
        return this.generate(
                AuthCaptchaCreateParameter.builder()
                        .type(CaptchaTypeEnum.SLIDER)
                        .expireIn(this.properties.getExpireIn())
                        .build()
        );
    }

    /**
     * 生成验证码
     *
     * @param parameter 验证码类型
     * @return 验证码数据
     */
    @Override
    public AuthCaptchaDTO generate(AuthCaptchaCreateParameter parameter) {
        ImageCaptchaInfo imageCaptchaInfo = this.imageCaptchaGenerator.generateCaptchaImage(parameter.getType().name());
        return this.buildDto(imageCaptchaInfo);
    }

    /**
     * 验证码验证
     *
     * @param parameter 参数
     * @return 是否验证成功
     */
    @Override
    public boolean validate(AuthCaptchaValidateParameter parameter) {
        String cacheKey = this.getCacheKey(parameter.getKey());
        Map<String, Object> validateData = (Map<String, Object>) this.authCache.getAndRemove(cacheKey);
        if (validateData == null) {
            return false;
        }
        ApiResponse<?> valid = this.imageCaptchaValidator.valid(this.buildImageCaptchaTrack(parameter), validateData);
        return valid.isSuccess();
    }


    protected ImageCaptchaTrack buildImageCaptchaTrack(AuthCaptchaValidateParameter parameter) {
        List<ImageCaptchaTrack.Track> trackList = parameter.getTrackList().stream()
                .map(item -> new ImageCaptchaTrack.Track(
                        item.getX(),
                        item.getY(),
                        item.getX(),
                        item.getType().name()
                )).toList();
        ImageCaptchaTrack imageCaptchaTrack = new ImageCaptchaTrack();
        imageCaptchaTrack.setBgImageHeight(parameter.getBgImageHeight());
        imageCaptchaTrack.setBgImageWidth(parameter.getBgImageWidth());
        imageCaptchaTrack.setSliderImageWidth(parameter.getSliderImageWidth());
        imageCaptchaTrack.setSliderImageHeight(parameter.getSliderImageHeight());
        imageCaptchaTrack.setStartSlidingTime(DateUtils.localDateTimeToDate(parameter.getStartSlidingTime()));
        imageCaptchaTrack.setEndSlidingTime(DateUtils.localDateTimeToDate(parameter.getEndSlidingTime()));
        imageCaptchaTrack.setTrackList(trackList);
        return imageCaptchaTrack;
    }

    protected AuthCaptchaDTO buildDto(ImageCaptchaInfo imageCaptchaInfo) {
        AuthCaptchaDTO dto = new AuthCaptchaDTO();
        BeanUtils.copyProperties(imageCaptchaInfo, dto);

        Map<String, Object> validData = this.imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);

        String key = IdGenerator.nextId() + "";
        this.authCache.put(this.getCacheKey(key), validData, this.properties.getExpireIn());

        dto.setKey(key);

        return dto;
    }

    protected String getCacheKey(String key) {
        return CACHE_KEY_PREFIX + key;
    }
}
