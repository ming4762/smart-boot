package com.smart.boot.autoconfigure.captcha;

import cloud.tianai.captcha.application.ImageCaptchaProperties;
import cloud.tianai.captcha.resource.DefaultBuiltInResources;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author shizhongming
 * 2025/2/17 15:43
 * @since 5.0.0
 */
@Getter
@Setter
@ConfigurationProperties("smart.captcha")
public class SmartCaptchaProperties {

    private SmartCaptchaImageProperties image = new SmartCaptchaImageProperties();

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    public static class SmartCaptchaImageProperties extends ImageCaptchaProperties {
        private List<String> resourceList;

        /** 是否初始化默认资源. */
        private Boolean initDefaultResource = false;
        /** 默认资源的位置. */
        private String defaultResourcePrefix = DefaultBuiltInResources.PATH_PREFIX;
        /** 字体包路径. */
        private List<String> fontPath;
    }
}
