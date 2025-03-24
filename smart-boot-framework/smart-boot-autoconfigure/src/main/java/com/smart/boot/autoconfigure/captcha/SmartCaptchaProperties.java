package com.smart.boot.autoconfigure.captcha;

import cloud.tianai.captcha.spring.autoconfiguration.SpringImageCaptchaProperties;
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
    public static class SmartCaptchaImageProperties extends SpringImageCaptchaProperties {
        private List<String> resourceList;
    }
}
