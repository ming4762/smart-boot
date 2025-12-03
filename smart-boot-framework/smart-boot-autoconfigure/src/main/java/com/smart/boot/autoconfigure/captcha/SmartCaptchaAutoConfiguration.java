package com.smart.boot.autoconfigure.captcha;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.resource.ResourceStore;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.captcha.handler.SmartCaptchaHandler;
import com.smart.framework.extension.captcha.handler.SmartImageCaptchaHandlerImpl;
import com.smart.framework.extension.captcha.handler.SmartTextCaptchaHandlerImpl;
import com.smart.framework.extension.captcha.resource.CaptchaResourceLoader;
import com.smart.framework.extension.captcha.service.DefaultSmartCaptchaServiceImpl;
import com.smart.framework.extension.captcha.service.SmartCaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 验证码自动配置类
 * @author shizhongming
 * 2024/3/6 17:19
 * @since 3.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmartCaptchaService.class)
@EnableConfigurationProperties(SmartCaptchaProperties.class)
public class SmartCaptchaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartTextCaptchaHandlerImpl textSmartCaptchaHandler(CacheService cacheService) {
        return new SmartTextCaptchaHandlerImpl(cacheService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartImageCaptchaHandlerImpl imageSmartCaptchaHandler(ImageCaptchaApplication imageCaptchaApplication) {
        return new SmartImageCaptchaHandlerImpl(imageCaptchaApplication);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartCaptchaService smartCaptchaService(List<SmartCaptchaHandler> captchaHandlerList) {
        return new DefaultSmartCaptchaServiceImpl(captchaHandlerList);
    }

    @Bean
    public CaptchaResourceLoader captchaResourceLoader(SmartCaptchaProperties smartCaptchaProperties, ResourceStore resourceStore) {
        return new CaptchaResourceLoader(resourceStore, smartCaptchaProperties.getImage().getResourceList());
    }
}
