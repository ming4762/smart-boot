package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysI18nApi;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/9 18:00
 * @since 5.0.0
 */
@Component
public class RemoteSysI18nApiFallback implements RemoteSysI18nApi {
    @Override
    public Map<String, String> readI18nByLocale(Locale locale) {
        return Collections.emptyMap();
    }
}
