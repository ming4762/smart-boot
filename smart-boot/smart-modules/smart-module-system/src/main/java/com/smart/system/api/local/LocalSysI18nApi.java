package com.smart.system.api.local;

import com.smart.module.api.system.SysI18nApi;
import com.smart.system.service.SysI18nService;
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
public class LocalSysI18nApi implements SysI18nApi {

    private final SysI18nService sysI18nService;

    public LocalSysI18nApi(SysI18nService sysI18nService) {
        this.sysI18nService = sysI18nService;
    }

    /**
     * 通过Locale 读取I18N信息
     *
     * @param locale Locale
     * @return 国际化信息
     */
    @Override
    public Map<String, String> readI18nByLocale(Locale locale) {
        return this.sysI18nService.readByLocale(locale);
    }
}
