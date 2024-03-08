package com.smart.auth.core.properties;

import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.dto.common.SmartFont;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * 认证验证码参数
 * @author shizhongming
 * 2024/3/6 15:41
 * @since 3.0.0
 */
@Getter
@Setter
public class AuthCaptchaProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 2897174151158289493L;
    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type = CaptchaTypeEnum.TEXT_PNG;

    /**
     * 验证码开关
     */
    private Boolean enabled = Boolean.TRUE;

    /**
     * 文本验证码参数
     */
    private TextCaptchaProperties text = new TextCaptchaProperties();

    /**
     * 图片验证码参数
     */
    private ImageCaptchaProperties image = new ImageCaptchaProperties();

    /**
     * 验证码创建地址
     */
    private String createUrl = "/auth/createCaptcha";

    /**
     * 过期时间 默认5分钟
     */
    private Duration expireIn = Duration.ofMinutes(5);

    /**
     * 文本验证码参数
     */
    @Getter
    @Setter
    public static class TextCaptchaProperties implements Serializable {
        @Serial
        private static final long serialVersionUID = -6876970459792547301L;
        /**
         * 字符长度
         */
        private Integer length = 4;

        /**
         * 宽度
         */
        private Integer width = 130;

        /**
         * 高度
         */
        private Integer height = 48;

        /**
         * 字体
         */
        private SmartFont font;

        /**
         * 文本类型
         * 1 字母数字混合
         * 2 纯数字
         * 3 纯字母
         * 4 纯大写字母
         * 5 纯小写字母
         * 6 数字大写字母
         */
        private Integer charType;

        /**
         * 是否忽略大小写，默认忽略
         */
        private Boolean ignoreCase;

    }

    /**
     * 图片验证码参数
     */
    @Getter
    @Setter
    public static class ImageCaptchaProperties implements Serializable {

        @Serial
        private static final long serialVersionUID = -2278503531404987655L;
    }
}
