package com.smart.module.api.auth.parameter;

import com.smart.module.api.auth.constants.CaptchaTrackTypeEnum;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serial;
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

    @Serial
    private static final long serialVersionUID = -7853271768900091241L;
    /**
     * 验证码的key
     */
    @NonNull
    private String key;

    /**
     * 验证码类型
     */
    private CaptchaTrackTypeEnum type;

    /**
     * 验证码，文本验证码使用
     */
    private String text;

    // ------------ 图片验证码 ------------------
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
        @Serial
        private static final long serialVersionUID = 3084353048139328758L;
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
