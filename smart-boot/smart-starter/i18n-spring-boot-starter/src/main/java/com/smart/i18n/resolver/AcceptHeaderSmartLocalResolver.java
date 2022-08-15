package com.smart.i18n.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;
import java.util.Objects;

/**
 * 从请求头中获取国际化信息
 * @author ShiZhongMing
 * 2022/8/15
 * @since 3.0.0
 */
public class AcceptHeaderSmartLocalResolver implements SmartLocaleResolver {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Locale resolve(@NonNull HttpServletRequest request) {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        Locale locale = localeResolver.resolveLocale(request);
        if (Objects.nonNull(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            if (StringUtils.isBlank(locale.getLanguage())) {
                return Locale.forLanguageTag(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
            }
            return locale;
        }
        return null;
    }
}
