package com.smart.commons.core.captcha.dto;

import com.smart.commons.core.captcha.constants.CaptchaTypeEnum;
import com.smart.commons.core.dto.common.SmartFont;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;

/**
 * 生成验证码参数
 * @author shizhongming
 * 2024/3/7 14:45
 * @since 3.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CaptchaGenerateParameter implements Serializable {
    @Serial
    private static final long serialVersionUID = -8414324658855329013L;

    /**
     * 验证码类型
     */
    private CaptchaTypeEnum type;

    /**
     * 过期时间
     */
    private Duration expireIn;

    private TextParameter text;

    private ImageParameter image;

    /**
     * 创建文本验证码参数
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TextParameter implements Serializable {
        @Serial
        private static final long serialVersionUID = 4617862971325382491L;
        /**
         * 字符长度
         */
        @Builder.Default
        private Integer length = 4;

        /**
         * 宽度
         */
        @Builder.Default
        private Integer width = 130;

        /**
         * 高度
         */
        @Builder.Default
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
    @AllArgsConstructor
    @Builder
    public static class ImageParameter implements Serializable {

        @Serial
        private static final long serialVersionUID = -7528465435064370193L;
    }
}
