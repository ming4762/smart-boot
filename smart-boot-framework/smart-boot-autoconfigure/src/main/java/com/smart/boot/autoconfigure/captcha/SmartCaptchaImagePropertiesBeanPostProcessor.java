package com.smart.boot.autoconfigure.captcha;

import cloud.tianai.captcha.spring.autoconfiguration.SpringImageCaptchaProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * tianai-captcha 参数拦截处理器，使用SmartCaptchaProperties替代属性
 * @author shizhongming
 * 2025/2/18 15:51
 * @since 5.0.0
 */
public class SmartCaptchaImagePropertiesBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (!(bean instanceof SpringImageCaptchaProperties properties)) {
            return bean;
        }
        SmartCaptchaProperties smartCaptchaProperties = applicationContext.getBean(SmartCaptchaProperties.class);
        BeanUtils.copyProperties(smartCaptchaProperties.getImage(), properties);
        return properties;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
