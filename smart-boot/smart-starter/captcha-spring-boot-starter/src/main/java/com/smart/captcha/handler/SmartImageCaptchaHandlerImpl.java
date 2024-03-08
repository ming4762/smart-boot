package com.smart.captcha.handler;

import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.common.model.dto.ImageCaptchaInfo;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.smart.commons.core.cache.CacheService;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.commons.core.utils.DateUtils;
import com.smart.commons.core.utils.IdGenerator;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 图片验证码服务类，基于tianai-captcha实现
 * @author shizhongming
 * 2024/3/6 17:33
 * @since 3.0.0
 */
public class SmartImageCaptchaHandlerImpl implements SmartCaptchaHandler {

    private final ImageCaptchaGenerator imageCaptchaGenerator;

    private final ImageCaptchaValidator imageCaptchaValidator;

    private final CacheService cacheService;

    public SmartImageCaptchaHandlerImpl(ImageCaptchaGenerator imageCaptchaGenerator, ImageCaptchaValidator imageCaptchaValidator, CacheService cacheService) {
        this.imageCaptchaGenerator = imageCaptchaGenerator;
        this.cacheService = cacheService;
        this.imageCaptchaValidator = imageCaptchaValidator;
    }

    /**
     * 生成验证码
     *
     * @param parameter 参数
     * @return 验证码信息
     */
    @Override
    public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {

        ImageCaptchaInfo imageCaptchaInfo = this.imageCaptchaGenerator.generateCaptchaImage(parameter.getType().name());
        CaptchaGenerateDTO.ImageDTO imageDto = new CaptchaGenerateDTO.ImageDTO();

        BeanUtils.copyProperties(imageCaptchaInfo, imageDto);
        // 保存校验数据
        Map<String, Object> validData = this.imageCaptchaValidator.generateImageCaptchaValidData(imageCaptchaInfo);
        String key = IdGenerator.nextId() + "";
        this.cacheService.put(this.getCacheKey(key), validData, Objects.requireNonNullElse(parameter.getExpireIn(), DEFAULT_EXPIRE_IN));

        return CaptchaGenerateDTO.builder()
                .key(key)
                .type(parameter.getType())
                .image(imageDto)
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
        CaptchaValidateParameter.ImageParameter imageParameter = parameter.getImage();
        String cacheKey = this.getCacheKey(parameter.getKey());
        Map<String, Object> validateData = (Map<String, Object>)this.cacheService.getAndRemove(cacheKey);
        if (validateData == null) {
            return false;
        }
        ApiResponse<?> response = this.imageCaptchaValidator.valid(this.buildImageCaptchaTrack(imageParameter), validateData);
        return response.isSuccess();
    }

    protected ImageCaptchaTrack buildImageCaptchaTrack(CaptchaValidateParameter.ImageParameter imageParameter) {
        List<ImageCaptchaTrack.Track> trackList = imageParameter.getTrackList().stream()
                .map(item -> new ImageCaptchaTrack.Track(
                        item.getX(),
                        item.getY(),
                        item.getX(),
                        item.getType().name()
                )).toList();
        ImageCaptchaTrack imageCaptchaTrack = new ImageCaptchaTrack();
        imageCaptchaTrack.setBgImageHeight(imageParameter.getBgImageHeight());
        imageCaptchaTrack.setBgImageWidth(imageParameter.getBgImageWidth());
        imageCaptchaTrack.setSliderImageWidth(imageParameter.getSliderImageWidth());
        imageCaptchaTrack.setSliderImageHeight(imageParameter.getSliderImageHeight());
        imageCaptchaTrack.setStartSlidingTime(DateUtils.localDateTimeToDate(imageParameter.getStartSlidingTime()));
        imageCaptchaTrack.setEndSlidingTime(DateUtils.localDateTimeToDate(imageParameter.getEndSlidingTime()));
        imageCaptchaTrack.setTrackList(trackList);
        return imageCaptchaTrack;
    }

    /**
     * 支持的验证码类型
     *
     * @param type 验证码类型
     * @return 是否支持
     */
    @Override
    public boolean support(CaptchaTypeEnum type) {
        return CaptchaTypeEnum.IMAGE_IDENT.endsWith(type.getIdent());
    }
}
