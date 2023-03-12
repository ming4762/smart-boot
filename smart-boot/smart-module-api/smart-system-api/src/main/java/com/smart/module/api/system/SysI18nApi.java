package com.smart.module.api.system;

import java.util.Locale;
import java.util.Map;

/**
 * @author zhongming4762
 * 2023/3/12
 */
public interface SysI18nApi {

    /**
     * 通过Locale 读取I18N信息
     * @param locale Locale
     * @return 国际化信息
     */
    Map<String, String> readI18nByLocale(Locale locale);
}
