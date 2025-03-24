package com.smart.framework.commons.core.utils;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * spring上下文工具类
 * @author shizhongming
 * 2025/1/9 21:31
 * @since 5.0.0
 */
public class ApplicationContextUtils {

    private ApplicationContextUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static <T> T getBean(String beanName, Class<T> requiredType) {
        try {
            return applicationContext.getBean(beanName, requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtils.applicationContext = applicationContext;
    }
}
