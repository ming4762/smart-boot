package com.smart.boot.captcha;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.resource.CrudResourceStore;
import cloud.tianai.captcha.spring.autoconfiguration.CacheStoreAutoConfiguration;
import com.smart.framework.commons.core.cache.CacheService;
import com.smart.framework.extension.captcha.handler.SmartCaptchaHandler;
import com.smart.framework.extension.captcha.handler.SmartImageCaptchaHandlerImpl;
import com.smart.framework.extension.captcha.handler.SmartTextCaptchaHandlerImpl;
import com.smart.framework.extension.captcha.resource.CaptchaResourceLoader;
import com.smart.framework.extension.captcha.resource.SmartRedisServiceResourceStore;
import com.smart.framework.extension.captcha.service.DefaultSmartCaptchaServiceImpl;
import com.smart.framework.extension.captcha.service.SmartCaptchaService;
import com.smart.framework.redis.service.RedisService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
@AutoConfigureBefore(CacheStoreAutoConfiguration.class)
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
    public CaptchaResourceLoader captchaResourceLoader(SmartCaptchaProperties smartCaptchaProperties, CrudResourceStore resourceStore) {
        return new CaptchaResourceLoader(resourceStore, smartCaptchaProperties.getImage().getResourceList());
    }

    @Bean
    public static SmartCaptchaImagePropertiesBeanPostProcessor smartCaptchaImagePropertiesBeanPostProcessor() {
        return new SmartCaptchaImagePropertiesBeanPostProcessor();
    }

    @Configuration
    @ConditionalOnClass(RedisService.class)
    public static class SmartCaptchaRedisAutoConfiguration {

        /**
         * redis 验证码资源存储
         * @param redisService redis服务
         * @return 验证码资源存储
         */
        @Bean
        public SmartRedisServiceResourceStore smartRedisServiceResourceStore(RedisService redisService) {
            return new SmartRedisServiceResourceStore(redisService);
        }
    }
}
