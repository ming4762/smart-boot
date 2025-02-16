package com.smart.framework.extension.captcha.handler;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import cloud.tianai.captcha.generator.common.model.dto.GenerateParam;
import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.smart.framework.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.framework.commons.core.captcha.dto.CaptchaGenerateDTO;
import com.smart.framework.commons.core.captcha.dto.CaptchaGenerateParameter;
import com.smart.framework.commons.core.captcha.dto.CaptchaValidateParameter;
import com.smart.framework.commons.core.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 图片验证码服务类，基于tianai-captcha实现
 * @author shizhongming
 * 2024/3/6 17:33
 * @since 3.0.0
 */
@RequiredArgsConstructor
public class SmartImageCaptchaHandlerImpl implements SmartCaptchaHandler {

    private final ImageCaptchaApplication imageCaptchaApplication;

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

    /**
     * 生成验证码
     *
     * @param parameter 参数
     * @return 验证码信息
     */
    @Override
    public CaptchaGenerateDTO generate(CaptchaGenerateParameter parameter) {
        GenerateParam generateParam = GenerateParam.builder()
                .type(parameter.getType().name())
                .build();
        // TODO: 2024/3/6 待完善：失效时间未设置
        CaptchaResponse<ImageCaptchaVO> captchaResponse = this.imageCaptchaApplication.generateCaptcha(generateParam);
        ImageCaptchaVO captcha = captchaResponse.getCaptcha();

        CaptchaGenerateDTO.ImageDTO imageDto = new CaptchaGenerateDTO.ImageDTO();
        BeanUtils.copyProperties(captcha, imageDto);
        return CaptchaGenerateDTO.builder()
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
        ApiResponse<?> matching = this.imageCaptchaApplication.matching(
                parameter.getKey(),
                this.buildImageCaptchaTrack(parameter.getImage())
        );
        return matching.isSuccess();
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
        imageCaptchaTrack.setTemplateImageWidth(imageParameter.getSliderImageWidth());
        imageCaptchaTrack.setTemplateImageHeight(imageParameter.getSliderImageHeight());
        imageCaptchaTrack.setStartTime(DateUtils.zonedDateTimeToDate(imageParameter.getStartSlidingTime()));
        imageCaptchaTrack.setStartTime(DateUtils.zonedDateTimeToDate(imageParameter.getEndSlidingTime()));
        imageCaptchaTrack.setTrackList(trackList);
        return imageCaptchaTrack;
    }
}
