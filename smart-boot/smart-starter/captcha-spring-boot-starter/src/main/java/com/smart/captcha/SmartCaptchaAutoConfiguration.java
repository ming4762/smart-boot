package com.smart.captcha;

import cloud.tianai.captcha.generator.ImageCaptchaGenerator;
import cloud.tianai.captcha.generator.ImageTransform;
import cloud.tianai.captcha.generator.impl.MultiImageCaptchaGenerator;
import cloud.tianai.captcha.generator.impl.transform.Base64ImageTransform;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.impl.DefaultImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.impl.DefaultResourceStore;
import cloud.tianai.captcha.validator.ImageCaptchaValidator;
import cloud.tianai.captcha.validator.impl.BasicCaptchaTrackValidator;
import com.smart.captcha.handler.SmartCaptchaHandler;
import com.smart.captcha.handler.SmartImageCaptchaHandlerImpl;
import com.smart.captcha.handler.SmartTextCaptchaHandlerImpl;
import com.smart.captcha.service.DefaultSmartCaptchaServiceImpl;
import com.smart.captcha.service.SmartCaptchaService;
import com.smart.commons.core.cache.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class SmartCaptchaAutoConfiguration {

    /**
     * 创建图片校验器
     * @return ImageCaptchaValidator
     */
    @Bean
    @ConditionalOnMissingBean
    public ImageCaptchaValidator imageCaptchaValidator() {
        return new BasicCaptchaTrackValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResourceStore resourceStore() {
        return new DefaultResourceStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageCaptchaResourceManager imageCaptchaResourceManager(ResourceStore resourceStore) {
        return new DefaultImageCaptchaResourceManager(resourceStore);
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageTransform imageTransform() {
        return new Base64ImageTransform();
    }

    @Bean
    @ConditionalOnMissingBean
    public ImageCaptchaGenerator imageCaptchaGenerator(ImageCaptchaResourceManager imageCaptchaResourceManager, ImageTransform imageTransform) {
        MultiImageCaptchaGenerator generator = new MultiImageCaptchaGenerator(imageCaptchaResourceManager, imageTransform);
        generator.init(true);
        return generator;
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartTextCaptchaHandlerImpl textSmartCaptchaHandler(CacheService cacheService) {
        return new SmartTextCaptchaHandlerImpl(cacheService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartImageCaptchaHandlerImpl imageSmartCaptchaHandler(ImageCaptchaGenerator imageCaptchaGenerator, ImageCaptchaValidator imageCaptchaValidator, CacheService cacheService) {
        return new SmartImageCaptchaHandlerImpl(imageCaptchaGenerator, imageCaptchaValidator, cacheService);
    }

    @Bean
    @ConditionalOnMissingBean
    public SmartCaptchaService smartCaptchaService(List<SmartCaptchaHandler> captchaHandlerList) {
        return new DefaultSmartCaptchaServiceImpl(captchaHandlerList);
    }
}
