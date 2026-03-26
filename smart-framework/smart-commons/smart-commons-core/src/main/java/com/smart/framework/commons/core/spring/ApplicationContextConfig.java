package com.smart.framework.commons.core.spring;

import com.smart.framework.commons.core.utils.ApplicationContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.jspecify.annotations.NonNull;

/**
 * @author shizhongming
 * 2025/1/9 21:35
 * @since 5.0.0
 */
@Configuration(proxyBeanMethods = false)
public class ApplicationContextConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.setApplicationContext(applicationContext);
    }
}
