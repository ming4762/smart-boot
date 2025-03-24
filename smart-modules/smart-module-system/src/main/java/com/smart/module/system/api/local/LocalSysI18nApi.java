package com.smart.module.system.api.local;

import com.smart.module.api.system.SysI18nApi;
import com.smart.module.system.service.SysI18nJsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/12
 */
@Component
@Primary
@RequiredArgsConstructor
public class LocalSysI18nApi implements SysI18nApi {

    private final SysI18nJsonService sysI18nJsonService;
    /**
     * 通过Locale 读取I18N信息
     *
     * @param locale Locale
     * @return 国际化信息
     */
    @Override
    public Map<String, String> readI18nByLocale(Locale locale) {
        return this.sysI18nJsonService.readBackendByLocale(locale);
    }
}
