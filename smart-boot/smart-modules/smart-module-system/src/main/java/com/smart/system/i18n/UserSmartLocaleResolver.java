package com.smart.system.i18n;

import com.smart.auth.core.userdetails.RestUserDetails;
import com.smart.auth.core.utils.AuthUtils;
import com.smart.i18n.resolver.SmartLocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

/**
 * 从用户信息中获取国际化信息
 * @author ShiZhongMing
 * 2022/8/15
 * @since 3.0.0
 */
@Component
public class UserSmartLocaleResolver implements SmartLocaleResolver {
    @Override
    public Locale resolve(@NonNull HttpServletRequest request) {
        RestUserDetails userDetails = AuthUtils.getCurrentUser();
        if (Objects.nonNull(userDetails) && StringUtils.isNotBlank(userDetails.getLocale())) {
            return Locale.forLanguageTag(userDetails.getLocale());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
