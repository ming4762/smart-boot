package com.smart.boot.autoconfigure.auth.captcha;

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
import com.smart.framework.auth.captcha.service.DefaultSmartAuthCaptchaServiceImpl;
import com.smart.framework.auth.captcha.service.SmartAuthCaptchaService;
import com.smart.framework.auth.core.properties.AuthProperties;
import com.smart.framework.auth.core.service.AuthCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongming4762
 * 2023/6/16
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AuthProperties.class)
@ConditionalOnClass(SmartAuthCaptchaService.class)
public class SmartAuthCaptchaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SmartAuthCaptchaService smartAuthCaptchaService(ImageCaptchaGenerator imageCaptchaGenerator, ImageCaptchaValidator imageCaptchaValidator, AuthCache<String, Object> authCache, AuthProperties authProperties) {
        return new DefaultSmartAuthCaptchaServiceImpl(imageCaptchaGenerator, imageCaptchaValidator, authCache, authProperties.getCaptcha());
    }

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
}
