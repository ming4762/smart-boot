package com.smart.cloud.common.i18n.reader;

import com.smart.i18n.reader.ResourceReader;
import com.smart.module.api.system.SysI18nApi;

import java.util.Locale;
import java.util.Map;

/**
 * 远程读取国际化信息
 * @author zhongming4762
 * 2023/3/12
 */
public class RemoteResourceReader implements ResourceReader {

    private final SysI18nApi sysI18nApi;

    public RemoteResourceReader(SysI18nApi sysI18nApi) {
        this.sysI18nApi = sysI18nApi;
    }

    /**
     * 读取资源
     *
     * @param locale 语言
     * @return 资源信息
     */
    @Override
    public Map<String, String> read(Locale locale) {
        return this.sysI18nApi.readI18nByLocale(locale);
    }
}
