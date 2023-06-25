package com.smart.module.api.auth.parameter;

import com.smart.module.api.auth.constants.CaptchaTrackTypeEnum;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 验证码验证参数
 * @author zhongming4762
 * 2023/6/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthCaptchaValidateParameter implements Serializable {

    /**
     * 验证码的key
     */
    @NonNull
    private String key;

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
    private List<Track> trackList;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Track implements Serializable {
        /** x. */
        private Integer x;
        /** y. */
        private Integer y;
        /** 时间. */
        private Integer t;
        /** 类型. */
        private CaptchaTrackTypeEnum type = CaptchaTrackTypeEnum.MOVE;
    }
}
