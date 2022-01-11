package com.smart.i18n;

import com.smart.commons.core.i18n.I18nUtils;
import com.smart.i18n.source.ReloadableMessageSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

/**
 * 国际化工具类初始化
 * 注入 messageSource
 * @author ShiZhongMing
 * 2022/1/11
 * @since 1.0
 */
public class I18nUtilsInit implements ApplicationContextAware {
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        I18nUtils.setMessageSource(applicationContext.getBean(ReloadableMessageSource.class));
    }
}
