package com.smart.module.system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.crud.service.BaseService;
import com.smart.module.system.constants.SysI18nPlatformEnum;
import com.smart.module.system.model.SysI18nJsonPO;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
* sys_i18n_json - 国际化信息json Service
* @author SmartCodeGenerator
* 2025年1月3日 14:39:00
*/
public interface SysI18nJsonService extends BaseService<SysI18nJsonPO> {

    /**
     * 读取后台国际化信息
     * @param locale 语言
     * @return 国际化信息
     */
    default Map<String, String> readBackendByLocale(Locale locale) {
        return this.readToMapByLocale(locale, SysI18nPlatformEnum.BACKEND);
    }

    /**
     * 读取国际化信息
     * @param locale 语言
     * @param platform 平台
     * @return 国际化信息
     */
    default Map<String, String> readToMapByLocale(Locale locale, SysI18nPlatformEnum platform) {
        JsonNode jsonNode = this.readToJsonByLocale(locale, platform);
        if (jsonNode == null) {
            return Map.of();
        }
        return JsonUtils.flattenJson(jsonNode, "")
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, item -> item.getValue() == null ? "" : item.getValue().toString()));
    }

    /**
     * 读取国际化信息
     * @param locale 语言
     * @param platform 平台
     * @return 国际化信息
     */
    JsonNode readToJsonByLocale(Locale locale, SysI18nPlatformEnum platform);
}