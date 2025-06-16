package com.smart.cloud.api.system.feign.fallback;

import com.smart.cloud.api.system.feign.RemoteSysI18nApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * @author shizhongming
 * 2025/6/9 18:00
 * @since 5.0.0
 */
@Component
@Slf4j
public class RemoteSysI18nApiFallback implements FallbackFactory<RemoteSysI18nApi> {

    @Override
    public RemoteSysI18nApi create(Throwable cause) {
        return new RemoteSysI18nApi() {

            private void errorLog() {
                log.error("RemoteSysI18nApiFallback", cause);
            }

            @Override
            public Map<String, String> readI18nByLocale(Locale locale) {
                this.errorLog();
                return Map.of();
            }
        };
    }
}
