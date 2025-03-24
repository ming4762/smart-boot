package com.smart.module.system.i18n;

import com.smart.framework.i18n.reader.ResourceReader;
import com.smart.module.api.system.SysI18nApi;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * 从数据库读取国际化信息
 * @author ShiZhongMing
 * 2021/11/23
 * @since 1.0.7
 */
@Component
public class I18nDbResourceReader implements ResourceReader {

    private final SysI18nApi sysI18nApi;

    public I18nDbResourceReader(SysI18nApi sysI18nApi) {
        this.sysI18nApi = sysI18nApi;
    }

    @Override
    public Map<String, String> read(Locale locale) {
        return this.sysI18nApi.readI18nByLocale(locale);
    }
}
