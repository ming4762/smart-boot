package com.smart.module.api.auth.dto;

import com.smart.module.api.auth.constants.CaptchaTrackTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 生成验证码数据
 * @author zhongming4762
 * 2023/6/9
 */
@Getter
@Setter
@ToString
public class AuthCaptchaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -94382272867474576L;
    /**
     * 验证码的key
     */
    private String key;

    /**
     * 文本验证码的值
     */
    private String text;


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
