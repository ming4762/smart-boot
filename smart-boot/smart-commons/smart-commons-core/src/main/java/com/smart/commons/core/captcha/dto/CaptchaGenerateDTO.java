package com.smart.commons.core.captcha.dto;

import com.smart.commons.core.captcha.constants.CaptchaTrackTypeEnum;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码生成结果
 * @author shizhongming
 * 2024/3/7 14:49
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaptchaGenerateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6239126727727017687L;

    /**
     * 验证码的key
     */
    private String key;

    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type;

    /**
     * 图片验证码结果
     */
    private ImageDTO image;

    /**
     * 文本验证码结果
     */
    private TextDTO text;


    @Getter
    @Setter
    public static class ImageDTO implements Serializable {

        @Serial
        private static final long serialVersionUID = 5502563809987822116L;

        /** 背景图. */
        private String backgroundImage;
        /** 模板图. */
        private String templateImage;
        /** 背景图片所属标签. */
        private String backgroundImageTag;
        /** 模板图片所属标签. */
        private String templateImageTag;
        /** 背景图片宽度. */
        private Integer backgroundImageWidth;
        /** 背景图片高度. */
        private Integer backgroundImageHeight;
        /** 滑块图片宽度. */
        private Integer templateImageWidth;
        /** 滑块图片高度. */
        private Integer templateImageHeight;
        /** 随机值. */
        private Integer randomX;
        /** 容错值, 可以为空 默认 0.02容错,校验的时候用. */
        private Float tolerant;
        /** 验证码类型. */
        private CaptchaTrackTypeEnum type;
        /** 透传字段，用于传给前端. */
        private transient Object data;
    }

    /**
     * 文本验证码结果
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextDTO implements Serializable {
        @Serial
        private static final long serialVersionUID = 6723530089522443084L;
        private String image;
    }
}
