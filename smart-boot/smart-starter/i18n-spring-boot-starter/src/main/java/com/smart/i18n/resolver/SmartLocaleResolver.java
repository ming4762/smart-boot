package com.smart.i18n.resolver;

import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 国际化解析器
 * @author ShiZhongMing
 * 2022/8/15
 * @since 3.0.0
 */
public interface SmartLocaleResolver extends Ordered {

    /**
     * 获取国际化语言
     * @param request HttpServletRequest
     * @return Locale
     */
    Locale resolve(@NonNull HttpServletRequest request);
}
