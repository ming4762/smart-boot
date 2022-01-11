package com.smart.system.i18n;

import com.smart.i18n.reader.ResourceReader;
import com.smart.system.service.SysI18nService;
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

    private final SysI18nService sysI18nService;

    public I18nDbResourceReader(SysI18nService sysI18nService) {
        this.sysI18nService = sysI18nService;
    }

    @Override
    public Map<String, String> read(Locale locale) {
        return this.sysI18nService.readByLocale(locale);
    }
}
