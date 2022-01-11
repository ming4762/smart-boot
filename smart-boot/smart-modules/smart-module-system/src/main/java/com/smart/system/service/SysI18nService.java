package com.smart.system.service;

import com.smart.crud.service.BaseService;
import com.smart.system.model.SysI18nPO;
import org.springframework.lang.NonNull;

import java.util.Locale;
import java.util.Map;

/**
 * @author ShiZhongMing
 * 2021/11/12
 * @since 1.0.7
 */
public interface SysI18nService extends BaseService<SysI18nPO> {

    /**
     * 读取国际化信息
     * @param locale 语言
     * @return 国际化信息
     */
    @NonNull
    Map<String, String> readByLocale(@NonNull Locale locale);
}
