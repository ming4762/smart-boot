package com.smart.commons.core.captcha.constants;

import lombok.Getter;

/**
 * 验证码类型
 * @author zhongming4762
 * 2023/6/9
 */
@Getter
public enum CaptchaTypeEnum {

    /**
     * 滑块
     */
    SLIDER(CaptchaTypeEnum.IMAGE_IDENT),

    /**
     * 旋转
     */
    ROTATE(CaptchaTypeEnum.IMAGE_IDENT),
    /**
     * 滑动还原
     */
    CONCAT(CaptchaTypeEnum.IMAGE_IDENT),
    /**
     * 文字点选
     */
    WORD_IMAGE_CLICK(CaptchaTypeEnum.IMAGE_IDENT),

    /**
     * png格式
     */
    TEXT_PNG(CaptchaTypeEnum.TEXT_IDENT),
    /**
     * GIF文本
     */
    TEXT_GIF(CaptchaTypeEnum.TEXT_IDENT),

    /**
     * 中文文本
     */
    TEXT_CHINESE(CaptchaTypeEnum.TEXT_IDENT),

    /**
     * 中文GIF文本
     */
    TEXT_CHINESE_GIF(CaptchaTypeEnum.TEXT_IDENT),

    /**
     * 算数类型
     */
    TEXT_ARITHMETIC(CaptchaTypeEnum.TEXT_IDENT),
    ;

    public static final String TEXT_IDENT = "TEXT";

    public static final String IMAGE_IDENT = "IMAGE";

    private final String ident;

    CaptchaTypeEnum(String ident) {
        this.ident = ident;
    }

    public boolean isText() {
        return TEXT_IDENT.equals(this.ident);
    }

    public boolean isImage() {
        return IMAGE_IDENT.equals(this.ident);
    }
}
