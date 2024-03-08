package com.smart.commons.core.captcha.dto;

import com.smart.commons.core.captcha.constants.CaptchaTrackTypeEnum;
import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验证码验证参数
 * @author shizhongming
 * 2024/3/7 14:52
 * @since 3.0.0
 */
@Getter
@Setter
public class CaptchaValidateParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -7155330088577210260L;
    /**
     * 验证码的key
     */
    @NonNull
    private String key;

    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type;


    /**
     * 图片验证码参数
     */
    private ImageParameter image;

    /**
     * 文本验证参数
     */
    private TextParameter text;

    @Getter
    @Setter
    public static class ImageParameter implements Serializable {
        @Serial
        private static final long serialVersionUID = -7589800335630788347L;
        /** 背景图片宽度. */
        private Integer bgImageWidth;
        /** 背景图片高度. */
        private Integer bgImageHeight;
        /** 滑块图片宽度. */
        private Integer sliderImageWidth;
        /** 滑块图片高度. */
        private Integer sliderImageHeight;
        /** 滑动开始时间. */
        private LocalDateTime startSlidingTime;
        /** 滑动结束时间. */
        private LocalDateTime endSlidingTime;
        /** 滑动的轨迹. */
        private List<ImageTrack> trackList;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageTrack implements Serializable {
        @Serial
        private static final long serialVersionUID = -2301151802413240271L;
        /** x. */
        private Integer x;
        /** y. */
        private Integer y;
        /** 时间. */
        private Integer t;
        /** 类型. */
        private CaptchaTrackTypeEnum type = CaptchaTrackTypeEnum.MOVE;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TextParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = -4392135739756150479L;

        /**
         * 验证码
         */
        private String code;
    }
}
