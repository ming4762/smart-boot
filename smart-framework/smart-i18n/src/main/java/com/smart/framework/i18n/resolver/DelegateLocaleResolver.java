package com.smart.framework.i18n.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.jspecify.annotations.NonNull;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import java.util.*;

/**
 * @author ShiZhongMing
 * 2022/8/15
 * @since 3.0.0
 */
public class DelegateLocaleResolver extends AbstractLocaleResolver implements ApplicationContextAware {

    private List<SmartLocaleResolver> localeResolverList = new ArrayList<>(0);

    @NonNull
    @Override
    public Locale resolveLocale(@NonNull HttpServletRequest request) {
        Locale locale = null;
        for (SmartLocaleResolver localeResolver : localeResolverList) {
            locale = localeResolver.resolve(request);
            if (locale != null) {
                break;
            }
        }
        if (locale == null) {
            locale = this.getDefaultLocale();
        }
        if (locale == null) {
            locale = request.getLocale();
        }
        return locale;
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException(
                "Cannot change HTTP Accept-Language header - use a different locale resolution strategy");
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.localeResolverList = Arrays.stream(applicationContext.getBeanNamesForType(SmartLocaleResolver.class))
                .filter(item -> !DispatcherServlet.LOCALE_RESOLVER_BEAN_NAME.equals(item))
                .map(item -> applicationContext.getBean(item, SmartLocaleResolver.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .toList();
    }
}
